package Controlador;

import modelo.Usuario;
import modelo.UsuarioDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ActualizarPerfilServlet")
public class ActualizarPerfilServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        UsuarioDao dao = new UsuarioDao();

        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String nombreCompleto = request.getParameter("nombreCompleto");
            if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
                nombreCompleto = ((Usuario) request.getSession().getAttribute("usuario")).getNombreCompleto();
            }
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");
            String direccion = request.getParameter("direccion");

            dao.actualizarDatos(idUsuario, nombreCompleto, telefono, email, direccion);

            Usuario actualizado = dao.leerPorId(idUsuario);
            
            HttpSession session = request.getSession();
            session.setAttribute("usuario", actualizado);
            session.setAttribute("nombreCompleto", actualizado.getNombreCompleto());
            session.setAttribute("username", actualizado.getUsername());

            response.sendRedirect(request.getContextPath() + "/Cliente/PerfilCliente.jsp?success=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/Cliente/PerfilCliente.jsp?error=1");
        }
    }
}
