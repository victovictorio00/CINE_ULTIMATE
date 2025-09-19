<%-- 
    Document   : Producto
    Created on : 26 may. 2025, 18:16:41
    Author     : Proyecto
--%>
<%@page import="java.util.Base64"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Producto" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <title>Lista de Productos</title>

        <!-- Bootstrap 4 CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />

        <style>
            body {
                padding: 2rem;
                background-color: #f8f9fa;
                font-family: Arial, sans-serif;
            }
            h3 {
                margin-bottom: 1.5rem;
            }
            .table-container {
                background: white;
                padding: 1.5rem;
                border-radius: 0.5rem;
                box-shadow: 0 0 12px rgb(0 0 0 / 0.1);
                max-width: 900px;
                margin: auto;
            }
            .btn-agregar {
                margin-bottom: 1rem;
            }
            .acciones a {
                margin-right: 10px;
            }
        </style>
    </head>
    <body>
        <div class="table-container">
            <h3 class="text-center">Lista de Productos</h3>

            <a href="ProductoServlet?action=nuevo" class="btn btn-primary btn-agregar">Agregar Producto</a>

            <table class="table table-striped table-bordered table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Precio</th>
                        <th>Descripción</th>
                        <th>Foto</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Producto> listaProductos = (List<Producto>) request.getAttribute("listaProductos");
                        if (listaProductos != null && !listaProductos.isEmpty()) {
                            for (Producto producto : listaProductos) {

                    %>
                    <tr>
                        <td><%= producto.getId()%></td>
                        <td><%= producto.getNombre()%></td>
                        <td>S/ <%= producto.getPrecio()%></td>
                        <td><%= producto.getDescripcion()%></td>
                        <td>
                            <%
                                byte[] foto = producto.getFoto();
                                if (foto != null) {
                                    String base64Image = java.util.Base64.getEncoder().encodeToString(foto);
                            %>
                            <img src="data:image/jpeg;base64,<%= base64Image%>" alt="Foto" style="width: 60px; height: auto;" />
                            <%
                            } else {
                            %>
                            Sin foto
                            <% }%>
                        </td>
                        <td class="acciones">
                            <a href="ProductoServlet?action=editar&id=<%= producto.getId()%>" class="btn btn-sm btn-info">Editar</a>
                            <a href="ProductoServlet?action=eliminar&id=<%= producto.getId()%>" class="btn btn-sm btn-danger" onclick="return confirm('¿Está seguro de eliminar este producto?');">Eliminar</a>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="5" class="text-center">No hay productos disponibles.</td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>

        <!-- Bootstrap JS y dependencias -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </body>
</html>