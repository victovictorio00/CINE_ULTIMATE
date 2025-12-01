package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import Conexion.Conexion;

public class AsientoDao implements DaoCrud<Asiento> {

    @Override
    public List<Asiento> listar() throws SQLException {
    List<Asiento> lista = new ArrayList<>();
    String sql = "{CALL listarAsientos()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            Asiento asiento = new Asiento();
            asiento.setId_asiento(rs.getInt("id_asiento"));
            asiento.setCodigo(rs.getString("codigo"));

            Sala sala = new Sala();
            sala.setIdSala(rs.getInt("id_sala"));
            asiento.setId_sala(sala);

            EstadoAsiento estado = new EstadoAsiento();
            estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
            estado.setNombre(rs.getString("nombre_estado"));
            asiento.setId_estado_asiento(estado);

            lista.add(asiento);
        }
    }
    return lista;
}


    @Override
public void insertar(Asiento asiento) throws SQLException {
    String sql = "{CALL insertarAsiento(?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, asiento.getId_sala().getIdSala());
        cs.setString(2, asiento.getCodigo());
        cs.setInt(3, asiento.getId_estado_asiento().getIdEstadoAsiento());
        cs.registerOutParameter(4, Types.INTEGER);

        cs.executeUpdate();

        asiento.setId_asiento(cs.getInt(4));
    }
}


    @Override
public Asiento leer(int id) throws SQLException {
    String sql = "{CALL leerAsiento(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);
        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Asiento asiento = new Asiento();
                asiento.setId_asiento(rs.getInt("id_asiento"));
                asiento.setCodigo(rs.getString("codigo"));

                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                asiento.setId_sala(sala);

                EstadoAsiento estado = new EstadoAsiento();
                estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                estado.setNombre(rs.getString("nombre_estado"));
                asiento.setId_estado_asiento(estado);

                return asiento;
            }
        }
    }
    return null;
}


    @Override
public void editar(Asiento asiento) throws SQLException {
    String sql = "{CALL editarAsiento(?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, asiento.getId_asiento());
        cs.setInt(2, asiento.getId_sala().getIdSala());
        cs.setString(3, asiento.getCodigo());
        cs.setInt(4, asiento.getId_estado_asiento().getIdEstadoAsiento());

        cs.executeUpdate();
    }
}


    @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarAsiento(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);
        cs.executeUpdate();
    }
}


    /**
     * Obtiene los asientos de una sala y añade columna calculada
     * "estado_actual" (ocupado | bloqueado | disponible) según detalle_ventas y
     * estado_asientos.
     *
     * Parámetros: idFuncion (para LEFT JOIN con detalle_ventas) y idSala
     * (WHERE). IMPORTANTE: el orden de parámetros en el PreparedStatement es
     * (idFuncion, idSala).
     */
    public List<Asiento> obtenerAsientosPorSalaYFuncion(int idSala, int idFuncion) throws SQLException {
    List<Asiento> asientos = new ArrayList<>();
    String sql = "{CALL obtenerAsientosPorSalaYFuncion(?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idSala);
        cs.setInt(2, idFuncion);

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Asiento asiento = new Asiento();
                asiento.setId_asiento(rs.getInt("id_asiento"));
                asiento.setCodigo(rs.getString("codigo"));

                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                asiento.setId_sala(sala);

                EstadoAsiento estado = new EstadoAsiento();
                estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                estado.setNombre(rs.getString("nombre_estado"));
                asiento.setId_estado_asiento(estado);

                asientos.add(asiento);
            }
        }
    }
    return asientos;
}


    /**
     * Obtiene asientos por sala (sin tener en cuenta función).
     */
    public List<Asiento> obtenerAsientosPorSala(int idSala) throws SQLException {
    List<Asiento> asientos = new ArrayList<>();
    String sql = "{CALL obtenerAsientosPorSala(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idSala);

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Asiento asiento = new Asiento();
                asiento.setId_asiento(rs.getInt("id_asiento"));
                asiento.setCodigo(rs.getString("codigo"));

                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                asiento.setId_sala(sala);

                EstadoAsiento estado = new EstadoAsiento();
                estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                estado.setNombre(rs.getString("nombre_estado"));
                asiento.setId_estado_asiento(estado);

                asientos.add(asiento);
            }
        }
    }

    return asientos;
}


    /**
     * Lee un asiento por código dentro de una sala concreta. Evita ambigüedad
     * si hay códigos repetidos en diferentes salas.
     */
    public Asiento leerPorCodigoYSala(String codigo, int idSala) throws SQLException {
    String sql = "{CALL leerAsientoPorCodigoYSala(?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setString(1, codigo);
        cs.setInt(2, idSala);

        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Asiento asiento = new Asiento();
                asiento.setId_asiento(rs.getInt("id_asiento"));
                asiento.setCodigo(rs.getString("codigo"));

                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                asiento.setId_sala(sala);

                EstadoAsiento estado = new EstadoAsiento();
                estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                estado.setNombre(rs.getString("nombre_estado"));
                asiento.setId_estado_asiento(estado);

                return asiento;
            }
        }
    }
    return null;
}


    /**
     * Comprueba si un asiento está disponible para una función (sin insertar).
     * Retorna true si NO existe registro en detalle_ventas para (idFuncion,
     * idAsiento).
     */
    public boolean isAsientoDisponibleEnFuncion(int idAsientoFuncion) throws SQLException {
    String sql = "{CALL isAsientoDisponibleEnFuncion(?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idAsientoFuncion);
        cs.registerOutParameter(2, Types.TINYINT);

        cs.execute();

        return cs.getInt(2) == 1;
    }
}


    /**
     * Helper ATÓMICO para insertar un detalle de venta tipo asiento dentro de
     * una transacción. - Debe llamarse con una Connection que ya tiene
     * setAutoCommit(false). - Si hay una única constraint
     * (id_funcion,id_asiento) y otro proceso ya insertó, este método capturará
     * el Duplicate Key (errorCode 1062) y devolverá false.
     *
     * NOTA: idealmente este método pertenece a DetalleVentaDao, lo incluyo aquí
     * por conveniencia.
     */
    public boolean insertarDetalleAsientoFuncionSiDisponible(int idVenta, int idFuncion, int idAsientoFuncion, double precioUnitario) throws SQLException {
    String sql = "{CALL insertarDetalleSiDisponible(?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idVenta);
        cs.setInt(2, idFuncion);
        cs.setInt(3, idAsientoFuncion);
        cs.setBigDecimal(4, BigDecimal.valueOf(precioUnitario));
        cs.registerOutParameter(5, Types.TINYINT);

        cs.execute();

        return cs.getInt(5) == 1;
    }
}

}
