package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.Conexion;

public class EmpleadoDao implements DaoCrud<Empleado> {

    //los CRUD no fueron integrados a procedure, son básicos
    @Override
public List<Empleado> listar() throws SQLException {
    List<Empleado> empleados = new ArrayList<>();
    String sql = "{CALL listarEmpleados()}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql);
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            Empleado empleado = new Empleado();
            empleado.setIdEmpleado(rs.getInt("id_empleado"));
            empleado.setNombre(rs.getString("nombre"));
            empleado.setDireccion(rs.getString("direccion"));
            empleado.setTelefono(rs.getString("telefono"));
            empleado.setCargo(rs.getString("cargo"));
            empleado.setSalario(rs.getDouble("salario"));
            empleados.add(empleado);
        }
    }
    return empleados;
}

 
    @Override
public void insertar(Empleado emp) throws SQLException {
    String sql = "{CALL insertarEmpleado(?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setString(1, emp.getNombre());
        cs.setString(2, emp.getDireccion());
        cs.setString(3, emp.getTelefono());
        cs.setString(4, emp.getCargo());
        cs.setDouble(5, emp.getSalario());

        cs.executeUpdate();
    }
}


    @Override
public Empleado leer(int id) throws SQLException {
    String sql = "{CALL obtenerEmpleadoPorId(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);

        try (ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setIdEmpleado(rs.getInt("id_empleado"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setDireccion(rs.getString("direccion"));
                empleado.setTelefono(rs.getString("telefono"));
                empleado.setCargo(rs.getString("cargo"));
                empleado.setSalario(rs.getDouble("salario"));
                return empleado;
            }
        }
    }
    return null;
}


    @Override
public void editar(Empleado emp) throws SQLException {
    String sql = "{CALL actualizarEmpleado(?, ?, ?, ?, ?, ?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, emp.getIdEmpleado());
        cs.setString(2, emp.getNombre());
        cs.setString(3, emp.getDireccion());
        cs.setString(4, emp.getTelefono());
        cs.setString(5, emp.getCargo());
        cs.setDouble(6, emp.getSalario());

        cs.executeUpdate();
    }
}


   @Override
public void eliminar(int id) throws SQLException {
    String sql = "{CALL eliminarEmpleado(?)}";

    try (Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, id);
        cs.executeUpdate();
    }
}

}
