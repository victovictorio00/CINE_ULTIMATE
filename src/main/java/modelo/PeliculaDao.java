package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class PeliculaDao implements DaoCrud<Pelicula> {

   @Override
public List<Pelicula> listar() throws SQLException {
    List<Pelicula> peliculas = new ArrayList<>();
    String sql = "{CALL listarPeliculas()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

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

    } catch (SQLException e) {
        System.err.println("⚠️ Error en PeliculaDao.listar(): " + e.getMessage());
        throw e;
    }

    return peliculas;
}


   @Override
public void insertar(Pelicula pelicula) throws SQLException {
    String sql = "{CALL insertarPelicula(?, ?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setString(1, pelicula.getNombre());
        cst.setString(2, pelicula.getSinopsis());
        cst.setInt(3, pelicula.getIdGenero().getIdGenero());

        if (pelicula.getFoto() != null) {
            cst.setBytes(4, pelicula.getFoto());
        } else {
            cst.setNull(4, Types.BLOB);
        }

        if (pelicula.getFechaEstreno() != null) {
            cst.setDate(5, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
        } else {
            cst.setNull(5, Types.DATE);
        }

        if (pelicula.getPrecio() != null) {
            cst.setDouble(6, pelicula.getPrecio());
        } else {
            cst.setNull(6, Types.DECIMAL);
        }

        if (pelicula.getTrailerUrl() != null) {
            cst.setString(7, pelicula.getTrailerUrl());
        } else {
            cst.setNull(7, Types.VARCHAR);
        }

        cst.executeUpdate();
    }
}


    @Override
public Pelicula leer(int id) throws SQLException {
    String sql = "{CALL obtenerPeliculaPorId(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);

        try (ResultSet rs = cst.executeQuery()) {
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
    String sql = "{CALL editarPelicula(?, ?, ?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, pelicula.getIdPelicula());
        cst.setString(2, pelicula.getNombre());
        cst.setString(3, pelicula.getSinopsis());
        cst.setInt(4, pelicula.getIdGenero().getIdGenero());
        cst.setDate(5, new java.sql.Date(pelicula.getFechaEstreno().getTime()));
        cst.setDouble(6, pelicula.getPrecio());
        cst.setString(7, pelicula.getTrailerUrl());

        if (pelicula.getFoto() != null) {
            cst.setBytes(8, pelicula.getFoto());
        } else {
            cst.setNull(8, Types.BLOB);
        }

        cst.executeUpdate();
    }
}


   @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarPelicula(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);
        cst.executeUpdate();
    }
}


    // Obtener foto por id (para ImageServlet)
   public byte[] obtenerFotoPorId(int idPelicula) throws SQLException {
    String sql = "{CALL obtenerFotoPelicula(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, idPelicula);

        try (ResultSet rs = cst.executeQuery()) {
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
    String sql = "{CALL getPeliculasFiltradas(?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        // idGenero
        if (generoIdString != null && !generoIdString.isEmpty()) {
            cst.setInt(1, Integer.parseInt(generoIdString));
        } else {
            cst.setNull(1, Types.INTEGER);
        }

        // fechaEstreno
        if (fechaSeleccionadaString != null && !fechaSeleccionadaString.isEmpty()) {
            cst.setDate(2, java.sql.Date.valueOf(fechaSeleccionadaString));
        } else {
            cst.setNull(2, Types.DATE);
        }

        try (ResultSet rs = cst.executeQuery()) {
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
