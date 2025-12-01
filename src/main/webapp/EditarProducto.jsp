<%-- 
    Document   : EditarProducto
    Created on : 26 may. 2025, 18:08:19
    Author     : Proyecto
--%>
<%@ page import="modelo.Producto" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Editar Producto</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosAdmin/EditarProducto.css">
</head>
<body>
    <div class="form-container">
        <h3>Editar Producto</h3>
        <form action="ProductoServlet?action=actualizar" method="POST"  enctype="multipart/form-data">
            <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">
            <input type="hidden" name="id" value="${producto.idProducto}" />
            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <input type="text" class="form-control" name="nombre" id="nombre" value="${producto.nombre}" required />
            </div>
            <div class="form-group">
                <label for="precio">Precio:</label>
                <input type="text" class="form-control" name="precio" id="precio" value="${producto.precio}" required />
            </div>
            <div class="form-group">
                <label for="descripcion">Descripción:</label>
                <textarea class="form-control" name="descripcion" id="descripcion" rows="4" required>${producto.descripcion}</textarea>
            </div>
            <div class="form-group">
                    <label for="foto">Foto del producto</label>
                    <input type="file" class="form-control-file" name="foto" id="foto" accept="image/*" required>
                </div>
            <button type="submit" class="btn btn-primary btn-block">Actualizar Producto</button>
        </form>
    </div>
</body>
</html>
