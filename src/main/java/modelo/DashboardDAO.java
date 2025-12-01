/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import Conexion.Conexion;
import java.sql.*;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    private final Connection con;

    public DashboardDAO(Connection con) {
        this.con = con;
    }

   public double getTotalVentas() throws SQLException {
    String sql = "{CALL getTotalVentas()}";
    try (CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

        return rs.next() ? rs.getDouble("total") : 0;
    }
}



   public int getTotalProductos() throws SQLException {
    String sql = "{CALL getTotalProductos(?)}";
    try (CallableStatement cs = con.prepareCall(sql)) {
        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.execute();
        return cs.getInt(1);
    }
}


    public int getTotalEmpleados() throws SQLException {
    String sql = "{CALL getTotalEmpleados(?)}";
    try (CallableStatement cs = con.prepareCall(sql)) {
        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.execute();
        return cs.getInt(1);
    }
}


    public int getTotalPeliculas() throws SQLException {
    String sql = "{CALL getTotalPeliculas(?)}";
    try (CallableStatement cs = con.prepareCall(sql)) {
        cs.registerOutParameter(1, java.sql.Types.INTEGER);
        cs.execute();
        return cs.getInt(1);
    }
}

   public List<Double> getVentasMensuales2025() throws SQLException {
    String sql = "{CALL getVentasMensuales2025()}";
    List<Double> ventasMensuales = new ArrayList<>(Collections.nCopies(12, 0.0)); // 12 meses

    try (CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            int mes = rs.getInt("mes");
            double total = rs.getDouble("total_mensual");
            if (mes >= 1 && mes <= 12) {
                ventasMensuales.set(mes - 1, total);
            }
        }
    }

    return ventasMensuales;
}


}

