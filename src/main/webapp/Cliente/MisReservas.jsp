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
                <th>Acciones</th>
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
                <td>
                   <a href="<%= request.getContextPath() %>/ClienteServlet?action=verVoucher&idVenta=<%= v.getIdVenta() %>" 
                     class="btn btn-info btn-sm">Ver</a>

                </td>
            </tr>
        <%      } } } %>
        </tbody>
    </table>

    <!-- Modal para mostrar el voucher -->
    <div class="modal fade" id="voucherModal" tabindex="-1" role="dialog" aria-labelledby="voucherModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="voucherModalLabel">Voucher de Reserva</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body" id="voucherContent">
            Cargando voucher...
          </div>
        </div>
      </div>
    </div>

    <% } else { %>
        <div class="alert alert-info text-center">No tienes reservas registradas.</div>
    <% } %>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
    $(".btn-ver").click(function(){
        let idVenta = $(this).data("id");

        $("#voucherContent").html("Cargando voucher...");
        $("#voucherModal").modal("show");

        $.ajax({
            url: "VoucherServlet",
            type: "GET",
            data: { idVenta: idVenta },
            success: function(response) {
                $("#voucherContent").html(response);
            },
            error: function() {
                $("#voucherContent").html("<div class='text-danger'>Error al cargar el voucher.</div>");
            }
        });
    });
});
</script>
</body>
</html>
