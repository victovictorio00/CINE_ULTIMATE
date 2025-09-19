/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.Usuarios;
import modelo.UsuarioDao;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
    private UsuarioDao usuarioDao;

    @Override
    public void init() {
        usuarioDao = new UsuarioDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("listar".equals(action)) {
                listarUsuarios(request, response);
            } else if ("nuevo".equals(action)) {
                mostrarFormularioNuevo(request, response);
            } else if ("editar".equals(action)) {
                mostrarFormularioEditar(request, response);
            } else if ("eliminar".equals(action)) {
                //eliminarUsuario(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("insertar".equals(action)) {
                insertarUsuario(request, response);
            } else if ("actualizar".equals(action)) {
                //actualizarUsuario(request, response);
            } else if ("insertarcliente".equals(action)) {
                insertarUsuarioCliente(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Usuarios> lista = usuarioDao.listar();
        request.setAttribute("listaUsuarios", lista);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Usuario.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearUsuario.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuarios usuario = usuarioDao.leer(id);
        if (usuario != null) {
            request.setAttribute("usuario", usuario);
            RequestDispatcher dispatcher = request.getRequestDispatcher("EditarUsuario.jsp");
            dispatcher.forward(request, response);
        } else {
            response.getWriter().println("Usuario no encontrado");
        }
    }

    private void insertarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");
        Usuarios usuario = new Usuarios();
        usuario.setUsername(username);
        usuario.setPassword(password); // No olvides cifrar la contraseña
        //usuario.setRol(rol);

        usuarioDao.insertar(usuario);
        response.sendRedirect("UsuarioServlet?action=listar");
    }

    private void insertarUsuarioCliente(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // El rol siempre será "cliente"
        String rol = "cliente";

        Usuarios usuario = new Usuarios();
        usuario.setUsername(username);
        usuario.setPassword(password);
        //usuario.setRol(rol);

        usuarioDao.insertar(usuario);
        //lo mandamos a el login ahora indicando que se registro correctamente
        response.sendRedirect("Login.jsp");
    }
    /*
    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");

        Usuarios usuario = new Usuarios(id, username, password, rol);
        usuarioDao.editar(usuario);
        response.sendRedirect("UsuarioServlet?action=listar");
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        usuarioDao.eliminar(id);
        response.sendRedirect("UsuarioServlet?action=listar");
    }
*/
}
