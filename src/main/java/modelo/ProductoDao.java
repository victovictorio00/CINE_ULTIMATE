package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class ProductoDao implements DaoCrud<Producto> {

   @Override
public List<Producto> listar() throws SQLException {
    List<Producto> productos = new ArrayList<>();
    String sql = "{CALL listarProductos()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setFoto(rs.getBytes("foto"));
            p.setStock(rs.getInt("stock"));
            p.setPrecio(rs.getDouble("precio"));
            productos.add(p);
        }
    }
    return productos;
}


   @Override
public void insertar(Producto p) throws SQLException {
    String sql = "{CALL insertarProducto(?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setString(1, p.getNombre());
        cst.setString(2, p.getDescripcion());
        cst.setBytes(3, p.getFoto());
        cst.setInt(4, p.getStock());
        cst.setDouble(5, p.getPrecio());

        cst.executeUpdate();
    }
}


    @Override
public Producto leer(int id) throws SQLException {
    String sql = "{CALL leerProducto(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);

        try (ResultSet rs = cst.executeQuery()) {
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
    return null;
}


   @Override
public void editar(Producto p) throws SQLException {
    String sql = "{CALL editarProducto(?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, p.getIdProducto());
        cst.setString(2, p.getNombre());
        cst.setString(3, p.getDescripcion());
        cst.setBytes(4, p.getFoto());
        cst.setInt(5, p.getStock());
        cst.setDouble(6, p.getPrecio());

        cst.executeUpdate();
    }
}


    @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarProducto(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);
        cst.executeUpdate();
    }
}

    
    //metodo actualizar - separado del editar (momentáneo)
    public void actualizar(Producto p) throws SQLException {
        editar(p);
    }
}
