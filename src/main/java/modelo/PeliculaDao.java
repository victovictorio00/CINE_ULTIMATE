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

    public List<Pelicula> getPeliculasFiltradas(String generoIdString, String fechaSeleccionadaString) throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        // 1. Construcción dinámica de la consulta
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM peliculas WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        // --- 1.1 FILTRO POR GÉNERO (Se mantiene la lógica) ---
        // ... (Tu código para el filtro de género, que es correcto) ...
        if (generoIdString != null && !generoIdString.isEmpty()) {
            try {
                int idGenero = Integer.parseInt(generoIdString);
                queryBuilder.append(" AND id_genero = ?");
                parameters.add(idGenero);
            } catch (NumberFormatException e) {
                // Manejo de error...
            }
        }
        // --- 1.2 FILTRO POR FECHA EXACTA (El cambio importante) ---
        if (fechaSeleccionadaString != null && !fechaSeleccionadaString.isEmpty()) {
            // CAMBIO CLAVE: 
            // 1. Usamos el operador '=' (igual) en lugar de '<=' para buscar la fecha exacta.
            // 2. Usamos DATE(columna) para ignorar la parte de la hora y garantizar la comparación.
            queryBuilder.append(" AND DATE(fecha_estreno) = ?");
            parameters.add(java.sql.Date.valueOf(fechaSeleccionadaString)); // 'YYYY-MM-DD' a SQL Date
        }
        // 2. Ejecución de la consulta (igual que antes)
        String finalQuery = queryBuilder.toString();
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(finalQuery)) {
            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // ... (Tu código para mapear ResultSet a Pelicula)
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
