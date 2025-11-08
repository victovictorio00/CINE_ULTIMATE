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

    // === Clasificar detalles por tipo ===
    // tipoItem = 1: Producto (dulcería)
    // tipoItem = 2: Entrada (función + asiento)
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

    // === Datos útiles para mostrar ===
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

        // Obtener códigos de asientos
        // NOTA: Depende de cómo guardaste los asientos
        // Opción 1: Si tienes AsientoFuncion con referencia a Asiento
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

    // Formatear fecha de venta
    SimpleDateFormat sdfVenta = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String fechaVenta = sdfVenta.format(venta.getFecha());
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Voucher de Compra | <%= venta.getIdVenta()%></title>

        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

        <style>
            body {
                margin: 0;
                padding: 0;
                font-family: 'Segoe UI', Arial, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding-top: 70px;
            }

            .voucher-header {
                background-color: #1a1a2e;
                color: white;
                height: 60px;
                display: flex;
                align-items: center;
                padding: 0 30px;
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                z-index: 1050;
                box-shadow: 0 2px 10px rgba(0,0,0,0.3);
            }

            .voucher-header .back-link {
                color: white;
                font-weight: 600;
                text-decoration: none;
                font-size: 16px;
                transition: all 0.3s;
            }

            .voucher-header .back-link:hover {
                color: #667eea;
            }

            .voucher-header .title {
                flex-grow: 1;
                text-align: center;
                font-weight: 700;
                font-size: 20px;
            }

            .voucher-container {
                max-width: 900px;
                background: white;
                margin: 30px auto;
                padding: 40px;
                border-radius: 15px;
                box-shadow: 0 10px 40px rgba(0,0,0,0.3);
            }

            .success-badge {
                background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
                color: white;
                padding: 15px 30px;
                border-radius: 50px;
                text-align: center;
                margin-bottom: 30px;
                font-size: 18px;
                font-weight: bold;
            }

            .success-badge i {
                margin-right: 10px;
            }

            .header-voucher {
                display: grid;
                grid-template-columns: 1fr auto 1fr;
                gap: 30px;
                margin-bottom: 40px;
                padding-bottom: 30px;
                border-bottom: 3px solid #667eea;
            }

            .order-info {
                display: flex;
                flex-direction: column;
                justify-content: center;
            }

            .order-info h6 {
                font-weight: 600;
                color: #666;
                margin-bottom: 5px;
                font-size: 14px;
            }

            .order-info .order-number {
                font-size: 28px;
                font-weight: 700;
                color: #667eea;
            }

            .qr-code {
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 15px;
                background: #f8f9fa;
                border-radius: 10px;
            }

            .qr-code img {
                max-width: 150px;
                height: auto;
            }

            .details-info {
                font-size: 14px;
                line-height: 1.8;
            }

            .details-info p {
                margin: 5px 0;
            }

            .details-info strong {
                color: #333;
                font-weight: 600;
            }

            .section-title {
                font-size: 18px;
                font-weight: 700;
                color: #1a1a2e;
                margin: 30px 0 15px 0;
                padding-bottom: 10px;
                border-bottom: 2px solid #e0e0e0;
            }

            .table {
                margin-bottom: 20px;
            }

            .table thead th {
                background-color: #f8f9fa;
                font-weight: 600;
                color: #333;
                border: none;
            }

            .table tbody td {
                vertical-align: middle;
            }

            .total-section {
                margin-top: 30px;
                padding: 20px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-radius: 10px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .total-section .total-item {
                text-align: center;
            }

            .total-section .total-label {
                font-size: 14px;
                opacity: 0.9;
                margin-bottom: 5px;
            }

            .total-section .total-amount {
                font-size: 24px;
                font-weight: bold;
            }

            .notes {
                margin-top: 30px;
                padding: 20px;
                background: #fff3cd;
                border-left: 4px solid #ffc107;
                border-radius: 5px;
                font-size: 13px;
                line-height: 1.7;
            }

            .notes p {
                margin-bottom: 10px;
            }

            .notes strong {
                color: #856404;
            }

            .footer-thanks {
                margin-top: 40px;
                text-align: center;
                font-size: 20px;
                font-weight: 700;
                color: #667eea;
            }

            .action-buttons {
                margin-top: 30px;
                display: flex;
                gap: 15px;
                justify-content: center;
            }

            .btn-custom {
                padding: 12px 30px;
                border-radius: 50px;
                font-weight: 600;
                transition: all 0.3s;
            }

            .btn-print {
                background: #667eea;
                color: white;
                border: none;
            }

            .btn-print:hover {
                background: #5568d3;
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            }

            .btn-home {
                background: white;
                color: #667eea;
                border: 2px solid #667eea;
            }

            .btn-home:hover {
                background: #667eea;
                color: white;
            }

            @media print {
                body {
                    background: white;
                    padding-top: 0;
                }
                .voucher-header,
                .action-buttons {
                    display: none;
                }
                .voucher-container {
                    box-shadow: none;
                    margin: 0;
                }
            }
        </style>
    </head>
    <body>

        <header class="voucher-header">
            <a href="<%= request.getContextPath()%>/DashboardServlet" class="back-link">
                <i class="fas fa-arrow-left"></i> Volver al Inicio
            </a>
            <div class="title">Voucher de Compra</div>
            <div></div>
        </header>

        <div class="voucher-container">

            <div class="success-badge">
                <i class="fas fa-check-circle"></i>
                ¡Compra Realizada con Éxito!
            </div>

            <div class="header-voucher">
                <div class="order-info">
                    <h6>N° de Orden:</h6>
                    <div class="order-number">#<%= String.format("%06d", venta.getIdVenta())%></div>
                    <small style="color: #999; margin-top: 5px;"><%= fechaVenta%></small>
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
            (d.getIdAsientoFuncion() != null && d.getIdAsientoFuncion().getAsiento() != null ? d.getIdAsientoFuncion().getAsiento().getCodigo() : "null"));
    }//--------------------
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
                    <tr style="background: #f8f9fa; font-weight: 600;">
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
                    <tr style="background: #f8f9fa; font-weight: 600;">
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
                <i class="fas fa-heart" style="color: #e74c3c;"></i>
                ¡Gracias por tu compra!
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

        <script>
                console.log('Voucher generado para venta ID:', <%= venta.getIdVenta()%>);
        </script>
    </body>
</html>