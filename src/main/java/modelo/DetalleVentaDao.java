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
        String sql = "SELECT * FROM detalle_ventas";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapear(rs));
            }
        }
        return detalles;
    }

    @Override
    public void insertar(DetalleVenta detalle) throws SQLException {

        String sql = "INSERT INTO detalle_ventas "
                + "(id_venta, id_producto, id_funcion, id_asiento_funcion, cantidad, tipo_item, precio_unitario) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, detalle.getVenta().getIdVenta());

            // Producto
            if (detalle.getProducto() != null && detalle.getProducto().getIdProducto() > 0) {
                pst.setInt(2, detalle.getProducto().getIdProducto());
            } else {
                pst.setNull(2, Types.INTEGER);
            }

            // Función
            if (detalle.getFuncion() != null && detalle.getFuncion().getIdFuncion() > 0) {
                pst.setInt(3, detalle.getFuncion().getIdFuncion());
            } else {
                pst.setNull(3, Types.INTEGER);
            }

            // AsientoFuncion
            if (detalle.getIdAsientoFuncion() != null && detalle.getIdAsientoFuncion().getIdAsientoFuncion() > 0) {
                pst.setInt(4, detalle.getIdAsientoFuncion().getIdAsientoFuncion());
            } else {
                pst.setNull(4, Types.INTEGER);
            }

            pst.setInt(5, detalle.getCantidad());
            pst.setInt(6, detalle.getTipoItem());
            pst.setDouble(7, detalle.getPrecioUnitario());

            pst.executeUpdate();

            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    detalle.setIdDetalleVenta(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public DetalleVenta leer(int id) throws SQLException {
        String sql = "SELECT * FROM detalle_ventas WHERE id_detalle_venta = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public void editar(DetalleVenta detalle) throws SQLException {
        String sql = "UPDATE detalle_ventas SET "
                + "id_venta = ?, id_producto = ?, id_funcion = ?, id_asiento_funcion = ?, "
                + "cantidad = ?, tipo_item = ?, precio_unitario = ? "
                + "WHERE id_detalle_venta = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, detalle.getVenta().getIdVenta());
            pst.setInt(2, (detalle.getProducto() != null && detalle.getProducto().getIdProducto() > 0)
                    ? detalle.getProducto().getIdProducto() : Types.NULL);
            pst.setInt(3, (detalle.getFuncion() != null && detalle.getFuncion().getIdFuncion() > 0)
                    ? detalle.getFuncion().getIdFuncion() : Types.NULL);
            pst.setInt(4, (detalle.getIdAsientoFuncion() != null && detalle.getIdAsientoFuncion().getIdAsientoFuncion() > 0)
                    ? detalle.getIdAsientoFuncion().getIdAsientoFuncion() : Types.NULL);
            pst.setInt(5, detalle.getCantidad());
            pst.setInt(6, detalle.getTipoItem());
            pst.setDouble(7, detalle.getPrecioUnitario());
            pst.setInt(8, detalle.getIdDetalleVenta());

            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM detalle_ventas WHERE id_detalle_venta = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /* ============================
       MÉTODOS EXTRA
       ============================ */
    public List<DetalleVenta> listarPorVenta(int idVenta) throws SQLException {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT dv.*,\n"
                + "       p.id_producto, p.nombre AS p_nombre, p.precio AS p_precio,\n"
                + "       f.id_funcion, f.fecha_inicio, f.fecha_fin,\n"
                + "       pel.id_pelicula, pel.nombre AS pel_nombre,\n"
                + "       s.id_sala, s.nombre AS s_nombre,\n"
                + "       a.id_asiento, a.codigo AS codigo   -- <-- ¡acá está el código!\n"
                + "FROM detalle_ventas dv\n"
                + "LEFT JOIN productos p ON p.id_producto = dv.id_producto\n"
                + "LEFT JOIN funciones f ON f.id_funcion = dv.id_funcion\n"
                + "LEFT JOIN peliculas pel ON pel.id_pelicula = f.id_pelicula\n"
                + "LEFT JOIN salas s ON s.id_sala = f.id_sala\n"
                + "LEFT JOIN asiento_funcion af ON af.id_asiento_funcion = dv.id_asiento_funcion\n"
                + "LEFT JOIN asientos a ON a.id_asiento = af.id_asiento\n"
                + "WHERE dv.id_venta = ?\n"
                + "ORDER BY dv.id_detalle_venta;";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idVenta);
            try (ResultSet rs = pst.executeQuery()) {
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
