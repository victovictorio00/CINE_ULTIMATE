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

        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

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
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, asiento.getId_sala().getIdSala());
            pst.setString(2, asiento.getCodigo());
            pst.setInt(3, asiento.getId_estado_asiento().getIdEstadoAsiento());
            pst.executeUpdate();
        }
    }

    @Override
    public Asiento leer(int id) throws SQLException {
        String query = "SELECT * FROM asientos WHERE id_asiento = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

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
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

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
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
    
    public List<Asiento> obtenerAsientosPorSalaYFuncion(int idSala, int idFuncion) throws SQLException {
        List<Asiento> asientos = new ArrayList<>();
        
        String query = "SELECT " +
                       "    a.id_asiento, " +
                       "    a.codigo, " +
                       "    a.id_sala, " +
                       "    a.id_estado_asiento, " +
                       "    ea.nombre as nombre_estado, " +
                       "    CASE " +
                       "        WHEN dv.id_asiento IS NOT NULL THEN 'ocupado' " +
                       "        WHEN ea.nombre = 'bloqueado' THEN 'bloqueado' " +
                       "        ELSE 'disponible' " +
                       "    END as estado_actual " +
                       "FROM asientos a " +
                       "INNER JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento " +
                       "LEFT JOIN detalle_ventas dv ON a.id_asiento = dv.id_asiento " +
                       "    AND dv.id_funcion = ? " +
                       "WHERE a.id_sala = ? " +
                       "ORDER BY a.codigo";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, idFuncion);
            pst.setInt(2, idSala);
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Asiento asiento = new Asiento();
                    asiento.setId_asiento(rs.getInt("id_asiento"));
                    asiento.setCodigo(rs.getString("codigo"));
                    
                    // Crear y asignar objeto Sala
                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    asiento.setId_sala(sala);
                    
                    // Crear y asignar objeto EstadoAsiento
                    EstadoAsiento estado = new EstadoAsiento();
                    estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    estado.setNombre(rs.getString("nombre_estado"));
                    asiento.setId_estado_asiento(estado);
                    
                    // Asignar el estado actual calculado
                    asiento.setEstadoActual(rs.getString("estado_actual"));
                    
                    asientos.add(asiento);
                }
            }
        }
        
        return asientos;
    }

    public List<Asiento> obtenerAsientosPorSala(int idSala) throws SQLException {
        List<Asiento> asientos = new ArrayList<>();
        
        String query = "SELECT a.*, ea.nombre as nombre_estado " +
                       "FROM asientos a " +
                       "INNER JOIN estado_asientos ea ON a.id_estado_asiento = ea.id_estado_asiento " +
                       "WHERE a.id_sala = ? " +
                       "ORDER BY a.codigo";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, idSala);
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Asiento asiento = new Asiento();
                    asiento.setId_asiento(rs.getInt("id_asiento"));
                    asiento.setCodigo(rs.getString("codigo"));
                    
                    Sala sala = new Sala();
                    sala.setIdSala(rs.getInt("id_sala"));
                    asiento.setId_sala(sala);
                    
                    EstadoAsiento estado = new EstadoAsiento();
                    estado.setIdEstadoAsiento(rs.getInt("id_estado_asiento"));
                    estado.setNombre(rs.getString("nombre_estado"));
                    asiento.setId_estado_asiento(estado);
                    
                    asientos.add(asiento);
                }
            }
        }
        
        return asientos;
    }
}
