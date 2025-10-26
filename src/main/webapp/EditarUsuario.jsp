<%@page import="java.util.List"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.Rol"%>
<%@page import="modelo.EstadoUsuario"%>
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Usuario</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h3>Editar Usuario</h3>
    <%
        Usuario usuario = (Usuario) request.getAttribute("usuario");
        List<Rol> roles = (List<Rol>) request.getAttribute("roles");
        List<EstadoUsuario> estados = (List<EstadoUsuario>) request.getAttribute("estados");
    %>

    <form action="<%= request.getContextPath() %>/UsuarioServlet" method="post">
        <input type="hidden" name="action" value="actualizar">
        <input type="hidden" name="idUsuario" value="<%= usuario.getIdUsuario() %>">

        <div class="form-group">
            <label>Nombre Completo</label>
            <input type="text" name="nombreCompleto" class="form-control" value="<%= usuario.getNombreCompleto() %>" required>
        </div>

        <div class="form-group">
            <label>DNI</label>
            <input type="text" name="dni" class="form-control" value="<%= usuario.getDni() %>" required>
        </div>

        <div class="form-group">
            <label>Nombre de usuario</label>
            <input type="text" name="username" class="form-control" value="<%= usuario.getUsername() %>" required>
        </div>

        <div class="form-group">
            <label>Contraseña</label>
            <input type="password" name="password" class="form-control" placeholder="Dejar vacío si no desea cambiar">
        </div>

        <div class="form-group">
            <label>Teléfono</label>
            <input type="text" name="telefono" class="form-control" value="<%= usuario.getTelefono() %>">
        </div>

        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" class="form-control" value="<%= usuario.getEmail() %>">
        </div>

        <div class="form-group">
            <label>Dirección</label>
            <input type="text" name="direccion" class="form-control" value="<%= usuario.getDireccion() %>">
        </div>

        <div class="form-group">
            <label>Rol</label>
            <select name="idRol" class="form-control" required>
                <% for (Rol r : roles) { %>
                    <option value="<%= r.getIdRol() %>" <%= r.getIdRol() == usuario.getIdRol().getIdRol() ? "selected" : "" %>>
                        <%= r.getNombre() %>
                    </option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label>Estado</label>
            <select name="idEstadoUsuario" class="form-control" required>
                <% for (EstadoUsuario e : estados) { %>
                    <option value="<%= e.getIdEstadoUsuario() %>" <%= e.getIdEstadoUsuario() == usuario.getIdEstadoUsuario().getIdEstadoUsuario() ? "selected" : "" %>>
                        <%= e.getNombre() %>
                    </option>
                <% } %>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Actualizar Usuario</button>
        <a href="UsuarioServlet?action=listar" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
