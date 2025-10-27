<%@ page import="modelo.ReservaTemporal" %>
<%@ page import="java.util.*" %>
<%
    ReservaTemporal rt = (ReservaTemporal) session.getAttribute("reservaTemporal");
    List<String> butacas = (List<String>) session.getAttribute("butacasSeleccionadas");
    Map<Integer,Integer> productos = (Map<Integer,Integer>) session.getAttribute("productosSeleccionados");
    Map<String,String> cliente = (Map<String,String>) session.getAttribute("clienteDatos");
    String metodo = (String) session.getAttribute("metodoPago");
    Double totalEntradas = null;
    Object tObj = session.getAttribute("totalEntradas");
    if (tObj != null) {
        try { totalEntradas = Double.parseDouble(tObj.toString()); } catch (Exception ex) {}
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Confirmación de compra</title>
    <link href="<%= request.getContextPath()%>/Estilos/peliculaClienteStyle.css" rel="stylesheet">
    <style>
        .container { max-width: 900px; margin: 30px auto; }
        .card { background:#fff; padding:20px; border-radius:10px; box-shadow:0 6px 20px rgba(0,0,0,0.05); }
        .grid { display:flex; gap:20px; }
        .col { flex:1; }
        .btn { background:#FF5733; color:#fff; padding:12px; border:none; border-radius:8px; cursor:pointer; }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <h2>Resumen de la compra</h2>
        <div class="grid" style="margin-top:16px;">
            <div class="col">
                <h4>Función</h4>
                <p><strong>Película:</strong> <%= (rt!=null? rt.getIdPelicula() : "n/d") %></p>
                <p><strong>Función:</strong> <%= (rt!=null? rt.getFecha() : "n/d") %></p>
                <p><strong>Sala:</strong> <%= (rt!=null? rt.getIdSala() : "n/d") %></p>

                <h4 style="margin-top:12px;">Butacas</h4>
                <p><%= (butacas!=null && !butacas.isEmpty() ? String.join(", ", butacas) : "Ninguna") %></p>
            </div>

            <div class="col">
                <h4>Dulcería</h4>
                <%
                    if (productos != null && !productos.isEmpty()) {
                %>
                <table style="width:100%; border-collapse:collapse;">
                    <tr>
                        <th style="text-align:left">Producto</th>
                        <th style="text-align:right">Cantidad</th>
                    </tr>
                    <%
                        for (Map.Entry<Integer,Integer> e : productos.entrySet()) {
                    %>
                    <tr>
                        <td>Producto #<%= e.getKey() %></td>
                        <td style="text-align:right;"><%= e.getValue() %></td>
                    </tr>
                    <%
                        }
                    %>
                </table>
                <%
                    } else {
                %>
                <p>Ningún producto</p>
                <%
                    }
                %>

                <h4 style="margin-top:12px;">Pago</h4>
                <p><strong>Método:</strong> <%= (metodo!=null? metodo : "n/d") %></p>
                <p><strong>Cliente:</strong> <%= (cliente!=null? cliente.getOrDefault("nombre","") : "Invitado") %></p>
            </div>
        </div>

        <div style="margin-top:16px;">
            <p><strong>Total entradas:</strong> S/. <%= (totalEntradas!=null? String.format("%.2f", totalEntradas) : "0.00") %></p>
        </div>

        <form method="post" action="<%= request.getContextPath()%>/ClienteServlet?action=confirmarCompra" onsubmit="this.querySelector('button').disabled=true;">
            <button class="btn" type="submit">Confirmar compra</button>
            <a style="margin-left:12px;" href="<%= request.getContextPath()%>/Cliente/MetodoPago.jsp">Volver (editar pago)</a>
        </form>
    </div>
</div>
</body>
</html>
