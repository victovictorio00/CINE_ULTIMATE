package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO: AsientoFuncionDao
 * -------------------------------------------
 * Gestiona todas las operaciones CRUD y auxiliares 
 * relacionadas con la tabla intermedia `asiento_funcion`.
 *
 * Esta tabla representa la disponibilidad de cada asiento 
 * en una función específica (por ejemplo, si está disponible,
 * ocupado, bloqueado, etc.).
 *
 * Relaciona directamente:
 *  - Asiento (posición física dentro de una sala)
 *  - Función (película + horario)
 *  - EstadoAsiento (disponible / ocupado / bloqueado)
 */
public class AsientoFuncionDao implements DaoCrud<AsientoFuncion> {

    /* ==========================================================
       CRUD BÁSICO
       ========================================================== */

    /**
     * Lista todos los registros de la tabla `asiento_funcion`.
     * 
     * Cada registro representa la relación entre un asiento,
     * una función y su estado actual.
     */
    @Override
    public List<AsientoFuncion> listar() throws SQLException {
        List<AsientoFuncion> lista = new ArrayList<>();

        String sql = "SELECT af.id_asiento_funcion, af.id_asiento, af.id_funcion, af.id_estado_asiento "
                   + "FROM asiento_funcion af";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Se utiliza el método privado "mapear" para construir el objeto
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /**
     * Inserta una nueva relación Asiento-Función en la base de datos.
     * 
     * Ejemplo: cuando se crean los asientos asociados a una nueva función.
     */
    @Override
    public void insertar(AsientoFuncion af) throws SQLException {
        String sql = "INSERT INTO asiento_funcion (id_asiento, id_funcion, id_estado_asiento) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, af.getAsiento().getId_asiento());
            pst.setInt(2, af.getFuncion().getIdFuncion());
            pst.setInt(3, af.getEstadoAsiento().getIdEstadoAsiento());
            pst.executeUpdate();

            // Recupera el ID autogenerado de la inserción
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    af.setIdAsientoFuncion(keys.getInt(1));
                }
            }
        }
    }

    /**
     * Lee (obtiene) un registro Asiento-Función específico por su ID.
     */
    @Override
    public AsientoFuncion leer(int id) throws SQLException {
        String sql = "SELECT af.id_asiento_funcion, af.id_asiento, af.id_funcion, af.id_estado_asiento, "
                   + "       a.codigo, a.id_sala "
                   + "FROM asiento_funcion af "
                   + "JOIN asientos a ON a.id_asiento = af.id_asiento "
                   + "WHERE af.id_asiento_funcion = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                // Retorna el objeto mapeado si se encuentra
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /**
     * Edita (actualiza) el estado del asiento dentro de una función.
     * 
     * Por ejemplo: pasar de disponible (1) a ocupado (2).
     */
    @Override
    public void editar(AsientoFuncion af) throws SQLException {
        String sql = "UPDATE asiento_funcion SET id_estado_asiento = ? WHERE id_asiento_funcion = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, af.getEstadoAsiento().getIdEstadoAsiento());
            pst.setInt(2, af.getIdAsientoFuncion());
            pst.executeUpdate();
        }
    }

    /**
     * Elimina un registro Asiento-Función.
     * (Generalmente no se usa en producción, salvo depuración).
     */
    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM asiento_funcion WHERE id_asiento_funcion = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /* ==========================================================
       MÉTODOS EXTRA
       ========================================================== */

    /**
     * Lista todos los asientos de una función (de cualquier sala)
     * con su estado actual (disponible, ocupado, bloqueado, etc.).
     *
     * @param idFuncion ID de la función (película + horario)
     * @return Lista de objetos AsientoFuncion
     */
    public List<AsientoFuncion> listarPorSalaYFuncion(int idFuncion) throws SQLException {
        List<AsientoFuncion> lista = new ArrayList<>();

        String sql = "SELECT af.id_asiento_funcion, af.id_asiento, af.id_funcion, af.id_estado_asiento, "
                   + "       a.codigo, a.id_sala "
                   + "FROM asiento_funcion af "
                   + "JOIN asientos a ON a.id_asiento = af.id_asiento "
                   + "WHERE af.id_funcion = ? "
                   + "ORDER BY a.codigo";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idFuncion);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Marca un asiento como ocupado (id_estado_asiento = 2).
     * 
     * Solo cambia si el asiento estaba disponible (id_estado_asiento = 1).
     * Devuelve true si la operación fue exitosa.
     */
    public boolean ocupar(int idAsientoFuncion) throws SQLException {
        String sql = "UPDATE asiento_funcion SET id_estado_asiento = 2 "
                   + "WHERE id_asiento_funcion = ? AND id_estado_asiento = 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idAsientoFuncion);
            return pst.executeUpdate() == 1;
        }
    }

    /**
     * Libera un asiento (cambia su estado a disponible = 1),
     * solo si estaba ocupado (2).
     */
    public void liberar(int idAsientoFuncion) throws SQLException {
        String sql = "UPDATE asiento_funcion SET id_estado_asiento = 1 "
                   + "WHERE id_asiento_funcion = ? AND id_estado_asiento = 2";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idAsientoFuncion);
            pst.executeUpdate();
        }
    }

    /* ==========================================================
       MÉTODO PRIVADO AUXILIAR
       ========================================================== */

    /**
     * Mapea un registro del ResultSet a un objeto AsientoFuncion.
     * 
     * Este método se reutiliza en listar(), leer() y listarPorSalaYFuncion().
     */
    private AsientoFuncion mapear(ResultSet rs) throws SQLException {
        AsientoFuncion af = new AsientoFuncion();
        af.setIdAsientoFuncion(rs.getInt("id_asiento_funcion"));

        // ----- Datos del asiento -----
        Asiento a = new Asiento();
        a.setId_asiento(rs.getInt("id_asiento"));
        a.setCodigo(rs.getString("codigo")); // Código físico (A1, B2, etc.)

        // Sala a la que pertenece el asiento
        Sala s = new Sala();
        s.setIdSala(rs.getInt("id_sala"));
        a.setId_sala(s);
        af.setAsiento(a);

        // ----- Datos de la función -----
        Funcion f = new Funcion();
        f.setIdFuncion(rs.getInt("id_funcion"));
        af.setFuncion(f);

        // ----- Estado del asiento -----
        EstadoAsiento ea = new EstadoAsiento();
        ea.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
        af.setEstadoAsiento(ea);

        return af;
    }
}
