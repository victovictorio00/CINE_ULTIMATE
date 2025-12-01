<%@page import="java.text.SimpleDateFormat"%>
<%@page import="modelo.FilaReservaDTO"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, modelo.Venta, modelo.DetalleVenta" %>
<%
    /* Recuperamos usuario y navbar igual que en Perfil */
    modelo.Usuario usuario = (modelo.Usuario) session.getAttribute("usuario");
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
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/Cliente/EstilosCliente/PerfilCliente.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
        
    </head>

    <body>
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
                        <% if (username == null || username.isEmpty()) {%>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/Login.jsp">Mi Cuenta</a></li>
                        <% } else {%>
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

        <div class="loco mt-5">
            <%
                Collection<FilaReservaDTO> filas = (Collection<FilaReservaDTO>) request.getAttribute("filas");
                if (filas == null) {
                    filas = Collections.emptyList();
                }
                List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
                if (ventas != null && !ventas.isEmpty()) {
            %>
            <h2 class="text-center"><i class="fas fa-ticket-alt"></i> Mis Reservas</h2>
            <div class="table-responsive mt-2 w-75 mx-auto">
                <table class="table table-dark table-hover">
                    <thead>
                        <tr>
                            <th><i class="fas fa-film"></i> Película</th>
                            <th><i class="fas fa-calendar-alt"></i> Función</th>
                            <th><i class="fas fa-clock"></i> Hora</th>
                            <th><i class="fas fa-couch"></i> Asientos</th>
                            <th><i class="fas fa-ticket-alt"></i> Costo Entradas</th>
                            <th><i class="fas fa-popcorn"></i> Productos (Cant.)</th>
                            <th><i class="fas fa-dollar-sign"></i> Total Reserva</th>
                            <th><i class="fas fa-eye"></i> Detalle</th>
                        </tr>
                    </thead>

                    <% for (FilaReservaDTO dto : filas) {%>
                    <tr>
                        <td><%= dto.getPelicula()%></td>
                        <td><%= dto.getFechaHora() != null ? new SimpleDateFormat("dd/MM/yyyy").format(dto.getFechaHora()) : "-"%></td>
                        <td><%= dto.getFechaHora() != null ? new SimpleDateFormat("HH:mm").format(dto.getFechaHora()) : "-"%></td>
                        <td><strong><%= dto.getRangoAsientos()%></strong></td>
                        <td>S/. <%= String.format("%.2f", dto.getTotalEntradas())%></td>
                        <td><%= dto.getCantidadProductos()%> u.</td>
                        <td>S/. <%= String.format("%.2f", (dto.getTotalEntradas() + dto.getTotalProductos()))%></td>
                        <td>
                            <a href="<%= request.getContextPath()%>/ClienteServlet?action=verVoucher&idVenta=<%= dto.getIdVenta()%>"
                               class="btn btn-orange btn-sm">
                                <i class="fas fa-eye"></i> Ver
                            </a>
                        </td>
                    </tr>
                    <% } %>

                </table>
            </div>

            <div class="modal fade" id="voucherModal" tabindex="-1" role="dialog" aria-labelledby="voucherModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="voucherModalLabel"><i class="fas fa-ticket-alt"></i> Voucher de Reserva</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body" id="voucherContent">
                            <div class="text-center"><i class="fas fa-spinner fa-spin"></i> Cargando voucher...</div>
                        </div>
                    </div>
                </div>
            </div>

            <% } else { %>
            <div class="alert alert-info text-center mt-5">
                <i class="fas fa-info-circle"></i> No tienes reservas registradas.
            </div>
            <% }%>
        </div>

        <footer>
            © 2025 CineMax | Todos los derechos reservados
        </footer>

        <script src="<%= request.getContextPath() %>/Cliente/lib/jquery/jquery-3.6.4.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/popper/popper.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>