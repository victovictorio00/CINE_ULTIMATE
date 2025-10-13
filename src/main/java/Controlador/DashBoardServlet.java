
package Controlador;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import modelo.Pelicula;
import modelo.PeliculaDao;

@WebServlet("/DashboardServlet")
public class DashBoardServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DashBoardServlet.class.getName());
    private PeliculaDao peliculaDao;

    @Override
    public void init() throws ServletException {
        this.peliculaDao = new PeliculaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener la lista de películas desde la base de datos
            List<Pelicula> lista = peliculaDao.listar();
            if (lista == null) {
                lista = new ArrayList<>(); // Evitar null en el JSP
            }
            // Establecer la lista como atributo en el request
            request.setAttribute("peliculas", lista);
            // Hacer forward al JSP
            request.getRequestDispatcher("/Cliente/DashboardCliente.jsp").forward(request, response);
            System.out.println("Número de películas: " + lista.size());
        } catch (SQLException e) {
            logger.severe("Error al listar películas: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar las películas.");
        }
    }
}