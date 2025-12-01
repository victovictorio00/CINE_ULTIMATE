package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class GeneroDao {

    // Método para obtener todos los géneros - única necesaria
     public List<Genero> getTodosLosGeneros() throws SQLException {
    List<Genero> lista = new ArrayList<>();
    String sql = "{CALL listarGeneros()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

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
