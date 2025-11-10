package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

/**
 * Clase DAO: ComprobanteDao
 * ------------------------------------------------------
 * Gestiona las operaciones CRUD básicas (Crear, Leer, Actualizar, Eliminar)
 * sobre la tabla `comprobantes` en la base de datos.
 *
 * Cada comprobante pertenece a una venta e incluye información 
 * sobre el tipo de comprobante (Boleta, Factura, Ticket) 
 * y la fecha de emisión.
 *
 * NOTA: En esta versión, los métodos son directos (sin stored procedures)
 * y usan sentencias SQL básicas.
 */
public class ComprobanteDao implements DaoCrud<Comprobante> {

    // ==========================================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================================

    /**
     * Lista todos los comprobantes registrados en la base de datos.
     * 
     * @return Lista de objetos Comprobante con sus datos y relación con la venta.
     */
    @Override
    public List<Comprobante> listar() throws SQLException {
        List<Comprobante> lista = new ArrayList<>();

        String query = "SELECT * FROM comprobantes";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Comprobante comp = new Comprobante();

                // Campos básicos del comprobante
                comp.setId_comprobante(rs.getInt("id_comprobante"));
                comp.setTipoComprobante(rs.getString("tipo_comprobante"));
                comp.setFechaEmision(rs.getTimestamp("fecha_emision"));

                // Relación con la venta
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                comp.setVenta(venta);

                lista.add(comp);
            }
        }
        return lista;
    }

    /**
     * Inserta un nuevo comprobante en la base de datos.
     * 
     * @param comp Objeto Comprobante con los datos a registrar.
     */
    @Override
    public void insertar(Comprobante comp) throws SQLException {
        String query = "INSERT INTO comprobantes (id_venta, tipo_comprobante, fecha_emision) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, comp.getVenta().getIdVenta());
            pst.setString(2, comp.getTipoComprobante());
            pst.setDate(3, new java.sql.Date(comp.getFechaEmision().getTime())); // convierte java.util.Date a java.sql.Date

            pst.executeUpdate();
        }
    }

    /**
     * Busca y devuelve un comprobante por su ID.
     * 
     * @param id Identificador único del comprobante.
     * @return Objeto Comprobante si se encuentra, o null si no existe.
     */
    @Override
    public Comprobante leer(int id) throws SQLException {
        String query = "SELECT * FROM comprobantes WHERE id_comprobante = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Comprobante comp = new Comprobante();
                    comp.setId_comprobante(rs.getInt("id_comprobante"));
                    comp.setTipoComprobante(rs.getString("tipo_comprobante"));
                    comp.setFechaEmision(rs.getTimestamp("fecha_emision"));

                    Venta venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    comp.setVenta(venta);

                    return comp;
                }
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un comprobante existente.
     * 
     * @param comp Objeto Comprobante con los datos actualizados.
     */
    @Override
    public void editar(Comprobante comp) throws SQLException {
        String query = "UPDATE comprobantes SET id_venta = ?, tipo_comprobante = ?, fecha_emision = ? "
                     + "WHERE id_comprobante = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, comp.getVenta().getIdVenta());
            pst.setString(2, comp.getTipoComprobante());
            pst.setDate(3, new java.sql.Date(comp.getFechaEmision().getTime()));
            pst.setInt(4, comp.getId_comprobante());

            pst.executeUpdate();
        }
    }

    /**
     * Elimina un comprobante de la base de datos según su ID.
     * 
     * @param id ID del comprobante a eliminar.
     */
    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM comprobantes WHERE id_comprobante = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
