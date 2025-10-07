/*
package Controlador;

import Conexion.Conexion;
import modelo.UsuarioDao;
import modelo.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private UsuarioDao usuarioDao;

    @Override
    public void init() {
        usuarioDao = new UsuarioDao(); // Inicializa el DAO
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Verificar las credenciales del usuario
            if (validateUser(username, password)) {
                // Consultar el rol del usuario desde la base de datos
                String rol = usuarioDao.getRolByUsername(username);

                // Crear una sesión para el usuario logueado
                HttpSession session = request.getSession();
                session.setAttribute("username", username);  // Guarda el nombre de usuario en la sesión
                session.setAttribute("userRole", rol);  // Guarda el rol en la sesión

                if ("admin".equals(rol)) {
                    response.sendRedirect("AdminDashboard.jsp");  // Página del Admin
                } else if ("cliente".equals(rol)) {
                    response.sendRedirect("ClienteServlet?action=listar");  // Página del Cliente (redirige a su lista de películas)
                } else {
                    response.getWriter().println("Rol no asignado.");
                }
            } else {
                response.getWriter().println("Credenciales incorrectas.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error al validar las credenciales.");
        }
    }

    // Verifica si el usuario y la contraseña son correctos
    private boolean validateUser(String username, String password) throws SQLException {
        return usuarioDao.validateUser(username, password);
    }
}
*/