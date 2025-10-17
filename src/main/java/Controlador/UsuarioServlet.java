package Controlador;

import java.io.BufferedReader;
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
import java.util.Properties;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

    private String RECAPTCHA_SECRET_KEY;
    private UsuarioDao usuarioDao;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServlet.class);

    @Override
    public void init() throws ServletException {
        usuarioDao = new UsuarioDao();
        Properties props = new Properties();

        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/secrets_temp.properties")) {
            if (input == null) {
                System.err.println("⚠️ ERROR: No se encontró /WEB-INF/secrets_temp.properties");
                RECAPTCHA_SECRET_KEY = "";
            } else {
                props.load(input);
                RECAPTCHA_SECRET_KEY = props.getProperty("recaptcha.secret.key", "");
                if (RECAPTCHA_SECRET_KEY.isEmpty()) {
                    System.err.println("⚠️ ERROR: Clave 'recaptcha.secret.key' vacía en properties.");
                }
            }
        } catch (IOException e) {
            throw new ServletException("Error al cargar configuración reCAPTCHA", e);
        }
    }

    /* ===============================
       GET — Acciones del Administrador
       =============================== */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        try {
        switch (action) {
            case "listar":
                listarUsuarios(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "eliminar": 
                eliminarUsuario(request, response);
                break;
            default:
                listarUsuarios(request, response);
                break;
        }
    } catch (SQLException e) {
        throw new ServletException(e);
    }
    }

    /* ===============================
       POST — Inserción / Registro
       =============================== */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Solo verificar reCAPTCHA para registro público
        if ("insertarcliente".equals(action)) {
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
                request.setAttribute("errorCaptcha", "Por favor, complete el reCAPTCHA.");
                request.getRequestDispatcher("Registro.jsp").forward(request, response);
                return;
            }

            try {
                if (!verifyRecaptcha(gRecaptchaResponse)) {
                    request.setAttribute("errorCaptcha", "Verificación de reCAPTCHA fallida. Intente de nuevo.");
                    request.getRequestDispatcher("Registro.jsp").forward(request, response);
                    return;
                }
            } catch (Exception e) {
                request.setAttribute("errorCaptcha", "Error de conexión con el servicio de Captcha.");
                request.getRequestDispatcher("Registro.jsp").forward(request, response);
                return;
            }
        }

        try {
           switch (action) {
    case "insertarcliente":
        insertarUsuarioCliente(request, response);
        break;
    case "crearUsuarioAdminPanel": 
        crearUsuarioAdminPanel(request, response);
        break;
    case "actualizar":
        actualizarUsuario(request, response);
    break;
    default:
        response.sendRedirect("UsuarioServlet?action=listar");
        break;
}
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /* ===============================
       LISTAR USUARIOS
       =============================== */
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Usuario> lista = usuarioDao.listarConDetalles();

        if (lista == null || lista.isEmpty()) {
            request.setAttribute("mensaje", "No hay usuarios registrados.");
        }

        request.setAttribute("listaUsuarios", lista);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Usuario.jsp");
        dispatcher.forward(request, response);
    }

    /* ===============================
       FORMULARIOS
       =============================== */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearUsuario.jsp");
        dispatcher.forward(request, response);
    }

    protected void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
    UsuarioDao usuarioDao = new UsuarioDao();

    try {
        // Usuario que vamos a editar
        Usuario usuario = usuarioDao.leer(idUsuario);

        // Listado de roles (desde la base de datos)
        List<Rol> roles = usuarioDao.listarRoles(); // Debes crear este método en UsuarioDao

        // Listado de estados de usuario
        List<EstadoUsuario> estados = usuarioDao.listarEstados(); // También debes crearlo en UsuarioDao

        // Enviar al JSP
        request.setAttribute("usuario", usuario);
        request.setAttribute("roles", roles);
        request.setAttribute("estados", estados);

        request.getRequestDispatcher("/EditarUsuario.jsp").forward(request, response);

    } catch (SQLException ex) {
        throw new ServletException("Error al cargar el formulario de edición", ex);
    }
}


    /* ===============================
       REGISTRAR ADMINISTRADOR DESDE PANEL
       =============================== */
