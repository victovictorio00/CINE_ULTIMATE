package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import Conexion.Conexion;

public class AsientoDao implements DaoCrud<Asiento> {

    @Override
    public List<Asiento> listar() throws SQLException {
        List<Asiento> lista = new ArrayList<>();
        String query = "SELECT a.*, ea.nombre AS nombre_estado "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "ORDER BY a.id_sala, a.codigo";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

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

                lista.add(asiento);
            }
        }
        return lista;
    }

    @Override
    public void insertar(Asiento asiento) throws SQLException {
        String sql = "INSERT INTO asientos (id_sala, codigo, id_estado_asiento) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, asiento.getId_sala().getIdSala());
            pst.setString(2, asiento.getCodigo());
            pst.setInt(3, asiento.getId_estado_asiento().getIdEstadoAsiento());
            pst.executeUpdate();

            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    asiento.setId_asiento(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public Asiento leer(int id) throws SQLException {
        String query = "SELECT a.*, ea.nombre AS nombre_estado "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "WHERE a.id_asiento = ? LIMIT 1";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

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

    @Override
    public void editar(Asiento asiento) throws SQLException {
        String query = "UPDATE asientos SET id_sala = ?, codigo = ?, id_estado_asiento = ? WHERE id_asiento = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, asiento.getId_sala().getIdSala());
            pst.setString(2, asiento.getCodigo());
            pst.setInt(3, asiento.getId_estado_asiento().getIdEstadoAsiento());
            pst.setInt(4, asiento.getId_asiento());
            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM asientos WHERE id_asiento = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /**
     * Obtiene los asientos de una sala y añade columna calculada
     * "estado_actual" (ocupado | bloqueado | disponible) según detalle_ventas y
     * estado_asientos.
     *
     * Parámetros: idFuncion (para LEFT JOIN con detalle_ventas) y idSala
     * (WHERE). IMPORTANTE: el orden de parámetros en el PreparedStatement es
     * (idFuncion, idSala).
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

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            // Orden: idFuncion, idSala
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
     * Obtiene asientos por sala (sin tener en cuenta función).
     */
    public List<Asiento> obtenerAsientosPorSala(int idSala) throws SQLException {
        List<Asiento> asientos = new ArrayList<>();

        String query = "SELECT a.*, ea.nombre as nombre_estado "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "WHERE a.id_sala = ? "
                + "ORDER BY a.codigo";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

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
     * Lee un asiento por código dentro de una sala concreta. Evita ambigüedad
     * si hay códigos repetidos en diferentes salas.
     */
    public Asiento leerPorCodigoYSala(String codigo, int idSala) throws SQLException {
        String query = "SELECT a.*, ea.nombre AS nombre_estado "
                + "FROM asientos a "
                + "LEFT JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento "
                + "WHERE a.codigo = ? AND a.id_sala = ? LIMIT 1";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

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
     * Comprueba si un asiento está disponible para una función (sin insertar).
     * Retorna true si NO existe registro en detalle_ventas para (idFuncion,
     * idAsiento).
     */
    public boolean isAsientoDisponibleEnFuncion(int idAsientoFuncion) throws SQLException {
        String sql = "SELECT COUNT(*) FROM detalle_ventas WHERE id_asiento_funcion = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idAsientoFuncion);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") == 0;
                }
            }
        }
        // Si algo falla, mejor tratar como no disponible
        return false;
    }

    /**
     * Helper ATÓMICO para insertar un detalle de venta tipo asiento dentro de
     * una transacción. - Debe llamarse con una Connection que ya tiene
     * setAutoCommit(false). - Si hay una única constraint
     * (id_funcion,id_asiento) y otro proceso ya insertó, este método capturará
     * el Duplicate Key (errorCode 1062) y devolverá false.
     *
     * NOTA: idealmente este método pertenece a DetalleVentaDao, lo incluyo aquí
     * por conveniencia.
     */
    public boolean insertarDetalleAsientoFuncionSiDisponible(Connection con, int idVenta, int idFuncion, int idAsientoFuncion, double precioUnitario) throws SQLException {
        String sql = "INSERT INTO detalle_ventas (id_venta, cantidad, tipo_item, precio_unitario, id_funcion, id_asiento_funcion) VALUES (?, 1, 1, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idVenta);
            pst.setBigDecimal(2, BigDecimal.valueOf(precioUnitario));
            pst.setInt(3, idFuncion);
            pst.setInt(4, idAsientoFuncion);
            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            // MySQL duplicate key -> SQLState "23000", errorCode 1062
            String sqlState = ex.getSQLState();
            int errorCode = ex.getErrorCode();
            if ("23000".equals(sqlState) || errorCode == 1062) {
                return false;
            }
            throw ex;
        }
    }
}
