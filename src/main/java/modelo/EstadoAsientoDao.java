package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class EstadoAsientoDao implements DaoCrud<EstadoAsiento> {

    //los CRUD no fueron integrados a procedure, son básicos
    @Override

public List<EstadoAsiento> listar() throws SQLException {
    List<EstadoAsiento> lista = new ArrayList<>();
    String sql = "{CALL listarEstadoAsientos()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

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
    String sql = "{CALL insertarEstadoAsiento(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setString(1, ea.getNombre());
        cs.executeUpdate();
    }
}


    @Override
public EstadoAsiento leer(int id) throws SQLException {
    String sql = "{CALL leerEstadoAsiento(?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);
        try (ResultSet rs = cs.executeQuery()) {
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
    String sql = "{CALL editarEstadoAsiento(?, ?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, ea.getIdEstadoAsiento());
        cs.setString(2, ea.getNombre());
        cs.executeUpdate();
    }
}


    @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarEstadoAsiento(?)}";
    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);
        cs.executeUpdate();
    }
}

}
