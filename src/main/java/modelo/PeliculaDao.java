package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Conexion.Conexion;

public class PeliculaDao implements DaoCrud<Pelicula> {

    @Override
    public List<Pelicula> listar() throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        String query = "SELECT * FROM peliculas";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                pelicula.setNombre(rs.getString("nombre"));
                pelicula.setSinopsis(rs.getString("sinopsis"));
                pelicula.setFoto(rs.getBytes("foto"));
                pelicula.setIdGenero(new Genero(rs.getInt("id_genero"), null));
                pelicula.setFechaEstreno(rs.getDate("fecha_estreno"));
                pelicula.setPrecio(rs.getDouble("precio"));

                peliculas.add(pelicula);
            }
        }
        return peliculas;
    }

    @Override
    public void insertar(Pelicula pelicula) throws SQLException {
        String query = "INSERT INTO peliculas (nombre, sinopsis, id_genero, foto, fecha_estreno, precio) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, pelicula.getNombre());
            pst.setString(2, pelicula.getSinopsis());
            pst.setInt(3, pelicula.getIdGenero().getIdGenero());
            pst.setBytes(4, pelicula.getFoto());
            pst.setDate(5, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
            pst.setDouble(6, pelicula.getPrecio());

            pst.executeUpdate();
        }
    }

    @Override
    public Pelicula leer(int id) throws SQLException {
        String query = "SELECT * FROM peliculas WHERE id_pelicula = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                    pelicula.setNombre(rs.getString("nombre"));
                    pelicula.setSinopsis(rs.getString("sinopsis"));
                    pelicula.setIdGenero(new Genero(rs.getInt("id_genero"), null));
                    pelicula.setFoto(rs.getBytes("foto"));
                    pelicula.setFechaEstreno(rs.getDate("fecha_estreno"));
                    pelicula.setPrecio(rs.getDouble("precio"));

                    return pelicula;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(Pelicula pelicula) throws SQLException {
        String query = "UPDATE peliculas SET nombre = ?, sinopsis = ?, id_genero = ?, foto = ?, fecha_estreno = ?, precio = ? WHERE id_pelicula = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, pelicula.getNombre());
            pst.setString(2, pelicula.getSinopsis());
            pst.setInt(3, pelicula.getIdGenero().getIdGenero());
            pst.setBytes(4, pelicula.getFoto());
            pst.setDate(5, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
            pst.setDouble(6, pelicula.getPrecio());
            pst.setInt(7, pelicula.getIdPelicula());

            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM peliculas WHERE id_pelicula = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    // Método con debug para depurar ImageServlet
    public byte[] obtenerFotoPorId(int idPelicula) throws SQLException {
        String sql = "SELECT foto FROM peliculas WHERE id_pelicula = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idPelicula);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    byte[] b = rs.getBytes("foto");
                    return b;
                }
            }
        }
        return null; // Retorna null si no encuentra la foto
    }
    
    public List<Pelicula> getPeliculasFiltradas(String generoIdString, String filtroFecha) throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        
        // 1. Construcción dinámica de la consulta
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM peliculas WHERE 1=1"); // 1=1 permite añadir condiciones fácilmente
        List<Object> parameters = new ArrayList<>();
        
        // --- 1.1 FILTRO POR GÉNERO ---
        if (generoIdString != null && !generoIdString.isEmpty()) {
            try {
                int idGenero = Integer.parseInt(generoIdString);
                queryBuilder.append(" AND id_genero = ?");
                parameters.add(idGenero);
            } catch (NumberFormatException e) {
                // Manejar error si el ID de género no es un número.
                System.err.println("Advertencia: El ID de género proporcionado no es un número válido.");
            }
        }

        if (filtroFecha != null && !filtroFecha.isEmpty()) {
            
            // Usamos la fecha actual para comparar
            Date today = new Date();
            java.sql.Date sqlToday = new java.sql.Date(today.getTime()); 
            
            switch (filtroFecha.toLowerCase()) {
                case "en cartelera":
                    // Películas cuya fecha de estreno es HOY o anterior
                    queryBuilder.append(" AND fecha_estreno <= ?");
                    parameters.add(sqlToday);
                    break;
                case "preventa":
                    // Películas con fecha de estreno posterior (futura)
                    // Podrías añadir lógica más estricta si la Preventa tiene un rango específico
                    queryBuilder.append(" AND fecha_estreno > ?");
                    parameters.add(sqlToday);
                    break;
                case "próximos estrenos":
                    // Similar a Preventa, ajusta esta lógica si quieres distinguirlos
                    queryBuilder.append(" AND fecha_estreno > ?");
                    parameters.add(sqlToday);
                    break;
                // Nota: Si usaste otros nombres de filtros en tu JSP, ajústalos aquí (e.g., "proximos_estrenos")
            }
        }
        
        // 2. Ejecución de la consulta
        String finalQuery = queryBuilder.toString();
        
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(finalQuery)) {
            
            // Asignar los parámetros a la consulta preparada
            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                    pelicula.setNombre(rs.getString("nombre"));
                    pelicula.setSinopsis(rs.getString("sinopsis"));
                    pelicula.setFoto(rs.getBytes("foto"));
                    pelicula.setIdGenero(new Genero(rs.getInt("id_genero"), null));
                    pelicula.setFechaEstreno(rs.getDate("fecha_estreno"));
                    pelicula.setPrecio(rs.getDouble("precio"));
                    
                    peliculas.add(pelicula);
                }
            }
        }
        return peliculas;
    }
}
