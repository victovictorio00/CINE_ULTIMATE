<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Empleado"%>
<%@page import="java.sql.SQLException"%>

<!-- ==========================================================
     Página: CrearEmpleado.jsp
     Descripción:
     Formulario para registrar un nuevo empleado dentro del 
     módulo de administración del sistema CineMax.
     Envía los datos al servlet EmpleadoServlet para ser procesados.
========================================================== -->

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Empleado</title>

    <!-- ==========================================================
         Librerías CSS de Bootstrap para estilos responsivos
    ========================================================== -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>

<body>
<div class="container mt-5">
    <!-- ==========================================================
         Título principal del formulario
    ========================================================== -->
    <h3>Crear Nuevo Empleado</h3>

    <!-- ==========================================================
         FORMULARIO DE REGISTRO
         - Método: POST
         - Acción: EmpleadoServlet con parámetro action=insertar
         El servlet procesará los datos y los guardará en la base de datos.
    ========================================================== -->
    <form action="EmpleadoServlet?action=insertar" method="post">

        <!-- Campo: Nombre del empleado -->
        <div class="form-group">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" class="form-control" required>
        </div>

        <!-- Campo: Dirección -->
        <div class="form-group">
            <label for="direccion">Dirección:</label>
            <input type="text" id="direccion" name="direccion" class="form-control" required>
        </div>

        <!-- Campo: Teléfono -->
        <div class="form-group">
            <label for="telefono">Teléfono:</label>
            <input type="text" id="telefono" name="telefono" class="form-control" required>
        </div>

        <!-- Campo: Cargo -->
        <div class="form-group">
            <label for="cargo">Cargo:</label>
            <input type="text" id="cargo" name="cargo" class="form-control" required>
        </div>

        <!-- Campo: Salario -->
        <div class="form-group">
            <label for="salario">Salario:</label>
            <input type="number" step="0.01" id="salario" name="salario" class="form-control" required>
        </div>

        <!-- ==========================================================
             BOTONES DE ACCIÓN
             - Crear: envía el formulario para registrar el empleado.
             - Cancelar: redirige al listado general.
        ========================================================== -->
        <button type="submit" class="btn btn-primary">Crear</button>
        <a href="EmpleadoServlet?action=listar" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

</body>
</html>
