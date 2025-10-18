package modelo;

import java.sql.*;
import java.util.*;
import Conexion.Conexion;

public class FuncionDao {
        // 🔹 Listar todas las funciones
    public List<Funcion> listar() throws SQLException {
        List<Funcion> lista = new ArrayList<>();

        String sql =
            "SELECT f.*, p.nombre AS pelicula_nombre, s.nombre AS sala_nombre, e.nombre AS estado_nombre " +
            "FROM funciones f " +
            "JOIN peliculas p ON f.id_pelicula = p.id_pelicula " +
            "JOIN salas s ON f.id_sala = s.id_sala " +
            "JOIN estado_funciones e ON f.id_estado_funcion = e.id_estado_funcion";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Funcion f = new Funcion();
                f.setIdFuncion(rs.getInt("id_funcion"));

                Pelicula p = new Pelicula();
                p.setIdPelicula(rs.getInt("id_pelicula"));
                p.setNombre(rs.getString("pelicula_nombre"));
                f.setPelicula(p);

                Sala s = new Sala();
                s.setIdSala(rs.getInt("id_sala"));
                s.setNombre(rs.getString("sala_nombre"));
                f.setSala(s);

                f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                f.setFechaFin(rs.getTimestamp("fecha_fin"));

                EstadoFuncion est = new EstadoFuncion();
                est.setIdEstadoFuncion(rs.getInt("id_estado_funcion"));
                est.setNombre(rs.getString("estado_nombre"));
                f.setEstadoFuncion(est);

                f.setAsientosDisponibles(rs.getInt("asientos_disponibles"));
                lista.add(f);
            }
        }
        return lista;
    }
    
    // 🔹 Insertar una nueva función
    public void insertar(Funcion f) throws SQLException {
        String sql = "INSERT INTO funciones (id_pelicula, id_sala, fecha_inicio, fecha_fin, id_estado_funcion, asientos_disponibles) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, f.getPelicula().getIdPelicula());
            ps.setInt(2, f.getSala().getIdSala());
            ps.setTimestamp(3, f.getFechaInicio());
            ps.setTimestamp(4, f.getFechaFin());
            ps.setInt(5, f.getEstadoFuncion().getIdEstadoFuncion());
            ps.setInt(6, f.getAsientosDisponibles());
            ps.executeUpdate();
        }
    }
     // 🔹 Eliminar una función
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM funciones WHERE id_funcion = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    // 🔹 Leer una función (para editar)
    public Funcion leer(int id) throws SQLException {
        Funcion f = null;
        String sql =
            "SELECT f.*, p.nombre AS pelicula_nombre, s.nombre AS sala_nombre, e.nombre AS estado_nombre " +
            "FROM funciones f " +
            "JOIN peliculas p ON f.id_pelicula = p.id_pelicula " +
            "JOIN salas s ON f.id_sala = s.id_sala " +
            "JOIN estado_funciones e ON f.id_estado_funcion = e.id_estado_funcion " +
            "WHERE f.id_funcion = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    f = new Funcion();
                    f.setIdFuncion(rs.getInt("id_funcion"));

                    Pelicula p = new Pelicula();
                    p.setIdPelicula(rs.getInt("id_pelicula"));
                    p.setNombre(rs.getString("pelicula_nombre"));
                    f.setPelicula(p);

                    Sala s = new Sala();
                    s.setIdSala(rs.getInt("id_sala"));
                    s.setNombre(rs.getString("sala_nombre"));
                    f.setSala(s);

                    f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                    f.setFechaFin(rs.getTimestamp("fecha_fin"));

                    EstadoFuncion est = new EstadoFuncion();
                    est.setIdEstadoFuncion(rs.getInt("id_estado_funcion"));
                    est.setNombre(rs.getString("estado_nombre"));
                    f.setEstadoFuncion(est);

                    f.setAsientosDisponibles(rs.getInt("asientos_disponibles"));
                }
            }
        }
        return f;
    }
    
}
