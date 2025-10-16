<%-- 
    Document   : Pelicula
    Created on : 27 may. 2025, 23:29:04
    Author     : Proyecto
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pelicula" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lista de Películas</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    </head>
    <body>

        <div class="container mt-5">
            <h3>Lista de Películas</h3>

            <!-- Botón para agregar nueva película -->
            <a href="PeliculaServlet?action=nuevo" class="btn btn-success mb-3">Agregar Película</a>

            <!-- Tabla de películas -->
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Sinopsis</th>
                        <th>Horario</th>
                        <th>Foto</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        // Obtener la lista de películas de la solicitud
                        List<Pelicula> listaPeliculas = (List<Pelicula>) request.getAttribute("listaPeliculas");

                        // Recorrer las películas y mostrarlas
                        for (Pelicula pelicula : listaPeliculas) {
                    %>
                    <tr>
                        <td><%= pelicula.getIdPelicula()%></td>
                        <td><%= pelicula.getNombre()%></td>
                        <td><%= pelicula.getSinopsis()%></td>
                        <td><%= pelicula.getFechaEstreno()%></td>
                        <td>
                            <%
                                byte[] foto = pelicula.getFoto();
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
                        <td>
                            <!-- Enlaces para editar y eliminar -->
                            <a href="PeliculaServlet?action=editar&id=<%= pelicula.getIdPelicula()%>" class="btn btn-primary btn-sm">Editar</a>
                            <a href="PeliculaServlet?action=eliminar&id=<%= pelicula.getIdPelicula()%>" class="btn btn-danger btn-sm"
                               onclick="return confirm('¿Está seguro de eliminar esta película?');">Eliminar</a>
                        </td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>

    </body>
</html>
