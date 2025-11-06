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

    // ===== RECUPERAR DATOS DE SESIÓN (NOMBRES CORRECTOS) =====
    String nombreCompleto = (String) sesion.getAttribute("nombreCompleto");
    String correoElectronico = (String) sesion.getAttribute("correoElectronico");
    String metodoPago = (String) sesion.getAttribute("metodoPago");
    
    Funcion funcion = (Funcion) sesion.getAttribute("funcionSeleccionada");
    String asientosSeleccionados = (String) sesion.getAttribute("asientosSeleccionados"); // String "A1,A2,B3"
    Double totalAsientos = (Double) sesion.getAttribute("totalAsientos");
    Double totalDulces = (Double) sesion.getAttribute("totalDulces");
    Map<Integer, Integer> carritoDulceria = (Map<Integer, Integer>) sesion.getAttribute("carritoDulceria");

    // Validar datos obligatorios
    if (funcion == null || asientosSeleccionados == null || totalAsientos == null) {
        response.sendRedirect(request.getContextPath() + "/CarteleraServlet");
        return;
    }

    // Valores por defecto
    if (totalDulces == null) totalDulces = 0.0;
    if (carritoDulceria == null) carritoDulceria = new java.util.HashMap<>();
    if (nombreCompleto == null) nombreCompleto = "No registrado";
    if (correoElectronico == null) correoElectronico = "No registrado";
    if (metodoPago == null) metodoPago = "No seleccionado";

    // Calcular total general
    double totalGeneral = totalAsientos + totalDulces;

    // Convertir asientos a lista
    List<String> listaAsientos = Arrays.asList(asientosSeleccionados.split(","));

    // Formatear fechas
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmación de Compra</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        :root {
            --dark: #343a40;
            --orange: #FF5733;
            --success: #28a745;
            --light: #f5f5f5;
            --white: #ffffff;
        }

        body {
            background-color: var(--light);
            font-family: 'Segoe UI', Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        .custom-header {
            background: linear-gradient(135deg, var(--dark) 0%, #1a1a1a 100%);
            color: var(--white);
            padding: 20px 30px;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
        }

        .custom-header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: bold;
        }

        .confirmation-container {
            max-width: 800px;
            margin: 40px auto;
            background: var(--white);
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.15);
            overflow: hidden;
        }

        .section {
            padding: 25px 35px;
            border-bottom: 1px solid #e0e0e0;
        }

        .section:last-of-type {
            border-bottom: none;
        }

        .section-header {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 15px;
            color: var(--dark);
            font-weight: bold;
            font-size: 18px;
        }

        .section-header i {
            color: var(--orange);
            font-size: 22px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #f0f0f0;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            color: #666;
            font-weight: 500;
        }

        .info-value {
            color: var(--dark);
            font-weight: 600;
        }

        .items-list {
            list-style: none;
            padding: 0;
            margin: 10px 0;
        }

        .items-list li {
            padding: 8px 12px;
            background: #f8f9fa;
            margin-bottom: 6px;
            border-radius: 6px;
            display: flex;
            justify-content: space-between;
        }

        .total-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px 35px;
        }

        .total-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            font-size: 16px;
        }

        .total-final {
            font-size: 24px;
            font-weight: bold;
            margin-top: 15px;
            padding-top: 15px;
            border-top: 2px solid rgba(255,255,255,0.3);
        }

        .actions {
            padding: 30px 35px;
            text-align: center;
            background: #f8f9fa;
        }

        .btn-confirm {
            background: var(--success);
            color: white;
            border: none;
            padding: 15px 40px;
            font-size: 18px;
            font-weight: bold;
            border-radius: 50px;
            cursor: pointer;
            box-shadow: 0 5px 15px rgba(40, 167, 69, 0.3);
            transition: all 0.3s ease;
            margin-right: 15px;
        }

        .btn-confirm:hover {
            background: #218838;
            transform: translateY(-2px);
            box-shadow: 0 7px 20px rgba(40, 167, 69, 0.4);
        }

        .btn-back {
            background: var(--dark);
            color: white;
            border: none;
            padding: 15px 30px;
            font-size: 16px;
            font-weight: 600;
            border-radius: 50px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-back:hover {
            background: #23272b;
        }

        .alert-info {
            background: #fff3cd;
            border: 1px solid #ffc107;
            color: #856404;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        footer {
            text-align: center;
            padding: 20px;
            color: #666;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <header class="custom-header">
        <h1><i class="fas fa-check-circle"></i> Confirmación de Compra</h1>
    </header>

    <div class="confirmation-container">
        <!-- Alerta informativa -->
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
            <p style="color: #999; font-style: italic;">No se seleccionaron productos de dulcería</p>
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
            <form id="formConfirmar" action="<%= request.getContextPath() %>/ClienteServlet" method="post" style="display: inline;">
                <input type="hidden" name="action" value="finalizarCompra">
                <button type="submit" class="btn-confirm" id="btnConfirmar">
                    <i class="fas fa-check"></i> Confirmar y Pagar
                </button>
            </form>
            <button type="button" class="btn-back" onclick="history.back()">
                <i class="fas fa-arrow-left"></i> Volver
            </button>
        </div>
    </div>

    <footer>
        © 2025 Cine Online | Todos los derechos reservados
    </footer>

    <script>
        const formConfirmar = document.getElementById('formConfirmar');
        const btnConfirmar = document.getElementById('btnConfirmar');

        formConfirmar.addEventListener('submit', function(e) {
            e.preventDefault();

            const confirmacion = confirm(
                '¿Confirmar la compra por S/. <%= String.format("%.2f", totalGeneral) %>?\n\n' +
                'Esta acción es irreversible.'
            );

            if (confirmacion) {
                btnConfirmar.disabled = true;
                btnConfirmar.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Procesando...';
                formConfirmar.submit();
            }
        });

        console.log('Confirmación de compra cargada');
        console.log('Total: S/. <%= String.format("%.2f", totalGeneral) %>');
    </script>
</body>
</html>