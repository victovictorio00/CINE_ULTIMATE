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

@WebServlet(name = "CarritoDulceriaServlet", urlPatterns = {"/CarritoDulceriaServlet"})
public class CarritoDulceriaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        if (!"actualizarDulceria".equals(accion)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            return;
        }
        
        String carritoJson = request.getParameter("carritoData");

        if (carritoJson == null || carritoJson.trim().isEmpty()) {
            session.removeAttribute("carritoDulceria");
            redirigirMetodoPago(request, response);
            return;
        }

        try {
            Map<Integer, Integer> carrito = parsearCarrito(carritoJson);
            session.setAttribute("carritoDulceria", carrito);

            double totalDulces = calcularTotalDulces(carrito);
            session.setAttribute("precioDulces", totalDulces);

            redirigirMetodoPago(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al procesar el carrito");
        }
    }

    private Map<Integer, Integer> parsearCarrito(String carritoJson) {
        Map<Integer, Integer> carrito = new HashMap<>();
        String limpio = carritoJson.trim().replaceAll("[{}\"]", "");
        if (limpio.isEmpty()) {
            return carrito;
        }

        String[] pares = limpio.split(",");
        for (String par : pares) {
            String[] kv = par.split(":");
            if (kv.length != 2) {
                continue;
            }
            try {
                int id = Integer.parseInt(kv[0].trim());
                int cantidad = Integer.parseInt(kv[1].trim());
                carrito.put(id, cantidad);
            } catch (NumberFormatException ex) {
                System.err.println("Error al parsear par: " + par);
            }
        }
        return carrito;
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
                e.printStackTrace();
            }
        }
        return total;
    }

    private void redirigirMetodoPago(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/ClienteServlet?action=metodoPago");
    }

    @Override
    public String getServletInfo() {
        return "Gestiona el carrito de dulcería sin usar Gson";
    }
}
