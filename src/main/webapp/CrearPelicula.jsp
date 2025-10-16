<%-- 
    Document   : CrearPelicula
    Created on : 27 may. 2025, 23:27:28
    Author     : Proyecto
--%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Genero" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Película</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>

<%
    List<Genero> generos = (List<Genero>) request.getAttribute("listaGeneros");
%>

<div class="container mt-5">
    <h3>Agregar Nueva Película</h3>

    <!-- Formulario para agregar una nueva película -->
    <form action="PeliculaServlet?action=insertar" method="POST" enctype="multipart/form-data">
        
        <!-- Nombre -->
        <div class="form-group">
            <label for="nombre">Nombre de la Película:</label>
            <input type="text" name="nombre" id="nombre" class="form-control" required>
        </div>

        <!-- Sinopsis -->
        <div class="form-group">
            <label for="sinopsis">Sinopsis:</label>
            <textarea name="sinopsis" id="sinopsis" class="form-control" rows="4" required></textarea>
        </div>

        <!-- Género -->
        <div class="form-group">
            <label for="idGenero">Género:</label>
            <select name="idGenero" id="idGenero" class="form-control" required>
                <option value="">-- Selecciona género --</option>
                <% if (generos != null) {
                       for (Genero g : generos) { %>
                           <option value="<%= g.getIdGenero() %>"><%= g.getNombre() %></option>
                <%     }
                   } %>
            </select>
        </div>

        <!-- Fecha de Estreno -->
        <div class="form-group">
            <label for="fechaEstreno">Fecha de Estreno:</label>
            <input type="date" name="fechaEstreno" id="fechaEstreno" class="form-control" required>
        </div>

        <!-- Precio -->
        <div class="form-group">
            <label for="precio">Precio:</label>
            <input type="number" step="0.01" name="precio" id="precio" class="form-control" required>
        </div>

        <!-- Foto -->
        <div class="form-group">
            <label for="foto">Foto de Película:</label>
            <input type="file" class="form-control-file" name="foto" id="foto" accept="image/*" required>
        </div>

        <button type="submit" class="btn btn-primary">Agregar Película</button>
        <a href="PeliculaServlet?action=listar" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

</body>
</html>
