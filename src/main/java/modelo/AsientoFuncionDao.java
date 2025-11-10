package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsientoFuncionDao implements DaoCrud<AsientoFuncion> {

    /* -----------------------
       CRUD BÁSICO
       ----------------------- */
    @Override
    public List<AsientoFuncion> listar() throws SQLException {
        List<AsientoFuncion> lista = new ArrayList<>();
        String sql = "SELECT af.id_asiento_funcion, af.id_asiento, af.id_funcion, af.id_estado_asiento "
                + "FROM asiento_funcion af";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public void insertar(AsientoFuncion af) throws SQLException {
        String sql = "INSERT INTO asiento_funcion (id_asiento, id_funcion, id_estado_asiento) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, af.getAsiento().getId_asiento());
            pst.setInt(2, af.getFuncion().getIdFuncion());
            pst.setInt(3, af.getEstadoAsiento().getIdEstadoAsiento());

            pst.executeUpdate();
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    af.setIdAsientoFuncion(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public AsientoFuncion leer(int id) throws SQLException {
        String sql = "SELECT af.id_asiento_funcion, af.id_asiento, af.id_funcion, af.id_estado_asiento, " +
             "       a.codigo, a.id_sala " +
             "FROM asiento_funcion af " +
             "JOIN asientos a ON a.id_asiento = af.id_asiento " +
             "WHERE af.id_asiento_funcion = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public void editar(AsientoFuncion af) throws SQLException {
        String sql = "UPDATE asiento_funcion SET id_estado_asiento = ? WHERE id_asiento_funcion = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, af.getEstadoAsiento().getIdEstadoAsiento());
            pst.setInt(2, af.getIdAsientoFuncion());
            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM asiento_funcion WHERE id_asiento_funcion = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /* -----------------------
       MÉTODOS EXTRA
       ----------------------- */
    /**
     * Lista todos los asientos de una sala para una función con su estado
     * actual.
     */
    public List<AsientoFuncion> listarPorSalaYFuncion(int idFuncion) throws SQLException {
        List<AsientoFuncion> lista = new ArrayList<>();

        String sql = "SELECT af.id_asiento_funcion, af.id_funcion, af.id_asiento, af.id_estado_asiento, "
                   + "a.codigo, a.id_sala, ea.nombre AS nombre_estado "
                   + "FROM asiento_funcion af "
                   + "INNER JOIN asientos a ON af.id_asiento = a.id_asiento "
                   + "INNER JOIN estado_asientos ea ON af.id_estado_asiento = ea.id_estado_asiento "
                   + "WHERE af.id_funcion = ? "
                   + "ORDER BY a.codigo";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idFuncion);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    AsientoFuncion af = new AsientoFuncion();
                    af.setIdAsientoFuncion(rs.getInt("id_asiento_funcion"));

                    Funcion f = new Funcion();
                    f.setIdFuncion(rs.getInt("id_funcion"));
                    af.setFuncion(f);

                    Asiento a = new Asiento();
                    a.setId_asiento(rs.getInt("id_asiento"));
                    a.setCodigo(rs.getString("codigo"));
                    Sala s = new Sala();
                    s.setIdSala(rs.getInt("id_sala"));
                    a.setId_sala(s);
                    af.setAsiento(a);

                    EstadoAsiento ea = new EstadoAsiento();
                    ea.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    ea.setNombre(rs.getString("nombre_estado"));
                    af.setEstadoAsiento(ea);

                    lista.add(af);
                }
            }
        }
        return lista;
    }

    /**
     * Marca un asiento como ocupado (estado = 2) para una función. Devuelve
     * true si la fila fue actualizada (es decir, estaba disponible).
     */
    public boolean ocupar(int idAsientoFuncion) throws SQLException {
        String sql = "UPDATE asiento_funcion SET id_estado_asiento = 2 "
                + "WHERE id_asiento_funcion = ? AND id_estado_asiento = 1";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idAsientoFuncion);
            return pst.executeUpdate() == 1;
        }
    }

    /**
     * Libera un asiento (estado = 1) para una función.
     */
    public void liberar(int idAsientoFuncion) throws SQLException {
        String sql = "UPDATE asiento_funcion SET id_estado_asiento = 1 "
                + "WHERE id_asiento_funcion = ? AND id_estado_asiento = 2";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idAsientoFuncion);
            pst.executeUpdate();
        }
    }

    /* -----------------------
       HELPERS PRIVADOS
       ----------------------- */
    private AsientoFuncion mapear(ResultSet rs) throws SQLException {
        AsientoFuncion af = new AsientoFuncion();
        af.setIdAsientoFuncion(rs.getInt("id_asiento_funcion"));

        Asiento a = new Asiento();
        a.setId_asiento(rs.getInt("id_asiento"));
        a.setCodigo(rs.getString("codigo"));   // <-- AÑADE ESTA LÍNEA
        Sala s = new Sala();
        s.setIdSala(rs.getInt("id_sala"));
        a.setId_sala(s);
        af.setAsiento(a);

        Funcion f = new Funcion();
        f.setIdFuncion(rs.getInt("id_funcion"));
        af.setFuncion(f);

        EstadoAsiento ea = new EstadoAsiento();
        ea.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
        af.setEstadoAsiento(ea);

        return af;
    }
    public void actualizarEstado(int idAsientoFuncion, int nuevoEstado) throws SQLException {
        String sql = "UPDATE asiento_funcion SET id_estado_asiento = ? WHERE id_asiento_funcion = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, nuevoEstado);
            pst.setInt(2, idAsientoFuncion);
            pst.executeUpdate();
        }
    }
}
