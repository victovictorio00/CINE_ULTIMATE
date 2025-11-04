<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ page import="modelo.Usuario" %>
<%@ page import="modelo.Rol" %>
<%@ page import="modelo.EstadoUsuario" %>
<%@ page import="java.util.List" %>

    <!DOCTYPE html>
    <html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Crear Usuario - Panel Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
    <div class="container mt-5">
        <h3>Crear Usuario - Panel Administrador</h3>

        <!-- Mostrar mensaje de error si existe -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/UsuarioServlet?action=crearUsuarioAdminPanel" method="post">
            <div class="mb-3">
                <label for="nombre_completo" class="form-label">Nombre Completo:</label>
                <input type="text" id="nombre_completo" name="nombreCompleto" class="form-control" required>
            </div>

            <div class="mb-3">
                <label for="dni" class="form-label">DNI:</label>
                <input type="text" id="dni" name="dni" class="form-control" maxlength="8" required>
            </div>

            <div class="mb-3">
                <label for="username" class="form-label">Nombre de Usuario:</label>
                <input type="text" id="username" name="username" class="form-control" required>
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">Contraseña:</label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>

            <div class="mb-3">
                <label for="telefono" class="form-label">Teléfono:</label>
                <input type="text" id="telefono" name="telefono" class="form-control">
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Correo:</label>
                <input type="email" id="email" name="email" class="form-control">
            </div>

            <div class="mb-3">
                <label for="direccion" class="form-label">Dirección:</label>
                <input type="text" id="direccion" name="direccion" class="form-control">
            </div>

            <button type="submit" class="btn btn-success">Crear Usuario</button>
            <a href="<%= request.getContextPath() %>/UsuarioServlet?action=listar" class="btn btn-secondary">Cancelar</a>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    </html>
