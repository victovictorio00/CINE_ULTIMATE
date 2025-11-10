package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class GeneroDao {

    // ==========================================================
    // MÉTODO: OBTENER TODOS LOS GÉNEROS
    // ==========================================================
    /**
     * Recupera todos los géneros disponibles en la tabla "generos".
     * 
     * @return Lista de objetos Genero con su ID y nombre.
     * @throws SQLException si ocurre un error en la conexión o ejecución de la consulta.
     */
    public List<Genero> getTodosLosGeneros() throws SQLException {
        // Lista donde se almacenarán los géneros obtenidos
        List<Genero> lista = new ArrayList<>();

        // Consulta SQL para seleccionar todos los registros de la tabla generos
        String sql = "SELECT id_genero, nombre FROM generos";

        // Conexión a la base de datos utilizando try-with-resources
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            // Iterar sobre los resultados del ResultSet
            while (rs.next()) {
                // Crear un objeto Genero por cada fila obtenida
                Genero genero = new Genero();
                genero.setIdGenero(rs.getInt("id_genero"));  // Asignar ID
                genero.setNombre(rs.getString("nombre"));    // Asignar nombre
                lista.add(genero);                           // Agregar a la lista
            }
        }

        // Retornar la lista completa de géneros
        return lista;
    }
}
