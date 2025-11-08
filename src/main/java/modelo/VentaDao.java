package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;

public class VentaDao implements DaoCrud<Venta> {

    @Override
    public List<Venta> listar() throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String query = "SELECT * FROM ventas";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

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

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

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

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

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
        String query = "SELECT v.id_venta,\n"
                + "       v.fecha,\n"
                + "       v.total,\n"
                + "       v.metodo_pago,\n"
                + "       v.id_usuario_cliente,\n"
                + "       u.nombre_completo\n"
                + "FROM ventas v\n"
                + "JOIN usuarios u ON u.id_usuario = v.id_usuario_cliente\n"
                + "WHERE v.id_venta = ?";

        Venta venta = null;

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
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
                    usuario.setNombreCompleto(rs.getString("nombre_completo")); // ✅ ¡esta línea!
                    venta.setIdUsuarioCliente(usuario);
                }
            }
        }
        return venta;
    }

    @Override
    public void editar(Venta venta) throws SQLException {
        String query = "UPDATE ventas SET id_usuario_cliente = ?, fecha = ?, total = ?, metodo_pago = ? WHERE id_venta = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

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

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public boolean guardarVenta(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas (id_usuario_cliente, fecha, total, metodo_pago) VALUES (?, NOW(), ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, cantidad, tipo_item, precio_unitario, id_producto, id_funcion, id_asiento_funcion) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);

            // 1. insertar venta
            int idVenta;
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
                psVenta.setDouble(2, venta.getTotal());
                psVenta.setString(3, venta.getMetodoPago());
                psVenta.executeUpdate();

                try (ResultSet keys = psVenta.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se generó ID de venta");
                    }
                    idVenta = keys.getInt(1);
                    venta.setIdVenta(idVenta);
                }
            }

            // 2. insertar detalles
            try (PreparedStatement psDet = con.prepareStatement(sqlDetalle)) {
                for (DetalleVenta d : detalles) {
                    psDet.setInt(1, idVenta);
                    psDet.setInt(2, d.getCantidad());
                    psDet.setInt(3, d.getTipoItem());
                    psDet.setDouble(4, d.getPrecioUnitario());

                    // producto
                    if (d.getProducto() != null && d.getProducto().getIdProducto() > 0) {
                        psDet.setInt(5, d.getProducto().getIdProducto());
                    } else {
                        psDet.setNull(5, Types.INTEGER);
                    }

                    // función
                    if (d.getFuncion() != null && d.getFuncion().getIdFuncion() > 0) {
                        psDet.setInt(6, d.getFuncion().getIdFuncion());
                    } else {
                        psDet.setNull(6, Types.INTEGER);
                    }

                    // asiento_funcion
                    AsientoFuncion af = d.getIdAsientoFuncion();
                    if (af != null && af.getIdAsientoFuncion() > 0) {
                        psDet.setInt(7, af.getIdAsientoFuncion());
                    } else {
                        psDet.setNull(7, Types.INTEGER);
                    }

                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
