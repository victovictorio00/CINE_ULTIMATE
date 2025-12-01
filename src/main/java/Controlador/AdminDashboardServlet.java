package Controlador;

import Conexion.Conexion;
import modelo.DashboardDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("rol") == null
                || !"admin".equals(sesion.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        try (Connection con = Conexion.getConnection()) {
            DashboardDAO dao = new DashboardDAO(con);

            request.setAttribute("totalVentas", dao.getTotalVentas());
            request.setAttribute("totalProductos", dao.getTotalProductos());
            request.setAttribute("totalEmpleados", dao.getTotalEmpleados());
            request.setAttribute("totalPeliculas", dao.getTotalPeliculas());

            request.setAttribute("ventasMensuales", dao.getVentasMensuales2025());

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar datos del dashboard: " + e.getMessage());
        }

        request.getRequestDispatcher("/AdminDashboard.jsp").forward(request, response);
    }
}
