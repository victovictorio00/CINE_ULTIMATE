package Controlador;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.*;
import Conexion.Conexion;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    private PeliculaDao peliculaDao;
    private AsientoDao asientoDao;
    private FuncionDao funcionDao;
    private ProductoDao productoDao;
    private UsuarioDao usuarioDao;
    private VentaDao ventaDao;
    private DetalleVentaDao detalleVentaDao;
    private AsientoFuncionDao asientoFuncionDao;   // <-- nuevo

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
        asientoFuncionDao = new AsientoFuncionDao();   // <-- instanciado
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
                case "misReservas":
                    mostrarReservasCliente(request, response);
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
        request.getRequestDispatcher("Cliente/home.jsp").forward(request, response);
    }

    // ============== PASO 1.5: INICIAR RESERVA DESDE DETALLE DE PELÍCULA ==============
    private void iniciarReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String idPeliculaStr = request.getParameter("id");
        String idFuncionStr = request.getParameter("idFuncion");

        if (idPeliculaStr == null || idFuncionStr == null || idFuncionStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Debe seleccionar una película y un horario");
            return;
        }

        try {
            int idFuncion = Integer.parseInt(idFuncionStr);
            Funcion funcion = funcionDao.leer(idFuncion);

            if (funcion == null) {
                request.setAttribute("error", "La función seleccionada no existe");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute(ATTR_FUNCION, funcion);
            limpiarSesionCompra(session);

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
        Funcion funcion = (Funcion) session.getAttribute(ATTR_FUNCION);

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
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                session.setAttribute(ATTR_FUNCION, funcion);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de función inválido");
                return;
            }
        }

        if (funcion.getSala() == null) {
            request.setAttribute("error", "La función no tiene sala asignada");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        List<AsientoFuncion> afList = asientoFuncionDao.listarPorSalaYFuncion(funcion.getIdFuncion());


        // Convertir a List<Asiento> con el estado ya seteado
        List<Asiento> asientos = new ArrayList<>();
        for (AsientoFuncion af : afList) {
            Asiento a = af.getAsiento();
            // le pegas el estado que viene de asiento_funcion
            a.setId_estado_asiento(af.getEstadoAsiento());
            asientos.add(a);
        }
        request.setAttribute("asientosFuncion", asientos);

        long duracionMin = (funcion.getFechaFin().getTime() - funcion.getFechaInicio().getTime()) / 60000;

        request.setAttribute("asientosFuncion", asientos);
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

        try {
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

            List<String> asientos = Arrays.asList(selectedSeats.split(","));
            // reemplaza TODO el for de validación por esto:
            for (String codigoAsiento : asientos) {
                // 1. obtener el id del asiento
                Asiento asiento = asientoDao.leerPorCodigoYSala(codigoAsiento.trim(),
                        funcion.getSala().getIdSala());
                if (asiento == null) {
                    request.setAttribute("error", "Asiento " + codigoAsiento + " no existe");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                // 2. consultar SU estado en esta función
                int idAf = obtenerIdAsientoFuncion(funcion.getIdFuncion(), asiento.getId_asiento());
                if (idAf == 0) {
                    request.setAttribute("error", "Asiento " + codigoAsiento + " no está registrado para esta función");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                // 3. verificar que esté libre (estado 1 = disponible)
                AsientoFuncion af = asientoFuncionDao.leer(idAf);
                if (af == null || af.getEstadoAsiento().getIdEstadoAsiento() != 1) {
                    request.setAttribute("error", "Asiento " + codigoAsiento + " ya no está disponible");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
            }

            double totalAsientos = asientos.size() * funcion.getPelicula().getPrecio();

            session.setAttribute(ATTR_ASIENTOS, selectedSeats);
            session.setAttribute(ATTR_TOTAL_ASIENTOS, totalAsientos);

            response.sendRedirect(request.getContextPath() + "/DulceriaServlet");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error en procesarSeleccionAsientos", e);
        }
    }

    // ============== PASO 4: PROCESAR SELECCIÓN DE COMBO ==============
    private void procesarSeleccionCombo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
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

        session.setAttribute(ATTR_CARRITO, carrito);
        session.setAttribute(ATTR_TOTAL_DULCES, totalDulces);
        request.getRequestDispatcher("Cliente/MetodoPago.jsp").forward(request, response);
    }

    // ============== PASO 5: PROCESAR DATOS DE PAGO ==============
    private void procesarDatosPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreCompleto = request.getParameter("nombreCompleto");
        String correoElectronico = request.getParameter("correoElectronico");
        String metodoPago = request.getParameter("metodoPago");

        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()
                || correoElectronico == null || correoElectronico.trim().isEmpty()
                || metodoPago == null || metodoPago.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan datos obligatorios");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute(ATTR_NOMBRE, nombreCompleto.trim());
        session.setAttribute(ATTR_CORREO, correoElectronico.trim());
        session.setAttribute(ATTR_METODO_PAGO, metodoPago.trim());

        response.sendRedirect(request.getContextPath() + "/ClienteServlet?action=confirmarReserva");
    }

    // ============== PASO 6: MOSTRAR CONFIRMACIÓN ==============
    private void mostrarConfirmacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session.getAttribute(ATTR_FUNCION) == null
                || session.getAttribute(ATTR_ASIENTOS) == null
                || session.getAttribute(ATTR_TOTAL_ASIENTOS) == null) {

            request.setAttribute("error", "Faltan datos para confirmar la reserva. Por favor, inicie el proceso nuevamente.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("Cliente/Confirmacion.jsp").forward(request, response);
    }

    // ============== PASO 7: FINALIZAR COMPRA ==============
    private void finalizarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        try {
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

            if (funcion == null || asientosStr == null || totalAsientos == null) {
                throw new IllegalStateException("Datos incompletos para finalizar compra");
            }
            if (totalDulces == null) {
                totalDulces = 0.0;
            }
            if (carrito == null) {
                carrito = new HashMap<>();
            }
            if (metodoPago == null || metodoPago.isEmpty()) {
                metodoPago = "efectivo";
            }

            List<String> asientos = Arrays.asList(asientosStr.split(","));
            Usuario usuario = usuarioDao.leer(userId);
            if (usuario == null) {
                throw new IllegalStateException("Usuario no encontrado: " + userId);
            }

            for (String codigoAsiento : asientos) {
                Asiento asiento = asientoDao.leerPorCodigoYSala(
                        codigoAsiento.trim(),
                        funcion.getSala().getIdSala()
                );

                int idAf = obtenerIdAsientoFuncion(funcion.getIdFuncion(), asiento.getId_asiento());
                AsientoFuncion afExistente = asientoFuncionDao.leer(idAf);

                if (afExistente == null || afExistente.getEstadoAsiento().getIdEstadoAsiento() != 1) {
                    // 🔄 Recargar los asientos actualizados para mostrar disponibilidad real
                    List<AsientoFuncion> afListActualizada = asientoFuncionDao.listarPorSalaYFuncion(funcion.getIdFuncion());
                    List<Asiento> asientosActualizados = new ArrayList<>();
                    for (AsientoFuncion af : afListActualizada) {
                        Asiento a = af.getAsiento();
                        a.setId_estado_asiento(af.getEstadoAsiento());
                        asientosActualizados.add(a);
                    }

                    request.setAttribute("mensaje",
                            "El asiento " + codigoAsiento
                            + " ya fue ocupado por otro usuario justo antes de tu confirmación. Por favor selecciona otro disponible.");
                    request.setAttribute("asientosFuncion", asientosActualizados);
                    request.setAttribute("funcion", funcion);
                    request.setAttribute("sala", funcion.getSala());
                    request.setAttribute("pelicula", funcion.getPelicula());
                    request.setAttribute("precioButaca", funcion.getPelicula().getPrecio());
                    request.setAttribute("genero", funcion.getPelicula().getIdGenero().getNombre());
                    long duracionMin = (funcion.getFechaFin().getTime() - funcion.getFechaInicio().getTime()) / 60000;
                    request.setAttribute("duracionMin", duracionMin);

                    request.getRequestDispatcher("Cliente/SeleccionAsiento.jsp").forward(request, response);
                    return;
                }
            }

            Venta venta = new Venta();
            venta.setIdUsuarioCliente(usuario);
            venta.setFecha(new java.util.Date());
            venta.setTotal(totalAsientos + totalDulces);
            venta.setMetodoPago(metodoPago);
            int idVenta = ventaDao.insertarYDevolverId(venta);
            venta.setIdVenta(idVenta);

            Comprobante comp = new Comprobante();
            comp.setVenta(venta);
            comp.setTipoComprobante("Boleta");
            comp.setFechaEmision(new Timestamp(System.currentTimeMillis()));
            new ComprobanteDao().insertar(comp);

            for (String codigoAsiento : asientos) {
                Asiento asiento = asientoDao.leerPorCodigoYSala(
                        codigoAsiento.trim(),
                        funcion.getSala().getIdSala()
                );

                int idAf = obtenerIdAsientoFuncion(funcion.getIdFuncion(), asiento.getId_asiento());
                AsientoFuncion afExistente = asientoFuncionDao.leer(idAf);

                if (afExistente == null || afExistente.getEstadoAsiento().getIdEstadoAsiento() != 1) {
                    request.setAttribute("mensaje",
                            "El asiento " + codigoAsiento
                            + " fue ocupado por otro usuario segundos antes de confirmar. Selecciona otro disponible.");
                    request.getRequestDispatcher("Cliente/SeleccionAsiento.jsp").forward(request, response);
                    return;
                }

                AsientoFuncion afParaDetalle = new AsientoFuncion();
                afParaDetalle.setIdAsientoFuncion(idAf);

                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setFuncion(funcion);
                detalle.setIdAsientoFuncion(afParaDetalle);
                detalle.setProducto(null);
                detalle.setCantidad(1);
                detalle.setTipoItem(2);
                detalle.setPrecioUnitario(funcion.getPelicula().getPrecio());
                detalleVentaDao.insertar(detalle);

                asientoFuncionDao.actualizarEstado(idAf, 2);
                System.out.println("DEBUG: Asiento " + codigoAsiento + " marcado como ocupado (2)");
            }
            for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
                Producto producto = productoDao.leer(entry.getKey());
                if (producto != null) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setVenta(venta);
                    detalle.setProducto(producto);
                    detalle.setFuncion(null);
                    detalle.setIdAsientoFuncion(null);
                    detalle.setCantidad(entry.getValue());
                    detalle.setTipoItem(1);
                    detalle.setPrecioUnitario(producto.getPrecio());
                    detalleVentaDao.insertar(detalle);
                }
            }
            session.setAttribute("idVenta", idVenta);
            limpiarSesionCompra(session);
            response.sendRedirect(request.getContextPath() + "/ClienteServlet?action=verVoucher");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al procesar la compra: " + e.getMessage());
        }
    }

    // ============== PASO 8: MOSTRAR VOUCHER ==============
    private void mostrarVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer idVenta = null;

        String idVentaParam = request.getParameter("idVenta");
        if (idVentaParam != null && !idVentaParam.isEmpty()) {
            try {
                idVenta = Integer.parseInt(idVentaParam);
                session.setAttribute("idVenta", idVenta);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de venta inválido");
                return;
            }
        } else {
            idVenta = (Integer) session.getAttribute("idVenta");
        }

        if (idVenta == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No hay venta registrada");
            return;
        }

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
    }

    /* ----------------------------------------------------------
       Helper para obtener id_asiento_funcion dado idFuncion + idAsiento
       ---------------------------------------------------------- */
    private int obtenerIdAsientoFuncion(int idFuncion, int idAsiento) throws SQLException {
        String sql = "SELECT id_asiento_funcion FROM asiento_funcion WHERE id_funcion = ? AND id_asiento = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, idFuncion);
            pst.setInt(2, idAsiento);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private void mostrarReservasCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        try {
            VentaDao ventaDao = new VentaDao();
            DetalleVentaDao detalleDao = new DetalleVentaDao();

            List<Venta> ventas = ventaDao.obtenerReservasPorUsuario(userId);
            Map<Integer, FilaReservaDTO> filasMap = new LinkedHashMap<>();

            for (Venta v : ventas) {
                List<DetalleVenta> detalles = detalleDao.listarPorVenta(v.getIdVenta());
                v.setDetalles(detalles);

                for (DetalleVenta d : detalles) {
                    Integer idFunc = d.getFuncion() != null ? d.getFuncion().getIdFuncion() : null;

                    if (d.getTipoItem() == 1) {
                        FilaReservaDTO dtoProd = filasMap.computeIfAbsent(v.getIdVenta(), k -> {
                            FilaReservaDTO f = new FilaReservaDTO();
                            f.setIdVenta(v.getIdVenta());
                            f.setPelicula("—");
                            f.setSala("—");
                            f.setFechaHora(null);
                            return f;
                        });
                        dtoProd.setCantidadProductos(dtoProd.getCantidadProductos() + d.getCantidad());
                        dtoProd.setTotalProductos(dtoProd.getTotalProductos() + d.getPrecioUnitario() * d.getCantidad());
                        continue;
                    }

                    if (idFunc != null) {   // BUTACAS
                        FilaReservaDTO dto = filasMap.computeIfAbsent(v.getIdVenta(), k -> {
                            FilaReservaDTO f = new FilaReservaDTO();
                            Funcion func = d.getFuncion();
                            f.setIdVenta(v.getIdVenta());
                            f.setPelicula(func.getPelicula().getNombre());
                            f.setSala(func.getSala().getNombre());
                            f.setFechaHora(func.getFechaInicio());
                            return f;
                        });

                        if (d.getTipoItem() == 2) {
                            dto.getAsientos().add(d.getIdAsientoFuncion().getAsiento().getCodigo());
                            dto.setTotalEntradas(dto.getTotalEntradas() + d.getPrecioUnitario() * d.getCantidad());
                        }
                    }
                }
            }

            request.setAttribute("filas", filasMap.values());
            request.setAttribute("ventas", ventas);
            request.getRequestDispatcher("Cliente/MisReservas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al cargar las reservas: " + e.getMessage());
        }
    }
}
