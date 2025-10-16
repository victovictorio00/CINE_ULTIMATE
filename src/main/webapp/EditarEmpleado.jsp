<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Empleado"%>

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
        Empleado empleado = (Empleado) request.getAttribute("empleado");
    %>

    <form action="EmpleadoServlet?action=actualizar" method="post">
        <!-- Corregido: usar getIdEmpleado() -->
        <input type="hidden" name="id" value="<%= empleado.getIdEmpleado() %>">

        <div class="form-group">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" class="form-control" value="<%= empleado.getNombre() %>" required>
        </div>

        <div class="form-group">
            <label for="direccion">Dirección:</label>
            <input type="text" id="direccion" name="direccion" class="form-control" value="<%= empleado.getDireccion() %>" required>
        </div>

        <div class="form-group">
            <label for="telefono">Teléfono:</label>
            <input type="text" id="telefono" name="telefono" class="form-control" value="<%= empleado.getTelefono() %>" required>
        </div>

        <div class="form-group">
            <label for="cargo">Cargo:</label>
            <input type="text" id="cargo" name="cargo" class="form-control" value="<%= empleado.getCargo() %>" required>
        </div>

        <div class="form-group">
            <label for="salario">Salario:</label>
            <input type="number" step="0.01" id="salario" name="salario" class="form-control" value="<%= empleado.getSalario() %>" required>
        </div>

        <button type="submit" class="btn btn-primary">Actualizar</button>
        <a href="EmpleadoServlet?action=listar" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

</body>
</html>
