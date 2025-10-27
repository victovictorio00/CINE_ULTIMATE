<%@ page import="java.util.*" %>
<%
    Map<String,Object> venta = (Map<String,Object>) request.getAttribute("venta");
    List<Map<String,Object>> asientos = (List<Map<String,Object>>) request.getAttribute("asientos");
    List<Map<String,Object>> productos = (List<Map<String,Object>>) request.getAttribute("productos");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Comprobante - CineOnline</title>
    <link href="<%= request.getContextPath()%>/Estilos/peliculaClienteStyle.css" rel="stylesheet">
    <style>
        .container { max-width: 900px; margin: 30px auto; }
        .card { background:#fff; padding:20px; border-radius:10px; box-shadow:0 6px 20px rgba(0,0,0,0.05); }
        .btn { background:#FF5733; color:#fff; padding:10px 14px; border:none; border-radius:6px; cursor:pointer; }
        table { width:100%; border-collapse:collapse; margin-top:10px; }
        th, td { padding:8px 6px; border-bottom:1px solid #eee; }
        th { text-align:left; }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <h2>Comprobante</h2>
        <p><strong>Comprobante #:</strong> <%= venta.get("idVenta") %></p>
        <p><strong>Fecha:</strong> <%= venta.get("fecha") %></p>
        <p><strong>Cliente:</strong> <%= venta.get("cliente") %></p>

        <h3>Butacas</h3>
        <table>
            <tr><th>Código</th><th style="text-align:right">Precio</th></tr>
            <%
                if (asientos != null) {
                    for (Map<String,Object> a : asientos) {
            %>
            <tr>
                <td><%= a.get("codigo") %></td>
                <td style="text-align:right">S/. <%= String.format("%.2f", a.get("precio")) %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr><td colspan="2">No hay asientos</td></tr>
            <% } %>
        </table>

        <h3>Productos</h3>
        <table>
            <tr><th>Producto</th><th style="text-align:right">Cantidad</th><th style="text-align:right">Subtotal</th></tr>
            <%
                if (productos != null) {
                    for (Map<String,Object> p : productos) {
            %>
            <tr>
                <td><%= p.get("nombre") %></td>
                <td style="text-align:right"><%= p.get("cantidad") %></td>
                <td style="text-align:right">S/. <%= String.format("%.2f", p.get("subtotal")) %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr><td colspan="3">No hay productos</td></tr>
            <% } %>
        </table>

        <h3 style="margin-top:12px;">Total: S/. <%= String.format("%.2f", venta.get("total")) %></h3>

        <a class="btn" href="<%= request.getContextPath()%>/DashboardServlet">Volver al inicio</a>
    </div>
</div>
</body>
</html>
