package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost/empleados1?useUnicode=true&characterEncoding=UTF-8"; //CAMBIAR SOLAMENTE empleados1 con el nombre de tu bd no tocar mas
    private static final String USER = "root";
    private static final String PASSWORD = ""; //recordar para MYSQL dejarlo en admin no vacio, solo para local dejarlo vacio 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al obtener la conexión a la base de datos.", e);
        }
    }
}