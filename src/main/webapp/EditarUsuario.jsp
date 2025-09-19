<%-- 
    Document   : EditarUsuario
    Created on : 26 may. 2025, 17:54:34
    Author     : Proyecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Usuario"%>
<%@page import="java.sql.SQLException"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Usuario</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h3>Editar Usuario</h3>

        <% 
            // Obtener el objeto usuario desde la solicitud
            Usuario usuario = (Usuario) request.getAttribute("usuario");
        %>

        <form action="UsuarioServlet?action=actualizar" method="post">
            <input type="hidden" name="id" value="<%= usuario.getId() %>">
            <div class="form-group">
                <label for="username">Nombre de Usuario:</label>
                <input type="text" id="username" name="username" class="form-control" value="<%= usuario.getUsername() %>" required>

                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" class="form-control" value="<%= usuario.getPassword() %>" required>

                <label for="rol">Rol:</label>
                <select id="rol" name="rol" class="form-control" required>
                    <option value="admin" <%= usuario.getRol().equals("admin") ? "selected" : "" %>>Administrador</option>
                    <option value="cliente" <%= usuario.getRol().equals("cliente") ? "selected" : "" %>>Cliente</option>
                </select>

                <br>
                <button type="submit" class="btn btn-primary">Actualizar</button>
                <a href="UsuarioServlet?action=listar" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>
