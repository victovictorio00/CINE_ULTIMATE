package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Pelicula;
import modelo.PeliculaDao;

@WebServlet("/DetallePeliculaServlet")  // <-- ESTA LÍNEA ES LA CLAVE
public class DetallePeliculaServlet extends HttpServlet {

    private PeliculaDao peliculaDao;

    @Override
    public void init() throws ServletException {
        this.peliculaDao = new PeliculaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

            request.setAttribute("pelicula", pelicula);
            request.getRequestDispatcher("/Cliente/DetallePelicula.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener la película");
        }
    }
}
