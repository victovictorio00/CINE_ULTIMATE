
package modelo; // o dao, según tu organización

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Conexion.Conexion;

public class GeneroDao {

    // Método para obtener todos los géneros
    public List<Genero> getTodosLosGeneros() {
        List<Genero> lista = new ArrayList<>();
        String query = "SELECT id_genero, nombre FROM Generos";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Genero g = new Genero();
                g.setIdGenero(rs.getInt("id_genero"));
                g.setNombre(rs.getString("nombre"));
                lista.add(g);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los géneros: " + e.getMessage());
        }
        return lista;
    }
}
