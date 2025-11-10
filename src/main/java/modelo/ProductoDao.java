package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class ProductoDao implements DaoCrud<Producto> {

    // ==========================================================
    // MÉTODO: LISTAR TODOS LOS PRODUCTOS
    // ==========================================================
    @Override
    public List<Producto> listar() throws SQLException {
        // Lista donde se almacenarán los productos obtenidos de la BD
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, foto, stock, precio FROM productos";

        // Conexión a la base de datos y ejecución de la consulta
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            // Recorremos los resultados y los mapeamos a objetos Producto
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFoto(rs.getBytes("foto"));         // Imagen en bytes
                p.setStock(rs.getInt("stock"));
                p.setPrecio(rs.getDouble("precio"));   // Precio del producto
                productos.add(p);                      // Agregar a la lista
            }
        }
        return productos;
    }

    // ==========================================================
    // MÉTODO: INSERTAR NUEVO PRODUCTO
    // ==========================================================
    @Override
    public void insertar(Producto p) throws SQLException {
        // Consulta SQL para insertar un producto nuevo
        String sql = "INSERT INTO productos (nombre, descripcion, foto, stock, precio) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Asignar parámetros a la consulta
            pst.setString(1, p.getNombre());
            pst.setString(2, p.getDescripcion());
            pst.setBytes(3, p.getFoto());
            pst.setInt(4, p.getStock());
            pst.setDouble(5, p.getPrecio());

            // Ejecutar la inserción
            pst.executeUpdate();
        }
    }

    // ==========================================================
    // MÉTODO: LEER UN PRODUCTO POR SU ID
    // ==========================================================
    @Override
    public Producto leer(int id) throws SQLException {
        // Consulta SQL para buscar un producto específico por su ID
        String sql = "SELECT id_producto, nombre, descripcion, foto, stock, precio FROM productos WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Asigna el ID del producto como parámetro
            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                // Si existe el producto, se devuelve el objeto completo
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setFoto(rs.getBytes("foto"));
                    p.setStock(rs.getInt("stock"));
                    p.setPrecio(rs.getDouble("precio"));
                    return p;
                }
            }
        }
        return null; // Retorna null si no se encuentra el producto
    }

    // ==========================================================
    // MÉTODO: EDITAR (ACTUALIZAR) UN PRODUCTO EXISTENTE
    // ==========================================================
    @Override
    public void editar(Producto p) throws SQLException {
        // Consulta SQL para actualizar los datos del producto
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, foto = ?, stock = ?, precio = ? WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Asignar los nuevos valores
            pst.setString(1, p.getNombre());
            pst.setString(2, p.getDescripcion());
            pst.setBytes(3, p.getFoto());
            pst.setInt(4, p.getStock());
            pst.setDouble(5, p.getPrecio());
            pst.setInt(6, p.getIdProducto());

            // Ejecutar la actualización
            pst.executeUpdate();
        }
    }

    // ==========================================================
    // MÉTODO: ELIMINAR UN PRODUCTO POR SU ID
    // ==========================================================
    @Override
    public void eliminar(int id) throws SQLException {
        // Consulta SQL para eliminar un producto por ID
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Asigna el ID del producto que se eliminará
            pst.setInt(1, id);

            // Ejecuta la eliminación
            pst.executeUpdate();
        }
    }

    // ==========================================================
    // MÉTODO: ACTUALIZAR (VERSIÓN ALTERNATIVA)
    // ==========================================================
    // Método auxiliar que reutiliza la lógica de editar()
    // Se deja por compatibilidad con otras partes del proyecto
    public void actualizar(Producto p) throws SQLException {
        editar(p);
    }
}
