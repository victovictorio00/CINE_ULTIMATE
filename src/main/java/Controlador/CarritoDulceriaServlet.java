package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Producto;
import modelo.ProductoDao;

/**
 * Procesa el carrito de la dulcería sin usar Gson.
 * Convierte el JSON simple {"5":2,"9":1,"12":3} en un Map<Integer,Integer>
 * y lo guarda en sesión.
 */
@WebServlet(name = "CarritoDulceriaServlet", urlPatterns = {"/CarritoDulceriaServlet"})
public class CarritoDulceriaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();

        if ("actualizarDulceria".equals(accion)) {
            String carritoJson = request.getParameter("carritoData");
            Map<Integer, Integer> carrito = new HashMap<>();
            if (carritoJson != null && !carritoJson.trim().isEmpty()) {
                try {
                    // Limpieza del string {"5":2,"9":1} → 5:2,9:1
                    String limpio = carritoJson.trim()
                            .replaceAll("[{}\"]", ""); // elimina { } y "

                    // Si hay datos, los separamos por coma
                    if (!limpio.isEmpty()) {
                        String[] pares = limpio.split(",");

                        for (String par : pares) {
                            String[] kv = par.split(":");
                            if (kv.length == 2) {
                                try {
                                    int id = Integer.parseInt(kv[0].trim());
                                    int cantidad = Integer.parseInt(kv[1].trim());
                                    carrito.put(id, cantidad);
                                } catch (NumberFormatException ex) {
                                    System.err.println("Error al parsear par: " + par);
                                }
                            }
                        }
                    }

                    // Guardamos en sesión
                    session.setAttribute("carritoDulceria", carrito);
                    System.out.println(" Carrito : " + carrito);
                    double totalDulces = calcularTotalDulces(carrito);
                    session.setAttribute("precioDulces", totalDulces);
                    

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al procesar el carrito");
                    return;
                }
            } else {
                session.removeAttribute("carritoDulceria");
                System.out.println("Carrito vacío o nulo.");
            }

            // Redirigir a método de pago
            response.sendRedirect(request.getContextPath() + "/ClienteServlet?action=metodoPago");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }

    private double calcularTotalDulces(Map<Integer, Integer> carrito) {
        double total = 0.0;
        ProductoDao dao = new ProductoDao();

        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            int idProducto = entry.getKey();
            int cantidad = entry.getValue();

            try {
                Producto producto = dao.leer(idProducto);
                if (producto != null) {
                    total += producto.getPrecio() * cantidad;
                }
            } catch (SQLException e) {
                System.err.println("⚠ Error al obtener producto ID: " + idProducto);
                e.printStackTrace();
            }
        }

        return total;
    }
    
    
    @Override
    public String getServletInfo() {
        return "Gestiona el carrito de dulcería sin usar Gson";
    }
}