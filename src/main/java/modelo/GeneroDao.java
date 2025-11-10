package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class GeneroDao {

    // Método para obtener todos los géneros - única necesaria
     public List<Genero> getTodosLosGeneros() throws SQLException {
        List<Genero> lista = new ArrayList<>();
        String sql = "SELECT id_genero, nombre FROM generos";

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
