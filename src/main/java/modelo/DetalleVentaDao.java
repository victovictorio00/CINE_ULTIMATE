package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class DetalleVentaDao implements DaoCrud<DetalleVenta>{

    @Override
    public List<DetalleVenta> listar() throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String query = "SELECT * FROM detalle_ventas"; //trae los Id de los fk
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                DetalleVenta detalle = new DetalleVenta();
                
                // Mapeo de atributos propios
                detalle.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setTipoItem(rs.getInt("tipo_item"));
                detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                
                // Mapeo de las Relaciones
                // Venta 
                /*
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta")); // Asume que getIdVenta() es el setter correcto
                detalle.setVenta(venta);
                
                // Producto
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                detalle.setProducto(producto);
                
                // Funcion
                Funcion funcion = new Funcion();
                funcion.setIdFuncion(rs.getInt("id_funcion"));
                detalle.setFuncion(funcion);
                
                // Asiento
                Asiento asiento = new Asiento();
                asiento.setIdAsiento(rs.getInt("id_asiento"));
                detalle.setAsiento(asiento);
                
                detalles.add(detalle);*/
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
            
            // Asignación de FKs (Extrayendo el ID del objeto)
            /*
            pst.setInt(1, detalle.getVenta().getIdVenta()); 
            pst.setInt(2, detalle.getProducto().getIdProducto());
            pst.setInt(3, detalle.getFuncion().getIdFuncion());
            pst.setInt(4, detalle.getAsiento().getIdAsiento());

            // Asignación de atributos propios
            pst.setInt(5, detalle.getCantidad());
            pst.setInt(6, detalle.getTipoItem());
            pst.setDouble(7, detalle.getPrecioUnitario());

            pst.executeUpdate();
*/
        }
    }

    @Override
    public DetalleVenta leer(int id) throws SQLException {
        String query = "SELECT * FROM detalle_ventas WHERE id_detalle_venta = ?";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, id);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    
                    // Mapeo de atributos propios
                    detalle.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setTipoItem(rs.getInt("tipo_item"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    
                    // Mapeo de las Relaciones (Opción 2: Asignar solo el ID de la FK al objeto)
                    /*
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
                    asiento.setIdAsiento(rs.getInt("id_asiento"));
                    detalle.setAsiento(asiento);
                    */
                    return detalle;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(DetalleVenta detalle) throws SQLException {
        String query = "UPDATE detalle_ventas SET "
                     + "id_venta = ?, id_producto = ?, id_funcion = ?, id_asiento = ?, "
                     + "cantidad = ?, tipo_item = ?, precio_unitario = ? "
                     + "WHERE id_detalle_venta = ?";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            // Asignación de FKs (Extrayendo el ID del objeto)
            /*
            pst.setInt(1, detalle.getVenta().getIdVenta());
            pst.setInt(2, detalle.getProducto().getIdProducto());
            pst.setInt(3, detalle.getFuncion().getIdFuncion());
            pst.setInt(4, detalle.getAsiento().getIdAsiento());

            // Asignación de atributos propios
            pst.setInt(5, detalle.getCantidad());
            pst.setInt(6, detalle.getTipoItem());
            pst.setDouble(7, detalle.getPrecioUnitario());

            // Cláusula WHERE
            pst.setInt(8, detalle.getIdDetalleVenta());

            pst.executeUpdate();*/
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
    
}
