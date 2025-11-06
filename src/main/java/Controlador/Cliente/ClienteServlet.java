package Controlador.Cliente;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.*;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    private PeliculaDao peliculaDao;
    private AsientoDao asientoDao;
    private FuncionDao funcionDao;
    private ProductoDao productoDao;
    private UsuarioDao usuarioDao;
    private VentaDao ventaDao;
    private DetalleVentaDao detalleVentaDao;

    // ============== CONSTANTES PARA ATRIBUTOS DE SESIÓN ==============
    private static final String ATTR_FUNCION = "funcionSeleccionada";
    private static final String ATTR_ASIENTOS = "asientosSeleccionados";
    private static final String ATTR_TOTAL_ASIENTOS = "totalAsientos";
    private static final String ATTR_CARRITO = "carritoDulceria";
    private static final String ATTR_TOTAL_DULCES = "totalDulces";
    private static final String ATTR_METODO_PAGO = "metodoPago";
    private static final String ATTR_NOMBRE = "nombreCompleto";
    private static final String ATTR_CORREO = "correoElectronico";

    @Override
    public void init() {
        peliculaDao = new PeliculaDao();
        asientoDao = new AsientoDao();
        funcionDao = new FuncionDao();
        productoDao = new ProductoDao();
        usuarioDao = new UsuarioDao();
        ventaDao = new VentaDao();
        detalleVentaDao = new DetalleVentaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!validarSesion(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'action'");
            return;
        }

        try {
            switch (action) {
                case "listar":
                    listarPeliculas(request, response);
                    break;
                case "mostrarAsientos":
                    mostrarSeleccionAsiento(request, response);
                    break;
                case "confirmarReserva":
                    mostrarConfirmacion(request, response);
                    break;
                case "verVoucher":
                    mostrarVoucher(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error en operación GET", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!validarSesion(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'action'");
            return;
        }

        try {
            switch (action) {
                case "reservar_p":
                    iniciarReserva(request, response);
                    break;
                case "seleccionarAsientos":
                    procesarSeleccionAsientos(request, response);
                    break;
                case "seleccionarCombo":
                    procesarSeleccionCombo(request, response);
                    break;
                case "ingresarDatosPago":
                    procesarDatosPago(request, response);
                    break;
                case "finalizarCompra":
                    finalizarCompra(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error en operación POST", e);
        }
    }

    // ============== VALIDACIÓN ==============
    private boolean validarSesion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return false;
        }
        return true;
    }

    // ============== PASO 1: LISTAR PELÍCULAS ==============
    private void listarPeliculas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Pelicula> peliculas = peliculaDao.listar();
        request.setAttribute("peliculas", peliculas);
        request.getRequestDispatcher("Cliente/DashboardCliente.jsp").forward(request, response);
    }

    // ============== PASO 1.5: INICIAR RESERVA DESDE DETALLE DE PELÍCULA ==============
    private void iniciarReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String idPeliculaStr = request.getParameter("id");
        String idFuncionStr = request.getParameter("idFuncion");

        // Validaciones
        if (idPeliculaStr == null || idFuncionStr == null || idFuncionStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Debe seleccionar una película y un horario");
            return;
        }

        try {
            int idFuncion = Integer.parseInt(idFuncionStr);

            // Obtener la función completa
            Funcion funcion = funcionDao.leer(idFuncion);

            if (funcion == null) {
                request.setAttribute("error", "La función seleccionada no existe");
                request.getRequestDispatcher("Cliente/Error.jsp").forward(request, response);
                return;
            }

            // Guardar función en sesión
            HttpSession session = request.getSession();
            session.setAttribute(ATTR_FUNCION, funcion);

            // Limpiar selecciones previas si existieran
            limpiarSesionCompra(session);

            // Redirigir a selección de asientos
            response.sendRedirect(request.getContextPath()
                    + "/ClienteServlet?action=mostrarAsientos&idFuncion=" + idFuncion);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de función inválido");
        }
    }

    // ============== PASO 2: MOSTRAR ASIENTOS ==============
    private void mostrarSeleccionAsiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();

        // Intentar obtener la función de la sesión primero
        Funcion funcion = (Funcion) session.getAttribute(ATTR_FUNCION);

        // Si no está en sesión, buscar por parámetro
        if (funcion == null) {
            String idFuncionParam = request.getParameter("idFuncion");
            if (idFuncionParam == null || idFuncionParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "No hay función seleccionada");
                return;
            }

            try {
                int idFuncion = Integer.parseInt(idFuncionParam);
                funcion = funcionDao.leer(idFuncion);

                if (funcion == null) {
                    request.setAttribute("error", "Función no encontrada");
                    request.getRequestDispatcher("Cliente/Error.jsp").forward(request, response);
                    return;
                }

                // Guardar en sesión para el flujo
                session.setAttribute(ATTR_FUNCION, funcion);

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de función inválido");
                return;
            }
        }

        // Validar que la función tenga sala
        if (funcion.getSala() == null) {
            request.setAttribute("error", "La función no tiene sala asignada");
            request.getRequestDispatcher("Cliente/Error.jsp").forward(request, response);
            return;
        }

        // Obtener asientos disponibles
        List<Asiento> asientos = asientoDao.obtenerAsientosPorSalaYFuncion(
                funcion.getSala().getIdSala(), funcion.getIdFuncion());

        // Calcular duración
        long duracionMin = (funcion.getFechaFin().getTime() - funcion.getFechaInicio().getTime()) / 60000;

        // Preparar atributos para JSP
        request.setAttribute("asientos", asientos);
        request.setAttribute("funcion", funcion);
        request.setAttribute("sala", funcion.getSala());
        request.setAttribute("pelicula", funcion.getPelicula());
        request.setAttribute("precioButaca", funcion.getPelicula().getPrecio());
        request.setAttribute("genero", funcion.getPelicula().getIdGenero().getNombre());
        request.setAttribute("duracionMin", duracionMin);

        request.getRequestDispatcher("Cliente/SeleccionAsiento.jsp").forward(request, response);
    }

    // ============== PASO 3: PROCESAR SELECCIÓN DE ASIENTOS ==============
    private void procesarSeleccionAsientos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String selectedSeats = request.getParameter("selectedSeats");
        if (selectedSeats == null || selectedSeats.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Debe seleccionar al menos un asiento");
            return;
        }

        HttpSession session = request.getSession();
        Funcion funcion = (Funcion) session.getAttribute(ATTR_FUNCION);

        if (funcion == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No hay función seleccionada");
            return;
        }

        // Validar disponibilidad de asientos
        List<String> asientos = Arrays.asList(selectedSeats.split(","));
        for (String codigoAsiento : asientos) {
            Asiento asiento = asientoDao.leerPorCodigo(codigoAsiento.trim());

            // ✅ CORRECCIÓN: Usar estaDisponible() en lugar de getOcupado()
            if (asiento == null || !asiento.estaDisponible()) {
                request.setAttribute("error", "El asiento " + codigoAsiento + " no está disponible");
                request.getRequestDispatcher("Cliente/Error.jsp").forward(request, response);
                return;
            }
        }

        // Calcular total de asientos
        double totalAsientos = asientos.size() * funcion.getPelicula().getPrecio();

        // Guardar en sesión
        session.setAttribute(ATTR_ASIENTOS, selectedSeats);
        session.setAttribute(ATTR_TOTAL_ASIENTOS, totalAsientos);

        // Log para debug
        System.out.println("=== ASIENTOS SELECCIONADOS ===");
        System.out.println("Códigos: " + selectedSeats);
        System.out.println("Total: S/. " + totalAsientos);

        // ✅ Redirect (no forward) a DulceriaServlet
        response.sendRedirect(request.getContextPath() + "/DulceriaServlet");
    }

    // ============== PASO 4: PROCESAR SELECCIÓN DE COMBO ==============
    private void procesarSeleccionCombo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();

        // Obtener productos seleccionados (formato: producto_idProducto=cantidad)
        Map<Integer, Integer> carrito = new HashMap<>();
        double totalDulces = 0.0;

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.startsWith("producto_")) {
                try {
                    int idProducto = Integer.parseInt(paramName.replace("producto_", ""));
                    String cantidadStr = request.getParameter(paramName);

                    if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                        int cantidad = Integer.parseInt(cantidadStr);
                        if (cantidad > 0) {
                            Producto producto = productoDao.leer(idProducto);
                            if (producto != null) {
                                carrito.put(idProducto, cantidad);
                                totalDulces += producto.getPrecio() * cantidad;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parseando producto: " + paramName);
                }
            }
        }

        // Guardar en sesión (incluso si está vacío)
        session.setAttribute(ATTR_CARRITO, carrito);
        session.setAttribute(ATTR_TOTAL_DULCES, totalDulces);

        // Log para debug
        System.out.println("=== DULCERÍA SELECCIONADA ===");
        System.out.println("Productos: " + carrito.size());
        System.out.println("Total dulces: S/. " + totalDulces);

        // Redirigir a método de pago
        request.getRequestDispatcher("Cliente/MetodoPago.jsp").forward(request, response);
    }

    // ============== PASO 5: PROCESAR DATOS DE PAGO ==============
    private void procesarDatosPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreCompleto = request.getParameter("nombreCompleto");
        String correoElectronico = request.getParameter("correoElectronico");
        String metodoPago = request.getParameter("metodoPago");

        // Validaciones básicas
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()
                || correoElectronico == null || correoElectronico.trim().isEmpty()
                || metodoPago == null || metodoPago.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan datos obligatorios");
            return;
        }

        // Guardar en sesión
        HttpSession session = request.getSession();
        session.setAttribute(ATTR_NOMBRE, nombreCompleto.trim());
        session.setAttribute(ATTR_CORREO, correoElectronico.trim());
        session.setAttribute(ATTR_METODO_PAGO, metodoPago.trim());

        // Log para debug
        System.out.println("=== DATOS DE PAGO ===");
        System.out.println("Nombre: " + nombreCompleto);
        System.out.println("Método: " + metodoPago);

        // Redirigir a confirmación
        response.sendRedirect(request.getContextPath() + "/ClienteServlet?action=confirmarReserva");
    }

    // ============== PASO 6: MOSTRAR CONFIRMACIÓN ==============
    private void mostrarConfirmacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Validar que existan todos los datos necesarios
        if (session.getAttribute(ATTR_FUNCION) == null
                || session.getAttribute(ATTR_ASIENTOS) == null
                || session.getAttribute(ATTR_TOTAL_ASIENTOS) == null) {

            request.setAttribute("error", "Faltan datos para confirmar la reserva. Por favor, inicie el proceso nuevamente.");
            request.getRequestDispatcher("Cliente/Error.jsp").forward(request, response);
            return;
        }

        // Forward a JSP de confirmación (todos los datos están en sesión)
        request.getRequestDispatcher("Cliente/Confirmacion.jsp").forward(request, response);
    }

    // ============== PASO 7: FINALIZAR COMPRA ==============
    private void finalizarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();

        try {
            // Recuperar datos de sesión
            Integer userIdObj = (Integer) session.getAttribute("userId");
            if (userIdObj == null) {
                throw new IllegalStateException("Usuario no identificado en sesión");
            }
            int userId = userIdObj;

            Funcion funcion = (Funcion) session.getAttribute(ATTR_FUNCION);
            String asientosStr = (String) session.getAttribute(ATTR_ASIENTOS);
            Double totalAsientos = (Double) session.getAttribute(ATTR_TOTAL_ASIENTOS);
            Double totalDulces = (Double) session.getAttribute(ATTR_TOTAL_DULCES);
            Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute(ATTR_CARRITO);
            String metodoPago = (String) session.getAttribute(ATTR_METODO_PAGO);

            // Validar datos obligatorios
            if (funcion == null || asientosStr == null || totalAsientos == null) {
                throw new IllegalStateException("Datos incompletos para finalizar compra");
            }

            // Valores por defecto
            if (totalDulces == null) {
                totalDulces = 0.0;
            }
            if (carrito == null) {
                carrito = new HashMap<>();
            }
            if (metodoPago == null || metodoPago.isEmpty()) {
                metodoPago = "efectivo"; // Valor por defecto
            }

            List<String> asientos = Arrays.asList(asientosStr.split(","));

            System.out.println("=== FINALIZANDO COMPRA ===");
            System.out.println("Usuario ID: " + userId);
            System.out.println("Función ID: " + funcion.getIdFuncion());
            System.out.println("Asientos: " + asientosStr);
            System.out.println("Total: S/. " + (totalAsientos + totalDulces));

            // ===== VALIDACIÓN FINAL: Verificar disponibilidad de asientos =====
            for (String codigoAsiento : asientos) {
                Asiento asiento = asientoDao.leerPorCodigo(codigoAsiento.trim());

                // ✅ CORRECCIÓN: Usar estaDisponible()
                if (asiento == null || !asiento.estaDisponible()) {
                    request.setAttribute("error", "El asiento " + codigoAsiento + " ya no está disponible");
                    request.getRequestDispatcher("Cliente/Error.jsp").forward(request, response);
                    return;
                }
            }

            // Crear venta
            Usuario usuario = usuarioDao.leer(userId);
            if (usuario == null) {
                throw new IllegalStateException("Usuario no encontrado: " + userId);
            }

            Venta venta = new Venta();
            venta.setIdUsuarioCliente(usuario);
            venta.setFecha(new Date());
            venta.setTotal(totalAsientos + totalDulces);
            venta.setMetodoPago(metodoPago);

            // Insertar venta y obtener ID
            int idVenta = ventaDao.insertarYDevolverId(venta);
            venta.setIdVenta(idVenta);

            System.out.println("Venta creada con ID: " + idVenta);

            // Guardar detalles de asientos
            for (String codigoAsiento : asientos) {
                Asiento asiento = asientoDao.leerPorCodigo(codigoAsiento.trim());

                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setFuncion(funcion);
                detalle.setAsiento(asiento);
                detalle.setProducto(null);
                detalle.setCantidad(1);
                detalle.setTipoItem(2); // Asiento
                detalle.setPrecioUnitario(funcion.getPelicula().getPrecio());

                detalleVentaDao.insertar(detalle);

                // Marcar asiento como ocupado
                asientoDao.actualizarEstadoOcupado(asiento.getId_asiento());
                System.out.println("Asiento " + codigoAsiento + " marcado como ocupado");
            }

            // Guardar detalles de dulcería
            for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
                Producto producto = productoDao.leer(entry.getKey());
                int idProducto = entry.getKey();
                int cantidad = entry.getValue();

                if (producto != null) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setVenta(venta);
                    detalle.setProducto(producto);
                    detalle.setFuncion(null);  // ✅ IMPORTANTE: Asegurarse que sea null
                    detalle.setAsiento(null);  // ✅ IMPORTANTE: Asegurarse que sea null
                    detalle.setCantidad(cantidad);
                    detalle.setTipoItem(1); // 1 = Producto (dulcería)
                    detalle.setPrecioUnitario(producto.getPrecio());

                    detalleVentaDao.insertar(detalle);
                    System.out.println("Producto " + producto.getNombre() + " x" + entry.getValue() + " guardado");
                }
            }

            // Guardar ID de venta para el voucher
            session.setAttribute("idVenta", idVenta);

            // Limpiar datos de sesión de compra
            limpiarSesionCompra(session);

            System.out.println("=== COMPRA FINALIZADA EXITOSAMENTE ===");

            // Redirigir al voucher
            response.sendRedirect(request.getContextPath() + "/ClienteServlet?action=verVoucher");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al finalizar compra: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al procesar la compra: " + e.getMessage());
        }
    }

    // ============== PASO 8: MOSTRAR VOUCHER ==============
    private void mostrarVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer idVenta = (Integer) session.getAttribute("idVenta");

        if (idVenta == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No hay venta registrada");
            return;
        }

        System.out.println("Mostrando voucher para venta ID: " + idVenta);

        // El JSP puede cargar los detalles con el idVenta
        request.getRequestDispatcher("Cliente/Voucher.jsp").forward(request, response);
    }

    // ============== UTILIDADES ==============
    private void limpiarSesionCompra(HttpSession session) {
        session.removeAttribute(ATTR_FUNCION);
        session.removeAttribute(ATTR_ASIENTOS);
        session.removeAttribute(ATTR_TOTAL_ASIENTOS);
        session.removeAttribute(ATTR_CARRITO);
        session.removeAttribute(ATTR_TOTAL_DULCES);
        session.removeAttribute(ATTR_METODO_PAGO);
        session.removeAttribute(ATTR_NOMBRE);
        session.removeAttribute(ATTR_CORREO);
        // NO limpiar idVenta aquí - se necesita para el voucher
    }
}
