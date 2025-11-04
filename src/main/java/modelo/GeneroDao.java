
package modelo; // o dao, según tu organización

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Conexion.Conexion;

public class GeneroDao {

    // Método para obtener todos los géneros
  public List<Genero> getTodosLosGeneros() throws SQLException {
        List<Genero> lista = new ArrayList<>();
        String sql = "SELECT id_genero, nombre FROM generos"; // tabla en minúsculas, como en tu BD

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Genero genero = new Genero();
                genero.setIdGenero(rs.getInt("id_genero"));
                genero.setNombre(rs.getString("nombre"));
                lista.add(genero);
            }
        }
        return lista;
    }
    
    
    
    
    
    
    
}
