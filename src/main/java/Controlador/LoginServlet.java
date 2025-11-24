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

    private static final int MAX_INTENTOS = 3;
    private static final int ESTADO_ACTIVO = 1;     // ajusta al id real de "ACTIVO"
    private static final int ESTADO_BLOQUEADO = 2;  // ajusta al id real de "BLOQUEADO"

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDao = new UsuarioDao();

        Properties props = new Properties();

        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/secrets_temp.properties")) {
            if (input == null) {
                System.err.println("ERROR CRÍTICO: No se encontró /WEB-INF/secret.properties.");
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
            throw new ServletException("Fallo en la configuración de la clave secreta.", e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        // Generar token si no existe
        if (session.getAttribute("csrfToken") == null) {
            String token = java.util.UUID.randomUUID().toString();
            session.setAttribute("csrfToken", token);
        }

        request.getRequestDispatcher("Login.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionToken = (String) request.getSession().getAttribute("csrfToken");
        String formToken = request.getParameter("csrfToken");

        if (sessionToken == null || !sessionToken.equals(formToken)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token inválido");
            return;
        }
        if (RECAPTCHA_SECRET_KEY == null || RECAPTCHA_SECRET_KEY.isEmpty()) {
            System.err.println("ERROR: RECAPTCHA_SECRET_KEY no cargada. No se pudo verificar reCAPTCHA.");
            request.setAttribute("error", "Error de configuración del servidor (Captcha).");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            request.setAttribute("error", "Por favor, complete el reCAPTCHA.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
        try {
            if (!verifyRecaptcha(gRecaptchaResponse)) {
                request.setAttribute("error", "Verificación de reCAPTCHA fallida. Intente de nuevo.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error de conexión con el servicio de Captcha.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String errorMsg = null;

        try {

            // Dentro de doPost, después de verificar reCAPTCHA y obtener username/password
            Usuario u = usuarioDao.getByUsername(username);
            if (u == null) {
                manejarFallo(request, response, username, "Credenciales incorrectas.");
                return;
            }

            // bloqueo / intentos (tu lógica existente)
            int intentosFallidosActuales = u.getNumeroIntentos();
            int estadoActualId = u.getIdEstadoUsuario() != null ? u.getIdEstadoUsuario().getIdEstadoUsuario() : 0;
            if (estadoActualId == ESTADO_BLOQUEADO) {
                manejarFallo(request, response, username, "Tu cuenta se encuentra bloqueada.");
                return;
            }
            if (intentosFallidosActuales > MAX_INTENTOS) {
                if (estadoActualId == ESTADO_ACTIVO) {
                    usuarioDao.bloquearUsuario(u.getIdUsuario(), ESTADO_BLOQUEADO);
                }
                manejarFallo(request, response, username, "Has superado el límite de intentos.");
                return;
            }

            // validar credenciales
            Usuario usuarioAutenticado = usuarioDao.validateUser(username, password);
            if (usuarioAutenticado == null) {
                usuarioDao.aumentarIntentos(u.getIdUsuario());
                manejarFallo(request, response, username, "Credenciales incorrectas.");
                return;
            }
            usuarioDao.resetearIntentos(u.getIdUsuario());
            String redirectParam = request.getParameter("redirect");
            String destinoSeguro = getSafeRedirect(request, redirectParam); // helper explicado abajo

            // Crear sesión y redirigir según rol o redirect seguro
            crearSesionYRedirigir(request, response, usuarioAutenticado, destinoSeguro);

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

    private void crearSesionYRedirigir(HttpServletRequest request, HttpServletResponse response, Usuario u, String destinoSeguro)
            throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        String csrfToken = java.util.UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);
        
        session.setAttribute("usuario", u);
        session.setAttribute("userId", u.getIdUsuario());
        session.setAttribute("username", u.getUsername());
        session.setAttribute("nombreCompleto", u.getNombreCompleto());
        Integer rolId = u.getIdRol() != null ? u.getIdRol().getIdRol() : null;
        session.setAttribute("userRoleId", rolId);

        // Ajustes de seguridad en cookie JSESSIONID (si quieres forzarlo)
        Cookie jsess = new Cookie("JSESSIONID", session.getId());
        jsess.setHttpOnly(true);
        jsess.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        if (request.isSecure()) {
            jsess.setSecure(true);
        }
        response.addCookie(jsess);

        if (destinoSeguro != null && !destinoSeguro.isEmpty()) {
            response.sendRedirect(response.encodeRedirectURL(destinoSeguro));
            return;
        }

        if (rolId != null) {
            if (rolId == 1) {
                session.setAttribute("rol", "cliente");
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/DashboardServlet"));
            } else if (rolId == 2) {
                session.setAttribute("rol", "admin");
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/AdminDashboardServlet"));
            } else {
                session.invalidate();
                manejarFallo(request, response, u.getUsername(), "Rol de usuario no válido.");
            }
        } else {
            session.invalidate();
            manejarFallo(request, response, u.getUsername(), "Rol de usuario no asignado.");
        }
    }

    private boolean verifyRecaptcha(String gRecaptchaResponse) throws Exception {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        String postParams = "secret=" + RECAPTCHA_SECRET_KEY
                + "&response=" + gRecaptchaResponse;

        // Abrir conexión
        java.net.URL obj = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        try (java.io.DataOutputStream wr = new java.io.DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postParams);
            wr.flush();
        }

        try (java.io.BufferedReader in = new java.io.BufferedReader(
                new java.io.InputStreamReader(con.getInputStream()))) {

            StringBuilder responseData = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseData.append(inputLine);
            }

            return responseData.toString().contains("\"success\": true");
        }
    }

    private String getSafeRedirect(HttpServletRequest request, String redirectParam) {
        if (redirectParam == null || redirectParam.isEmpty()) {
            return null;
        }
        try {
            String decoded = java.net.URLDecoder.decode(redirectParam, java.nio.charset.StandardCharsets.UTF_8.name());
            // Evitar URIs absolutas externas y esquemas
            if (decoded.startsWith("http://") || decoded.startsWith("https://") || decoded.contains("://")) {
                return null;
            }
            // Aceptar sólo URIs que empiecen con el contextPath o que sean relativas
            String context = request.getContextPath(); // ej: /MiApp
            if (decoded.startsWith(context) || decoded.startsWith("/")) {
                return decoded;
            } else {
                return context + "/" + decoded; // opcional: normalizar relativas
            }
        } catch (Exception e) {
            return null;
        }
    }

}
