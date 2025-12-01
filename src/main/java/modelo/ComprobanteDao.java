package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class ComprobanteDao implements DaoCrud<Comprobante> {
    
    //los CRUD no fueron integrados a procedure, son básicos
 @Override
public List<Comprobante> listar() throws SQLException {
    List<Comprobante> lista = new ArrayList<>();
    String sql = "{CALL listarComprobantes()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            Comprobante comp = new Comprobante();
            comp.setId_comprobante(rs.getInt("id_comprobante"));
            comp.setTipoComprobante(rs.getString("tipo_comprobante"));
            comp.setFechaEmision(rs.getTimestamp("fecha_emision"));

            Venta venta = new Venta();
            venta.setIdVenta(rs.getInt("id_venta"));
            comp.setVenta(venta);

            lista.add(comp);
        }
    }

    return lista;
}



    @Override
public void insertar(Comprobante comp) throws SQLException {
    String sql = "{CALL insertarComprobante(?, ?, ?)}";

    // Abrir la conexión dentro del método
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, comp.getVenta().getIdVenta());
        cs.setString(2, comp.getTipoComprobante());
        cs.setTimestamp(3, new java.sql.Timestamp(comp.getFechaEmision().getTime()));
        cs.executeUpdate();
    }
}



  @Override
public Comprobante leer(int id) throws SQLException {
    String sql = "{CALL leerComprobante(?)}";

    // Abrir la conexión dentro del try
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);

        try (ResultSet rs = cs.executeQuery()) {
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


@Override
public void editar(Comprobante comp) throws SQLException {
    String sql = "{CALL editarComprobante(?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, comp.getId_comprobante());
        cs.setInt(2, comp.getVenta().getIdVenta());
        cs.setString(3, comp.getTipoComprobante());
        cs.setTimestamp(4, new java.sql.Timestamp(comp.getFechaEmision().getTime()));

        cs.executeUpdate();
    }
}



  @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarComprobante(?)}";
    
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);
        cs.executeUpdate();
    }
}


}
