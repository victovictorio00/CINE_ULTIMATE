package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class ComprobanteDao implements DaoCrud<Comprobante> {

    @Override
    public List<Comprobante> listar() throws SQLException {
        List<Comprobante> lista = new ArrayList<>();
        String query = "SELECT * FROM comprobantes";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Comprobante comp = new Comprobante();
                comp.setId_comprobante(rs.getInt("id_comprobante"));
                comp.setTipo_comprobante(rs.getString("tipo_comprobante"));
                comp.setFecha_emision(rs.getDate("fecha_emision"));

                Ventas venta = new Ventas();
                venta.setId_venta(rs.getInt("id_venta"));
                comp.setId_venta(venta);

                lista.add(comp);
            }
        }
        return lista;
    }

    @Override
    public void insertar(Comprobante comp) throws SQLException {
        String query = "INSERT INTO comprobantes (id_venta, tipo_comprobante, fecha_emision) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, comp.getId_venta().getId_venta());
            pst.setString(2, comp.getTipo_comprobante());
            pst.setDate(3, new java.sql.Date(comp.getFecha_emision().getTime()));
            pst.executeUpdate();
        }
    }

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
                    comp.setTipo_comprobante(rs.getString("tipo_comprobante"));
                    comp.setFecha_emision(rs.getDate("fecha_emision"));

                    Ventas venta = new Ventas();
                    venta.setId_venta(rs.getInt("id_venta"));
                    comp.setId_venta(venta);

                    return comp;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(Comprobante comp) throws SQLException {
        String query = "UPDATE comprobantes SET id_venta = ?, tipo_comprobante = ?, fecha_emision = ? WHERE id_comprobante = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, comp.getId_venta().getId_venta());
            pst.setString(2, comp.getTipo_comprobante());
            pst.setDate(3, new java.sql.Date(comp.getFecha_emision().getTime()));
            pst.setInt(4, comp.getId_comprobante());
            pst.executeUpdate();
        }
    }

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