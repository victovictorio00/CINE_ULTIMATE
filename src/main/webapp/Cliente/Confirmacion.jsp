<%@page import="modelo.ProductoDao"%>
<%@page import="modelo.Producto"%>
<%@page import="java.util.Map"%>
<%@page import="modelo.Funcion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>

<%
    // Validar sesión
    HttpSession sesion = request.getSession(false);
    if (sesion == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }
    String nombreCompleto = (String) sesion.getAttribute("nombreCompleto");
    String correoElectronico = (String) sesion.getAttribute("correoElectronico");
    String metodoPago = (String) sesion.getAttribute("metodoPago");
    
    Funcion funcion = (Funcion) sesion.getAttribute("funcionSeleccionada");
    String asientosSeleccionados = (String) sesion.getAttribute("asientosSeleccionados");
    Double totalAsientos = (Double) sesion.getAttribute("totalAsientos");
    Double totalDulces = (Double) sesion.getAttribute("totalDulces");
    Map<Integer, Integer> carritoDulceria = (Map<Integer, Integer>) sesion.getAttribute("carritoDulceria");

    if (funcion == null || asientosSeleccionados == null || totalAsientos == null) {
        response.sendRedirect(request.getContextPath() + "/CarteleraServlet");
        return;
    }

    if (totalDulces == null) totalDulces = 0.0;
    if (carritoDulceria == null) carritoDulceria = new java.util.HashMap<>();
    if (nombreCompleto == null) nombreCompleto = "No registrado";
    if (correoElectronico == null) correoElectronico = "No registrado";
    if (metodoPago == null) metodoPago = "No seleccionado";

    double totalGeneral = totalAsientos + totalDulces;
    List<String> listaAsientos = Arrays.asList(asientosSeleccionados.split(","));

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Confirmación de Compra</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
        <script src="<%= request.getContextPath() %>/Cliente/lib/bootstrap/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosCliente/Confirmacion.css">
    </head>
    <body>
        <header class="custom-header">
            <h1><i class="fas fa-check-circle mb-2"></i> Confirmación de Compra</h1>
            <button type="button" class="btn-back" onclick="history.back()">
                    <i class="fas fa-arrow-left"></i> Volver
            </button>
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

        <div class="confirmation-container">
            <div class="section">
                <div class="alert-info">
                    <i class="fas fa-info-circle"></i>
                    <strong>Revisa tu compra antes de confirmar.</strong> 
                    Una vez confirmada, no se podrán hacer cambios ni devoluciones.
                </div>
            </div>

            <!-- Función y Película -->
            <div class="section">
                <div class="section-header">
                    <i class="fas fa-film"></i>
                    <span>Función Seleccionada</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Película:</span>
                    <span class="info-value"><%= funcion.getPelicula().getNombre() %></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Fecha:</span>
                    <span class="info-value"><%= dateFormat.format(funcion.getFechaInicio()) %></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Horario:</span>
                    <span class="info-value">
                        <%= timeFormat.format(funcion.getFechaInicio()) %> - 
                        <%= timeFormat.format(funcion.getFechaFin()) %>
                    </span>
                </div>
                <div class="info-row">
                    <span class="info-label">Sala:</span>
                    <span class="info-value"><%= funcion.getSala().getNombre() %></span>
                </div>
            </div>

            <!-- Asientos -->
            <div class="section">
                <div class="section-header">
                    <i class="fas fa-couch"></i>
                    <span>Asientos Seleccionados (<%= listaAsientos.size() %>)</span>
                </div>
                <ul class="items-list">
                    <% for (String asiento : listaAsientos) { %>
                    <li>
                        <span><i class="fas fa-chair"></i> Asiento <%= asiento.trim() %></span>
                        <span><strong>S/. <%= String.format("%.2f", funcion.getPelicula().getPrecio()) %></strong></span>
                    </li>
                    <% } %>
                </ul>
            </div>

            <!-- Dulcería -->
            <% if (!carritoDulceria.isEmpty()) { %>
            <div class="section">
                <div class="section-header">
                    <i class="fas fa-candy-cane"></i>
                    <span>Dulcería (<%= carritoDulceria.size() %> productos)</span>
                </div>
                <ul class="items-list">
                    <%
                        ProductoDao productoDao = new ProductoDao();
                        for (Map.Entry<Integer, Integer> entry : carritoDulceria.entrySet()) {
                            Producto producto = productoDao.leer(entry.getKey());
                            int cantidad = entry.getValue();
                            double subtotal = producto.getPrecio() * cantidad;
                    %>
                    <li>
                        <span>
                            <i class="fas fa-shopping-bag"></i>
                            <%= producto.getNombre() %> x<%= cantidad %>
                        </span>
                        <span><strong>S/. <%= String.format("%.2f", subtotal) %></strong></span>
                    </li>
                    <% } %>
                </ul>
            </div>
            <% } else { %>
            <div class="section">
                <div class="section-header">
                    <i class="fas fa-candy-cane"></i>
                    <span>Dulcería</span>
                </div>
                <p>No se seleccionaron productos de dulcería</p>
            </div>
            <% } %>

            <!-- Datos del Cliente -->
            <div class="section">
                <div class="section-header">
                    <i class="fas fa-user"></i>
                    <span>Datos del Cliente</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Nombre:</span>
                    <span class="info-value"><%= nombreCompleto %></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Correo:</span>
                    <span class="info-value"><%= correoElectronico %></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Método de Pago:</span>
                    <span class="info-value"><%= metodoPago %></span>
                </div>
            </div>

            <!-- Total -->
            <div class="total-section">
                <div class="total-row">
                    <span>Subtotal Entradas:</span>
                    <span><strong>S/. <%= String.format("%.2f", totalAsientos) %></strong></span>
                </div>
                <% if (totalDulces > 0) { %>
                <div class="total-row">
                    <span>Subtotal Dulcería:</span>
                    <span><strong>S/. <%= String.format("%.2f", totalDulces) %></strong></span>
                </div>
                <% } %>
                <div class="total-row total-final">
                    <span>TOTAL A PAGAR:</span>
                    <span>S/. <%= String.format("%.2f", totalGeneral) %></span>
                </div>
            </div>

            <!-- Acciones -->
            <div class="actions">
                <form id="formConfirmar" action="<%= request.getContextPath() %>/ClienteServlet" method="post">
                    <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">
                    <input type="hidden" name="action" value="finalizarCompra">
                    <button type="submit" class="btn-confirm" id="btnConfirmar">
                        <i class="fas fa-check"></i> Confirmar y Pagar
                    </button>
                </form>
            </div>
        </div>

        <footer>
            © 2025 Cine Online | Todos los derechos reservados
        </footer>

        <%-- Inicializar datos de configuración para JavaScript --%>
        <script>
            window.confirmacionData = {
                totalGeneral: '<%= String.format("%.2f", totalGeneral) %>'
            };
        </script>

        <%-- Script externo de confirmación --%>
        <script src="<%= request.getContextPath() %>/Cliente/JS/confirmacionLogic.js"></script>
    </body>
</html>