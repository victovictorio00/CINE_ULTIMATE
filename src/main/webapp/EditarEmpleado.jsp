<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Empleado"%>
<%@page import="java.sql.SQLException"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Empleado</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h3>Editar Empleado</h3>

    <%
        // Obtener el objeto empleado desde la solicitud
        Empleado empleado = (Empleado) request.getAttribute("empleado");
    %>

    <form action="EmpleadoServlet?action=actualizar" method="post">
        <input type="hidden" name="id" value="<%= empleado.getId() %>">
        <div class="form-group">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" class="form-control" value="<%= empleado.getNombre() %>" required>

            <label for="direccion">Dirección:</label>
            <input type="text" id="direccion" name="direccion" class="form-control" value="<%= empleado.getDireccion() %>" required>

            <label for="telefono">Teléfono:</label>
            <input type="text" id="telefono" name="telefono" class="form-control" value="<%= empleado.getTelefono() %>" required>

            <br>
            <button type="submit" class="btn btn-primary">Actualizar</button>
            <a href="EmpleadoServlet?action=listar" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>

</body>
</html>
