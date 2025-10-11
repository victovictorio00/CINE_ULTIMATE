package Controlador;

import modelo.UsuarioDao;
import modelo.Usuario;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private UsuarioDao usuarioDao;

    // constantes de lógica
    private static final int MAX_INTENTOS = 3;
    private static final int ESTADO_ACTIVO = 1;     // ajusta al id real de "ACTIVO"
    private static final int ESTADO_BLOQUEADO = 2;  // ajusta al id real de "BLOQUEADO"

    @Override
    public void init() {
        usuarioDao = new UsuarioDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        // Lógica de redirección según el rol
        if (rolId != null) {
            if (rolId == 1) { // 1 = Cliente (ajusta si es necesario)
                response.sendRedirect(request.getContextPath() + "/DashboardServlet");
            } else if (rolId == 2) { // 2 = Admin (ajusta si es necesario)
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
}
