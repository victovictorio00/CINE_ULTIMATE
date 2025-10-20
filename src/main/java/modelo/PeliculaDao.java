package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class PeliculaDao implements DaoCrud<Pelicula> {

    @Override
    public List<Pelicula> listar() throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        String query = "SELECT * FROM peliculas";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setIdPelicula(rs.getInt("id_pelicula"));
                pelicula.setNombre(rs.getString("nombre"));
                pelicula.setSinopsis(rs.getString("sinopsis"));
                pelicula.setFoto(rs.getBytes("foto"));
                pelicula.setIdGenero(new Genero(rs.getInt("id_genero"), null));
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

    @Override
    public void insertar(Pelicula pelicula) throws SQLException {
        String sql = "INSERT INTO peliculas (nombre, sinopsis, id_genero, foto, fecha_estreno, precio, trailer_url) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, pelicula.getNombre());
            pst.setString(2, pelicula.getSinopsis());
            pst.setInt(3, pelicula.getIdGenero().getIdGenero());

            // Foto (puede ser null)
            if (pelicula.getFoto() != null) {
                pst.setBytes(4, pelicula.getFoto());
            } else {
                pst.setNull(4, Types.BLOB);
            }

            // Fecha (puede ser null)
            if (pelicula.getFechaEstreno() != null) {
                pst.setDate(5, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
            } else {
                pst.setNull(5, Types.DATE);
            }

            // Precio
            if (pelicula.getPrecio() != null) {
                pst.setDouble(6, pelicula.getPrecio());
            } else {
                pst.setNull(6, Types.DECIMAL);
            }

            // Trailer URL
            if (pelicula.getTrailerUrl() != null) {
                pst.setString(7, pelicula.getTrailerUrl());
            } else {
                pst.setNull(7, Types.VARCHAR);
            }

            pst.executeUpdate();
        }
    }

    @Override
    public Pelicula leer(int id) throws SQLException {
        String query = "SELECT p.*, g.nombre AS nombre_genero " +
                       "FROM peliculas p " +
                       "INNER JOIN generos g ON p.id_genero = g.id_genero " +
                       "WHERE p.id_pelicula = ?";

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
        return null;
    }

   @Override
public void editar(Pelicula pelicula) throws SQLException {
    String sqlConFoto = "UPDATE peliculas SET nombre=?, sinopsis=?, id_genero=?, fecha_estreno=?, precio=?, trailer_url=?, foto=? WHERE id_pelicula=?";
    String sqlSinFoto = "UPDATE peliculas SET nombre=?, sinopsis=?, id_genero=?, fecha_estreno=?, precio=?, trailer_url=? WHERE id_pelicula=?";

    try (Connection con = Conexion.getConnection()) {
        if (pelicula.getFoto() != null) { // 👉 si viene una nueva foto
            try (PreparedStatement pst = con.prepareStatement(sqlConFoto)) {
                pst.setString(1, pelicula.getNombre());
                pst.setString(2, pelicula.getSinopsis());
                pst.setInt(3, pelicula.getIdGenero().getIdGenero());
                pst.setDate(4, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
                pst.setDouble(5, pelicula.getPrecio());
                pst.setString(6, pelicula.getTrailerUrl());
                pst.setBytes(7, pelicula.getFoto());  // se guarda la nueva foto
                pst.setInt(8, pelicula.getIdPelicula());
                pst.executeUpdate();
            }
        } else { // 👉 no hay foto nueva → mantener la anterior
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


    @Override
    public void eliminar(int id) throws SQLException {
        try (Connection con = Conexion.getConnection()) {
            // Eliminar funciones asociadas primero
            String sqlFunciones = "DELETE FROM funciones WHERE id_pelicula = ?";
            try (PreparedStatement pst = con.prepareStatement(sqlFunciones)) {
                pst.setInt(1, id);
                pst.executeUpdate();
            }

            // Luego eliminar película
            String sqlPelicula = "DELETE FROM peliculas WHERE id_pelicula = ?";
            try (PreparedStatement pst = con.prepareStatement(sqlPelicula)) {
                pst.setInt(1, id);
                pst.executeUpdate();
            }
        }
    }

    // Obtener foto por id (para ImageServlet)
    public byte[] obtenerFotoPorId(int idPelicula) throws SQLException {
        String sql = "SELECT foto FROM peliculas WHERE id_pelicula = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idPelicula);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("foto");
                }
            }
        }
        return null;
    }

    // Listar con filtros dinámicos (género y fecha)
    public List<Pelicula> getPeliculasFiltradas(String generoIdString, String fechaSeleccionadaString) throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM peliculas WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (generoIdString != null && !generoIdString.isEmpty()) {
            try {
                int idGenero = Integer.parseInt(generoIdString);
                queryBuilder.append(" AND id_genero = ?");
                parameters.add(idGenero);
            } catch (NumberFormatException e) {
                // ignorar error de formato
            }
        }

        if (fechaSeleccionadaString != null && !fechaSeleccionadaString.isEmpty()) {
            queryBuilder.append(" AND DATE(fecha_estreno) = ?");
            parameters.add(java.sql.Date.valueOf(fechaSeleccionadaString));
        }

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
                    pelicula.setIdGenero(new Genero(rs.getInt("id_genero"), null));
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
