package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionDao {

   public void insertar(Funcion f) throws SQLException {
    String sql = "{CALL insertarFuncion(?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, f.getPelicula().getIdPelicula());
        cs.setInt(2, f.getSala().getIdSala());
        cs.setTimestamp(3, f.getFechaInicio());
        cs.setTimestamp(4, f.getFechaFin());
        cs.setInt(5, f.getEstadoFuncion().getIdEstadoFuncion());
        cs.setInt(6, f.getAsientosDisponibles());

        cs.executeUpdate();
    }
}

    // Actualizar función existente
   public void actualizar(Funcion f) throws SQLException {
    String sql = "{CALL actualizarFuncion(?, ?, ?, ?, ?, ?, ?)}";
    try (Connection con = Conexion.getConnection(); CallableStatement cs = con.prepareCall(sql)) {
        cs.setInt(1, f.getIdFuncion());
        cs.setInt(2, f.getPelicula().getIdPelicula());
        cs.setInt(3, f.getSala().getIdSala());
        cs.setTimestamp(4, f.getFechaInicio());
        cs.setTimestamp(5, f.getFechaFin());
        cs.setInt(6, f.getEstadoFuncion().getIdEstadoFuncion());
        cs.setInt(7, f.getAsientosDisponibles());
        cs.executeUpdate();
    }
}


    // Eliminar función junto con sus asientos asociados
   public void eliminar(int idFuncion) throws SQLException {
    String sql = "{CALL eliminarFuncion(?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {
        cs.setInt(1, idFuncion);
        cs.executeUpdate();
    }
}


    // Listar todas las funciones
    public List<Funcion> listar() throws SQLException {
    List<Funcion> lista = new ArrayList<>();
    String sql = "{CALL listarFunciones()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

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
    String sql = "{CALL obtenerFuncionesPorPelicula(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idPelicula);
        try (ResultSet rs = cs.executeQuery()) {
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
    String sql = "{CALL obtenerFuncionPorId(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idFuncion);
        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
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

                return f;
            }
        }
    }
    return null;
}


    // Verificar si existe conflicto de horarios en una sala
    public boolean existeConflicto(int idSala, Timestamp inicio, Timestamp fin, Integer idFuncionEditar) throws SQLException {
    String sql = "{CALL existeConflictoFuncion(?, ?, ?, ?)}";
    try (Connection c = Conexion.getConnection(); CallableStatement cs = c.prepareCall(sql)) {

        cs.setInt(1, idSala);
        cs.setTimestamp(2, inicio);
        cs.setTimestamp(3, fin);
        if (idFuncionEditar != null) {
            cs.setInt(4, idFuncionEditar);
        } else {
            cs.setNull(4, java.sql.Types.INTEGER);
        }

        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("cantidad") > 0;
            }
        }
    }
    return false;
}


   public Funcion leer(int id) throws SQLException {
    String sql = "{CALL leerFuncion(?)}";

    try (Connection c = Conexion.getConnection(); CallableStatement cs = c.prepareCall(sql)) {
        cs.setInt(1, id);

        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Funcion f = new Funcion();
                f.setIdFuncion(rs.getInt("id_funcion"));

                Pelicula p = new Pelicula();
                p.setIdPelicula(rs.getInt("id_pelicula"));
                p.setNombre(rs.getString("pelicula"));
                p.setPrecio(rs.getDouble("precio"));
                p.setIdGenero(new Genero(rs.getInt("id_genero"), rs.getString("genero")));
                f.setPelicula(p);

                Sala s = new Sala();
                s.setIdSala(rs.getInt("id_sala"));
                s.setNombre(rs.getString("sala"));
                f.setSala(s);

                f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                f.setFechaFin(rs.getTimestamp("fecha_fin"));

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
