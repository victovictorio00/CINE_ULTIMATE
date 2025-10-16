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
import modelo.Producto;
import modelo.ProductoDao;

@WebServlet("/DulceriaServlet")
public class DulceriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ProductoDao productoDao = new ProductoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Producto> todosLosProductos;
        Map<String, List<Producto>> productosPorCategoria = new HashMap<>();

        // Inicializar categorías para asegurar que el mapa siempre tenga las claves esperadas por el JSP
        productosPorCategoria.put("COMBOS", new ArrayList<>());
        productosPorCategoria.put("BEBIDAS", new ArrayList<>());
        productosPorCategoria.put("DULCES", new ArrayList<>());
        productosPorCategoria.put("CANCHITA", new ArrayList<>());
        productosPorCategoria.put("SNACKS", new ArrayList<>());
        productosPorCategoria.put("OTROS", new ArrayList<>());
        try {
            // 1. Obtener todos los productos del DAO (solo se usa el método listar())
            todosLosProductos = productoDao.listar();

            // 2. Clasificar y Agrupar los productos
            for (Producto p : todosLosProductos) {
                String categoria = clasificarProducto(p);
                
                // Asegurar que la categoría obtenida sea válida y esté en el mapa
                if (productosPorCategoria.containsKey(categoria)) {
                    productosPorCategoria.get(categoria).add(p);
                } else {
                    // Si no cae en ninguna categoría conocida, agrégalo a la lista de DULCES por defecto
                    productosPorCategoria.get("OTROS").add(p);
                }
            }

            // 3. Establecer el atributo en el request para el JSP
            request.setAttribute("productosCategorizados", productosPorCategoria);

            // 4. Redireccionar al JSP de la Dulcería
            request.getRequestDispatcher("/Cliente/DulceriaCliente.jsp").forward(request, response);

        } catch (SQLException e) {
            // Manejo de errores de base de datos
            e.printStackTrace();
            request.setAttribute("error", "Error del sistema al cargar los productos: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * Lógica para clasificar un producto en una categoría
     * basándose en su nombre y/o descripción.
     */
    private String clasificarProducto(Producto p) {
        String nombre = p.getNombre().toUpperCase();
        String descripcion = p.getDescripcion().toUpperCase();

        // --- Lógica de Clasificación ---
        
        // 1. COMBOS (Máxima prioridad por nombre)
        if (nombre.contains("COMBO") || nombre.contains("DUO") || nombre.contains("TRIO") || nombre.contains("PACK")) {
            return "COMBOS";
        }
        
        // 2. CANCHITA / POPCORN
        if (nombre.contains("CANCHITA") || nombre.contains("CANCHA") || nombre.contains("POPCORN") || descripcion.contains("CANCHITA") || descripcion.contains("POPCORN") || descripcion.contains("CANCHA")) {
            return "CANCHITA";
        }

        // 3. BEBIDAS / GASEOSAS
        if (nombre.contains("COCA") || nombre.contains("INCA") || nombre.contains("FANTA") || nombre.contains("AGUA") || nombre.contains("GASEOSA") || descripcion.contains("BEBIDA")) {
            return "BEBIDAS";
        }

        // 4. DULCES (Generalmente chocolate, golosinas específicas)
        if (nombre.contains("M&M") || nombre.contains("CHOCOLATE") || nombre.contains("GOMITAS") || nombre.contains("CARAMELO") || nombre.contains("GOLOSINA") || descripcion.contains("TORTA")) {
            return "DULCES";
        }
        
        // 5. SNACKS (Papitas, nachos, productos salados no considerados canchita)
        if (nombre.contains("PAPAS") || nombre.contains("NACHOS") || nombre.contains("CHIPS") || descripcion.contains("SALADO") || descripcion.contains("SNACKS") || descripcion.contains("SNACK")) {
            return "SNACKS";
        }

        // 6. Valor por defecto si no coincide con nada
        // Puedes cambiar esto a la categoría que más te convenga (e.g., DULCES)
        return "OTROS"; 
    }
}