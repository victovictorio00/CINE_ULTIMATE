package Controlador;

import modelo.UsuarioDao;
import modelo.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private String RECAPTCHA_SECRET_KEY;

    private UsuarioDao usuarioDao;

    // constantes de lógica
    private static final int MAX_INTENTOS = 3;
    private static final int ESTADO_ACTIVO = 1;     // ajusta al id real de "ACTIVO"
    private static final int ESTADO_BLOQUEADO = 2;  // ajusta al id real de "BLOQUEADO"

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDao = new UsuarioDao();

        // Cargar la clave secreta desde el archivo de propiedades
        Properties props = new Properties();

        // La ruta es relativa a la raíz de la aplicación web (WEB-INF)
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/secrets_temp.properties")) {
            if (input == null) {
                // Si el archivo no se encuentra (típico si el compañero olvida crearlo)
                System.err.println("ERROR CRÍTICO: No se encontró /WEB-INF/secret.properties.");
                // Asigna un valor vacío para que la validación del doPost falle de forma controlada
                RECAPTCHA_SECRET_KEY = "";
            } else {
                props.load(input);
                RECAPTCHA_SECRET_KEY = props.getProperty("recaptcha.secret.key");

                if (RECAPTCHA_SECRET_KEY == null || RECAPTCHA_SECRET_KEY.isEmpty()) {
                    System.err.println("ERROR CRÍTICO: La clave 'recaptcha.secret.key' está vacía en el archivo.");
                    RECAPTCHA_SECRET_KEY = ""; // Asegura que la verificación falle
                } else {
                    System.out.println("DEBUG: Clave de reCAPTCHA cargada exitosamente. Longitud: " + RECAPTCHA_SECRET_KEY.length());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de propiedades: " + e.getMessage());
            // Lanza una excepción para detener el servlet si la configuración es crítica
            throw new ServletException("Fallo en la configuración de la clave secreta.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validación de Clave Secreta (Verificar que se cargó algo en init())
        if (RECAPTCHA_SECRET_KEY == null || RECAPTCHA_SECRET_KEY.isEmpty()) {
             System.err.println("ERROR: RECAPTCHA_SECRET_KEY no cargada. No se pudo verificar reCAPTCHA.");
             request.setAttribute("error", "Error de configuración del servidor (Captcha).");
             request.getRequestDispatcher("Login.jsp").forward(request, response);
             return;
        }

        // 1. Obtener la respuesta de reCAPTCHA
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        // 2. Validación Inicial: ¿El usuario hizo clic en la casilla?
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            request.setAttribute("error", "Por favor, complete el reCAPTCHA.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        // 3. Verificación con Google (La parte que llama al servidor)
        try {
            if (!verifyRecaptcha(gRecaptchaResponse)) {
                request.setAttribute("error", "Verificación de reCAPTCHA fallida. Intente de nuevo.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            // Manejar errores de conexión (ej: la máquina no tiene internet)
            request.setAttribute("error", "Error de conexión con el servicio de Captcha.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        // Opcional pero recomendado para caracteres especiales
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String errorMsg = null;

        try {
            //Trae al usuario por username (para leer intentos y estado)
            Usuario u = usuarioDao.getByUsername(username);

            //Verifica existencia
            if (u == null) {
                //Mensaje de error genérico
                errorMsg = "Credenciales incorrectas.";
                manejarFallo(request, response, username, errorMsg);
                return;
            }

            int intentosFallidosActuales = u.getNumeroIntentos();
            int estadoActualId = u.getIdEstadoUsuario() != null ? u.getIdEstadoUsuario().getIdEstadoUsuario() : 0;

            // verifica bloqueo
            if (estadoActualId == ESTADO_BLOQUEADO) {
                errorMsg = "Tu cuenta se encuentra bloqueada. Contacta al administrador.";
                manejarFallo(request, response, username, errorMsg);
                return;
            }

            // verifica numero de intentos
            if (intentosFallidosActuales > MAX_INTENTOS) {
                // Si la BD no lo había bloqueado, lo bloqueamos ahora
                if (estadoActualId == ESTADO_ACTIVO) {
                    usuarioDao.bloquearUsuario(u.getIdUsuario(), ESTADO_BLOQUEADO);
                }
                errorMsg = "Has superado el límite de intentos (" + MAX_INTENTOS + "). Tu cuenta ha sido bloqueada.";
                manejarFallo(request, response, username, errorMsg);
                return;
            }

            // Valida credenciales (BCrypt/fallback dentro del DAO)
            Usuario usuarioAutenticado = usuarioDao.validateUser(username, password);

            if (usuarioAutenticado != null) {
                // Resetear intentos a 3
                usuarioDao.resetearIntentos(u.getIdUsuario());
                crearSesionYRedirigir(request, response, usuarioAutenticado);
                return;
            }

            // Fallo de autenticacion 
            usuarioDao.aumentarIntentos(u.getIdUsuario());
            int nuevosIntentosFallidos = intentosFallidosActuales + 1;

            if (nuevosIntentosFallidos > MAX_INTENTOS) {
                // Bloqueo y mensaje final
                usuarioDao.bloquearUsuario(u.getIdUsuario(), ESTADO_BLOQUEADO);
                errorMsg = "¡Último intento fallido! Tu cuenta ha sido bloqueada.";
            } else {
                // Mensaje de advertencia
                errorMsg = "Credenciales incorrectas. Intento " + nuevosIntentosFallidos + " de " + MAX_INTENTOS + ".";
            }

            manejarFallo(request, response, username, errorMsg);

        } catch (SQLException e) {
            e.printStackTrace();
            errorMsg = "Error interno del servidor al procesar la solicitud.";
            manejarFallo(request, response, username, errorMsg);
        }
    }

    private void manejarFallo(HttpServletRequest request, HttpServletResponse response, String username, String errorMsg)
            throws ServletException, IOException {
        request.setAttribute("error", errorMsg);
        request.setAttribute("lastUsername", username);
        request.getRequestDispatcher("/Login.jsp").forward(request, response);
    }
    
    private void crearSesionYRedirigir(HttpServletRequest request, HttpServletResponse response, Usuario u)
        throws IOException, ServletException {
    HttpSession session = request.getSession(true);
    session.setAttribute("userId", u.getIdUsuario());
    session.setAttribute("username", u.getUsername());
    session.setAttribute("nombreCompleto", u.getNombreCompleto());
    Integer rolId = u.getIdRol() != null ? u.getIdRol().getIdRol() : null;
    session.setAttribute("userRoleId", rolId);

    // 🔹 NUEVO: atributo "rol" que usarán tus JSP para validación
    if (rolId != null) {
        if (rolId == 1) { // Cliente
            session.setAttribute("rol", "cliente");  // ← añadido
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
        } else if (rolId == 2) { // Admin
            session.setAttribute("rol", "admin");    // ← añadido
            response.sendRedirect(request.getContextPath() + "/AdminDashboard.jsp");
        } else {
            session.invalidate();
            manejarFallo(request, response, u.getUsername(), "Rol de usuario no válido.");
        }
    } else {
        session.invalidate();
        manejarFallo(request, response, u.getUsername(), "Rol de usuario no asignado.");
    }
}




    /**
     * Método auxiliar para enviar la solicitud de verificación a Google.
     */
    private boolean verifyRecaptcha(String gRecaptchaResponse) throws Exception {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        // Construir la URL con parámetros: clave secreta y respuesta del usuario
        String postParams = "secret=" + RECAPTCHA_SECRET_KEY
                + "&response=" + gRecaptchaResponse;

        // Abrir conexión
        java.net.URL obj = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        // Enviar parámetros
        try (java.io.DataOutputStream wr = new java.io.DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postParams);
            wr.flush();
        }

        // Leer la respuesta JSON
        try (java.io.BufferedReader in = new java.io.BufferedReader(
                new java.io.InputStreamReader(con.getInputStream()))) {

            StringBuilder responseData = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseData.append(inputLine);
            }

            // Simplemente busca la cadena "success": true. 
            // Idealmente, usarías una librería JSON (Gson) aquí.
            return responseData.toString().contains("\"success\": true");
        }
    }
}