private void crearUsuarioAdminPanel(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException {

    String nombreCompleto = request.getParameter("nombreCompleto");
    String dni = request.getParameter("dni");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String telefono = request.getParameter("telefono");
    String email = request.getParameter("email");
    String direccion = request.getParameter("direccion");

    // Validaciones básicas de campos obligatorios
    if (nombreCompleto == null || nombreCompleto.isEmpty() ||
        dni == null || dni.isEmpty() ||
        username == null || username.isEmpty() ||
        password == null || password.isEmpty()) {

        request.setAttribute("error", "Debe completar todos los campos obligatorios.");
        request.getRequestDispatcher("CrearUsuario.jsp").forward(request, response);
        return;
    }

    // Verificar que el username no exista
    if (usuarioDao.existeUsername(username)) {
        request.setAttribute("error", "El nombre de usuario ya existe. Intente con otro.");
        request.getRequestDispatcher("CrearUsuario.jsp").forward(request, response);
        return;
    }

    // Verificar que el DNI no exista
    if (usuarioDao.existeDNI(dni)) {
        request.setAttribute("error", "El DNI ya está registrado. Intente con otro.");
        request.getRequestDispatcher("CrearUsuario.jsp").forward(request, response);
        return;
    }

    // Crear usuario
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    Rol rol = new Rol(1, "Cliente"); // se asigna automáticamente
    EstadoUsuario estado = new EstadoUsuario(1, "Activo");

    Usuario usuario = new Usuario(
        0, rol, estado, nombreCompleto, dni, username,
        hashedPassword, telefono, email, direccion, 0
    );

    try {
        usuarioDao.insertar(usuario);
        response.sendRedirect("UsuarioServlet?action=listar");
    } catch (SQLException e) {
        // Atrapar cualquier otro error de BD inesperado
        request.setAttribute("error", "Ocurrió un error al registrar el usuario: " + e.getMessage());
        request.getRequestDispatcher("CrearUsuario.jsp").forward(request, response);
    }
}



    /* ===============================
       REGISTRAR CLIENTE
       =============================== */
    private void insertarUsuarioCliente(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String nombreCompleto = request.getParameter("nombreCompleto");
        String dni = request.getParameter("dni");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Rol rol = new Rol(1, "Cliente");
        EstadoUsuario estado = new EstadoUsuario(1, "Activo");

        Usuario usuario = new Usuario(
                0, rol, estado, nombreCompleto, dni, username,
                hashedPassword, telefono, email, direccion, 0
        );

        String ipAddress = request.getRemoteAddr();
        logger.info("AUDITORIA: Registro de nuevo cliente. Username={}, IP={}", username, ipAddress);

        usuarioDao.insertar(usuario);
        response.sendRedirect("Login.jsp");
    }

    /* ===============================
       VALIDACIÓN DE reCAPTCHA
       =============================== */
    private boolean verifyRecaptcha(String gRecaptchaResponse) throws Exception {
        if (RECAPTCHA_SECRET_KEY == null || RECAPTCHA_SECRET_KEY.isEmpty()) {
            System.err.println("⚠️ reCAPTCHA no configurado. Validación deshabilitada temporalmente.");
            return false;
        }

        String url = "https://www.google.com/recaptcha/api/siteverify";
        String postParams = "secret=" + RECAPTCHA_SECRET_KEY + "&response=" + gRecaptchaResponse;

        java.net.URL obj = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postParams);
            wr.flush();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder responseData = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseData.append(inputLine);
            }
            return responseData.toString().contains("\"success\": true");
        }
    }
    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException {

    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
    String nombreCompleto = request.getParameter("nombreCompleto");
    String dni = request.getParameter("dni");
    String username = request.getParameter("username");
    String password = request.getParameter("password"); // Puede estar vacío
    String telefono = request.getParameter("telefono");
    String email = request.getParameter("email");
    String direccion = request.getParameter("direccion");

    int idRol = Integer.parseInt(request.getParameter("idRol"));
    int idEstado = Integer.parseInt(request.getParameter("idEstadoUsuario"));

    // Crear objetos Rol y EstadoUsuario
    Rol rol = new Rol();
    rol.setIdRol(idRol);

    EstadoUsuario estado = new EstadoUsuario();
    estado.setIdEstadoUsuario(idEstado);

    // Leer usuario actual de la base de datos
    Usuario usuario = usuarioDao.leer(idUsuario);
    if (usuario == null) {
        request.setAttribute("error", "Usuario no encontrado.");
        request.getRequestDispatcher("EditarUsuario.jsp").forward(request, response);
        return;
    }

    // Actualizar datos
    usuario.setNombreCompleto(nombreCompleto);
    usuario.setDni(dni);
    usuario.setUsername(username);
    usuario.setTelefono(telefono);
    usuario.setEmail(email);
    usuario.setDireccion(direccion);
    usuario.setIdRol(rol);
    usuario.setIdEstadoUsuario(estado);

    // Actualizar contraseña solo si se ingresó una nueva
    if (password != null && !password.trim().isEmpty()) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        usuario.setPassword(hashedPassword);
    }

    // Guardar cambios en la base de datos
    usuarioDao.editar(usuario);

    // Redirigir a la lista de usuarios
    response.sendRedirect("UsuarioServlet?action=listar");
}
/* ===============================
   ELIMINAR USUARIO
   =============================== */
private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {

    // Obtener el ID desde el parámetro
    int idUsuario = Integer.parseInt(request.getParameter("id"));

    // Llamar al DAO para eliminar
    usuarioDao.eliminar(idUsuario);

    // Redirigir nuevamente a la lista
    response.sendRedirect("UsuarioServlet?action=listar");
}


}
