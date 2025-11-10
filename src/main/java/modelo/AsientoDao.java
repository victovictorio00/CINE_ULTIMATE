package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import Conexion.Conexion;

/**
 * DAO: AsientoDao
 * 
 * Gestiona todas las operaciones CRUD (crear, leer, actualizar, eliminar)
 * relacionadas con los asientos del cine. 
 * También contiene métodos personalizados para manejar la relación con
 * las funciones y los estados de ocupación.
 */
public class AsientoDao implements DaoCrud<Asiento> {

    // ===========================================================
    // MÉTODOS CRUD BÁSICOS
    // ===========================================================

    /**
     * Lista todos los asientos de todas las salas.
     * Incluye el nombre del estado actual (disponible, bloqueado, etc.)
     */
    @Override
    public List<Asiento> listar() throws SQLException {
        List<Asiento> lista = new ArrayList<>();

        String query = "SELECT a.*, ea.nombre AS nombre_estado "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "ORDER BY a.id_sala, a.codigo";

        // Conexión a la base de datos
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Crear objeto Asiento con datos de la BD
                Asiento asiento = new Asiento();
                asiento.setId_asiento(rs.getInt("id_asiento"));
                asiento.setCodigo(rs.getString("codigo"));

                // Relación con la sala
                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                asiento.setId_sala(sala);

                // Relación con el estado (disponible, bloqueado, etc.)
                EstadoAsiento estado = new EstadoAsiento();
                estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                estado.setNombre(rs.getString("nombre_estado"));
                asiento.setId_estado_asiento(estado);

                lista.add(asiento);
            }
        }
        return lista;
    }

    /**
     * Inserta un nuevo asiento en la base de datos.
     * Guarda el id generado automáticamente.
     */
    @Override
    public void insertar(Asiento asiento) throws SQLException {
        String sql = "INSERT INTO asientos (id_sala, codigo, id_estado_asiento) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, asiento.getId_sala().getIdSala());
            pst.setString(2, asiento.getCodigo());
            pst.setInt(3, asiento.getId_estado_asiento().getIdEstadoAsiento());
            pst.executeUpdate();

            // Recuperar el ID autogenerado
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    asiento.setId_asiento(keys.getInt(1));
                }
            }
        }
    }

    /**
     * Lee (obtiene) un asiento específico por su ID.
     */
    @Override
    public Asiento leer(int id) throws SQLException {
        String query = "SELECT a.*, ea.nombre AS nombre_estado "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "WHERE a.id_asiento = ? LIMIT 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Asiento asiento = new Asiento();
                    asiento.setId_asiento(rs.getInt("id_asiento"));
                    asiento.setCodigo(rs.getString("codigo"));

                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    asiento.setId_sala(sala);

                    EstadoAsiento estado = new EstadoAsiento();
                    estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    estado.setNombre(rs.getString("nombre_estado"));
                    asiento.setId_estado_asiento(estado);

                    return asiento;
                }
            }
        }
        return null;
    }

    /**
     * Edita (actualiza) los datos de un asiento existente.
     */
    @Override
    public void editar(Asiento asiento) throws SQLException {
        String query = "UPDATE asientos SET id_sala = ?, codigo = ?, id_estado_asiento = ? WHERE id_asiento = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, asiento.getId_sala().getIdSala());
            pst.setString(2, asiento.getCodigo());
            pst.setInt(3, asiento.getId_estado_asiento().getIdEstadoAsiento());
            pst.setInt(4, asiento.getId_asiento());
            pst.executeUpdate();
        }
    }

    /**
     * Elimina un asiento de la base de datos.
     */
    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM asientos WHERE id_asiento = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    // ===========================================================
    // MÉTODOS PERSONALIZADOS
    // ===========================================================

    /**
     * Obtiene todos los asientos de una sala para una función específica.
     * Determina si cada asiento está:
     *  - 'ocupado' si aparece en detalle_ventas para esa función
     *  - 'bloqueado' si su estado es bloqueado
     *  - 'disponible' en cualquier otro caso
     */
    public List<Asiento> obtenerAsientosPorSalaYFuncion(int idSala, int idFuncion) throws SQLException {
        List<Asiento> asientos = new ArrayList<>();

        String sql = "SELECT a.id_asiento, a.codigo, a.id_sala, a.id_estado_asiento, ea.nombre AS nombre_estado, "
                + "       CASE WHEN dv.id_asiento IS NOT NULL THEN 'ocupado' "
                + "            WHEN ea.nombre = 'bloqueado' THEN 'bloqueado' "
                + "            ELSE 'disponible' END AS estado_actual "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "LEFT JOIN detalle_ventas dv ON a.id_asiento = dv.id_asiento AND dv.id_funcion = ? "
                + "WHERE a.id_sala = ? "
                + "ORDER BY a.codigo";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idFuncion);
            pst.setInt(2, idSala);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Asiento asiento = new Asiento();
                    asiento.setId_asiento(rs.getInt("id_asiento"));
                    asiento.setCodigo(rs.getString("codigo"));

                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    asiento.setId_sala(sala);

                    EstadoAsiento estado = new EstadoAsiento();
                    estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    estado.setNombre(rs.getString("nombre_estado"));
                    asiento.setId_estado_asiento(estado);

                    asientos.add(asiento);
                }
            }
        }
        return asientos;
    }

    /**
     * Lista todos los asientos de una sala (sin importar la función).
     */
    public List<Asiento> obtenerAsientosPorSala(int idSala) throws SQLException {
        List<Asiento> asientos = new ArrayList<>();

        String query = "SELECT a.*, ea.nombre as nombre_estado "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "WHERE a.id_sala = ? "
                + "ORDER BY a.codigo";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, idSala);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Asiento asiento = new Asiento();
                    asiento.setId_asiento(rs.getInt("id_asiento"));
                    asiento.setCodigo(rs.getString("codigo"));

                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    asiento.setId_sala(sala);

                    EstadoAsiento estado = new EstadoAsiento();
                    estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    estado.setNombre(rs.getString("nombre_estado"));
                    asiento.setId_estado_asiento(estado);

                    asientos.add(asiento);
                }
            }
        }
        return asientos;
    }

    /**
     * Busca un asiento específico por su código dentro de una sala.
     * Esto evita conflictos si hay asientos con el mismo código en distintas salas.
     */
    public Asiento leerPorCodigoYSala(String codigo, int idSala) throws SQLException {
        String query = "SELECT a.*, ea.nombre AS nombre_estado "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "WHERE a.codigo = ? AND a.id_sala = ? LIMIT 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, codigo);
            pst.setInt(2, idSala);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Asiento asiento = new Asiento();
                    asiento.setId_asiento(rs.getInt("id_asiento"));
                    asiento.setCodigo(rs.getString("codigo"));

                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    asiento.setId_sala(sala);

                    EstadoAsiento estado = new EstadoAsiento();
                    estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    estado.setNombre(rs.getString("nombre_estado"));
                    asiento.setId_estado_asiento(estado);

                    return asiento;
                }
            }
        }
        return null;
    }

    /**
     * Verifica si un asiento está disponible para una función específica.
     * Devuelve true si NO existe registro en detalle_ventas con ese asiento.
     */
    public boolean isAsientoDisponibleEnFuncion(int idAsientoFuncion) throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM detalle_ventas WHERE id_asiento_funcion = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idAsientoFuncion);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") == 0; // true si no hay registros
                }
            }
        }
        // En caso de error, se asume no disponible
        return false;
    }

    /**
     * Inserta un detalle de venta (asiento reservado) de manera segura dentro de una transacción.
     * - Debe llamarse con una conexión que tenga `setAutoCommit(false)`.
     * - Si el asiento ya fue reservado (clave duplicada), devuelve false.
     */
    public boolean insertarDetalleAsientoFuncionSiDisponible(Connection con, int idVenta, int idFuncion, int idAsientoFuncion, double precioUnitario) throws SQLException {
        String sql = "INSERT INTO detalle_ventas (id_venta, cantidad, tipo_item, precio_unitario, id_funcion, id_asiento_funcion) "
                   + "VALUES (?, 1, 1, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idVenta);
            pst.setBigDecimal(2, BigDecimal.valueOf(precioUnitario));
            pst.setInt(3, idFuncion);
            pst.setInt(4, idAsientoFuncion);
            pst.executeUpdate();
            return true;

        } catch (SQLException ex) {
            // Captura error de clave duplicada (otro usuario reservó al mismo tiempo)
            String sqlState = ex.getSQLState();
            int errorCode = ex.getErrorCode();
            if ("23000".equals(sqlState) || errorCode == 1062) {
                return false;
            }
            throw ex;
        }
    }
}
