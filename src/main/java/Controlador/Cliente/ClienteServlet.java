package Controlador.Cliente;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import modelo.Pelicula;
import modelo.PeliculaDao;
import modelo.Asiento; 
import modelo.AsientoDao;
import modelo.Funcion;  
import modelo.FuncionDao;  
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import modelo.DetalleVenta;
import modelo.DetalleVentaDao;
import modelo.Producto;
import modelo.ProductoDao;
import modelo.Sala;
import modelo.SalaDao;
import modelo.Usuario;
import modelo.UsuarioDao;
import modelo.Venta;
import modelo.VentaDao;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    private PeliculaDao peliculaDao;
    private AsientoDao asientoDao;
    private FuncionDao funcionDao;
    private SalaDao salaDao;

    @Override
    public void init() {
        peliculaDao = new PeliculaDao();
        asientoDao = new AsientoDao();
        funcionDao = new FuncionDao();
        salaDao = new SalaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

                HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        Object u = session.getAttribute("username");
        if (!(u instanceof String) || ((String) u).trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro 'action'");
            return;
        }

        try {
            switch (action) {
                case "listar": listarPeliculas(request, response); break;
                case "reservar": mostrarSeleccionAsiento(request, response); break;
                case "confirmarPago": mostrarVoucher(request, response); break;
                case "metodoPago": mostrarMetodoPago(request, response); break;
                case "confirmarReserva": confirmarReserva(request, response); break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar la solicitud", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "seleccionarCombo":
                    seleccionarCombo(request, response);
                    break;
                case "confirmarAsiento":
                    procesarSeleccionAsiento(request, response);
                    break;
                case "procesarPago":
                    procesarPago(request, response);
                    break;
                case "guardarVenta":
                    guardarVenta(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (SQLException e) {
            throw new ServletException("Error al procesar la acción", e);
        }
    }

    private void procesarSeleccionAsiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idFuncion = request.getParameter("idFuncion");
        String asientoSeleccionado = request.getParameter("asientoSeleccionado");

        HttpSession session = request.getSession();
        session.setAttribute("idFuncion", idFuncion);
        session.setAttribute("asientoSeleccionado", asientoSeleccionado);

        request.setAttribute("idFuncion", idFuncion);
        request.setAttribute("asientoSeleccionado", asientoSeleccionado);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/MetodoPago.jsp");
        dispatcher.forward(request, response);
    }

    private void listarPeliculas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Pelicula> peliculas = peliculaDao.listar();
        request.setAttribute("peliculas", peliculas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/DashboardCliente.jsp");
        dispatcher.forward(request, response);
    }
    private void mostrarSeleccionAsiento(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String idFuncionParam = request.getParameter("idFuncion");
        if (idFuncionParam == null || idFuncionParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Cliente/SeleccionFuncion.jsp");
            return;
        }
        int idFuncion = Integer.parseInt(idFuncionParam);
        Funcion funcion = funcionDao.leer(idFuncion);
        if (funcion == null) {
            request.setAttribute("error", "Función no encontrada");
            request.getRequestDispatcher("Cliente/Error.jsp").forward(request, response);
            return;
        }
        long duracionMin = (funcion.getFechaFin().getTime()
                - funcion.getFechaInicio().getTime()) / 60000;
        /*  Asientos */
        List<Asiento> asientos = asientoDao.obtenerAsientosPorSalaYFuncion(
                funcion.getSala().getIdSala(), idFuncion);
        /* Guardamos la funcion seleccionada en la sesion*/
        HttpSession session = request.getSession();
        session.setAttribute("funcionSeleccionada", funcion);
        /* Enviar al JSP */
        request.setAttribute("asientos", asientos);
        request.setAttribute("funcion", funcion);
        request.setAttribute("sala", funcion.getSala());  
        request.setAttribute("pelicula", funcion.getPelicula()); 
        request.setAttribute("precioButaca", funcion.getPelicula().getPrecio());
        request.setAttribute("genero", funcion.getPelicula().getIdGenero().getNombre());
        request.setAttribute("duracionMin", duracionMin);
        request.setAttribute("idFuncion", idFuncion);

        request.getRequestDispatcher("Cliente/SeleccionAsiento.jsp").forward(request, response);
    }

    private void seleccionarCombo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String selectedSeats = request.getParameter("selectedSeats");
        String idFuncion = request.getParameter("idFuncion");
        // Guardar en sesión
        HttpSession session = request.getSession();
        session.setAttribute("selectedSeats", selectedSeats);
        session.setAttribute("idFuncion", idFuncion);
        // Validar que los asientos estén disponibles antes de continuar
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/SeleccionarCombo.jsp");
        dispatcher.forward(request, response);
    }
    private void procesarPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener datos del formulario método de pago
        String nombreCompleto = request.getParameter("nombreCompleto");
        String correoElectronico = request.getParameter("correoElectronico");
        String metodoPago = request.getParameter("metodoPago");
        // Guardar temporalmente en sesión
        HttpSession session = request.getSession();
        session.setAttribute("nombreCompleto", nombreCompleto);
        session.setAttribute("correoElectronico", correoElectronico);
        session.setAttribute("metodoPago", metodoPago);
        // Ir a la página de confirmación
        response.sendRedirect(request.getContextPath() + "/ClienteServlet?action=confirmarReserva");
    }
    
        private void confirmarReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }
        // Recuperar los datos almacenados en pasos anteriores
        Object funcion = session.getAttribute("funcionSeleccionada");
        Object asientosSeleccionados = session.getAttribute("butacasSeleccionadas");
        Object precioAsientos = session.getAttribute("totalAsientos");
        Object carrito = session.getAttribute("carritoDulceria");
        Object nombreCompleto = session.getAttribute("nombreCompleto");
        Object correoElectronico = session.getAttribute("correoElectronico");
        Object metodoPago = session.getAttribute("metodoPago");
        // (Opcional) puedes agregar validación por si algo falta
        if (funcion == null || asientosSeleccionados == null || precioAsientos == null || carrito == null) {
            request.setAttribute("error", "Faltan datos para confirmar la reserva.");
            request.getRequestDispatcher("Cliente/Error.jsp").forward(request, response);
            System.err.println("LLEGOOOO");
            return;
        }
        // Enviar los datos a la JSP de confirmación
        request.getRequestDispatcher("Cliente/Confirmacion.jsp").forward(request, response);
    }
    private void mostrarVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/Voucher.jsp");
        dispatcher.forward(request, response);
    }
    private void generarVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String asientoSeleccionado = request.getParameter("asiento");
        String comboSeleccionado = request.getParameter("combo");
        String metodoPago = request.getParameter("metodoPago");

        double precioAsiento = 10.00;
        double precioCombo = 15.00;
        double total = precioAsiento + precioCombo;

        request.setAttribute("asiento", asientoSeleccionado);
        request.setAttribute("combo", comboSeleccionado);
        request.setAttribute("metodoPago", metodoPago);
        request.setAttribute("total", total);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/Voucher.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarMetodoPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente/MetodoPago.jsp");
        dispatcher.forward(request, response);
    }

    private void guardarVenta(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        try {
            // === Datos del cliente ===
            int userId = (int) sesion.getAttribute("userId");
            String metodoPago = (String) sesion.getAttribute("metodoPago");
            // === Datos de la función y selección ===
            Funcion funcion = (Funcion) sesion.getAttribute("funcionSeleccionada");
            String selectedSeatsParam =  (String)sesion.getAttribute("butacasSeleccionadas");// VER TIPO Y TRATAR
            List<String> asientosSeleccionados = Arrays.asList(selectedSeatsParam.split(","));
            
            
            double totalAsientos = Double.parseDouble(sesion.getAttribute("totalAsientos").toString());
            double precioDulces = Double.parseDouble(sesion.getAttribute("precioDulces").toString());
            Map<Integer, Integer> carritoDulceria = (Map<Integer, Integer>) sesion.getAttribute("carritoDulceria");
            // === Calcular total general ===
            double totalVenta = totalAsientos + precioDulces;

            // === Crear la venta ===
            UsuarioDao usuarioDao = new UsuarioDao();
            Usuario usuario = usuarioDao.leer(userId);

            Venta venta = new Venta();
            venta.setIdUsuarioCliente(usuario);
            
            // 🔹 Generar la fecha actual con el formato correcto
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH:mm");
            String fechaActual = sdf.format(new Date());
            System.out.println("DEBUG Fecha generada: " + fechaActual); // para ver si coincide
            // 🔹 Pasarla como String (como tu método lo espera)
            venta.setFecha(new Date());
            venta.setTotal(totalVenta);
            venta.setMetodoPago(metodoPago);

            // === Guardar venta ===
            VentaDao ventaDao = new VentaDao();
            
            // ANTES DE HACER RSTO VEIRIGFIAR EN LA ELECCION DE ASINETO QUE ESTE LIBRE  EE
            
            
            int idVentaGenerada = ventaDao.insertarYDevolverId(venta);
            sesion.setAttribute("idVenta", idVentaGenerada);

            // === Guardar detalles de los asientos ===
            PeliculaDao peliDao = new PeliculaDao();
            
            if (asientosSeleccionados != null) {
                DetalleVentaDao detalleDao = new DetalleVentaDao();
                for (String a : asientosSeleccionados) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setVenta(venta);
                    detalle.setFuncion(funcion);
                    detalle.setAsiento(asientoDao.leerPorCodigo(a));
                    detalle.setCantidad(1);
                    detalle.setTipoItem(1); // 1 = asiento
                    detalle.setPrecioUnitario(funcion.getPelicula().getPrecio());
                    detalleDao.insertar(detalle);
                    if (detalle.getAsiento() != null) {
                        asientoDao.actualizarEstadoOcupado(detalle.getAsiento().getId_asiento());
                    }
                }
            }
            // === Guardar detalles de dulcería ===
            if (carritoDulceria != null && !carritoDulceria.isEmpty()) {
                ProductoDao productoDao = new ProductoDao();
                DetalleVentaDao detalleDao = new DetalleVentaDao();

                for (Map.Entry<Integer, Integer> entry : carritoDulceria.entrySet()) {
                    int idProducto = entry.getKey();
                    int cantidad = entry.getValue();

                    Producto producto = productoDao.leer(idProducto);

                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setVenta(venta);
                    detalle.setProducto(producto);
                    detalle.setCantidad(cantidad);
                    detalle.setTipoItem(2); // 2 = dulcería
                    detalle.setPrecioUnitario(producto.getPrecio());
                    detalleDao.insertar(detalle);
                }
            }
            // === Limpieza y redirección ===
            sesion.removeAttribute("funcionSeleccionada");
            sesion.removeAttribute("butacasSeleccionadas");
            sesion.removeAttribute("totalAsientos");
            sesion.removeAttribute("carritoDulceria");
            sesion.removeAttribute("precioDulces");
            sesion.removeAttribute("metodoPago");
            response.sendRedirect(request.getContextPath() + "/Cliente/Voucher.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al guardar la venta: " + e.getMessage());
        }
    }
}
