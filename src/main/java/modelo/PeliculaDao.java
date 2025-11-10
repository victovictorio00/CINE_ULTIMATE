package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class PeliculaDao implements DaoCrud<Pelicula> {

    // ==========================================================
    // MÉTODO: LISTAR TODAS LAS PELÍCULAS (JOIN CON GÉNERO)
    // ==========================================================
    @Override
    public List<Pelicula> listar() throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        String query
                = "SELECT p.id_pelicula, p.nombre, p.sinopsis, p.foto, p.id_genero, "
                + "       p.fecha_estreno, p.precio, p.trailer_url, "
                + "       g.nombre AS nombre_genero "
                + "FROM peliculas p "
                + "JOIN generos g ON p.id_genero = g.id_genero";

        // Ejecuta la consulta y mapea cada registro a un objeto Pelicula
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                pelicula.setNombre(rs.getString("nombre"));
                pelicula.setSinopsis(rs.getString("sinopsis"));
                pelicula.setFoto(rs.getBytes("foto"));  // Imagen almacenada como bytes
                pelicula.setIdGenero(new Genero(rs.getInt("id_genero"), rs.getString("nombre_genero")));
                pelicula.setFechaEstreno(rs.getDate("fecha_estreno"));
                pelicula.setPrecio(rs.getDouble("precio"));
                pelicula.setTrailerUrl(rs.getString("trailer_url"));
                peliculas.add(pelicula);
            }

        } catch (SQLException e) {
            System.err.println("⚠️ Error en PeliculaDao.listar(): " + e.getMessage());
            throw e;
        }
        return peliculas;
    }

    // ==========================================================
    // MÉTODO: INSERTAR UNA NUEVA PELÍCULA
    // ==========================================================
    @Override
    public void insertar(Pelicula pelicula) throws SQLException {
        String sql = "INSERT INTO peliculas (nombre, sinopsis, id_genero, foto, fecha_estreno, precio, trailer_url) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, pelicula.getNombre());
            pst.setString(2, pelicula.getSinopsis());
            pst.setInt(3, pelicula.getIdGenero().getIdGenero());

            // Foto opcional (puede ser nula)
            if (pelicula.getFoto() != null) {
                pst.setBytes(4, pelicula.getFoto());
            } else {
                pst.setNull(4, Types.BLOB);
            }

            // Fecha de estreno opcional
            if (pelicula.getFechaEstreno() != null) {
                pst.setDate(5, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
            } else {
                pst.setNull(5, Types.DATE);
            }

            // Precio opcional
            if (pelicula.getPrecio() != null) {
                pst.setDouble(6, pelicula.getPrecio());
            } else {
                pst.setNull(6, Types.DECIMAL);
            }

            // URL del trailer opcional
            if (pelicula.getTrailerUrl() != null) {
                pst.setString(7, pelicula.getTrailerUrl());
            } else {
                pst.setNull(7, Types.VARCHAR);
            }

            pst.executeUpdate(); // Ejecutar inserción
        }
    }

    // ==========================================================
    // MÉTODO: LEER UNA PELÍCULA POR ID (JOIN CON GÉNERO)
    // ==========================================================
    @Override
    public Pelicula leer(int id) throws SQLException {
        String query = "SELECT p.*, g.nombre AS nombre_genero "
                + "FROM peliculas p "
                + "INNER JOIN generos g ON p.id_genero = g.id_genero "
                + "WHERE p.id_pelicula = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                    pelicula.setNombre(rs.getString("nombre"));
                    pelicula.setSinopsis(rs.getString("sinopsis"));
                    pelicula.setIdGenero(new Genero(rs.getInt("id_genero"), rs.getString("nombre_genero")));
                    pelicula.setFoto(rs.getBytes("foto"));
                    pelicula.setFechaEstreno(rs.getDate("fecha_estreno"));
                    pelicula.setPrecio(rs.getDouble("precio"));
                    pelicula.setTrailerUrl(rs.getString("trailer_url"));
                    return pelicula;
                }
            }
        }
        return null; // Si no encuentra la película
    }

    // ==========================================================
    // MÉTODO: EDITAR (ACTUALIZAR) UNA PELÍCULA
    // ==========================================================
    @Override
    public void editar(Pelicula pelicula) throws SQLException {
        // Dos versiones del SQL: con o sin foto
        String sqlConFoto = "UPDATE peliculas SET nombre=?, sinopsis=?, id_genero=?, fecha_estreno=?, precio=?, trailer_url=?, foto=? WHERE id_pelicula=?";
        String sqlSinFoto = "UPDATE peliculas SET nombre=?, sinopsis=?, id_genero=?, fecha_estreno=?, precio=?, trailer_url=? WHERE id_pelicula=?";

        try (Connection con = Conexion.getConnection()) {
            if (pelicula.getFoto() != null) {
                // Actualiza también la foto
                try (PreparedStatement pst = con.prepareStatement(sqlConFoto)) {
                    pst.setString(1, pelicula.getNombre());
                    pst.setString(2, pelicula.getSinopsis());
                    pst.setInt(3, pelicula.getIdGenero().getIdGenero());
                    pst.setDate(4, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
                    pst.setDouble(5, pelicula.getPrecio());
                    pst.setString(6, pelicula.getTrailerUrl());
                    pst.setBytes(7, pelicula.getFoto());
                    pst.setInt(8, pelicula.getIdPelicula());
                    pst.executeUpdate();
                }
            } else {
                // No cambia la foto
                try (PreparedStatement pst = con.prepareStatement(sqlSinFoto)) {
                    pst.setString(1, pelicula.getNombre());
                    pst.setString(2, pelicula.getSinopsis());
                    pst.setInt(3, pelicula.getIdGenero().getIdGenero());
                    pst.setDate(4, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
                    pst.setDouble(5, pelicula.getPrecio());
                    pst.setString(6, pelicula.getTrailerUrl());
                    pst.setInt(7, pelicula.getIdPelicula());
                    pst.executeUpdate();
                }
            }
        }
    }

    // ==========================================================
    // MÉTODO: ELIMINAR UNA PELÍCULA (Y SUS FUNCIONES ASOCIADAS)
    // ==========================================================
    @Override
    public void eliminar(int id) throws SQLException {
        try (Connection con = Conexion.getConnection()) {
            // Primero se eliminan las funciones asociadas
            String sqlFunciones = "DELETE FROM funciones WHERE id_pelicula = ?";
            try (PreparedStatement pst = con.prepareStatement(sqlFunciones)) {
                pst.setInt(1, id);
                pst.executeUpdate();
            }

            // Luego se elimina la película
            String sqlPelicula = "DELETE FROM peliculas WHERE id_pelicula = ?";
            try (PreparedStatement pst = con.prepareStatement(sqlPelicula)) {
                pst.setInt(1, id);
                pst.executeUpdate();
            }
        }
    }

    // ==========================================================
    // MÉTODO: OBTENER LA FOTO DE UNA PELÍCULA POR SU ID
    // ==========================================================
    public byte[] obtenerFotoPorId(int idPelicula) throws SQLException {
        String sql = "SELECT foto FROM peliculas WHERE id_pelicula = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idPelicula);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("foto"); // Devuelve los bytes de la imagen
                }
            }
        }
        return null;
    }

    // ==========================================================
    // MÉTODO: LISTAR PELÍCULAS CON FILTROS (GÉNERO Y FECHA)
    // ==========================================================
    public List<Pelicula> getPeliculasFiltradas(String generoIdString, String fechaSeleccionadaString) throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();

        // Construye la consulta dinámica según los filtros
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT p.*, g.nombre AS nombre_genero "
                + "FROM peliculas p "
                + "LEFT JOIN generos g ON p.id_genero = g.id_genero "
                + "WHERE 1=1"
        );
        List<Object> parameters = new ArrayList<>();

        // Filtro por género (si está presente)
        if (generoIdString != null && !generoIdString.isEmpty()) {
            try {
                int idGenero = Integer.parseInt(generoIdString);
                queryBuilder.append(" AND p.id_genero = ?");
                parameters.add(idGenero);
            } catch (NumberFormatException e) {
                // Si el formato no es válido, se ignora
            }
        }

        // Filtro por fecha de estreno (si está presente)
        if (fechaSeleccionadaString != null && !fechaSeleccionadaString.isEmpty()) {
            queryBuilder.append(" AND DATE(fecha_estreno) = ?");
            parameters.add(java.sql.Date.valueOf(fechaSeleccionadaString));
        }

        // Ejecuta la consulta final con los filtros aplicados
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(queryBuilder.toString())) {

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
                    pelicula.setIdGenero(new Genero(rs.getInt("id_genero"), rs.getString("nombre_genero")));
                    pelicula.setFechaEstreno(rs.getDate("fecha_estreno"));
                    pelicula.setPrecio(rs.getDouble("precio"));
                    pelicula.setTrailerUrl(rs.getString("trailer_url"));
                    peliculas.add(pelicula);
                }
            }
        }
        return peliculas;
    }
}
