<%-- 
    Document   : CrearProducto
    Created on : 26 may. 2025, 18:06:04
    Author     : Proyecto
--%>
<%@ page import="modelo.Producto" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Crear Producto</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
            padding: 2rem;
        }
        .form-container {
            max-width: 600px;
            margin: auto;
            background: white;
            padding: 2rem;
            border-radius: 0.5rem;
            box-shadow: 0 0 12px rgb(0 0 0 / 0.1);
        }
        h3 {
            margin-bottom: 1.5rem;
            text-align: center;
        }
    </style>
    <script>
        // Validación de tamaño de imagen antes de enviar
        function validarFormulario() {
            const inputFoto = document.getElementById("foto");
            const archivo = inputFoto.files[0];
            const maxSize = 1024 * 1024; // 1 MB

            if (archivo && archivo.size > maxSize) {
                alert("La imagen es demasiado grande. El tamaño máximo permitido es 1 MB.");
                inputFoto.value = "";
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <div class="form-container">
        <h3>Agregar Producto</h3>
        <form action="ProductoServlet?action=insertar" method="POST" enctype="multipart/form-data" onsubmit="return validarFormulario()">
            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <input type="text" class="form-control" name="nombre" id="nombre" required />
            </div>
            <div class="form-group">
                <label for="precio">Precio:</label>
                <input type="text" class="form-control" name="precio" id="precio" required />
            </div>
            <div class="form-group">
                <label for="descripcion">Descripción:</label>
                <textarea class="form-control" name="descripcion" id="descripcion" rows="4" required></textarea>
            </div>
            <div class="form-group">
                <label for="foto">Foto del producto</label>
                <input type="file" class="form-control-file" name="foto" id="foto" accept="image/*" required>
                <small class="form-text text-muted">Tamaño máximo permitido: 1 MB</small>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Agregar Producto</button>
        </form>
    </div>
</body>
</html>
