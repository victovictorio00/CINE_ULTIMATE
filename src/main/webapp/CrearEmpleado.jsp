<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Empleado"%>
<%@page import="java.sql.SQLException"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Empleado</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h3>Crear Nuevo Empleado</h3>
    <form action="EmpleadoServlet?action=insertar" method="post">
        <div class="form-group">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="direccion">Dirección:</label>
            <input type="text" id="direccion" name="direccion" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="telefono">Teléfono:</label>
            <input type="text" id="telefono" name="telefono" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="cargo">Cargo:</label>
            <input type="text" id="cargo" name="cargo" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="salario">Salario:</label>
            <input type="number" step="0.01" id="salario" name="salario" class="form-control" required>
        </div>

        <button type="submit" class="btn btn-primary">Crear</button>
        <a href="EmpleadoServlet?action=listar" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

</body>
</html>
