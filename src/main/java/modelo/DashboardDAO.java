/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


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
        String sql = "SELECT IFNULL(SUM(total), 0) AS total FROM ventas";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble("total") : 0;
        }
    }

    public int getTotalProductos() throws SQLException {
        String sql = "SELECT COUNT(id_producto) AS total FROM productos";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    public int getTotalEmpleados() throws SQLException {
        String sql = "SELECT COUNT(id_empleado) AS total FROM empleados";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    public int getTotalPeliculas() throws SQLException {
        String sql = "SELECT COUNT(id_pelicula) AS total FROM peliculas";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }
    public List<Double> getVentasMensuales2025() throws SQLException {
   String sql = "SELECT MONTH(fecha) AS mes, " +
             "IFNULL(SUM(total), 0) AS total_mensual " +
             "FROM ventas " +
             "WHERE YEAR(fecha) = 2025 " +
             "GROUP BY MONTH(fecha) " +
             "ORDER BY mes;";


    List<Double> ventasMensuales = new ArrayList<>(Collections.nCopies(12, 0.0)); // 12 meses
    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

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

