<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, modelo.Venta, modelo.DetalleVenta" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Reservas</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">Mis Reservas</h2>
    <%
        List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
        if (ventas != null && !ventas.isEmpty()) {
    %>
    <table class="table table-bordered table-striped">
        <thead class="thead-dark">
            <tr>
                <th>Película</th>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Asientos</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
        <% for (Venta v : ventas) {
            List<DetalleVenta> detalles = v.getDetalles();
            for (DetalleVenta d : detalles) {
                if (d.getFuncion() != null) {
        %>
            <tr>
                <td><%= d.getFuncion().getPelicula().getNombre() %></td>
                <td><%= d.getFuncion().getFechaInicio() %></td>
                <td><%= d.getFuncion().getFechaFin() %></td>
                <td><%= d.getIdAsientoFuncion() != null && d.getIdAsientoFuncion().getAsiento() != null
                        ? d.getIdAsientoFuncion().getAsiento().getCodigo()
                        : "N/A" %></td>
                <td>S/. <%= String.format("%.2f", v.getTotal()) %></td>
            </tr>
        <%      } } } %>
        </tbody>
    </table>
    <% } else { %>
        <div class="alert alert-info text-center">No tienes reservas registradas.</div>
    <% } %>
</div>
</body>
</html>
