<%-- 
    Document   : Usuario
    Created on : 26 may. 2025, 17:21:56
    Author     : Proyecto
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="modelo.Usuario" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h3>Gestión de Usuarios</h3>
    
    <!-- Formulario para agregar nuevo usuario -->
        <form action="UsuarioServlet?action=insertar" method="post">
        <div class="form-group">
            <label for="nombre_completo">Nombre Completo:</label>
            <input type="text" id="nombre_completo" name="nombre_completo" class="form-control" required>
        </div>
        <div class="form-group">
            <label for="dni">DNI:</label>
            <input type="text" id="dni" name="dni" class="form-control" maxlength="8" required>
        </div>
        <div class="form-group">
            <label for="username">Nombre de Usuario:</label>
            <input type="text" id="username" name="username" class="form-control" required>
        </div>
        <div class="form-group">
            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" class="form-control" required>
        </div>
        <div class="form-group">
            <label for="rol">Rol:</label>
                <select name="id_rol">
                    <option value="1">Administrador</option>
                    <option value=" 2">Usuario</option>
                  </select>
        </div>
        <!-- Estado usuario por defecto -->
        <input type="hidden" name="id_estado_usuario" value="1">

        <div class="form-group">
            <label for="telefono">Teléfono:</label>
            <input type="text" id="telefono" name="telefono" class="form-control">
        </div>
        <div class="form-group">
            <label for="email">Correo:</label>
            <input type="email" id="email" name="email" class="form-control">
        </div>
        <div class="form-group">
            <label for="direccion">Dirección:</label>
            <input type="text" id="direccion" name="direccion" class="form-control">
        </div>
        <div class="form-group">
            <label for="id_estado_usuario">Estado:</label>
            <select name="id_estado_usuario" id="id_estado_usuario" class="form-control" required>
                <option value="1">Activo</option>
                <option value="2">Inactivo</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Agregar Usuario</button>
    </form>
    
    <!-- Mostrar lista de usuarios -->
    <h4 class="mt-4">Usuarios Registrados</h4>
    <table class="table table-striped">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre de Usuario</th>
                <th>Rol</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <%
            List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
                if (listaUsuarios != null && listaUsuarios.size() > 0) {
                    for (Usuario usuario : listaUsuarios) {
            %>
            <tr>
                <td><%= usuario.getIdUsuario() %></td>
                <td><%= usuario.getUsername() %></td>
                <td><%= usuario.getRol()%></td>
                <td>
                    <a href="UsuarioServlet?action=editar&id=<%= usuario.getIdUsuario() %>" 
                       class="btn btn-primary btn-sm">Editar</a>
                    <a href="UsuarioServlet?action=eliminar&id=<%= usuario.getIdUsuario() %>" 
                       class="btn btn-danger btn-sm" 
                       onclick="return confirm('¿Está seguro de eliminar este usuario?');">Eliminar</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="4">No hay usuarios disponibles.</td>
            </tr>
            <% 
                }
            %>
        </tbody>
    </table>
</div>
</body>
</html>
