package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

/**
 * Clase DAO: EstadoAsientoDao
 * ------------------------------------------------------
 * Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la tabla {@code estado_asientos} de la base de datos CineMax.
 *
 * Cada registro representa el estado de un asiento dentro de una sala o función,
 * por ejemplo:
 *  - Disponible
 *  - Ocupado
 *  - Bloqueado
 *
 * Esta clase implementa la interfaz genérica {@link DaoCrud}
 * y utiliza consultas SQL directas (sin procedimientos almacenados).
 */
public class EstadoAsientoDao implements DaoCrud<EstadoAsiento> {

    // ==========================================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================================

    /**
     * Recupera todos los estados de asientos registrados en la base de datos.
     *
     * @return Lista de objetos {@link EstadoAsiento}.
     * @throws SQLException Si ocurre un error durante la consulta SQL.
     */
    @Override
    public List<EstadoAsiento> listar() throws SQLException {
        List<EstadoAsiento> lista = new ArrayList<>();
        String query = "SELECT * FROM estado_asientos";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                EstadoAsiento ea = new EstadoAsiento();
                ea.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                ea.setNombre(rs.getString("nombre"));
                lista.add(ea);
            }
        }
        return lista;
    }

    /**
     * Inserta un nuevo estado de asiento en la base de datos.
     *
     * @param ea Objeto {@link EstadoAsiento} con los datos a registrar.
     * @throws SQLException Si ocurre un error durante la inserción SQL.
     */
    @Override
    public void insertar(EstadoAsiento ea) throws SQLException {
        String query = "INSERT INTO estado_asientos (nombre) VALUES (?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, ea.getNombre());
            pst.executeUpdate();
        }
    }

    /**
     * Busca y devuelve un estado de asiento específico por su ID.
     *
     * @param id Identificador del estado.
     * @return Objeto {@link EstadoAsiento} si se encuentra, o null si no existe.
     * @throws SQLException Si ocurre un error durante la búsqueda SQL.
     */
    @Override
    public EstadoAsiento leer(int id) throws SQLException {
        String query = "SELECT * FROM estado_asientos WHERE id_estado_asiento = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    EstadoAsiento ea = new EstadoAsiento();
                    ea.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    ea.setNombre(rs.getString("nombre"));
                    return ea;
                }
            }
        }
        return null;
    }

    /**
     * Actualiza el nombre de un estado de asiento existente.
     *
     * @param ea Objeto {@link EstadoAsiento} con el nuevo nombre y su ID.
     * @throws SQLException Si ocurre un error durante la actualización SQL.
     */
    @Override
    public void editar(EstadoAsiento ea) throws SQLException {
        String query = "UPDATE estado_asientos SET nombre = ? WHERE id_estado_asiento = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, ea.getNombre());
            pst.setInt(2, ea.getIdEstadoAsiento());
            pst.executeUpdate();
        }
    }

    /**
     * Elimina un estado de asiento de la base de datos según su ID.
     *
     * @param id Identificador del estado a eliminar.
     * @throws SQLException Si ocurre un error durante la eliminación SQL.
     */
    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM estado_asientos WHERE id_estado_asiento = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
