package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class EstadoAsientoDao implements DaoCrud<EstadoAsiento> {

    @Override
    public List<EstadoAsiento> listar() throws SQLException {
        List<EstadoAsiento> lista = new ArrayList<>();
        String query = "SELECT * FROM estado_asientos";

        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                EstadoAsiento ea = new EstadoAsiento();
                ea.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                ea.setNombre(rs.getString("nombre"));
                lista.add(ea);
            }
        }
        return lista;
    }

    @Override
    public void insertar(EstadoAsiento ea) throws SQLException {
        String query = "INSERT INTO estado_asientos (nombre) VALUES (?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, ea.getNombre());
            pst.executeUpdate();
        }
    }

    @Override
    public EstadoAsiento leer(int id) throws SQLException {
        String query = "SELECT * FROM estado_asientos WHERE id_estado_asiento = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    EstadoAsiento ea = new EstadoAsiento();
                    ea.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    ea.setNombre(rs.getString("nombre"));
                    return ea;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(EstadoAsiento ea) throws SQLException {
        String query = "UPDATE estado_asientos SET nombre = ? WHERE id_estado_asiento = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, ea.getNombre());
            pst.setInt(2, ea.getIdEstadoAsiento());
            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM estado_asientos WHERE id_estado_asiento = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}