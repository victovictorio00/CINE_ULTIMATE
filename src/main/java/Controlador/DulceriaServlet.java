package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Producto;
import modelo.ProductoDao;

@WebServlet("/DulceriaServlet")
public class DulceriaServlet extends HttpServlet {

    private final ProductoDao productoDao = new ProductoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ NO leer de parámetros - ya están en sesión
        HttpSession session = request.getSession(false);

        // Validar sesión
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        try {
            // Cargar productos
            List<Producto> todosLosProductos = productoDao.listar();

            // Categorizar
            Map<String, List<Producto>> productosPorCategoria = new HashMap<>();
            productosPorCategoria.put("COMBOS", new ArrayList<>());
            productosPorCategoria.put("BEBIDAS", new ArrayList<>());
            productosPorCategoria.put("DULCES", new ArrayList<>());
            productosPorCategoria.put("CANCHITA", new ArrayList<>());
            productosPorCategoria.put("SNACKS", new ArrayList<>());
            productosPorCategoria.put("OTROS", new ArrayList<>());

            for (Producto p : todosLosProductos) {
                String categoria = clasificarProducto(p);
                if (productosPorCategoria.containsKey(categoria)) {
                    productosPorCategoria.get(categoria).add(p);
                } else {
                    productosPorCategoria.get("OTROS").add(p);
                }
            }

            request.setAttribute("productosCategorizados", productosPorCategoria);
            request.getRequestDispatcher("/Cliente/DulceriaCliente.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar productos: " + e.getMessage());
            request.getRequestDispatcher("/Cliente/error.jsp").forward(request, response);
        }
    }

    private String clasificarProducto(Producto p) {
        String nombre = p.getNombre().toUpperCase();

        if (nombre.contains("COMBO") || nombre.contains("DUO") || nombre.contains("TRIO")) {
            return "COMBOS";
        }
        if (nombre.contains("CANCHITA") || nombre.contains("POPCORN")) {
            return "CANCHITA";
        }
        if (nombre.contains("COCA") || nombre.contains("INCA") || nombre.contains("FANTA") || nombre.contains("AGUA")) {
            return "BEBIDAS";
        }
        if (nombre.contains("M&M") || nombre.contains("CHOCOLATE") || nombre.contains("GOMITAS")) {
            return "DULCES";
        }
        if (nombre.contains("PAPAS") || nombre.contains("NACHOS") || nombre.contains("CHIPS")) {
            return "SNACKS";
        }
        return "OTROS";
    }
}
