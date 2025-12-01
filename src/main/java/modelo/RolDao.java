package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Conexion.Conexion;

public class RolDao {
    // Método para obtener todos los roles
    public List<Rol> getTodosLosRoles() {
        List<Rol> lista = new ArrayList<>();
        String query = "SELECT id_rol, nombre FROM roles";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Rol r = new Rol();
                r.setIdRol(rs.getInt("id_rol"));
                r.setNombre(rs.getString("nombre"));
                lista.add(r);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los roles: " + e.getMessage());
        }
        return lista;
    }
}
