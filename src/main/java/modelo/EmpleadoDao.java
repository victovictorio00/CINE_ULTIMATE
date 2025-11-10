package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

/**
 * Clase DAO: EmpleadoDao
 * ------------------------------------------------------
 * Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad {@link Empleado} en la base de datos del sistema CineMax.
 *
 * Los empleados representan al personal administrativo, de atención
 * o de soporte técnico dentro del cine.
 *
 * NOTA: En esta implementación, las consultas SQL son directas (sin procedimientos almacenados).
 */
public class EmpleadoDao implements DaoCrud<Empleado> {

    // ==========================================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================================

    /**
     * Recupera la lista completa de empleados registrados en la base de datos.
     *
     * @return Lista de objetos {@link Empleado}.
     * @throws SQLException Si ocurre un error durante la ejecución del SQL.
     */
    @Override
    public List<Empleado> listar() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String query = "SELECT * FROM empleados";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setIdEmpleado(rs.getInt("id_empleado"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setDireccion(rs.getString("direccion"));
                empleado.setTelefono(rs.getString("telefono"));
                empleado.setCargo(rs.getString("cargo"));
                empleado.setSalario(rs.getDouble("salario"));

                empleados.add(empleado);
            }
        }
        return empleados;
    }

    /**
     * Inserta un nuevo empleado en la base de datos.
     *
     * @param emp Objeto {@link Empleado} con los datos a registrar.
     * @throws SQLException Si ocurre un error durante la inserción SQL.
     */
    @Override
    public void insertar(Empleado emp) throws SQLException {
        String query = "INSERT INTO empleados (nombre, direccion, telefono, cargo, salario) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, emp.getNombre());
            pst.setString(2, emp.getDireccion());
            pst.setString(3, emp.getTelefono());
            pst.setString(4, emp.getCargo());
            pst.setDouble(5, emp.getSalario());
            pst.executeUpdate();
        }
    }

    /**
     * Busca y devuelve un empleado específico según su identificador.
     *
     * @param id Identificador único del empleado.
     * @return Objeto {@link Empleado} si se encuentra, o null si no existe.
     * @throws SQLException Si ocurre un error durante la búsqueda SQL.
     */
    @Override
    public Empleado leer(int id) throws SQLException {
        String query = "SELECT * FROM empleados WHERE id_empleado = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Empleado empleado = new Empleado();
                    empleado.setIdEmpleado(rs.getInt("id_empleado"));
                    empleado.setNombre(rs.getString("nombre"));
                    empleado.setDireccion(rs.getString("direccion"));
                    empleado.setTelefono(rs.getString("telefono"));
                    empleado.setCargo(rs.getString("cargo"));
                    empleado.setSalario(rs.getDouble("salario"));
                    return empleado;
                }
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un empleado existente en la base de datos.
     *
     * @param emp Objeto {@link Empleado} con la información actualizada.
     * @throws SQLException Si ocurre un error durante la actualización.
     */
    @Override
    public void editar(Empleado emp) throws SQLException {
        String query = "UPDATE empleados SET nombre = ?, direccion = ?, telefono = ?, cargo = ?, salario = ? WHERE id_empleado = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, emp.getNombre());
            pst.setString(2, emp.getDireccion());
            pst.setString(3, emp.getTelefono());
            pst.setString(4, emp.getCargo());
            pst.setDouble(5, emp.getSalario());
            pst.setInt(6, emp.getIdEmpleado());

            pst.executeUpdate();
        }
    }

    /**
     * Elimina un empleado de la base de datos según su ID.
     *
     * @param id Identificador del empleado a eliminar.
     * @throws SQLException Si ocurre un error durante la eliminación SQL.
     */
    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM empleados WHERE id_empleado = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
