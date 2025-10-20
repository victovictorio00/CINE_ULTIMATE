package Controlador;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.Pelicula;
import modelo.PeliculaDao;
import modelo.Genero;
import modelo.GeneroDao;

@WebServlet("/PeliculaServlet")
@MultipartConfig
public class PeliculaServlet extends HttpServlet {

    private PeliculaDao peliculaDao;

    @Override
    public void init() {
        peliculaDao = new PeliculaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("rol") == null ||
            !"admin".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "listar";

        try {
            switch (action) {
                case "listar":
                    listarPeliculas(request, response);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "eliminar":
                    eliminarPelicula(request, response);
                    break;
                default:
                    listarPeliculas(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar acción: " + action, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("rol") == null ||
            !"admin".equals(session.getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insertar":
                    insertarPelicula(request, response);
                    break;
                case "actualizar":
                    actualizarPelicula(request, response);
                    break;
                default:
                    listarPeliculas(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar formulario de película.", e);
        }
    }

    // ==============================
    // MÉTODOS CRUD
    // ==============================

    private void listarPeliculas(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Pelicula> lista = peliculaDao.listar();
        request.setAttribute("listaPeliculas", lista);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Pelicula.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            GeneroDao generoDao = new GeneroDao();
            List<Genero> listaGeneros = generoDao.getTodosLosGeneros();
            request.setAttribute("listaGeneros", listaGeneros);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearPelicula.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int id = Integer.parseInt(request.getParameter("id"));
        Pelicula pelicula = peliculaDao.leer(id);

        if (pelicula != null) {
            GeneroDao generoDao = new GeneroDao();
            List<Genero> listaGeneros = generoDao.getTodosLosGeneros();
            request.setAttribute("listaGeneros", listaGeneros);

            request.setAttribute("pelicula", pelicula);
            RequestDispatcher dispatcher = request.getRequestDispatcher("EditarPelicula.jsp");
            dispatcher.forward(request, response);
        } else {
            response.getWriter().println("Película no encontrada.");
        }
    }

    private void insertarPelicula(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String nombre = request.getParameter("nombre");
        String sinopsis = request.getParameter("sinopsis");
        int idGenero = Integer.parseInt(request.getParameter("idGenero"));
        java.sql.Date fechaEstreno = java.sql.Date.valueOf(request.getParameter("fechaEstreno"));
        double precio = Double.parseDouble(request.getParameter("precio"));

        // Imagen
        Part filePart = request.getPart("foto");
        byte[] foto = null;
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream inputStream = filePart.getInputStream()) {
                foto = inputStream.readAllBytes();
            }
        }

        String trailerUrl = request.getParameter("trailerUrl");

        Pelicula pelicula = new Pelicula();
        pelicula.setNombre(nombre);
        pelicula.setSinopsis(sinopsis);
        pelicula.setIdGenero(new Genero(idGenero, null));
        pelicula.setFechaEstreno(fechaEstreno);
        pelicula.setPrecio(precio);
        pelicula.setFoto(foto);
        pelicula.setTrailerUrl(trailerUrl);

        peliculaDao.insertar(pelicula);
        response.sendRedirect("PeliculaServlet?action=listar");
    }

    private void actualizarPelicula(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String sinopsis = request.getParameter("sinopsis");
        int idGenero = Integer.parseInt(request.getParameter("idGenero"));
        java.sql.Date fechaEstreno = java.sql.Date.valueOf(request.getParameter("fechaEstreno"));
        double precio = Double.parseDouble(request.getParameter("precio"));

        // Imagen opcional
        Part filePart = request.getPart("foto");
        byte[] foto = null;
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream inputStream = filePart.getInputStream()) {
                foto = inputStream.readAllBytes();
            }
        }

        String trailerUrl = request.getParameter("trailerUrl");

        Pelicula pelicula = new Pelicula();
        pelicula.setIdPelicula(id);
        pelicula.setNombre(nombre);
        pelicula.setSinopsis(sinopsis);
        pelicula.setIdGenero(new Genero(idGenero, null));
        pelicula.setFechaEstreno(fechaEstreno);
        pelicula.setPrecio(precio);
        pelicula.setTrailerUrl(trailerUrl);

        if (foto != null) {
            pelicula.setFoto(foto); // 👈 solo si subiste una nueva
        }

        peliculaDao.editar(pelicula);
        response.sendRedirect("PeliculaServlet?action=listar");
    }

    private void eliminarPelicula(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        peliculaDao.eliminar(id);
        response.sendRedirect("PeliculaServlet?action=listar");
    }
}
