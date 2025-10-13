
package Controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Pelicula;
import modelo.PeliculaDao;

@WebServlet("/CarteleraServlet") // Tu ruta de acceso
public class CarteleraServlet extends HttpServlet {

    private PeliculaDao peliculaDao = new PeliculaDao();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener parámetros de filtro
        String genero = request.getParameter("genero");
        String fechaEstreno = request.getParameter("fecha");

        List<Pelicula> peliculas;

        try {
            if (genero != null || fechaEstreno != null) {
                // Llama al nuevo método para filtrar
                peliculas = peliculaDao.getPeliculasFiltradas(genero, fechaEstreno);
            } else {
                // Llama al método de carga inicial (tu antiguo listar())
                peliculas = peliculaDao.listar(); 
            }
            
            request.setAttribute("peliculas", peliculas);
            request.getRequestDispatcher("/Cliente/PeliculaCliente.jsp").forward(request, response);
            
        } catch (SQLException e) {
            // Manejo básico de errores de DB
            request.setAttribute("error", "Error al cargar la cartelera: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
