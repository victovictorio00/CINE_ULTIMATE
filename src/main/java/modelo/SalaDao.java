package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class SalaDao implements DaoCrud<Sala> {

    @Override
    public List<Sala> listar() throws SQLException {
        List<Sala> salas = new ArrayList<>();
        String query = "SELECT * FROM salas";
        try (Connection con = Conexion.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                sala.setNombre(rs.getString("nombre"));
                sala.setCapacidad(rs.getInt("capacidad"));
                salas.add(sala);
            }
        }
        return salas;
    }

    @Override
    public void insertar(Sala emp) throws SQLException {
        String query = "INSERT INTO salas (nombre, capacidad) VALUES (?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, emp.getNombre());
            pst.setInt(2, emp.getCapacidad());
            pst.executeUpdate();
        }
    }

    @Override
    public Sala leer(int id) throws SQLException {
        String query = "SELECT * FROM salas WHERE id_sala = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    sala.setNombre(rs.getString("nombre"));
                    sala.setCapacidad(rs.getInt("capacidad"));
                    return sala;
                }
            }
        }
        return null;
    }

    @Override
    public void editar(Sala emp) throws SQLException {
        String query = "UPDATE salas SET nombre = ?, capacidad = ? WHERE id_sala = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, emp.getNombre());
            pst.setInt(2, emp.getCapacidad());
            pst.setInt(3, emp.getIdSala());
            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM salas WHERE id_sala = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
    
}
