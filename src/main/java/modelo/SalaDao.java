package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class SalaDao implements DaoCrud<Sala> {
    
    // ==========================================================
    // MÉTODO: LISTAR TODAS LAS SALAS
    // ==========================================================
    @Override
    public List<Sala> listar() throws SQLException {
        // Lista para almacenar las salas obtenidas de la base de datos
        List<Sala> salas = new ArrayList<>();
        String query = "SELECT * FROM salas";

        // Conexión a la base de datos y ejecución de la consulta
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            // Itera sobre los resultados y crea objetos Sala
            while (rs.next()) {
                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                sala.setNombre(rs.getString("nombre"));
                sala.setCapacidad(rs.getInt("capacidad"));
                salas.add(sala); // Añade cada sala a la lista
            }
        }
        return salas;
    }

    // ==========================================================
    // MÉTODO: INSERTAR NUEVA SALA
    // ==========================================================
    @Override
    public void insertar(Sala emp) throws SQLException {
        // Inserta una nueva sala con su nombre y capacidad
        String query = "INSERT INTO salas (nombre, capacidad) VALUES (?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // Asignar parámetros a la consulta
            pst.setString(1, emp.getNombre());
            pst.setInt(2, emp.getCapacidad());

            // Ejecutar la inserción
            pst.executeUpdate();
        }
    }

    // ==========================================================
    // MÉTODO: LEER UNA SALA POR SU ID
    // ==========================================================
    @Override
    public Sala leer(int id) throws SQLException {
        // Busca una sala específica según su identificador
        String query = "SELECT * FROM salas WHERE id_sala = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // Establece el parámetro de búsqueda
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                // Si existe la sala, se devuelve el objeto completo
                if (rs.next()) {
                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    sala.setNombre(rs.getString("nombre"));
                    sala.setCapacidad(rs.getInt("capacidad"));
                    return sala;
                }
            }
        }
        return null; // Si no encuentra la sala, devuelve null
    }

    // ==========================================================
    // MÉTODO: EDITAR (ACTUALIZAR) UNA SALA
    // ==========================================================
    @Override
    public void editar(Sala emp) throws SQLException {
        // Actualiza los datos de una sala ya registrada
        String query = "UPDATE salas SET nombre = ?, capacidad = ? WHERE id_sala = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // Asignar nuevos valores
            pst.setString(1, emp.getNombre());
            pst.setInt(2, emp.getCapacidad());
            pst.setInt(3, emp.getIdSala());

            // Ejecutar actualización
            pst.executeUpdate();
        }
    }

    // ==========================================================
    // MÉTODO: ELIMINAR SALA
    // ==========================================================
    @Override
    public void eliminar(int id) throws SQLException {
        // Elimina una sala de la base de datos según su ID
        String query = "DELETE FROM salas WHERE id_sala = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // Asignar el ID de la sala a eliminar
            pst.setInt(1, id);

            // Ejecutar la eliminación
            pst.executeUpdate();
        }
    }
}
