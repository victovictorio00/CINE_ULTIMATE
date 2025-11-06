package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class DetalleVentaDao implements DaoCrud<DetalleVenta> {

    @Override
    public List<DetalleVenta> listar() throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String query = "SELECT * FROM detalle_ventas";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                DetalleVenta detalle = new DetalleVenta();

                detalle.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setTipoItem(rs.getInt("tipo_item"));
                detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));

                // Relaciones (solo se asignan IDs básicos)
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                detalle.setVenta(venta);

                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                detalle.setProducto(producto);

                Funcion funcion = new Funcion();
                funcion.setIdFuncion(rs.getInt("id_funcion"));
                detalle.setFuncion(funcion);

                Asiento asiento = new Asiento();
                asiento.setId_asiento(rs.getInt("id_asiento"));
                detalle.setAsiento(asiento);

                detalles.add(detalle);
            }
        }
        return detalles;
    }

    @Override
    public void insertar(DetalleVenta detalle) throws SQLException {
        String sql = "{CALL sp_insertar_detalle_venta(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection con = Conexion.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            // Parámetro 1: ID Venta (Siempre existe)
            cs.setInt(1, detalle.getVenta().getIdVenta());

            // Parámetro 2: ID Producto (Dulcería)
            if (detalle.getProducto() != null) {
                cs.setInt(2, detalle.getProducto().getIdProducto());
            } else {
                // CORRECCIÓN CLAVE: Usar setNull para insertar NULL
                cs.setNull(2, java.sql.Types.INTEGER);
            }

            // Parámetro 3: ID Función
            if (detalle.getFuncion() != null) {
                cs.setInt(3, detalle.getFuncion().getIdFuncion());
            } else {
                // Usar setNull para insertar NULL
                cs.setNull(3, java.sql.Types.INTEGER);
            }

            // Parámetro 4: ID Asiento
            if (detalle.getAsiento() != null) {
                cs.setInt(4, detalle.getAsiento().getId_asiento());
            } else {
                // Usar setNull para insertar NULL
                cs.setNull(4, java.sql.Types.INTEGER);
            }

            // Parámetros restantes (valores no nulos)
            cs.setInt(5, detalle.getCantidad());
            cs.setInt(6, detalle.getTipoItem());
            cs.setDouble(7, detalle.getPrecioUnitario());

            cs.execute(); // Ejecuta el procedimiento
        }
    }

    @Override
    public DetalleVenta leer(int id) throws SQLException {
        String query = "SELECT * FROM detalle_ventas WHERE id_detalle_venta = ?";
        DetalleVenta detalle = null;

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    detalle = new DetalleVenta();
                    detalle.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setTipoItem(rs.getInt("tipo_item"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));

                    Venta venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    detalle.setVenta(venta);

                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    detalle.setProducto(producto);

                    Funcion funcion = new Funcion();
                    funcion.setIdFuncion(rs.getInt("id_funcion"));
                    detalle.setFuncion(funcion);

                    Asiento asiento = new Asiento();
                    asiento.setId_asiento(rs.getInt("id_asiento"));
                    detalle.setAsiento(asiento);
                }
            }
        }
        return detalle;
    }

    @Override
    public void editar(DetalleVenta detalle) throws SQLException {
        String query = "UPDATE detalle_ventas SET "
                + "id_venta = ?, id_producto = ?, id_funcion = ?, id_asiento = ?, "
                + "cantidad = ?, tipo_item = ?, precio_unitario = ? "
                + "WHERE id_detalle_venta = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, detalle.getVenta().getIdVenta());
            pst.setInt(2, (detalle.getProducto() != null)
                    ? detalle.getProducto().getIdProducto() : Types.NULL);
            pst.setInt(3, (detalle.getFuncion() != null)
                    ? detalle.getFuncion().getIdFuncion() : Types.NULL);
            pst.setInt(4, (detalle.getAsiento() != null)
                    ? detalle.getAsiento().getId_asiento() : Types.NULL);
            pst.setInt(5, detalle.getCantidad());
            pst.setInt(6, detalle.getTipoItem());
            pst.setDouble(7, detalle.getPrecioUnitario());
            pst.setInt(8, detalle.getIdDetalleVenta());

            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM detalle_ventas WHERE id_detalle_venta = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public List<DetalleVenta> listarPorVenta(int idVenta) throws SQLException {
        List<DetalleVenta> lista = new ArrayList<>();

        String sql
                = "SELECT "
                + "  dv.id_detalle_venta, "
                + "  dv.cantidad, "
                + "  dv.tipo_item, "
                + "  dv.precio_unitario, "
                + "  dv.id_venta, "
                + "  dv.id_producto, "
                + "  dv.id_funcion, "
                + "  dv.id_asiento, "
                + "  v.id_venta      AS v_id_venta, "
                + "  v.fecha         AS v_fecha_venta, "
                + "  v.total         AS v_total, "
                + "  p.id_producto   AS p_id_producto, "
                + "  p.nombre        AS p_nombre, "
                + "  p.precio        AS p_precio, "
                + "  f.id_funcion    AS f_id_funcion, "
                + "  f.fecha_fin     AS f_fecha, "
                + "  f.hora_inicio   AS f_hora_inicio, "
                + "  a.id_asiento    AS a_id_asiento, "
                + "  a.codigo        AS a_codigo "
                + "FROM detalle_ventas dv "
                + "JOIN ventas        v  ON v.id_venta   = dv.id_venta "
                + "LEFT JOIN productos p  ON p.id_producto = dv.id_producto "
                + "LEFT JOIN funciones f  ON f.id_funcion  = dv.id_funcion "
                + "LEFT JOIN asientos  a  ON a.id_asiento  = dv.id_asiento "
                + "WHERE dv.id_venta = ? "
                + "ORDER BY dv.id_detalle_venta";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idVenta);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta d = new DetalleVenta();
                    d.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setTipoItem(rs.getInt("tipo_item"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));

                    // Venta
                    Venta v = new Venta();
                    v.setIdVenta(rs.getInt("v_id_venta"));
                    v.setFecha(rs.getTimestamp("v_fecha_venta"));
                    v.setTotal(rs.getDouble("v_total"));
                    d.setVenta(v);

                    // Producto (puede ser null)
                    int idProd = rs.getInt("p_id_producto");
                    if (!rs.wasNull()) {
                        Producto p = new Producto();
                        p.setIdProducto(idProd);
                        p.setNombre(rs.getString("p_nombre"));
                        p.setPrecio(rs.getDouble("p_precio"));
                        d.setProducto(p);
                    }

                    // Función (puede ser null)
                    int idFunc = rs.getInt("f_id_funcion");
                    if (!rs.wasNull()) {
                        Funcion f = new Funcion();
                        f.setIdFuncion(idFunc);
                        f.setFechaFin(rs.getTimestamp("f_fecha"));
                        f.setFechaInicio(rs.getTimestamp("f_hora_inicio"));
                        d.setFuncion(f);
                    }

                    // Asiento (puede ser null)
                    int idAsiento = rs.getInt("a_id_asiento");
                    if (!rs.wasNull()) {
                        Asiento a = new Asiento();
                        a.setId_asiento(idAsiento);
                        a.setCodigo(rs.getString("a_codigo"));
                        d.setAsiento(a);
                    }

                    lista.add(d);
                }
            }
        }
        return lista;
    }
}
