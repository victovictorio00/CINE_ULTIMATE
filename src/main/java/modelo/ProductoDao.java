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
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getString("precio"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setFoto(rs.getBytes("foto"));
                productos.add(producto);
            }
        }
        return productos;
    }

    // Insertar un producto
    @Override
    public void insertar(Producto producto) throws SQLException {
        String query = "INSERT INTO productos (nombre, precio, descripcion, foto) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, producto.getNombre());
            pst.setString(2, producto.getPrecio());
            pst.setString(3, producto.getDescripcion());
            pst.setBytes(4, producto.getFoto());
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
                    producto.setId(rs.getInt("id"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecio(rs.getString("precio"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setFoto(rs.getBytes("foto"));
                    return producto;
                }
            }
        }
        return null;
    }

    // Actualizar un producto
    @Override
    public void editar(Producto producto) throws SQLException {
        String query = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ?, foto = ?  WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, producto.getNombre());
            pst.setString(2, producto.getPrecio());
            pst.setString(3, producto.getDescripcion());
            pst.setBytes(4, producto.getFoto());
            pst.setInt(5, producto.getId());
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
    String query = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ?, foto = ?  WHERE id = ?";
    try (Connection con = Conexion.getConnection();
         PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, producto.getNombre());
        pst.setString(2, producto.getPrecio());  // Si el precio es un String en tu base de datos
        pst.setString(3, producto.getDescripcion());
        pst.setBytes(4, producto.getFoto());
        pst.setInt(5, producto.getId());  // Asegúrate de usar el ID correcto
        pst.executeUpdate();
    }
}

    
    
    
    
}
