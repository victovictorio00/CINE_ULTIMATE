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
import modelo.Producto;
import modelo.ProductoDao;
import java.util.List;
import javax.servlet.annotation.MultipartConfig;

@WebServlet("/ProductoServlet")
@MultipartConfig
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

        try {
            if ("listar".equals(action)) {
                listarProductos(request, response);
            } else if ("nuevo".equals(action)) {
                mostrarFormularioNuevo(request, response);
            } else if ("editar".equals(action)) {
                mostrarFormularioEditar(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarProducto(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("insertar".equals(action)) {
                insertarProducto(request, response);
            } else if ("actualizar".equals(action)) {
                actualizarProducto(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Producto> lista = productoDao.listar();  // Obtiene los productos de la base de datos
        request.setAttribute("listaProductos", lista);  // Pasa los productos a la vista JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("Producto.jsp");  // Redirige a la página de lista
        dispatcher.forward(request, response);  // Muestra los productos en el JSP
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("CrearProducto.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Producto producto = productoDao.leer(id);
        if (producto != null) {
            request.setAttribute("producto", producto);
            RequestDispatcher dispatcher = request.getRequestDispatcher("EditarProducto.jsp");
            dispatcher.forward(request, response);
        } else {
            response.getWriter().println("Producto no encontrado");
        }
    }

    private void insertarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String nombre = request.getParameter("nombre");
        String precio = request.getParameter("precio");
        String descripcion = request.getParameter("descripcion");

        // Imagen
        Part filePart = request.getPart("foto");  // nombre del campo en el formulario
        byte[] foto = null;
        if (filePart != null && filePart.getSize() > 0) {
            InputStream inputStream = filePart.getInputStream();
            foto = inputStream.readAllBytes();
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setFoto(foto);
        //producto.setStock(stock);

        productoDao.insertar(producto);
        response.sendRedirect("ProductoServlet?action=listar");
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String precio = request.getParameter("precio");
        String descripcion = request.getParameter("descripcion");

        // Imagen
        Part filePart = request.getPart("foto");
        byte[] foto = null;
        if (filePart != null && filePart.getSize() > 0) {
            InputStream inputStream = filePart.getInputStream();
            foto = inputStream.readAllBytes();
        }

        //Producto producto = new Producto(id, nombre, descripcion, foto, stock);
        //productoDao.actualizar(producto);
        //response.sendRedirect("ProductoServlet?action=listar");
    }

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productoDao.eliminar(id);
        response.sendRedirect("ProductoServlet?action=listar");
    }
}
