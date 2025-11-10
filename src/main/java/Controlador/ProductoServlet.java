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
@MultipartConfig( // configuración para manejar formularios con archivos (imágenes)
    maxFileSize = 5 * 1024 * 1024,       // Tamaño máximo por archivo: 5 MB
    maxRequestSize = 10 * 1024 * 1024    // Tamaño máximo total por request: 10 MB
)
public class ProductoServlet extends HttpServlet {

    // Instancia del DAO para acceder a la base de datos
    private ProductoDao productoDao;

    // ==========================================================
    // MÉTODO init(): se ejecuta una sola vez al iniciar el servlet
    // ==========================================================
    @Override
    public void init() {
        productoDao = new ProductoDao(); // Inicializa el DAO
    }

    // ==========================================================
    // MÉTODO doGet(): maneja peticiones tipo GET (listar, editar, eliminar, etc.)
    // ==========================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la acción enviada desde la URL (por ejemplo ?action=listar)
        String action = request.getParameter("action");
        if (action == null) action = "listar"; // acción por defecto

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

    // ==========================================================
    // MÉTODO doPost(): maneja peticiones tipo POST (insertar, actualizar, etc.)
    // ==========================================================
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

    // ==========================================================
    // ACCIONES GET
    // ==========================================================

    // Muestra la lista completa de productos
    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Producto> lista = productoDao.listar(); // obtiene todos los productos
        request.setAttribute("listaProductos", lista); // los envía a la vista
        RequestDispatcher dispatcher = request.getRequestDispatcher("Producto.jsp"); // JSP destino
        dispatcher.forward(request, response);
    }

    // Muestra el formulario para registrar un nuevo producto
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearProducto.jsp");
        dispatcher.forward(request, response);
    }

    // Muestra el formulario con los datos de un producto existente
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

    // Elimina un producto según su ID
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseIntSafe(request.getParameter("id"), 0);
        if (id > 0) {
            productoDao.eliminar(id);
        }
        response.sendRedirect("ProductoServlet?action=listar");
    }

    // ==========================================================
    // ACCIONES POST
    // ==========================================================

    // Inserta un nuevo producto en la base de datos
    private void insertarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        double precio = parseDoubleSafe(request.getParameter("precio"), 0.0);
        int stock = parseIntSafe(request.getParameter("stock"), 0);

        // Lee la imagen subida desde el formulario
        byte[] foto = leerBytesDeParte(request.getPart("foto"));

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setFoto(foto);
        p.setStock(stock);
        p.setPrecio(precio);

        productoDao.insertar(p); // Guarda el producto
        response.sendRedirect("ProductoServlet?action=listar");
    }

    // Actualiza un producto existente
    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        int id = parseIntSafe(request.getParameter("id"), 0);
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        double precio = parseDoubleSafe(request.getParameter("precio"), 0.0);
        int stock = parseIntSafe(request.getParameter("stock"), 0);

        // Si no se sube una nueva imagen, se conserva la existente
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

        productoDao.editar(p); // Actualiza la base de datos
        response.sendRedirect("ProductoServlet?action=listar");
    }

    // ==========================================================
    // MÉTODOS AUXILIARES (Helpers)
    // ==========================================================

    // Convierte un String a entero de forma segura
    private static int parseIntSafe(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    // Convierte un String a double de forma segura
    private static double parseDoubleSafe(String s, double def) {
        try { return Double.parseDouble(s); } catch (Exception e) { return def; }
    }

    // Lee los bytes de un archivo enviado desde un formulario (Part)
    private static byte[] leerBytesDeParte(Part part) throws IOException {
        if (part == null || part.getSize() <= 0) return null;
        try (InputStream is = part.getInputStream()) {
            return is.readAllBytes();
        }
    }
}
