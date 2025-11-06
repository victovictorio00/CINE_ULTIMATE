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
        String query = "SELECT * FROM ventas WHERE id_venta = ?";
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
        try (Connection con = Conexion.getConnection(); CallableStatement cs = con.prepareCall("{CALL sp_guardar_venta_con_detalles(?, ?, ?, ?, ?, ?)}")) {

            // Construir JSON de detalles
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < detalles.size(); i++) {
                DetalleVenta d = detalles.get(i);
                json.append(String.format(
                        "{\"cantidad\":%d,\"tipo_item\":%d,\"precio_unitario\":%.2f,\"id_producto\":%s,\"id_funcion\":%s,\"id_asiento\":%s}",
                        d.getCantidad(),
                        d.getTipoItem(),
                        d.getPrecioUnitario(),
                        d.getProducto() != null ? d.getProducto().getIdProducto() : "null",
                        d.getFuncion() != null ? d.getFuncion().getIdFuncion() : "null",
                        d.getAsiento() != null ? d.getAsiento().getId_asiento() : "null"
                ));
                if (i < detalles.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            // IN
            cs.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
            cs.setDouble(2, venta.getTotal());
            cs.setString(3, venta.getMetodoPago());
            cs.setString(4, json.toString());

            // OUT
            cs.registerOutParameter(5, Types.INTEGER); // p_id_venta
            cs.registerOutParameter(6, Types.VARCHAR); // p_error

            cs.execute();

            String error = cs.getString(6);
            if (error != null) {
                throw new SQLException(error);
            }

            venta.setIdVenta(cs.getInt(5));
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
