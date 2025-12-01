package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class SalaDao implements DaoCrud<Sala> {
    //los CRUD y otros métodos no fueron integrados a procedure, son básicos
  @Override
public List<Sala> listar() throws SQLException {
    List<Sala> salas = new ArrayList<>();
    String sql = "{CALL listarSalas()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql);
         ResultSet rs = cst.executeQuery()) {

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
    String sql = "{CALL insertarSala(?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setString(1, emp.getNombre());
        cst.setInt(2, emp.getCapacidad());

        cst.executeUpdate();
    }
}


    @Override
public Sala leer(int id) throws SQLException {
    String sql = "{CALL leerSala(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);

        try (ResultSet rs = cst.executeQuery()) {
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
    String sql = "{CALL editarSala(?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, emp.getIdSala());
        cst.setString(2, emp.getNombre());
        cst.setInt(3, emp.getCapacidad());

        cst.executeUpdate();
    }
}


   @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarSala(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cst = con.prepareCall(sql)) {

        cst.setInt(1, id);
        cst.executeUpdate();
    }
}

}
