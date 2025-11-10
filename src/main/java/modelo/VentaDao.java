package modelo;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;

public class VentaDao implements DaoCrud<Venta> {

    @Override
    public List<Venta> listar() throws SQLException {
        // Lista para almacenar todas las ventas obtenidas
        List<Venta> ventas = new ArrayList<>();
        String query = "SELECT * FROM ventas";

        // Conexión a la base de datos y ejecución de la consulta
        try (Connection con = Conexion.getConnection(); 
             PreparedStatement pst = con.prepareStatement(query); 
             ResultSet rs = pst.executeQuery()) {

            // Recorrer los resultados
            while (rs.next()) {
                Venta venta = new Venta();

                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setTotal(rs.getDouble("total"));
                venta.setMetodoPago(rs.getString("metodo_pago"));
                venta.setFecha(rs.getTimestamp("fecha")); // se guarda como Timestamp

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
        // Inserta una venta básica en la base de datos
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

            // Recuperar el ID generado automáticamente
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
        // Consulta para obtener una venta específica con su cliente
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

                    // Asociar el cliente a la venta
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario_cliente"));
                    usuario.setNombreCompleto(rs.getString("nombre_completo")); // obtiene el nombre
                    venta.setIdUsuarioCliente(usuario);
                }
            }
        }
        return venta;
    }

    @Override
    public void editar(Venta venta) throws SQLException {
        // Actualiza una venta existente en la base de datos
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
        // Elimina una venta específica por su ID
        String query = "DELETE FROM ventas WHERE id_venta = ?";

        try (Connection con = Conexion.getConnection(); 
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    // Inserta una venta con sus detalles asociados (productos, funciones, etc.)
    public boolean guardarVenta(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas (id_usuario_cliente, fecha, total, metodo_pago) VALUES (?, NOW(), ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, cantidad, tipo_item, precio_unitario, id_producto, id_funcion, id_asiento_funcion) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false); // Inicia la transacción

            // 1. Insertar la venta principal
            int idVenta;
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getIdUsuarioCliente().getIdUsuario());
                psVenta.setDouble(2, venta.getTotal());
                psVenta.setString(3, venta.getMetodoPago());
                psVenta.executeUpdate();

                // Obtener ID generado
                try (ResultSet keys = psVenta.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se generó ID de venta");
                    }
                    idVenta = keys.getInt(1);
                    venta.setIdVenta(idVenta);
                }
            }

            // 2. Insertar los detalles relacionados con la venta
            try (PreparedStatement psDet = con.prepareStatement(sqlDetalle)) {
                for (DetalleVenta d : detalles) {
                    psDet.setInt(1, idVenta);
                    psDet.setInt(2, d.getCantidad());
                    psDet.setInt(3, d.getTipoItem());
                    psDet.setDouble(4, d.getPrecioUnitario());

                    // Si hay producto
                    if (d.getProducto() != null && d.getProducto().getIdProducto() > 0) {
                        psDet.setInt(5, d.getProducto().getIdProducto());
                    } else {
                        psDet.setNull(5, Types.INTEGER);
                    }

                    // Si hay función
                    if (d.getFuncion() != null && d.getFuncion().getIdFuncion() > 0) {
                        psDet.setInt(6, d.getFuncion().getIdFuncion());
                    } else {
                        psDet.setNull(6, Types.INTEGER);
                    }

                    // Si hay asiento_funcion
                    AsientoFuncion af = d.getIdAsientoFuncion();
                    if (af != null && af.getIdAsientoFuncion() > 0) {
                        psDet.setInt(7, af.getIdAsientoFuncion());
                    } else {
                        psDet.setNull(7, Types.INTEGER);
                    }

                    psDet.addBatch(); // Añadir al batch para ejecución masiva
                }
                psDet.executeBatch();
            }

            // Confirmar transacción
            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si hay error
        }
    }
    
    public List<Venta> obtenerReservasPorUsuario(int idUsuario) throws SQLException {
        // Lista las ventas hechas por un usuario específico
        List<Venta> lista = new ArrayList<>();

        String sql = "SELECT v.id_venta, v.fecha, v.total, v.metodo_pago, "
                   + "u.nombre_completo "
                   + "FROM ventas v "
                   + "JOIN usuarios u ON v.id_usuario_cliente = u.id_usuario "
                   + "WHERE v.id_usuario_cliente = ? "
                   + "ORDER BY v.fecha DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idUsuario);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Venta venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setFecha(rs.getTimestamp("fecha"));
                    venta.setTotal(rs.getDouble("total"));
                    venta.setMetodoPago(rs.getString("metodo_pago"));

                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(idUsuario);
                    usuario.setNombreCompleto(rs.getString("nombre_completo"));
                    venta.setIdUsuarioCliente(usuario);

                    lista.add(venta);
                }
            }
        }

        return lista;
    }

}
