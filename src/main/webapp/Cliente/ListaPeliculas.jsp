<%-- 
    Document   : ListaPeliculas
    Created on : 28 may. 2025, 3:47:12
    Author     : Proyecto
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                <th>Nombre</th>
                <th>Sinopsis</th>
                <th>Horario</th>
                <th>Foto</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="pelicula" items="${peliculas}">
                <tr>
                    <td>${pelicula.nombre}</td>
                    <td>${pelicula.sinopsis}</td>
                    <td>${pelicula.horario}</td>
                    <td>${pelicula.foto}</td>
                    <td>
                        <a href="ClienteServlet?action=seleccionar&id=${pelicula.id}" class="btn btn-primary btn-sm">Seleccionar</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
