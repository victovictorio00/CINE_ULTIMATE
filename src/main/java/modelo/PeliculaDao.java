/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class PeliculaDao implements DaoCrud<Pelicula> {

    @Override
    public List<Pelicula> listar() throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        String query = "SELECT * FROM peliculas";  // Consulta para listar todas las películas
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setId(rs.getInt("id"));
                pelicula.setNombre(rs.getString("nombre"));
                pelicula.setSinopsis(rs.getString("sinopsis"));
                pelicula.setHorario(rs.getString("horario"));
                pelicula.setFoto(rs.getBytes("foto"));

                peliculas.add(pelicula);
            }
        }
        return peliculas;
    }

    @Override
    public void insertar(Pelicula pelicula) throws SQLException {
        String query = "INSERT INTO peliculas (nombre, sinopsis, horario, foto) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, pelicula.getNombre());
            pst.setString(2, pelicula.getSinopsis());
            pst.setString(3, pelicula.getHorario());
            pst.setBytes(4, pelicula.getFoto());

            pst.executeUpdate();  // Ejecuta la inserción en la base de datos
        }
    }

    @Override
    public Pelicula leer(int id) throws SQLException {
        String query = "SELECT * FROM peliculas WHERE id = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(rs.getInt("id"));
                    pelicula.setNombre(rs.getString("nombre"));
                    pelicula.setSinopsis(rs.getString("sinopsis"));
                    pelicula.setHorario(rs.getString("horario"));
                    pelicula.setFoto(rs.getBytes("foto"));

                    return pelicula;  // Devuelve la película con los datos obtenidos de la base de datos
                }
            }
        }
        return null;  // Si no se encuentra la película, retorna null
    }

    @Override
    public void editar(Pelicula pelicula) throws SQLException {
        String query = "UPDATE peliculas SET nombre = ?, sinopsis = ?, horario = ?, foto = ?  WHERE id = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, pelicula.getNombre());
            pst.setString(2, pelicula.getSinopsis());
            pst.setString(3, pelicula.getHorario());
            pst.setBytes(4, pelicula.getFoto());
            pst.setInt(5, pelicula.getId());
            pst.executeUpdate();  // Ejecuta la actualización en la base de datos
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM peliculas WHERE id = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();  // Ejecuta la eliminación de la película
        }
    }
    
    
    public void actualizar(Pelicula pelicula) throws SQLException {
    String query = "UPDATE  peliculas SET nombre = ?, sinopsis = ?, horario = ?, foto = ?  WHERE id = ?";
    try (Connection con = Conexion.getConnection();
         PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, pelicula.getNombre());
            pst.setString(2, pelicula.getSinopsis());
            pst.setString(3, pelicula.getHorario());
            pst.setBytes(4, pelicula.getFoto());
            pst.setInt(5, pelicula.getId());
            pst.executeUpdate();  // Ejecuta la actualización en la base de datos
        }
}
}
