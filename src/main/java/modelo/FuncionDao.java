package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionDao {

    public void insertar(Funcion f) throws SQLException {
        String sqlFuncion = "INSERT INTO funciones (id_pelicula, id_sala, fecha_inicio, fecha_fin, id_estado_funcion, asientos_disponibles) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlAsientoFuncion = "INSERT INTO asiento_funcion (id_asiento, id_funcion, id_estado_asiento) VALUES (?, ?, 1)";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psFuncion = con.prepareStatement(sqlFuncion, Statement.RETURN_GENERATED_KEYS)) {

                // Insertar función
                psFuncion.setInt(1, f.getPelicula().getIdPelicula());
                psFuncion.setInt(2, f.getSala().getIdSala());
                psFuncion.setTimestamp(3, f.getFechaInicio());
                psFuncion.setTimestamp(4, f.getFechaFin());
                psFuncion.setInt(5, f.getEstadoFuncion().getIdEstadoFuncion());
                psFuncion.setInt(6, f.getAsientosDisponibles());

                psFuncion.executeUpdate();

                // Obtener ID de la función recién creada
                int idFuncion = 0;
                try (ResultSet keys = psFuncion.getGeneratedKeys()) {
                    if (keys.next()) {
                        idFuncion = keys.getInt(1);
                        f.setIdFuncion(idFuncion);
                    } else {
                        throw new SQLException("No se pudo obtener id de la función insertada.");
                    }
                }

                // Definir rango de asientos según la sala (sintaxis clásica)
                int idSala = f.getSala().getIdSala();
                int inicio;
                int fin;

                switch (idSala) {
                    case 1:
                        inicio = 1;
                        fin = 100;
                        break;
                    case 2:
                        inicio = 101;
                        fin = 200;
                        break;
                    case 3:
                        inicio = 201;
                        fin = 300;
                        break;
                    case 4:
                        inicio = 301;
                        fin = 400;
                        break;
                    default:
                        throw new SQLException("Sala no válida (debe ser 1-4). idSala=" + idSala);
                }

                // Insertar los 100 asientos para esa función usando batch
                try (PreparedStatement psAsiento = con.prepareStatement(sqlAsientoFuncion)) {
                    for (int i = inicio; i <= fin; i++) {
                        psAsiento.setInt(1, i);
                        psAsiento.setInt(2, idFuncion);
                        psAsiento.addBatch();
                    }
                    psAsiento.executeBatch();
                }

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
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

    // Eliminar función junto con sus asientos asociados
    public void eliminar(int idFuncion) throws SQLException {
        String sqlAsientos = "DELETE FROM asiento_funcion WHERE id_funcion = ?";
        String sqlFuncion = "DELETE FROM funciones WHERE id_funcion = ?";

        try (Connection con = Conexion.getConnection()) {
            // Desactivamos auto-commit para manejar la transacción manualmente
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(sqlAsientos); PreparedStatement ps2 = con.prepareStatement(sqlFuncion)) {

                // Primero eliminamos los asientos vinculados a esa función
                ps1.setInt(1, idFuncion);
                ps1.executeUpdate();

                // Luego eliminamos la función
                ps2.setInt(1, idFuncion);
                ps2.executeUpdate();

                // Confirmamos la transacción
                con.commit();
            } catch (SQLException ex) {
                // Si algo falla, revertimos los cambios
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

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
        String sql = "SELECT f.id_funcion, f.fecha_inicio, f.fecha_fin, f.asientos_disponibles, f.activa, "
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
                    f.setActiva(rs.getInt("activa"));

                    lista.add(f);
                }
            }
        }
        return lista;
    }

    // Obtener una función por ID
    public Funcion obtener(int idFuncion) throws SQLException {
        String sql = "SELECT f.id_funcion, f.id_pelicula, p.nombre AS pelicula, "
                + "f.id_sala, s.nombre AS sala, f.fecha_inicio, f.fecha_fin, "
                + "f.id_estado_funcion, e.nombre AS estado, f.asientos_disponibles "
                + "FROM funciones f "
                + "JOIN peliculas p ON f.id_pelicula = p.id_pelicula "
                + "JOIN salas s ON f.id_sala = s.id_sala "
                + "JOIN estado_funciones e ON f.id_estado_funcion = e.id_estado_funcion "
                + "WHERE f.id_funcion = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Funcion f = new Funcion();
                f.setIdFuncion(rs.getInt("id_funcion"));

                Pelicula p = new Pelicula();
                p.setIdPelicula(rs.getInt("id_pelicula"));
                p.setNombre(rs.getString("pelicula"));  // ✅ Ahora sí cargas el nombre
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
