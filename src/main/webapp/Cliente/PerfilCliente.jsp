<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.Usuario" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }
    // Generar token CSRF si no existe en la sesión
    String csrfToken = (String) session.getAttribute("csrfToken");
    if (csrfToken == null) {
        csrfToken = java.util.UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);
    }

    String username = (String) session.getAttribute("username");
    String nombreCompleto = (String) session.getAttribute("nombreCompleto");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Perfil | CineMax</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
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

<!-- PERFIL -->
<div class="profile-container">
    <%
        String success = request.getParameter("success");
        if ("1".equals(success)) {
    %>
        <div class="alert alert-success text-center mb-4">Datos actualizados correctamente.</div>
    <%
        }
    %>

    <h2 class="text-center">Mi Perfil</h2>

    <h5 class="section-title">Datos de Socio CineMax</h5>
    <p>
        La información que te identifica como cliente de CineMax no puede ser editada.
        Si alguno de los datos no es correcto o deseas cambiarlo, escríbenos a 
        <a href="#">Contáctanos</a>.
    </p>

    <!-- DATOS DE SOCIO -->
    <div class="form-row">
        <div class="form-group col-md-6">
            <label>Nombre completo</label>
            <input type="text" class="form-control" value="<%= usuario.getNombreCompleto() %>" disabled>
        </div>
        <div class="form-group col-md-6">
            <label>Tipo de documento</label>
            <input type="text" class="form-control" value="DNI" disabled>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group col-md-6">
            <label>Número de documento</label>
            <input type="text" class="form-control" value="<%= usuario.getDni() %>" disabled>
        </div>
        <div class="form-group col-md-6">
            <label>Usuario</label>
            <input type="text" class="form-control" value="<%= usuario.getUsername() %>" disabled>
        </div>
    </div>

    <div class="divider"></div>

    <!-- DATOS DE CONTACTO -->
    <h5 class="section-title">Datos de Contacto</h5>
    <form action="<%= request.getContextPath() %>/ActualizarPerfilServlet" method="post">
        <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">
        <input type="hidden" name="idUsuario" value="<%= usuario.getIdUsuario() %>">

        <div class="form-row">
            <div class="form-group col-md-6">
                <label>Teléfono de contacto</label>
                <input type="text" class="form-control" name="telefono"
                       value="<%= usuario.getTelefono() != null ? usuario.getTelefono() : "" %>">
            </div>
            <div class="form-group col-md-6">
                <label>Correo electrónico</label>
                <input type="email" class="form-control" name="email" value="<%= usuario.getEmail() %>">
            </div>
        </div>

        <div class="form-group">
            <label>Dirección</label>
            <input type="text" class="form-control" name="direccion"
                   value="<%= usuario.getDireccion() != null ? usuario.getDireccion() : "" %>">
        </div>

        <div class="form-group form-check mt-2">
            <input type="checkbox" class="form-check-input" id="promos" checked>
            <label class="form-check-label" for="promos">
                Acepto recibir promociones y publicidad de CineMax mediante el uso de mis datos personales.
            </label>
        </div>

        <button type="submit" class="btn btn-orange mt-3">Guardar Datos</button>
    </form>

    <div class="divider"></div>

    <!-- CAMBIAR CONTRASEÑA -->
    <h5 class="section-title">Cambiar Contraseña</h5>
    <form action="<%= request.getContextPath() %>/CambiarPasswordServlet" method="post">
        <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">
        <input type="hidden" name="idUsuario" value="<%= usuario.getIdUsuario() %>">

        <div class="form-row">
            <div class="form-group col-md-6">
                <label>Nueva contraseña</label>
                <input type="password" name="nuevaPass" class="form-control" required minlength="6">
            </div>
            <div class="form-group col-md-6">
                <label>Confirmar contraseña</label>
                <input type="password" name="confirmPass" class="form-control" required minlength="6">
            </div>
        </div>

        <button type="submit" class="btn btn-outline-orange">Guardar Nueva Contraseña</button>
    </form>
</div>

<footer>
    © 2025 CineMax | Todos los derechos reservados
</footer>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
