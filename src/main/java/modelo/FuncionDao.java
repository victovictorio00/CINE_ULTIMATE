package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionDao {

    // Insertar nueva función
    public void insertar(Funcion f) throws SQLException {
        String sql = "INSERT INTO funciones (id_pelicula, id_sala, fecha_inicio, fecha_fin, id_estado_funcion, asientos_disponibles) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, f.getPelicula().getIdPelicula());
            ps.setInt(2, f.getSala().getIdSala());
            ps.setTimestamp(3, f.getFechaInicio());
            ps.setTimestamp(4, f.getFechaFin());
            ps.setInt(5, f.getEstadoFuncion().getIdEstadoFuncion());
            ps.setInt(6, f.getAsientosDisponibles());
            ps.executeUpdate();
        }
    }

    // Actualizar función existente
    public void actualizar(Funcion f) throws SQLException {
        String sql = "UPDATE funciones SET id_pelicula=?, id_sala=?, fecha_inicio=?, fecha_fin=?, id_estado_funcion=?, asientos_disponibles=? "
                + "WHERE id_funcion=?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, f.getPelicula().getIdPelicula());
            ps.setInt(2, f.getSala().getIdSala());
            ps.setTimestamp(3, f.getFechaInicio());
            ps.setTimestamp(4, f.getFechaFin());
            ps.setInt(5, f.getEstadoFuncion().getIdEstadoFuncion());
            ps.setInt(6, f.getAsientosDisponibles());
            ps.setInt(7, f.getIdFuncion());
            ps.executeUpdate();
        }
    }

    // Eliminar función
    public void eliminar(int idFuncion) throws SQLException {
        String sql = "DELETE FROM funciones WHERE id_funcion=?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            ps.executeUpdate();
        }
    }

    // Listar todas las funciones
    // Listar todas las funciones
    public List<Funcion> listar() throws SQLException {
        List<Funcion> lista = new ArrayList<>();
        String sql = "SELECT f.id_funcion, f.fecha_inicio, f.fecha_fin, f.asientos_disponibles, "
                + "p.id_pelicula, p.nombre AS pelicula, "
                + "s.id_sala, s.nombre AS sala, "
                + "e.id_estado_funcion, e.nombre AS estado "
                + "FROM funciones f "
                + "JOIN peliculas p ON f.id_pelicula = p.id_pelicula "
                + "JOIN salas s ON f.id_sala = s.id_sala "
                + "JOIN estado_funciones e ON f.id_estado_funcion = e.id_estado_funcion"; // 👈 corregido
        try (Connection con = Conexion.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Funcion f = new Funcion();
                f.setIdFuncion(rs.getInt("id_funcion"));

                Pelicula p = new Pelicula();
                p.setIdPelicula(rs.getInt("id_pelicula"));
                p.setNombre(rs.getString("pelicula"));
                f.setPelicula(p);

                Sala s = new Sala();
                s.setIdSala(rs.getInt("id_sala"));
                s.setNombre(rs.getString("sala"));
                f.setSala(s);

                EstadoFuncion e = new EstadoFuncion();
                e.setIdEstadoFuncion(rs.getInt("id_estado_funcion"));
                e.setNombre(rs.getString("estado"));
                f.setEstadoFuncion(e);

                f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                f.setFechaFin(rs.getTimestamp("fecha_fin"));
                f.setAsientosDisponibles(rs.getInt("asientos_disponibles"));

                lista.add(f);
            }
        }
        return lista;
    }

    public List<Funcion> obtenerFunciones(int idPelicula) throws SQLException {
        List<Funcion> lista = new ArrayList<>();
        String sql = "SELECT f.id_funcion, f.fecha_inicio, f.fecha_fin, f.asientos_disponibles, "
                + "p.id_pelicula, p.nombre AS pelicula, "
                + "s.id_sala, s.nombre AS sala, "
                + "e.id_estado_funcion, e.nombre AS estado "
                + "FROM funciones f "
                + "JOIN peliculas p ON f.id_pelicula = p.id_pelicula "
                + "JOIN salas s ON f.id_sala = s.id_sala "
                + "JOIN estado_funciones e ON f.id_estado_funcion = e.id_estado_funcion "
                + "WHERE p.id_pelicula = ?";  // ← filtro por película

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPelicula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Funcion f = new Funcion();
                    f.setIdFuncion(rs.getInt("id_funcion"));

                    Pelicula p = new Pelicula();
                    p.setIdPelicula(rs.getInt("id_pelicula"));
                    p.setNombre(rs.getString("pelicula"));
                    f.setPelicula(p);

                    Sala s = new Sala();
                    s.setIdSala(rs.getInt("id_sala"));
                    s.setNombre(rs.getString("sala"));
                    f.setSala(s);

                    EstadoFuncion e = new EstadoFuncion();
                    e.setIdEstadoFuncion(rs.getInt("id_estado_funcion"));
                    e.setNombre(rs.getString("estado"));
                    f.setEstadoFuncion(e);

                    f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                    f.setFechaFin(rs.getTimestamp("fecha_fin"));
                    f.setAsientosDisponibles(rs.getInt("asientos_disponibles"));

                    lista.add(f);
                }
            }
        }
        return lista;
    }

    // Obtener una función por ID
    public Funcion obtener(int idFuncion) throws SQLException {
        String sql = "SELECT * FROM funciones WHERE id_funcion=?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Funcion f = new Funcion();
                f.setIdFuncion(rs.getInt("id_funcion"));

                Pelicula p = new Pelicula();
                p.setIdPelicula(rs.getInt("id_pelicula"));
                f.setPelicula(p);

                Sala s = new Sala();
                s.setIdSala(rs.getInt("id_sala"));
                f.setSala(s);

                EstadoFuncion e = new EstadoFuncion();
                e.setIdEstadoFuncion(rs.getInt("id_estado_funcion"));
                f.setEstadoFuncion(e);

                f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                f.setFechaFin(rs.getTimestamp("fecha_fin"));
                f.setAsientosDisponibles(rs.getInt("asientos_disponibles"));

                return f;
            }
        }
        return null;
    }

    // Verificar si existe conflicto de horarios en una sala
    public boolean existeConflicto(int idSala, Timestamp inicio, Timestamp fin, Integer idFuncionEditar) {
        String sql = "SELECT COUNT(*) FROM funciones "
                + "WHERE id_sala=? "
                + (idFuncionEditar != null ? "AND id_funcion<>? " : "")
                + "AND ( "
                + "   (? BETWEEN fecha_inicio AND fecha_fin) "
                + "OR (? BETWEEN fecha_inicio AND fecha_fin) "
                + "OR (fecha_inicio BETWEEN ? AND ?) "
                + "OR (fecha_fin BETWEEN ? AND ?) "
                + ")";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setInt(i++, idSala);
            if (idFuncionEditar != null) {
                ps.setInt(i++, idFuncionEditar);
            }
            ps.setTimestamp(i++, inicio);
            ps.setTimestamp(i++, fin);
            ps.setTimestamp(i++, inicio);
            ps.setTimestamp(i++, fin);
            ps.setTimestamp(i++, inicio);
            ps.setTimestamp(i++, fin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Funcion leer(int id) throws SQLException {
        String sql
                = "SELECT f.id_funcion, "
                + "       f.id_pelicula, p.nombre AS pelicula, p.precio, "
                + "       f.id_sala, s.nombre AS sala, "
                + "       f.fecha_inicio, f.fecha_fin, "
                + "       f.id_estado_funcion, e.nombre AS estado, "
                + "       f.asientos_disponibles, "
                + "       g.id_genero, g.nombre AS genero "
                + "FROM funciones f "
                + "JOIN peliculas  p ON f.id_pelicula = p.id_pelicula "
                + "JOIN salas      s ON f.id_sala = s.id_sala "
                + "JOIN estado_funciones e ON f.id_estado_funcion = e.id_estado_funcion "
                + "JOIN generos    g ON p.id_genero = g.id_genero "
                + "WHERE f.id_funcion = ?";

        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Funcion f = new Funcion();
                    f.setIdFuncion(rs.getInt("id_funcion"));

                    /* Película con precio y genero */
                    Pelicula p = new Pelicula();
                    p.setIdPelicula(rs.getInt("id_pelicula"));
                    p.setNombre(rs.getString("pelicula"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setIdGenero(new Genero(rs.getInt("id_genero"), rs.getString("genero")));
                    f.setPelicula(p);

                    /* Sala con nombre */
                    Sala s = new Sala();
                    s.setIdSala(rs.getInt("id_sala"));
                    s.setNombre(rs.getString("sala"));
                    f.setSala(s);

                    /* Fechas */
                    f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                    f.setFechaFin(rs.getTimestamp("fecha_fin"));

                    /* Estado */
                    EstadoFuncion ef = new EstadoFuncion();
                    ef.setIdEstadoFuncion(rs.getInt("id_estado_funcion"));
                    ef.setNombre(rs.getString("estado"));
                    f.setEstadoFuncion(ef);

                    f.setAsientosDisponibles(rs.getInt("asientos_disponibles"));
                    return f;
                }
            }
        }
        return null;
    }
}
