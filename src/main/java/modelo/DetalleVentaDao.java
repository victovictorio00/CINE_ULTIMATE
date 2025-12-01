package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class DetalleVentaDao implements DaoCrud<DetalleVenta> {

    /* ============================
       CRUD BÁSICO
       ============================ */
   @Override
public List<DetalleVenta> listar() throws SQLException {
    List<DetalleVenta> detalles = new ArrayList<>();
    String sql = "{CALL listarDetalleVentas()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            detalles.add(mapear(rs));
        }
    }
    return detalles;
}


   @Override
public void insertar(DetalleVenta detalle) throws SQLException {
    String sql = "{CALL insertarDetalleVenta(?, ?, ?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, detalle.getVenta().getIdVenta());
        cs.setObject(2, detalle.getProducto() != null ? detalle.getProducto().getIdProducto() : null, Types.INTEGER);
        cs.setObject(3, detalle.getFuncion() != null ? detalle.getFuncion().getIdFuncion() : null, Types.INTEGER);
        cs.setObject(4, detalle.getIdAsientoFuncion() != null ? detalle.getIdAsientoFuncion().getIdAsientoFuncion() : null, Types.INTEGER);
        cs.setInt(5, detalle.getCantidad());
        cs.setInt(6, detalle.getTipoItem());
        cs.setDouble(7, detalle.getPrecioUnitario());
        cs.registerOutParameter(8, Types.INTEGER);

        cs.execute();

        detalle.setIdDetalleVenta(cs.getInt(8));
    }
}


    @Override
public DetalleVenta leer(int id) throws SQLException {
    String sql = "{CALL leerDetalleVenta(?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);

        try (ResultSet rs = cs.executeQuery()) {
            return rs.next() ? mapear(rs) : null;
        }
    }
}


    @Override
public void editar(DetalleVenta detalle) throws SQLException {
    String sql = "{CALL actualizarDetalleVenta(?, ?, ?, ?, ?, ?, ?, ?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, detalle.getIdDetalleVenta());
        cs.setInt(2, detalle.getVenta().getIdVenta());

        cs.setObject(3, (detalle.getProducto() != null && detalle.getProducto().getIdProducto() > 0)
                ? detalle.getProducto().getIdProducto() : null, java.sql.Types.INTEGER);
        cs.setObject(4, (detalle.getFuncion() != null && detalle.getFuncion().getIdFuncion() > 0)
                ? detalle.getFuncion().getIdFuncion() : null, java.sql.Types.INTEGER);
        cs.setObject(5, (detalle.getIdAsientoFuncion() != null && detalle.getIdAsientoFuncion().getIdAsientoFuncion() > 0)
                ? detalle.getIdAsientoFuncion().getIdAsientoFuncion() : null, java.sql.Types.INTEGER);

        cs.setInt(6, detalle.getCantidad());
        cs.setInt(7, detalle.getTipoItem());
        cs.setDouble(8, detalle.getPrecioUnitario());

        cs.executeUpdate();
    }
}


   @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarDetalleVenta(?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {
        cs.setInt(1, id);
        cs.executeUpdate();
    }
}


    /* ============================
       MÉTODOS EXTRA
       ============================ */
    
public List<DetalleVenta> listarPorVenta(int idVenta) throws SQLException {
    List<DetalleVenta> lista = new ArrayList<>();
    String sql = "{CALL listarDetallePorVenta(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, idVenta);

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
    }
    return lista;
}


    /* ============================
       HELPERS PRIVADOS
       ============================ */
    private DetalleVenta mapear(ResultSet rs) throws SQLException {
        DetalleVenta d = new DetalleVenta();
        d.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setTipoItem(rs.getInt("tipo_item"));
        d.setPrecioUnitario(rs.getDouble("precio_unitario"));

        Venta v = new Venta();
        v.setIdVenta(rs.getInt("id_venta"));
        d.setVenta(v);

        int idProd = rs.getInt("id_producto");
        if (!rs.wasNull()) {
            Producto p = new Producto();
            p.setIdProducto(idProd);
            p.setNombre(rs.getString("p_nombre"));
            p.setPrecio(rs.getDouble("p_precio"));
            d.setProducto(p);
        }

        int idFunc = rs.getInt("id_funcion");
        if (!rs.wasNull()) {
            Funcion f = new Funcion();
            f.setIdFuncion(idFunc);
            f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
            f.setFechaFin(rs.getTimestamp("fecha_fin"));

            Pelicula p = new Pelicula();
            p.setIdPelicula(rs.getInt("id_pelicula"));
            p.setNombre(rs.getString("pel_nombre"));
            f.setPelicula(p);

            Sala s = new Sala();
            s.setIdSala(rs.getInt("id_sala"));
            s.setNombre(rs.getString("s_nombre"));
            f.setSala(s);

            d.setFuncion(f);
        }

        int idAF = rs.getInt("id_asiento_funcion");
        if (!rs.wasNull()) {
            AsientoFuncion af = new AsientoFuncion();
            af.setIdAsientoFuncion(idAF);

            Asiento a = new Asiento();
            a.setId_asiento(rs.getInt("id_asiento"));
            a.setCodigo(rs.getString("codigo"));  // <-- acá va el código
            af.setAsiento(a);

            d.setIdAsientoFuncion(af);
        }

        return d;
    }
}
