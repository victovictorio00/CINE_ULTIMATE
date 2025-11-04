package Controlador;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import modelo.Producto;
import modelo.ProductoDao;

@WebServlet("/ProductoServlet")
@MultipartConfig( // límites opcionales
    maxFileSize = 5 * 1024 * 1024,       // 5 MB por archivo
    maxRequestSize = 10 * 1024 * 1024    // 10 MB por request
)
public class ProductoServlet extends HttpServlet {

    private ProductoDao productoDao;

    @Override
    public void init() {
        productoDao = new ProductoDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        

        String action = request.getParameter("action");
        if (action == null) action = "listar";

        try {
            switch (action) {
                case "listar":
                    listarProductos(request, response);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "eliminar":
                    eliminarProducto(request, response);
                    break;
                default:
                    listarProductos(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "insertar":
                    insertarProducto(request, response);
                    break;
                case "actualizar":
                    actualizarProducto(request, response);
                    break;
                default:
                    response.sendRedirect("ProductoServlet?action=listar");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /* ======================= GET actions ======================= */

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Producto> lista = productoDao.listar();
        request.setAttribute("listaProductos", lista);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Producto.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearProducto.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int id = parseIntSafe(request.getParameter("id"), 0);
        Producto producto = productoDao.leer(id);

        if (producto != null) {
            request.setAttribute("producto", producto);
            RequestDispatcher dispatcher = request.getRequestDispatcher("EditarProducto.jsp");
            dispatcher.forward(request, response);
        } else {
            response.getWriter().println("Producto no encontrado");
        }
    }

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseIntSafe(request.getParameter("id"), 0);
        if (id > 0) {
            productoDao.eliminar(id);
        }
        response.sendRedirect("ProductoServlet?action=listar");
    }

    /* ======================= POST actions ======================= */

    private void insertarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        double precio = parseDoubleSafe(request.getParameter("precio"), 0.0);
        int stock = parseIntSafe(request.getParameter("stock"), 0);

        // Imagen
        byte[] foto = leerBytesDeParte(request.getPart("foto"));

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setFoto(foto);
        p.setStock(stock);
        p.setPrecio(precio);

        productoDao.insertar(p);
        response.sendRedirect("ProductoServlet?action=listar");
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        int id = parseIntSafe(request.getParameter("id"), 0);
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        double precio = parseDoubleSafe(request.getParameter("precio"), 0.0);
        int stock = parseIntSafe(request.getParameter("stock"), 0);

        // Imagen: si no suben nueva, conservar la actual
        byte[] fotoNueva = leerBytesDeParte(request.getPart("foto"));
        byte[] fotoFinal = fotoNueva;

        if (fotoNueva == null || fotoNueva.length == 0) {
            Producto existente = productoDao.leer(id);
            if (existente != null) {
                fotoFinal = existente.getFoto();
            }
        }

        Producto p = new Producto();
        p.setIdProducto(id);
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setFoto(fotoFinal);
        p.setStock(stock);
        p.setPrecio(precio);

        productoDao.editar(p); // o productoDao.actualizar(p) si dejaste ese alias
        response.sendRedirect("ProductoServlet?action=listar");
    }

    /* ======================= Helpers ======================= */

    private static int parseIntSafe(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    private static double parseDoubleSafe(String s, double def) {
        try { return Double.parseDouble(s); } catch (Exception e) { return def; }
    }

    private static byte[] leerBytesDeParte(Part part) throws IOException {
        if (part == null || part.getSize() <= 0) return null;
        try (InputStream is = part.getInputStream()) {
            return is.readAllBytes();
        }
    }
}
