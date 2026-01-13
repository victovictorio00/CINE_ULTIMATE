package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;

public class VentaDao implements DaoCrud<Venta> {

    @Override
public List<Venta> listar() throws SQLException {
    List<Venta> ventas = new ArrayList<>();
    String sql = "{CALL listarVentas()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

        while (rs.next()) {
            Venta venta = new Venta();
            venta.setIdVenta(rs.getInt("id_venta"));
            venta.setTotal(rs.getDouble("total"));
            venta.setMetodoPago(rs.getString("metodo_pago"));
            venta.setFecha(rs.getTimestamp("fecha"));

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("id_usuario_cliente"));
            usuario.setNombreCompleto(rs.getString("nombre_completo"));
            venta.setIdUsuarioCliente(usuario);

            ventas.add(venta);
        }
    }
    return ventas;
}


    @Override
public void insertar(Venta venta) throws SQLException {
    String sql = "{CALL insertarVenta(?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
        cst.setDouble(2, venta.getTotal());
        cst.setString(3, venta.getMetodoPago());

        cst.execute();
    }
}


    /**
     * Inserta la venta y retorna el ID autogenerado.
     */
   public int insertarYDevolverId(Venta venta) throws SQLException {
    String sql = "{CALL insertarVentaYDevolverId(?, ?, ?, ?)}";
    int idGenerado = 0;

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
        cst.setDouble(2, venta.getTotal());
        cst.setString(3, venta.getMetodoPago());
        cst.registerOutParameter(4, java.sql.Types.INTEGER);

        cst.execute();

        idGenerado = cst.getInt(4);
        venta.setIdVenta(idGenerado);
    }

    return idGenerado;
}


   @Override
public Venta leer(int id) throws SQLException {
    String sql = "{CALL leerVenta(?)}";

    Venta venta = null;

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);
        try (ResultSet rs = cst.executeQuery()) {
            if (rs.next()) {
                venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setTotal(rs.getDouble("total"));
                venta.setMetodoPago(rs.getString("metodo_pago"));
                venta.setFecha(rs.getTimestamp("fecha"));

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario_cliente"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                venta.setIdUsuarioCliente(usuario);
            }
        }
    }
    return venta;
}


   @Override
public void editar(Venta venta) throws SQLException {
    String sql = "{CALL editarVenta(?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, venta.getIdVenta());
        cst.setInt(2, venta.getIdUsuarioCliente().getIdUsuario());
        cst.setTimestamp(3, new Timestamp(venta.getFecha().getTime()));
        cst.setDouble(4, venta.getTotal());
        cst.setString(5, venta.getMetodoPago());

        cst.execute();
    }
}


    @Override
    public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarVentaCompleta(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        con.setAutoCommit(false); // opcional, ya que el procedimiento ya tiene START TRANSACTION

        cst.setInt(1, id);
        cst.execute();

        con.commit(); // opcional
    } catch (SQLException e) {
        e.printStackTrace();
        throw e; // propagar el error
    }
}


   public boolean guardarVenta(Venta venta, List<DetalleVenta> detalles) {
    String sqlVenta = "{CALL insertarVenta(?, ?, ?, ?)}";
    String sqlDetalle = "{CALL insertarDetalle(?, ?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection()) {
        con.setAutoCommit(false);

        int idVenta;

        //  Insertar venta
        try (CallableStatement csVenta = con.prepareCall(sqlVenta)) {
            csVenta.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
            csVenta.setDouble(2, venta.getTotal());
            csVenta.setString(3, venta.getMetodoPago());
            csVenta.registerOutParameter(4, java.sql.Types.INTEGER);

            csVenta.execute();
            idVenta = csVenta.getInt(4);
            venta.setIdVenta(idVenta);
        }

        //  Insertar detalles
        try (CallableStatement csDet = con.prepareCall(sqlDetalle)) {
            for (DetalleVenta d : detalles) {
                csDet.setInt(1, idVenta);
                csDet.setInt(2, d.getCantidad());
                csDet.setInt(3, d.getTipoItem());
                csDet.setDouble(4, d.getPrecioUnitario());

                csDet.setObject(5, d.getProducto() != null ? d.getProducto().getIdProducto() : null, java.sql.Types.INTEGER);
                csDet.setObject(6, d.getFuncion() != null ? d.getFuncion().getIdFuncion() : null, java.sql.Types.INTEGER);
                csDet.setObject(7, d.getIdAsientoFuncion() != null ? d.getIdAsientoFuncion().getIdAsientoFuncion() : null, java.sql.Types.INTEGER);

                csDet.execute();
            }
        }

        con.commit();
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
  public List<Venta> obtenerReservasPorUsuario(int idUsuario) throws SQLException {
    List<Venta> lista = new ArrayList<>();

    String sql = "{CALL obtenerReservasPorUsuario(?)}"; // Llamada al procedimiento

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, idUsuario);
        try (ResultSet rs = cst.executeQuery()) {
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setFecha(rs.getTimestamp("fecha"));
                venta.setTotal(rs.getDouble("total"));
                venta.setMetodoPago(rs.getString("metodo_pago"));

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                venta.setIdUsuarioCliente(usuario);

                lista.add(venta);
            }
        }
    }

    return lista;
}


}
