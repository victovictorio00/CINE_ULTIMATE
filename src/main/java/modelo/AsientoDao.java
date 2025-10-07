package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class AsientoDao implements DaoCrud<Asiento> {

    @Override
    public List<Asiento> listar() throws SQLException {
        List<Asiento> lista = new ArrayList<>();
        String query = "SELECT * FROM asientos";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Asiento asiento = new Asiento();
                asiento.setId_asiento(rs.getInt("id_asiento"));
                asiento.setCodigo(rs.getString("codigo"));
                
                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                asiento.setId_sala(sala);
                
                EstadoAsiento estado = new EstadoAsiento();
                estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                asiento.setId_estado_asiento(estado);
                
                lista.add(asiento);
            }
        }
        return lista;
    }

    @Override
    public void insertar(Asiento asiento) throws SQLException {
        String query = "INSERT INTO asientos (id_sala, codigo, id_estado_asiento) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, asiento.getId_sala().getIdSala());
            pst.setString(2, asiento.getCodigo());
            pst.setInt(3, asiento.getId_estado_asiento().getIdEstadoAsiento());
            pst.executeUpdate();
        }
    }

    @Override
    public Asiento leer(int id) throws SQLException {
        String query = "SELECT * FROM asientos WHERE id_asiento = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Asiento asiento = new Asiento();
                    asiento.setId_asiento(rs.getInt("id_asiento"));
                    asiento.setCodigo(rs.getString("codigo"));

                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    asiento.setId_sala(sala);

                    EstadoAsiento estado = new EstadoAsiento();
                    estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    asiento.setId_estado_asiento(estado);

                    return asiento;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(Asiento asiento) throws SQLException {
        String query = "UPDATE asientos SET id_sala = ?, codigo = ?, id_estado_asiento = ? WHERE id_asiento = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, asiento.getId_sala().getIdSala());
            pst.setString(2, asiento.getCodigo());
            pst.setInt(3, asiento.getId_estado_asiento().getIdEstadoAsiento());
            pst.setInt(4, asiento.getId_asiento());
            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM asientos WHERE id_asiento = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}