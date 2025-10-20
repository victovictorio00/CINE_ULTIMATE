package modelo.Cliente;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDaoCliente {

    // 🔹 Listar todas las películas para el cliente
    public List<Pelicula> listar() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT p.id_pelicula, p.nombre, p.sinopsis, g.nombre AS genero, p.trailer_url, p.foto " +
                     "FROM peliculas p " +
                     "INNER JOIN generos g ON p.id_genero = g.id_genero";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pelicula peli = new Pelicula(
                        rs.getInt("id_pelicula"),
                        rs.getString("nombre"),
                        rs.getString("sinopsis"),
                        rs.getString("genero"),
                        rs.getString("trailer_url"),
                        convertirFotoEnBase64(rs.getBytes("foto"))
                );
                lista.add(peli);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // 🔹 Obtener una película por ID
    public Pelicula getPeliculaById(int id) {
        Pelicula pelicula = null;
        String sql = "SELECT p.id_pelicula, p.nombre, p.sinopsis, g.nombre AS genero, p.trailer_url, p.foto " +
                     "FROM peliculas p " +
                     "INNER JOIN generos g ON p.id_genero = g.id_genero " +
                     "WHERE p.id_pelicula = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pelicula = new Pelicula(
                        rs.getInt("id_pelicula"),
                        rs.getString("nombre"),
                        rs.getString("sinopsis"),
                        rs.getString("genero"),
                        rs.getString("trailer_url"),
                        convertirFotoEnBase64(rs.getBytes("foto"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pelicula;
    }

    // 🔹 Convierte BLOB → Base64 para mostrar imagen
    private String convertirFotoEnBase64(byte[] fotoBytes) {
        if (fotoBytes == null) {
            return "";
        }
        return "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(fotoBytes);
    }
}
