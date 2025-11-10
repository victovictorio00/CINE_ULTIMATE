package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Conexion.Conexion;

public class RolDao {

    // ==========================================================
    // MÉTODO: OBTENER TODOS LOS ROLES
    // ==========================================================

    /**
     * Obtiene la lista de todos los roles disponibles en la tabla "roles".
     * 
     * @return Lista de objetos Rol con id y nombre.
     */
    public List<Rol> getTodosLosRoles() {
        // Lista para almacenar los roles obtenidos de la base de datos
        List<Rol> lista = new ArrayList<>();

        // Consulta SQL para seleccionar todos los roles
        String query = "SELECT id_rol, nombre FROM roles";
        
        // Bloque try-with-resources para manejar la conexión y liberar recursos automáticamente
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            
            // Itera sobre el resultado y crea objetos Rol
            while (rs.next()) {
                Rol r = new Rol();
                r.setIdRol(rs.getInt("id_rol"));      // Asigna el ID del rol
                r.setNombre(rs.getString("nombre"));  // Asigna el nombre del rol
                lista.add(r);                         // Agrega el rol a la lista
            }

        } catch (SQLException e) {
            // Muestra un mensaje en caso de error de conexión o ejecución
            JOptionPane.showMessageDialog(null, "Error al cargar los roles: " + e.getMessage());
        }

        // Retorna la lista de roles obtenidos
        return lista;
    }
}
