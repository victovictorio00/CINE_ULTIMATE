package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.EstadoUsuario;
import modelo.Rol;
import modelo.Usuario;
import modelo.UsuarioDao;
import org.mindrot.jbcrypt.BCrypt;

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
            if ("insertarcliente".equals(action)) {
                insertarUsuarioCliente(request, response);
            } else if ("actualizar".equals(action)) {
                //actualizarUsuario(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Usuario> lista = usuarioDao.listar();
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
        Usuario usuario = usuarioDao.leer(id);
        if (usuario != null) {
            request.setAttribute("usuario", usuario);
            RequestDispatcher dispatcher = request.getRequestDispatcher("EditarUsuario.jsp");
            dispatcher.forward(request, response);
        } else {
            response.getWriter().println("Usuario no encontrado");
        }
    }

    private void insertarUsuarioAdmin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // El rol siempre será "cliente" osea id 2
        Rol rol = new Rol(2, "Administrador");

        EstadoUsuario estado = new EstadoUsuario();
        estado.setIdEstadoUsuario(1);
        estado.setNombre("activo");

        String nombre_completo = username;

        String dni = "";
        Random rand = new Random();
        int dni2 = 10000000 + rand.nextInt(90000000); // genera entre 10000000 y 99999999
        dni = "" + dni2;

        String telefono = "";
        String email = "";
        String direccion = "";

        Usuario usuario = new Usuario(
                0, // id_usuario (0 si es autoincremental en la BD)
                rol, // id_rol
                estado, // id_estado_usuario
                nombre_completo, // nombre completo
                dni, // DNI
                username, // nombre de usuario
                hashedPassword, // contraseña
                telefono, // teléfono
                email, // correo
                direccion, // dirección
                0 // número de intentos (empieza en 0)
        );

        usuarioDao.insertar(usuario);
        //lo mandamos a el login ahora indicando que se registro correctamente
        response.sendRedirect("Login.jsp");
    }

    private void insertarUsuarioCliente(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String nombreCompleto = request.getParameter("nombreCompleto");
        String dni = request.getParameter("dni");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        // El rol siempre será "cliente" osea id 2
        Rol rol = new Rol(1, "Cliente");
        // El estado del cliente será "activo" al crearse
        EstadoUsuario estado = new EstadoUsuario(1, "activo");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");

        Usuario usuario = new Usuario(
                0, // id_usuario (0 si es autoincremental en la BD)
                rol, // id_rol
                estado, // id_estado_usuario
                nombreCompleto, // nombre completo
                dni, // DNI
                username, // nombre de usuario
                hashedPassword, // contraseña
                telefono, // teléfono
                email, // correo
                direccion, // dirección
                0 // número de intentos (empieza en 0)
        );
        usuarioDao.insertar(usuario);
        response.sendRedirect("Login.jsp");
    }
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
