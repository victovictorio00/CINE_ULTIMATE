/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class ProductoDao implements DaoCrud<Producto> {

    // Listar productos
    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM productos";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setFoto(rs.getBytes("foto"));
                producto.setStock(rs.getInt("stock"));
                productos.add(producto);
            }
        }
        return productos;
    }

    // Insertar un producto
    @Override
    public void insertar(Producto producto) throws SQLException {
        String query = "INSERT INTO productos (nombre, descripcion, foto, stock) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, producto.getNombre());
            pst.setString(2, producto.getDescripcion());
            pst.setBytes(3, producto.getFoto());
            pst.setInt(4, producto.getStock());
            pst.executeUpdate();
        }
    }

    // Leer un producto por su ID
    @Override
    public Producto leer(int id) throws SQLException {
        String query = "SELECT * FROM productos WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setFoto(rs.getBytes("foto"));
                    producto.setStock(rs.getInt("stock"));
                    return producto;
                }
            }
        }
        return null;
    }

    // Actualizar un producto
    @Override
    public void editar(Producto producto) throws SQLException {
        String query = "UPDATE productos SET nombre = ?, descripcion = ?, foto = , stock = ?  WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, producto.getNombre());
            pst.setString(2, producto.getDescripcion());
            pst.setBytes(3, producto.getFoto());
            pst.setInt(4, producto.getStock());
            pst.setInt(5, producto.getIdProducto());
            pst.executeUpdate();
        }
    }

    // Eliminar un producto
    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM productos WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
    
    
    
public void actualizar(Producto producto) throws SQLException {
    String query = "UPDATE productos SET nombre = ?, descripcion = ?, foto = ?, stock = ? WHERE id = ?";
    try (Connection con = Conexion.getConnection();
         PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, producto.getNombre());
            pst.setString(2, producto.getDescripcion());
            pst.setBytes(3, producto.getFoto());
            pst.setInt(4, producto.getStock());
            pst.setInt(5, producto.getIdProducto());
            pst.executeUpdate();
    }
}
}
