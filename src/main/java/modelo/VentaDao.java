package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDao implements DaoCrud<Venta> {

    @Override
    public List<Venta> listar() throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String query = "SELECT * FROM ventas";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Venta venta = new Venta();

                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setTotal(rs.getDouble("total"));
                venta.setMetodoPago(rs.getString("metodo_pago"));
                venta.setFecha(rs.getTimestamp("fecha")); // se guarda como texto en tu modelo

                // Mapeo del usuario cliente
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario_cliente"));
                venta.setIdUsuarioCliente(usuario);

                ventas.add(venta);
            }
        }
        return ventas;
    }

    @Override
    public void insertar(Venta venta) throws SQLException {
        String query = "INSERT INTO ventas (id_usuario_cliente, fecha, total, metodo_pago) VALUES (?, NOW(), ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
            pst.setDouble(2, venta.getTotal());
            pst.setString(3, venta.getMetodoPago());

            pst.executeUpdate();
        }
    }

    /**
     * Inserta la venta y retorna el ID autogenerado.
     */
    public int insertarYDevolverId(Venta venta) throws SQLException {
        String query = "INSERT INTO ventas (id_usuario_cliente, fecha, total, metodo_pago) VALUES (?, NOW(), ?, ?)";
        int idGenerado = 0;

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
            pst.setDouble(2, venta.getTotal());
            pst.setString(3, venta.getMetodoPago());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                venta.setIdVenta(idGenerado);
            }
        }
        return idGenerado;
    }

    @Override
    public Venta leer(int id) throws SQLException {
        String query = "SELECT * FROM ventas WHERE id_venta = ?";
        Venta venta = null;

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setTotal(rs.getDouble("total"));
                    venta.setMetodoPago(rs.getString("metodo_pago"));
                    venta.setFecha(rs.getTimestamp("fecha"));

                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario_cliente"));
                    venta.setIdUsuarioCliente(usuario);
                }
            }
        }
        return venta;
    }

    @Override
    public void editar(Venta venta) throws SQLException {
        String query = "UPDATE ventas SET id_usuario_cliente = ?, fecha = ?, total = ?, metodo_pago = ? WHERE id_venta = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
            pst.setTimestamp(2, new Timestamp(venta.getFecha().getTime()));
            pst.setDouble(3, venta.getTotal());
            pst.setString(4, venta.getMetodoPago());
            pst.setInt(5, venta.getIdVenta());

            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM ventas WHERE id_venta = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
    
    public boolean guardarVenta(Venta venta, List<DetalleVenta> detalles) {
        Connection con = null;
        PreparedStatement pstVenta = null;
        PreparedStatement pstDetalle = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false); // iniciar transacción

            // 1️⃣ Insertar venta y obtener ID generado
            String sqlVenta = "INSERT INTO ventas (id_usuario_cliente, fecha, total, metodo_pago) VALUES (?, NOW(), ?, ?)";
            pstVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            pstVenta.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
            pstVenta.setDouble(2, venta.getTotal());
            pstVenta.setString(3, venta.getMetodoPago());
            pstVenta.executeUpdate();

            rs = pstVenta.getGeneratedKeys();
            int idVenta = 0;
            if (rs.next()) {
                idVenta = rs.getInt(1);
                venta.setIdVenta(idVenta);
            }

            // 2️⃣ Insertar detalles asociados
            String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_funcion, id_asiento, id_producto, cantidad, tipo_item, precio_unitario) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstDetalle = con.prepareStatement(sqlDetalle);

            for (DetalleVenta d : detalles) {
                pstDetalle.setInt(1, idVenta);

                // Campos opcionales
                if (d.getFuncion() != null) {
                    pstDetalle.setInt(2, d.getFuncion().getIdFuncion());
                } else {
                    pstDetalle.setNull(2, Types.INTEGER);
                }

                if (d.getAsiento() != null) {
                    pstDetalle.setInt(3, d.getAsiento().getId_asiento());
                } else {
                    pstDetalle.setNull(3, Types.INTEGER);
                }

                if (d.getProducto() != null) {
                    pstDetalle.setInt(4, d.getProducto().getIdProducto());
                } else {
                    pstDetalle.setNull(4, Types.INTEGER);
                }

                pstDetalle.setInt(5, d.getCantidad());
                pstDetalle.setInt(6, d.getTipoItem());
                pstDetalle.setDouble(7, d.getPrecioUnitario());

                pstDetalle.addBatch();
            }

            pstDetalle.executeBatch();

            // 3️⃣ Confirmar transacción
            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstVenta != null) pstVenta.close();
                if (pstDetalle != null) pstDetalle.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
}