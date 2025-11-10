package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

/**
 * Clase DAO: DetalleVentaDao
 * ------------------------------------------------------
 * Gestiona las operaciones CRUD y consultas adicionales
 * relacionadas con los detalles de las ventas dentro del sistema CineMax.
 *
 * Cada DetalleVenta puede representar:
 *  - Un producto adquirido en la dulcería.
 *  - Una reserva de asiento para una función específica.
 *
 * La tabla 'detalle_ventas' funciona como tabla puente entre:
 *  - Ventas
 *  - Productos
 *  - Funciones
 *  - Asientos
 */
public class DetalleVentaDao implements DaoCrud<DetalleVenta> {

    // ==========================================================
    // CRUD BÁSICO
    // ==========================================================

    /**
     * Lista todos los detalles de venta registrados en la base de datos.
     *
     * @return Lista completa de objetos DetalleVenta.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    @Override
    public List<DetalleVenta> listar() throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_ventas";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapear(rs));
            }
        }
        return detalles;
    }

    /**
     * Inserta un nuevo detalle de venta en la base de datos.
     *
     * @param detalle Objeto DetalleVenta con los datos a registrar.
     * @throws SQLException Si ocurre un error durante la inserción SQL.
     */
    @Override
    public void insertar(DetalleVenta detalle) throws SQLException {

        String sql = "INSERT INTO detalle_ventas "
                + "(id_venta, id_producto, id_funcion, id_asiento_funcion, cantidad, tipo_item, precio_unitario) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Venta
            pst.setInt(1, detalle.getVenta().getIdVenta());

            // Producto
            if (detalle.getProducto() != null && detalle.getProducto().getIdProducto() > 0)
                pst.setInt(2, detalle.getProducto().getIdProducto());
            else
                pst.setNull(2, Types.INTEGER);

            // Función
            if (detalle.getFuncion() != null && detalle.getFuncion().getIdFuncion() > 0)
                pst.setInt(3, detalle.getFuncion().getIdFuncion());
            else
                pst.setNull(3, Types.INTEGER);

            // Asiento-Función
            if (detalle.getIdAsientoFuncion() != null && detalle.getIdAsientoFuncion().getIdAsientoFuncion() > 0)
                pst.setInt(4, detalle.getIdAsientoFuncion().getIdAsientoFuncion());
            else
                pst.setNull(4, Types.INTEGER);

            pst.setInt(5, detalle.getCantidad());
            pst.setInt(6, detalle.getTipoItem());
            pst.setDouble(7, detalle.getPrecioUnitario());

            pst.executeUpdate();

            // Recuperar ID generado
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    detalle.setIdDetalleVenta(keys.getInt(1));
                }
            }
        }
    }

    /**
     * Lee (busca) un detalle de venta según su ID.
     *
     * @param id Identificador del detalle de venta.
     * @return Objeto DetalleVenta si existe, o null si no se encuentra.
     * @throws SQLException Si ocurre un error durante la consulta SQL.
     */
    @Override
    public DetalleVenta leer(int id) throws SQLException {
        String sql = "SELECT * FROM detalle_ventas WHERE id_detalle_venta = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /**
     * Actualiza un detalle de venta existente.
     *
     * @param detalle Objeto DetalleVenta con los nuevos valores.
     * @throws SQLException Si ocurre un error durante la actualización.
     */
    @Override
    public void editar(DetalleVenta detalle) throws SQLException {
        String sql = "UPDATE detalle_ventas SET "
                + "id_venta = ?, id_producto = ?, id_funcion = ?, id_asiento_funcion = ?, "
                + "cantidad = ?, tipo_item = ?, precio_unitario = ? "
                + "WHERE id_detalle_venta = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, detalle.getVenta().getIdVenta());
            pst.setInt(2, (detalle.getProducto() != null && detalle.getProducto().getIdProducto() > 0)
                    ? detalle.getProducto().getIdProducto() : Types.NULL);
            pst.setInt(3, (detalle.getFuncion() != null && detalle.getFuncion().getIdFuncion() > 0)
                    ? detalle.getFuncion().getIdFuncion() : Types.NULL);
            pst.setInt(4, (detalle.getIdAsientoFuncion() != null && detalle.getIdAsientoFuncion().getIdAsientoFuncion() > 0)
                    ? detalle.getIdAsientoFuncion().getIdAsientoFuncion() : Types.NULL);
            pst.setInt(5, detalle.getCantidad());
            pst.setInt(6, detalle.getTipoItem());
            pst.setDouble(7, detalle.getPrecioUnitario());
            pst.setInt(8, detalle.getIdDetalleVenta());

            pst.executeUpdate();
        }
    }

    /**
     * Elimina un detalle de venta según su ID.
     *
     * @param id Identificador del detalle.
     * @throws SQLException Si ocurre un error durante la eliminación.
     */
    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM detalle_ventas WHERE id_detalle_venta = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }


    // ==========================================================
    // MÉTODOS EXTRA
    // ==========================================================

    /**
     * Lista todos los detalles pertenecientes a una venta específica,
     * incluyendo la información de producto, función, película, sala y asiento.
     *
     * @param idVenta Identificador de la venta principal.
     * @return Lista de DetalleVenta con toda la información relacionada.
     * @throws SQLException Si ocurre un error durante la consulta.
     */
    public List<DetalleVenta> listarPorVenta(int idVenta) throws SQLException {
        List<DetalleVenta> lista = new ArrayList<>();

        String sql = "SELECT dv.*, "
                + "p.id_producto, p.nombre AS p_nombre, p.precio AS p_precio, "
                + "f.id_funcion, f.fecha_inicio, f.fecha_fin, "
                + "pel.id_pelicula, pel.nombre AS pel_nombre, "
                + "s.id_sala, s.nombre AS s_nombre, "
                + "a.id_asiento, a.codigo AS codigo "
                + "FROM detalle_ventas dv "
                + "LEFT JOIN productos p ON p.id_producto = dv.id_producto "
                + "LEFT JOIN funciones f ON f.id_funcion = dv.id_funcion "
                + "LEFT JOIN peliculas pel ON pel.id_pelicula = f.id_pelicula "
                + "LEFT JOIN salas s ON s.id_sala = f.id_sala "
                + "LEFT JOIN asiento_funcion af ON af.id_asiento_funcion = dv.id_asiento_funcion "
                + "LEFT JOIN asientos a ON a.id_asiento = af.id_asiento "
                + "WHERE dv.id_venta = ? "
                + "ORDER BY dv.id_detalle_venta;";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idVenta);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }


    // ==========================================================
    // MÉTODO PRIVADO DE MAPEADO
    // ==========================================================

    /**
     * Convierte un registro del ResultSet en un objeto DetalleVenta,
     * incluyendo sus relaciones con otras entidades.
     *
     * @param rs ResultSet actual.
     * @return Objeto DetalleVenta completamente mapeado.
     */
    private DetalleVenta mapear(ResultSet rs) throws SQLException {
        DetalleVenta d = new DetalleVenta();

        // Datos base
        d.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setTipoItem(rs.getInt("tipo_item"));
        d.setPrecioUnitario(rs.getDouble("precio_unitario"));

        // Relación con venta
        Venta v = new Venta();
        v.setIdVenta(rs.getInt("id_venta"));
        d.setVenta(v);

        // Relación con producto
        int idProd = rs.getInt("id_producto");
        if (!rs.wasNull()) {
            Producto p = new Producto();
            p.setIdProducto(idProd);
            p.setNombre(rs.getString("p_nombre"));
            p.setPrecio(rs.getDouble("p_precio"));
            d.setProducto(p);
        }

        // Relación con función y película
        int idFunc = rs.getInt("id_funcion");
        if (!rs.wasNull()) {
            Funcion f = new Funcion();
            f.setIdFuncion(idFunc);
            f.setFechaInicio(rs.getTimestamp("fecha_inicio"));
            f.setFechaFin(rs.getTimestamp("fecha_fin"));

            Pelicula p = new Pelicula();
            p.setIdPelicula(rs.getInt("id_pelicula"));
            p.setNombre(rs.getString("pel_nombre"));
            f.setPelicula(p);

            Sala s = new Sala();
            s.setIdSala(rs.getInt("id_sala"));
            s.setNombre(rs.getString("s_nombre"));
            f.setSala(s);

            d.setFuncion(f);
        }

        // Relación con asiento
        int idAF = rs.getInt("id_asiento_funcion");
        if (!rs.wasNull()) {
            AsientoFuncion af = new AsientoFuncion();
            af.setIdAsientoFuncion(idAF);

            Asiento a = new Asiento();
            a.setId_asiento(rs.getInt("id_asiento"));
            a.setCodigo(rs.getString("codigo"));
            af.setAsiento(a);

            d.setIdAsientoFuncion(af);
        }

        return d;
    }
}
