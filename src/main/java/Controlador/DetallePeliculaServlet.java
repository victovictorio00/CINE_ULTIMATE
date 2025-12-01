package Controlador;

import Conexion.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Pelicula;
import modelo.PeliculaDao;

@WebServlet(name = "DetallePeliculaServlet", urlPatterns = {"/DetallePeliculaServlet"})
public class DetallePeliculaServlet extends HttpServlet {

    private PeliculaDao peliculaDao;

    @Override
    public void init() throws ServletException {
        this.peliculaDao = new PeliculaDao();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el ID de la película");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Pelicula pelicula = peliculaDao.leer(id);

            if (pelicula == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Película no encontrada");
                return;
            }

            try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(
                    "UPDATE funciones SET activa = 0 WHERE fecha_fin < NOW()")) {
                int rows = ps.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error al desactivar funciones: " + e.getMessage());
            }

            // Enviar la película al JSP
            request.setAttribute("pelicula", pelicula);
            request.getRequestDispatcher("/Cliente/DetallePelicula.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener la película");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}