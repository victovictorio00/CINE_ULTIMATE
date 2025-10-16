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
    private final GeneroDao generoDao = new GeneroDao(); // Necesario para el menú de filtros

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- 1. Obtener Parámetros de Filtrado ---
        // 'genero' contendrá el ID numérico del género si se filtra por enlace.
        String generoIdString = request.getParameter("genero");

        // 'fechaSeleccionada' contendrá la fecha en formato YYYY-MM-DD si se filtra por formulario.
        String fechaSeleccionada = request.getParameter("fechaSeleccionada");

        List<Pelicula> peliculas;
        List<Genero> listaGeneros;

        try {
            // --- 2. Lógica de Filtrado de Películas ---
            if (generoIdString != null || fechaSeleccionada != null) {
                // Si hay al menos un filtro activo (género O fecha), filtramos.
                peliculas = peliculaDao.getPeliculasFiltradas(generoIdString, fechaSeleccionada);
            } else {
                // Si no hay filtros, cargamos todas las películas para la cartelera inicial.
                peliculas = peliculaDao.listar();
            }

            // --- 3. Cargar la lista de Géneros para la Interfaz de Filtros ---
            // Esta lista es necesaria para poblar la barra lateral de filtros en el JSP.
            listaGeneros = generoDao.getTodosLosGeneros();

            // --- 4. Establecer Atributos en el Request ---
            request.setAttribute("peliculas", peliculas);
            request.setAttribute("generos", listaGeneros);

            // También enviamos los filtros activos para que el JSP pueda resaltarlos
            request.setAttribute("filtroActivoGenero", generoIdString);
            request.setAttribute("filtroActivoFecha", fechaSeleccionada);

            // --- 5. Redireccionar al JSP del Cliente ---
            request.getRequestDispatcher("/Cliente/PeliculaCliente.jsp").forward(request, response);

        } catch (SQLException e) {
            // Manejo de errores de base de datos
            e.printStackTrace(); // Recomendado para depuración
            request.setAttribute("error", "Error del sistema al cargar la cartelera: " + e.getMessage());
            // Si hay un error, redirigir a una página de error o al mismo dashboard.
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
