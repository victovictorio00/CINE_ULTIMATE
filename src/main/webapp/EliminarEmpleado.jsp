<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="modelo.EmpleadoDao" %>
<%@ page import="modelo.Empleado" %>
<%@ page import="java.sql.SQLException" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eliminar Empleado</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <h3>Eliminar Empleado</h3>

    <%
        // Crear la instancia del DAO
        EmpleadoDao empleadoDAO = new EmpleadoDao();

        // Obtener el id del empleado desde la solicitud
        String id = request.getParameter("id");
        Empleado empleado = empleadoDAO.leer(Integer.parseInt(id));  // Obtener el empleado por su id
    %>

    <p>¿Está seguro de eliminar al siguiente empleado?</p>
    <table class="table">
        <tr><td>ID:</td><td><%= empleado.getId() %></td></tr>
        <tr><td>Nombre:</td><td><%= empleado.getNombre() %></td></tr>
        <tr><td>Dirección:</td><td><%= empleado.getDireccion() %></td></tr>
        <tr><td>Teléfono:</td><td><%= empleado.getTelefono() %></td></tr>
    </table>

    <form action="EmpleadoServlet?action=eliminar" method="post">
        <input type="hidden" name="id" value="<%= empleado.getId() %>">
        <button type="submit" class="btn btn-danger">Eliminar</button>
        <a href="EmpleadoServlet?action=listar" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

</body>
</html>
