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
    String sql = "{CALL listarAsientoFuncion()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            lista.add(mapear(rs));
        }
    }
    return lista;
}


    @Override
public void insertar(AsientoFuncion af) throws SQLException {
    String sql = "{CALL insertarAsientoFuncion(?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, af.getAsiento().getId_asiento());
        cs.setInt(2, af.getFuncion().getIdFuncion());
        cs.setInt(3, af.getEstadoAsiento().getIdEstadoAsiento());
        cs.registerOutParameter(4, Types.INTEGER);

        cs.executeUpdate();

        af.setIdAsientoFuncion(cs.getInt(4));
    }
}


   @Override
public AsientoFuncion leer(int id) throws SQLException {
    String sql = "{CALL leerAsientoFuncion(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);

        try (ResultSet rs = cs.executeQuery()) {
            return rs.next() ? mapear(rs) : null;
        }
    }
}


  @Override
public void editar(AsientoFuncion af) throws SQLException {
    String sql = "{CALL editarAsientoFuncion(?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, af.getIdAsientoFuncion());
        cs.setInt(2, af.getEstadoAsiento().getIdEstadoAsiento());

        cs.executeUpdate();
    }
}


    @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarAsientoFuncion(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);
        cs.executeUpdate();
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
    String sql = "{CALL listarAsientoFuncionPorFuncion(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idFuncion);
        try (ResultSet rs = cs.executeQuery()) {
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
    String sql = "{CALL ocuparAsientoFuncion(?, ?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idAsientoFuncion);
        cs.registerOutParameter(2, Types.TINYINT);

        cs.execute();

        return cs.getInt(2) == 1; // devuelve true si se actualizó un registro
    }
}


    /**
     * Libera un asiento (estado = 1) para una función.
     */
   public boolean liberar(int idAsientoFuncion) throws SQLException {
    String sql = "{CALL liberarAsientoFuncion(?, ?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idAsientoFuncion);
        cs.registerOutParameter(2, Types.TINYINT);

        cs.execute();

        return cs.getInt(2) == 1; // true si se liberó el asiento
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
    public boolean actualizarEstado(int idAsientoFuncion, int nuevoEstado) throws SQLException {
    String sql = "{CALL actualizarEstadoAsientoFuncion(?, ?, ?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idAsientoFuncion);
        cs.setInt(2, nuevoEstado);
        cs.registerOutParameter(3, Types.TINYINT);

        cs.execute();

        return cs.getInt(3) == 1; // true si se actualizó correctamente
    }
}

}
