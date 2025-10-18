package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.Funcion;
import modelo.FuncionDao;

@WebServlet(name = "FuncionClienteServlet", urlPatterns = {"/FuncionClienteServlet"})
public class FuncionClienteServlet extends HttpServlet {

    private FuncionDao funcionDao;

    @Override
    public void init() throws ServletException {
        funcionDao = new FuncionDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 🔹 Obtenemos la lista de funciones desde la base de datos
            List<Funcion> funciones = funcionDao.listar();

            // 🔹 Si quieres filtrar solo las activas, puedes hacer esto:
            // funciones.removeIf(f -> f.getEstadoFuncion() == null || !"Activa".equalsIgnoreCase(f.getEstadoFuncion().getNombre()));

            // 🔹 Enviamos la lista al JSP
            request.setAttribute("funciones", funciones);

            // 🔹 Redirigimos al index.jsp (cliente)
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error al cargar funciones disponibles para el cliente", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // para compatibilidad si se envía por POST
    }
}
