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
import java.net.HttpURLConnection; // Necesario para el método de verificación
import java.io.DataOutputStream;   // Necesario para el método de verificación
import java.io.InputStream;
import java.io.InputStreamReader;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

    private String RECAPTCHA_SECRET_KEY; // Clave para la verificación
    private UsuarioDao usuarioDao;

    @Override
    public void init() throws ServletException {
        // Inicializa el DAO
        usuarioDao = new UsuarioDao();

        // 1. Lógica para cargar la Clave Secreta
        Properties props = new Properties();
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/secrets_temp.properties")) {
            if (input == null) {
                System.err.println("ERROR CRÍTICO: No se encontró /WEB-INF/secrets_temp.properties.");
                RECAPTCHA_SECRET_KEY = "";
            } else {
                props.load(input);
                RECAPTCHA_SECRET_KEY = props.getProperty("recaptcha.secret.key");

                if (RECAPTCHA_SECRET_KEY == null || RECAPTCHA_SECRET_KEY.isEmpty()) {
                    System.err.println("ERROR CRÍTICO: La clave 'recaptcha.secret.key' está vacía.");
                    RECAPTCHA_SECRET_KEY = "";
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de propiedades: " + e.getMessage());
            throw new ServletException("Fallo en la configuración de la clave secreta.", e);
        }
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
        // **********************************************
        // 2. LÓGICA DE VERIFICACIÓN DE RECAPTCHA (NUEVO)
        // **********************************************
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        // 2.1. Validación Inicial
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            request.setAttribute("errorCaptcha", "Por favor, complete el reCAPTCHA.");
            // Usaremos RequestDispatcher para volver al formulario y mostrar el error
            request.getRequestDispatcher("Registro.jsp").forward(request, response);
            return;
        }

        // 2.2. Verificación con Google
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

    private boolean verifyRecaptcha(String gRecaptchaResponse) throws Exception {
        if (RECAPTCHA_SECRET_KEY.isEmpty()) {
            // Si la clave falló al cargar, forzamos un error de verificación
            System.err.println("Advertencia: Clave Secreta vacía. Captcha fallido por configuración.");
            return false;
        }

        String url = "https://www.google.com/recaptcha/api/siteverify";

        // Construir la URL con parámetros: clave secreta y respuesta del usuario
        String postParams = "secret=" + RECAPTCHA_SECRET_KEY
                + "&response=" + gRecaptchaResponse;

        java.net.URL obj = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        // Enviar parámetros
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postParams);
            wr.flush();
        }

        // Leer la respuesta JSON
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {

            StringBuilder responseData = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseData.append(inputLine);
            }

            // Busca la cadena "success": true. 
            return responseData.toString().contains("\"success\": true");
        }
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
