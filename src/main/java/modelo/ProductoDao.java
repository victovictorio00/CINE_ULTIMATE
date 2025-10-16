package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class ProductoDao implements DaoCrud<Producto> {

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, foto, stock, precio FROM productos";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFoto(rs.getBytes("foto"));
                p.setStock(rs.getInt("stock"));
                p.setPrecio(rs.getDouble("precio"));   // << precio
                productos.add(p);
            }
        }
        return productos;
    }

    @Override
    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, foto, stock, precio) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, p.getNombre());
            pst.setString(2, p.getDescripcion());
            pst.setBytes(3, p.getFoto());
            pst.setInt(4, p.getStock());
            pst.setDouble(5, p.getPrecio());          // << precio
            pst.executeUpdate();
        }
    }

    @Override
    public Producto leer(int id) throws SQLException {
        String sql = "SELECT id_producto, nombre, descripcion, foto, stock, precio FROM productos WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setFoto(rs.getBytes("foto"));
                    p.setStock(rs.getInt("stock"));
                    p.setPrecio(rs.getDouble("precio")); // << precio
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(Producto p) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, foto = ?, stock = ?, precio = ? WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, p.getNombre());
            pst.setString(2, p.getDescripcion());
            pst.setBytes(3, p.getFoto());
            pst.setInt(4, p.getStock());
            pst.setDouble(5, p.getPrecio());          // << precio
            pst.setInt(6, p.getIdProducto());
            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    // Si quieres mantener un método "actualizar" separado, que llame a editar(...)
    public void actualizar(Producto p) throws SQLException {
        editar(p);
    }
}
