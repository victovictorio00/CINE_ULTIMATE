package Controlador;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.Usuario;
import modelo.UsuarioDao;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/CambiarPasswordServlet")
public class CambiarPasswordServlet extends HttpServlet {

    private UsuarioDao usuarioDao;

    @Override
    public void init() throws ServletException {
        usuarioDao = new UsuarioDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int idUsuario = usuario.getIdUsuario();

        String nuevaPass = request.getParameter("nuevaPass");
        String confirmPass = request.getParameter("confirmPass");

        if (nuevaPass == null || confirmPass == null ||
            nuevaPass.trim().isEmpty() || confirmPass.trim().isEmpty()) {
            request.setAttribute("error", "Debes ingresar ambos campos de contraseña.");
            request.getRequestDispatcher("/Cliente/PerfilCliente.jsp").forward(request, response);
            return;
        }

        if (!nuevaPass.equals(confirmPass)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/Cliente/PerfilCliente.jsp").forward(request, response);
            return;
        }

        // === EN ESTA LÍNEA SE HACE EL HASHEO DE LA NUEVA CONTRASEÑA ===
        String hashedPassword = BCrypt.hashpw(nuevaPass, BCrypt.gensalt()); // ✅ IMPORTANTE

        try {
            usuarioDao.actualizarPassword(idUsuario, hashedPassword);
            System.out.println("✅ Contraseña actualizada correctamente para ID=" + idUsuario);

            response.sendRedirect(request.getContextPath() + "/Cliente/PerfilCliente.jsp?success=1");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al actualizar la contraseña: " + e.getMessage());
            request.getRequestDispatcher("/Cliente/PerfilCliente.jsp").forward(request, response);
        }
    }
}