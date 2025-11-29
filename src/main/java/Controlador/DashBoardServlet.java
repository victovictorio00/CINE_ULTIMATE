package Controlador;

import Conexion.Conexion;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import modelo.Pelicula;
import modelo.PeliculaDao;

@WebServlet(urlPatterns = "/DashboardServlet", loadOnStartup = 1)
public class DashBoardServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DashBoardServlet.class.getName());
    private PeliculaDao peliculaDao;

    @Override
    public void init() throws ServletException {
        super.init();
        this.peliculaDao = new PeliculaDao();
        // Desactivar funciones pasadas al iniciar
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(
                "UPDATE funciones SET activa = 0 WHERE fecha_fin < NOW()")) {
            int rows = ps.executeUpdate();
            System.out.println("Funciones desactivadas al inicio: " + rows);
        } catch (Exception e) {
            System.err.println("Error al desactivar funciones: " + e.getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
   
        HttpSession session = request.getSession(false);
        if (session != null) {

            // **IMPORTANTE:** Usa los nombres EXACTOS de tus atributos
            session.removeAttribute("funcionSeleccionada");
            session.removeAttribute("asientosSeleccionados");
            session.removeAttribute("totalAsientos");
            session.removeAttribute("carritoDulceria");
            session.removeAttribute("totalDulces");
            session.removeAttribute("metodoPago");
            session.removeAttribute("nombreCompleto");
            session.removeAttribute("correoElectronico");
        }

        try {
            // Obtener la lista de películas desde la base de datos
            List<Pelicula> lista = peliculaDao.listar();
            if (lista == null) {
                lista = new ArrayList<>(); // Evitar null en el JSP
            }
            // Establecer la lista como atributo en el request
            request.setAttribute("peliculas", lista);
            // Hacer forward al JSP
            request.getRequestDispatcher("/Cliente/home.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.severe("Error al listar películas: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar las películas.");
        }
    }
}
