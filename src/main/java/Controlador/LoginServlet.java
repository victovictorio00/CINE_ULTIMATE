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

    // ⬇⬇⬇ 1) AGREGA ESTAS CONSTANTES
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

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        try {
            // ⬇⬇⬇ Trae al usuario por username (para leer intentos y estado)
            Usuario u = usuarioDao.getByUsername(username);

            // Usuario no existe -> mensaje genérico (no revelar si existe)
            if (u == null) {
                request.setAttribute("error", "Credenciales incorrectas.");
                request.setAttribute("lastUsername", username);
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
                return;
            }

            int intentosRestantes = Math.max(u.getNumeroIntentos(), 0);

            // Ya bloqueado
            if (u.getIdEstadoUsuario() != null &&
                u.getIdEstadoUsuario().getIdEstadoUsuario() == ESTADO_BLOQUEADO) {
                request.setAttribute("error", "Tu cuenta está bloqueada. Contacta al administrador.");
                request.setAttribute("lastUsername", username);
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
                return;
            }

            // Sin intentos: asegura bloqueo y muestra mensaje
            if (intentosRestantes <= 0) {
                usuarioDao.bloquearUsuario(u.getIdUsuario(), ESTADO_BLOQUEADO);
                request.setAttribute("error", "Has superado el límite de intentos. Tu cuenta ha sido bloqueada.");
                request.setAttribute("lastUsername", username);
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
                return;
            }

            // Valida credenciales (BCrypt/fallback dentro del DAO)
            Usuario ok = usuarioDao.validateUser(username, password);

            if (ok != null) {
                // Resetear intentos a 3
                usuarioDao.resetearIntentosA3(ok.getIdUsuario());

                // (Opcional) verifica estado activo si tu DAO no lo filtra
                if (ok.getIdEstadoUsuario() == null ||
                    ok.getIdEstadoUsuario().getIdEstadoUsuario() != ESTADO_ACTIVO) {
                    request.setAttribute("error", "Tu usuario no está activo.");
                    request.setAttribute("lastUsername", username);
                    request.getRequestDispatcher("/Login.jsp").forward(request, response);
                    return;
                }

                // Crear sesión y redirigir según rol
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", ok.getIdUsuario());
                session.setAttribute("username", ok.getUsername());
                session.setAttribute("nombreCompleto", ok.getNombreCompleto());
                Integer rolId = ok.getIdRol() != null ? ok.getIdRol().getIdRol() : null;
                session.setAttribute("userRoleId", rolId);

                if (rolId != null && rolId == 1) {          // 1 = admin (ajusta)
                    response.sendRedirect("AdminDashboard.jsp");
                } else if (rolId != null && rolId == 2) {   // 2 = cliente (ajusta)
                    response.sendRedirect("ClienteServlet?action=listar");
                } else {
                    session.invalidate();
                    request.setAttribute("error", "Rol no asignado.");
                    request.setAttribute("lastUsername", username);
                    request.getRequestDispatcher("/Login.jsp").forward(request, response);
                }
                return;
            }

            // Credenciales incorrectas: decrementar e informar
            usuarioDao.decrementarIntentos(u.getIdUsuario());
            int restantesPost = Math.max(intentosRestantes - 1, 0);
            int intentoN = MAX_INTENTOS - restantesPost; // 1, 2, 3

            String msg;
            if (restantesPost <= 0) {
                usuarioDao.bloquearUsuario(u.getIdUsuario(), ESTADO_BLOQUEADO);
                msg = "Has superado el límite de intentos. Tu cuenta ha sido bloqueada.";
            } else {
                msg = "Error de credenciales. Intento " + intentoN + " de " + MAX_INTENTOS +
                      ". Te quedan " + restantesPost + ".";
            }

            request.setAttribute("error", msg);
            request.setAttribute("lastUsername", username);
            request.getRequestDispatcher("/Login.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al validar las credenciales.");
            request.setAttribute("lastUsername", username);
            request.getRequestDispatcher("/Login.jsp").forward(request, response);
        }
    }
}
