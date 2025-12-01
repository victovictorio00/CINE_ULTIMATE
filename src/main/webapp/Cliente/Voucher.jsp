<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>

<%
    // === Obtener la sesión y el idVenta guardado ===
    HttpSession sesion = request.getSession(false);
    if (sesion == null || sesion.getAttribute("idVenta") == null) {
        response.sendRedirect(request.getContextPath() + "/CarteleraServlet");
        return;
    }
    int idVenta = (int) sesion.getAttribute("idVenta");
    // === Obtener la venta y los detalles desde la BD ===
    VentaDao ventaDao = new VentaDao();
    DetalleVentaDao detalleDao = new DetalleVentaDao();
    Venta venta = ventaDao.leer(idVenta);
    if (venta == null) {
        response.sendRedirect(request.getContextPath() + "/CarteleraServlet");
        return;
    }
    List<DetalleVenta> detalles = detalleDao.listarPorVenta(idVenta);
    List<DetalleVenta> detallesEntradas = new ArrayList<>();
    List<DetalleVenta> detallesDulceria = new ArrayList<>();

    for (DetalleVenta d : detalles) {
        if (d.getTipoItem() == 1) {
            // Productos
            detallesDulceria.add(d);
        } else if (d.getTipoItem() == 2) {
            // Entradas
            detallesEntradas.add(d);
        }
    }
    // === Datos necesarios para mostrar ===
    String cliente = venta.getIdUsuarioCliente() != null
            ? venta.getIdUsuarioCliente().getNombreCompleto() : "Cliente";
    String cine = "CineMax";
    String pelicula = "-";
    String sala = "-";
    String horario = "-";
    String butacas = "";

    if (!detallesEntradas.isEmpty()) {
        DetalleVenta primeraEntrada = detallesEntradas.get(0);
        if (primeraEntrada.getFuncion() != null) {
            pelicula = primeraEntrada.getFuncion().getPelicula().getNombre();
            sala = primeraEntrada.getFuncion().getSala().getNombre();
            // Formatear horario
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            horario = sdf.format(primeraEntrada.getFuncion().getFechaInicio());
        }
        for (DetalleVenta d : detallesEntradas) {
            // CORRECCIÓN: Usar getIdAsientoFuncion() para obtener el objeto AsientoFuncion
            if (d.getIdAsientoFuncion() != null && d.getIdAsientoFuncion().getAsiento() != null) {
                butacas += d.getIdAsientoFuncion().getAsiento().getCodigo() + ", ";
            }
        }
        if (!butacas.isEmpty()) {
            butacas = butacas.substring(0, butacas.length() - 2);
        } else {
            butacas = "N/A";
        }
    }
    SimpleDateFormat sdfVenta = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String fechaVenta = sdfVenta.format(venta.getFecha());
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Voucher de Compra | <%= venta.getIdVenta()%></title>

        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosCliente/Voucher.css">
    </head>
    <body>
        <header class="voucher-header">
            <a href="<%= request.getContextPath()%>/DashboardServlet" class="back-link">
                <i class="fas fa-arrow-left"></i> Volver al Inicio
            </a>
            <div class="title">Voucher de Compra</div>
            <div></div>
        </header>
        <% if (request.getAttribute("mensaje") != null) { %>
        <div class="position-fixed top-0 end-0 p-3">
            <div id="liveToast" class="toast show align-items-center text-bg-warning border-0 shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">
                        <strong>¡Upss!</strong> <%= request.getAttribute("mensaje") %>
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Cerrar"></button>
                </div>
            </div>
        </div>
        <% } %>
        <div class="voucher-container">
            <div class="success-badge">
                <i class="fas fa-check-circle"></i>
                ¡Compra Realizada con Éxito!
            </div>
            <div class="header-voucher">
                <div class="order-info">
                    <h6>N° de Orden:</h6>
                    <div class="order-number">#<%= String.format("%06d", venta.getIdVenta())%></div>
                    <small><%= fechaVenta%></small>
                </div>
                <div class="qr-code">
                    <img src="<%= request.getContextPath()%>/Cliente/images/qr.png" alt="Código QR" />
                </div>
                <div class="details-info">
                    <p><strong>Cine:</strong> <%= cine%></p>
                    <p><strong>Película:</strong> <%= pelicula%></p>
                    <p><strong>Sala:</strong> <%= sala%></p>
                    <p><strong>Función:</strong> <%= horario%></p>
                    <p><strong>Butacas:</strong> <%= butacas%></p>
                    <p><strong>Cliente:</strong> <%= cliente%></p>
                    <p><strong>Pago:</strong> <%= venta.getMetodoPago()%></p>
                </div>
            </div>
            <% if (!detallesEntradas.isEmpty()) { %>
            <h5 class="section-title">
                <i class="fas fa-ticket-alt"></i> Entradas
            </h5>
            <table class="table table-sm table-hover">
                <thead>
                    <tr>
                        <th>Película</th>
                        <th>Asiento</th>
                        <th class="text-right">Precio</th>
                        <th class="text-center">Cant.</th>
                        <th class="text-right">Total</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (DetalleVenta d : detallesEntradas) {
                            System.out.println("DEBUG: idAsientoFuncion = " + 
                                (d.getIdAsientoFuncion() != null ? d.getIdAsientoFuncion().getIdAsientoFuncion() : "null"));
                            System.out.println("DEBUG: asiento = " + 
                                (d.getIdAsientoFuncion() != null && d.getIdAsientoFuncion().getAsiento() != null ? d.getIdAsientoFuncion().getAsiento().getCodigo() : "null"));}
                        double subtotalEntradas = 0;
                        for (DetalleVenta d : detallesEntradas) {
                            String codigoAsiento = "N/A";
                            if (d.getIdAsientoFuncion() != null && d.getIdAsientoFuncion().getAsiento() != null) {
                                codigoAsiento = d.getIdAsientoFuncion().getAsiento().getCodigo();
                            }
                            double totalItem = d.getPrecioUnitario() * d.getCantidad();
                            subtotalEntradas += totalItem;
                    %>
                    <tr>
                        <td><%= d.getFuncion().getPelicula().getNombre()%></td>
                        <td><strong><%= codigoAsiento%></strong></td>
                        <td class="text-right">S/. <%= String.format("%.2f", d.getPrecioUnitario())%></td>
                        <td class="text-center"><%= d.getCantidad()%></td>
                        <td class="text-right"><strong>S/. <%= String.format("%.2f", totalItem)%></strong></td>
                    </tr>
                    <% }%>
                    <tr>
                        <td colspan="4" class="text-right">Subtotal Entradas:</td>
                        <td class="text-right">S/. <%= String.format("%.2f", subtotalEntradas)%></td>
                    </tr>
                </tbody>
            </table>
            <% } %>

            <% if (!detallesDulceria.isEmpty()) { %>
            <h5 class="section-title">
                <i class="fas fa-candy-cane"></i> Dulcería
            </h5>
            <table class="table table-sm table-hover">
                <thead>
                    <tr>
                        <th>Producto</th>
                        <th class="text-right">Precio</th>
                        <th class="text-center">Cantidad</th>
                        <th class="text-right">Total</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        double subtotalDulceria = 0;
                        for (DetalleVenta d : detallesDulceria) {
                            double totalItem = d.getPrecioUnitario() * d.getCantidad();
                            subtotalDulceria += totalItem;
                    %>
                    <tr>
                        <td><%= d.getProducto().getNombre()%></td>
                        <td class="text-right">S/. <%= String.format("%.2f", d.getPrecioUnitario())%></td>
                        <td class="text-center"><%= d.getCantidad()%></td>
                        <td class="text-right"><strong>S/. <%= String.format("%.2f", totalItem)%></strong></td>
                    </tr>
                    <% }%>
                    <tr class="colorcito">
                        <td colspan="3" class="text-right">Subtotal Dulcería:</td>
                        <td class="text-right">S/. <%= String.format("%.2f", subtotalDulceria)%></td>
                    </tr>
                </tbody>
            </table>
            <% }%>

            <div class="total-section">
                <div class="total-item">
                    <div class="total-label">Total en Soles</div>
                    <div class="total-amount">S/. <%= String.format("%.2f", venta.getTotal())%></div>
                </div>
                <div class="total-item">
                    <div class="total-label">Total en Dólares</div>
                    <div class="total-amount">USD <%= String.format("%.2f", venta.getTotal() / 3.80)%></div>
                </div>
            </div>

            <div class="notes">
                <p><strong><i class="fas fa-exclamation-triangle"></i> Condiciones Importantes:</strong></p>
                <p>• La compra y el canje de las entradas y/o combos, solo son válidos para el mismo día de la función.</p>
                <p>• Si utilizaste códigos promocionales o boletos corporativos debes presentar los cupones físicos en el ingreso a sala.</p>
                <p>• Esta compra no permite cambio de función, anulación y/o devolución de dinero.</p>
                <p><strong>Instrucciones:</strong></p>
                <p>1. Imprime este documento o presenta tu smartphone con el código QR al ingreso a salas.</p>
                <p>2. Si compraste dulcería, dirígete a la zona de despacho con este voucher.</p>
                <p>3. Este no es un comprobante de pago válido. Solicítalo el mismo día en administración.</p>
            </div>
            <div class="action-buttons">
                <button onclick="window.print()" class="btn btn-custom btn-print">
                    <i class="fas fa-print"></i> Imprimir Voucher
                </button>
                <a href="<%= request.getContextPath()%>/DashboardServlet" class="btn btn-custom btn-home">
                    <i class="fas fa-home"></i> Volver al Inicio
                </a>
            </div>

            <div class="footer-thanks">
                <i class="fas fa-heart"></i>
                ¡Gracias por tu compra!
            </div>
        </div>
        <script src="<%= request.getContextPath() %>/Cliente/lib/jquery/jquery-3.6.4.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>