/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controlador;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.Pelicula;
import modelo.PeliculaDao;
import java.util.List;
import javax.servlet.annotation.MultipartConfig;
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
        // Control de acceso
        HttpSession session = request.getSession(false);
       if (session == null || session.getAttribute("userRoleId") == null 
        || (int) session.getAttribute("userRoleId") != 2) { // 2 = Admin
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso no autorizado");
            return;
        }


        String action = request.getParameter("action");

        try {
            if ("listar".equals(action)) {
                listarPeliculas(request, response);
            } else if ("nuevo".equals(action)) {
                mostrarFormularioNuevo(request, response);
            } else if ("editar".equals(action)) {
                mostrarFormularioEditar(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarPelicula(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Control de acceso
        HttpSession session = request.getSession(false);
        //-
        if (session == null || session.getAttribute("userRoleId") == null 
             || (int) session.getAttribute("userRoleId") != 2) {
         response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso no autorizado");
         return;
     }



        String action = request.getParameter("action");

        try {
            if ("insertar".equals(action)) {
                insertarPelicula(request, response);
            } else if ("actualizar".equals(action)) {
                actualizarPelicula(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Método para listar todas las películas
    private void listarPeliculas(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Pelicula> lista = peliculaDao.listar();  // Obtener las películas
        request.setAttribute("listaPeliculas", lista);  // Pasa la lista al JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("Pelicula.jsp");
        dispatcher.forward(request, response);  // Redirige a la página

    }

    // Método para mostrar el formulario de nueva película
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


    // Método para mostrar el formulario de edición de película
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
        response.getWriter().println("Película no encontrada");
    }
}


    // Método para insertar una nueva película
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

    Pelicula pelicula = new Pelicula();
    pelicula.setNombre(nombre);
    pelicula.setSinopsis(sinopsis);
    pelicula.setIdGenero(new Genero(idGenero, null));
    pelicula.setFechaEstreno(fechaEstreno);
    pelicula.setPrecio(precio);
    pelicula.setFoto(foto);

    peliculaDao.insertar(pelicula);
    response.sendRedirect("PeliculaServlet?action=listar");
}


    // Método para actualizar una película
    private void actualizarPelicula(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException {
    int id = Integer.parseInt(request.getParameter("id"));
    String nombre = request.getParameter("nombre");
    String sinopsis = request.getParameter("sinopsis");
    int idGenero = Integer.parseInt(request.getParameter("idGenero"));
    java.sql.Date fechaEstreno = java.sql.Date.valueOf(request.getParameter("fechaEstreno"));
    double precio = Double.parseDouble(request.getParameter("precio"));

    // Foto (opcional)
    Part filePart = request.getPart("foto");
    byte[] foto = null;
    if (filePart != null && filePart.getSize() > 0) {
        try (InputStream inputStream = filePart.getInputStream()) {
            foto = inputStream.readAllBytes();
        }
    }

    Pelicula pelicula = new Pelicula();
    pelicula.setIdPelicula(id);
    pelicula.setNombre(nombre);
    pelicula.setSinopsis(sinopsis);
    pelicula.setIdGenero(new Genero(idGenero, null));
    pelicula.setFechaEstreno(fechaEstreno);
    pelicula.setPrecio(precio);
    pelicula.setFoto(foto); // si es null, el DAO mantiene la foto existente

    peliculaDao.editar(pelicula);
    response.sendRedirect("PeliculaServlet?action=listar");
}


    // Método para eliminar una película
    private void eliminarPelicula(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        peliculaDao.eliminar(id);  // Elimina la película de la base de datos
        response.sendRedirect("PeliculaServlet?action=listar");  // Redirige al listado de películas
    }
}
