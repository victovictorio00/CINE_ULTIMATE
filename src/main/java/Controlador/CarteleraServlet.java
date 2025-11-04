package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Genero;
import modelo.GeneroDao;
import modelo.Pelicula;
import modelo.PeliculaDao;

@WebServlet("/CarteleraServlet")
public class CarteleraServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Instancias de los DAOs
    private final PeliculaDao peliculaDao = new PeliculaDao();
    private final GeneroDao generoDao = new GeneroDao(); // Para filtros de géneros

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Evitar caché para que siempre cargue imágenes nuevas
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // --- 1. Obtener Parámetros de Filtrado ---
        String generoIdString = request.getParameter("genero"); // filtro por género
        String fechaSeleccionada = request.getParameter("fechaSeleccionada"); // filtro por fecha

        List<Pelicula> peliculas;
        List<Genero> listaGeneros;

        try {
            // --- 2. Lógica de Filtrado de Películas ---
            if (generoIdString != null || fechaSeleccionada != null) {
                peliculas = peliculaDao.getPeliculasFiltradas(generoIdString, fechaSeleccionada);
            } else {
                peliculas = peliculaDao.listar();
            }

            // --- 3. Cargar la lista de Géneros para filtros en la vista ---
            listaGeneros = generoDao.getTodosLosGeneros();

            // --- 4. Setear atributos al request ---
            request.setAttribute("peliculas", peliculas);
            request.setAttribute("generos", listaGeneros);
            request.setAttribute("filtroActivoGenero", generoIdString);
            request.setAttribute("filtroActivoFecha", fechaSeleccionada);

            // --- 5. Redireccionar al JSP del Cliente ---
            request.getRequestDispatcher("/Cliente/PeliculaCliente.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace(); // Log para depuración
            request.setAttribute("error", "Error al cargar la cartelera: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
