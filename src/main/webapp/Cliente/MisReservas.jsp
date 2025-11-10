<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, modelo.Venta, modelo.DetalleVenta, modelo.Usuario" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }

    String username = (String) session.getAttribute("username");
    String nombreCompleto = (String) session.getAttribute("nombreCompleto");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Reservas | CineMax</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- Usa el mismo CSS del perfil -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosCliente/PerfilCliente.css">
</head>

<body>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-dark fixed-top">
    <a class="navbar-brand" href="<%= request.getContextPath()%>/DashboardServlet">CineMax</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mx-auto">
            <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/DashboardServlet">Inicio</a></li>
            <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/CarteleraServlet">Películas</a></li>
            <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/DulceriaServlet">Dulcería</a></li>
            <% if (username == null || username.isEmpty()) { %>
                <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/Login.jsp">Mi Cuenta</a></li>
            <% } else { %>
                <li class="nav-item dropdown active">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        Hola, <%= (nombreCompleto != null && !nombreCompleto.isEmpty()) ? nombreCompleto.split(" ")[0] : username%>
                    </a>
                    <div class="dropdown-menu bg-dark" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/ClienteServlet?action=misReservas">Mis Reservas</a>
                        <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/Cliente/PerfilCliente.jsp">Mi Perfil</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/LogoutServlet">Cerrar Sesión</a>
                    </div>
                </li>
            <% } %>
        </ul>
    </div>
</nav>

<!-- CONTENIDO -->
<div class="profile-container">
    <h2 class="text-center">Mis Reservas</h2>

    <%
        List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
        if (ventas != null && !ventas.isEmpty()) {
    %>

    <div class="table-responsive mt-4">
        <table class="table table-bordered table-striped text-center align-middle">
            <thead class="thead-dark">
                <tr>
                    <th>Película</th>
                    <th>Fecha</th>
                    <th>Hora</th>
                    <th>Asiento</th>
                    <th>Total</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <% for (Venta v : ventas) {
                    for (DetalleVenta d : v.getDetalles()) {
                        if (d.getFuncion() != null) {
                %>
                <tr>
                    <td><%= d.getFuncion().getPelicula().getNombre() %></td>
                    <td><%= d.getFuncion().getFechaInicio() %></td>
                    <td><%= d.getFuncion().getFechaFin() %></td>
                    <td><%= (d.getIdAsientoFuncion() != null && d.getIdAsientoFuncion().getAsiento() != null)
                            ? d.getIdAsientoFuncion().getAsiento().getCodigo() : "N/A" %></td>
                    <td>S/. <%= String.format("%.2f", v.getTotal()) %></td>
                    <td>
                        <button class="btn btn-orange btn-sm btn-ver" data-id="<%= v.getIdVenta() %>">
                            <i class="fas fa-eye"></i> Ver Voucher
                        </button>
                    </td>
                </tr>
                <% } } } %>
            </tbody>
        </table>
    </div>

    <!-- MODAL -->
    <div class="modal fade" id="voucherModal" tabindex="-1" role="dialog" aria-labelledby="voucherModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header bg-dark text-white">
            <h5 class="modal-title" id="voucherModalLabel">Voucher de Reserva</h5>
            <button type="button" class="close text-white" data-dismiss="modal" aria-label="Cerrar">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body" id="voucherContent">
            <div class="text-center text-muted">Cargando voucher...</div>
          </div>
        </div>
      </div>
    </div>

    <% } else { %>
        <div class="alert alert-info text-center mt-4">No tienes reservas registradas actualmente.</div>
    <% } %>
</div>

<footer>
    © 2025 CineMax | Todos los derechos reservados
</footer>

<!-- SCRIPTS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<script>
$(document).ready(function(){
    $(".btn-ver").click(function(){
        let idVenta = $(this).data("id");
        $("#voucherContent").html("<div class='text-center text-muted'>Cargando voucher...</div>");
        $("#voucherModal").modal("show");

        $.ajax({
            url: "VoucherServlet",
            type: "GET",
            data: { idVenta: idVenta },
            success: function(response) {
                $("#voucherContent").html(response);
            },
            error: function() {
                $("#voucherContent").html("<div class='text-danger text-center'>Error al cargar el voucher.</div>");
            }
        });
    });
});
</script>
</body>
</html>
