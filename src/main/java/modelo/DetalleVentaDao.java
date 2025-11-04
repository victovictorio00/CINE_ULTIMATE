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

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

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
        String query = "INSERT INTO detalle_ventas ("
                     + "id_venta, id_producto, id_funcion, id_asiento, "
                     + "cantidad, tipo_item, precio_unitario) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // FK: Venta (obligatoria)
            pst.setInt(1, detalle.getVenta().getIdVenta());

            // FK: Producto (puede ser null si es asiento)
            if (detalle.getProducto() != null) {
                pst.setInt(2, detalle.getProducto().getIdProducto());
            } else {
                pst.setNull(2, Types.INTEGER);
            }

            // FK: Función (solo si es asiento)
            if (detalle.getFuncion() != null) {
                pst.setInt(3, detalle.getFuncion().getIdFuncion());
            } else {
                pst.setNull(3, Types.INTEGER);
            }

            // FK: Asiento (solo si es asiento)
            if (detalle.getAsiento() != null) {
                pst.setInt(4, detalle.getAsiento().getId_asiento());
            } else {
                pst.setNull(4, Types.INTEGER);
            }

            // Campos propios
            pst.setInt(5, detalle.getCantidad());
            pst.setInt(6, detalle.getTipoItem());
            pst.setDouble(7, detalle.getPrecioUnitario());

            pst.executeUpdate(); // ✅ Inserta realmente
        }
    }

    @Override
    public DetalleVenta leer(int id) throws SQLException {
        String query = "SELECT * FROM detalle_ventas WHERE id_detalle_venta = ?";
        DetalleVenta detalle = null;

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

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

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, detalle.getVenta().getIdVenta());
            pst.setInt(2, (detalle.getProducto() != null)
                    ? detalle.getProducto().getIdProducto() : Types.NULL);
            pst.setInt(3, (detalle.getFuncion() != null)
                    ? detalle.getFuncion().getIdFuncion() : Types.NULL);
            pst.setInt(4, (detalle.getAsiento() != null)
                    ? detalle.getAsiento().getId_asiento(): Types.NULL);
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

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
    
    public List<DetalleVenta> listarPorVenta(int idVenta) throws SQLException {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_ventas WHERE id_venta = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idVenta);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta d = new DetalleVenta();
                    d.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setTipoItem(rs.getInt("tipo_item"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    // ...rellena los objetos relacionados según tus getters/setters...
                    
                    VentaDao vd = new VentaDao();
                    d.setVenta(vd.leer(rs.getInt("id_venta")));

                    ProductoDao pd = new ProductoDao();
                    d.setProducto(pd.leer(rs.getInt("id_producto")));

                    FuncionDao fd = new FuncionDao();
                    d.setFuncion(fd.leer(rs.getInt("id_funcion")));

                    AsientoDao ad = new AsientoDao();
                    d.setAsiento(ad.leer(rs.getInt("id_asiento")));                   
                    
                    
                    lista.add(d);
                }
                int a =1;
            }
        }
        return lista;
    }
    
}