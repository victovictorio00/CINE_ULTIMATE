<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="modelo.Pelicula" %>
<%@ page import="modelo.Genero" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Película</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>

<%
    Pelicula peli = (Pelicula) request.getAttribute("pelicula");
    List<Genero> generos = (List<Genero>) request.getAttribute("listaGeneros");
    String fechaFormateada = "";
    if (peli.getFechaEstreno() != null) {
        fechaFormateada = new SimpleDateFormat("yyyy-MM-dd").format(peli.getFechaEstreno());
    }
%>

<div class="container mt-5">
    <h3 class="mb-4">Editar Película</h3>

    <!-- Formulario para editar una película -->
    <form action="PeliculaServlet?action=actualizar" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="id" value="<%= peli.getIdPelicula() %>">  

        <!-- Nombre -->
        <div class="form-group">
            <label for="nombre">Nombre de la Película:</label>
            <input type="text" name="nombre" id="nombre" class="form-control" 
                   value="<%= peli.getNombre() %>" required>
        </div>

        <!-- Sinopsis -->
        <div class="form-group">
            <label for="sinopsis">Sinopsis:</label>
            <textarea name="sinopsis" id="sinopsis" class="form-control" rows="4" required><%= peli.getSinopsis() %></textarea>
        </div>

        <!-- Género -->
        <div class="form-group">
            <label for="idGenero">Género:</label>
            <select name="idGenero" id="idGenero" class="form-control" required>
                <% for (Genero g : generos) {
                       String selected = (peli.getIdGenero() != null && peli.getIdGenero().getIdGenero() == g.getIdGenero()) ? "selected" : "";
                %>
                    <option value="<%= g.getIdGenero() %>" <%= selected %>><%= g.getNombre() %></option>
                <% } %>
            </select>
        </div>

        <!-- Fecha de estreno -->
        <div class="form-group">
            <label for="fechaEstreno">Fecha de Estreno:</label>
            <input type="date" name="fechaEstreno" id="fechaEstreno" 
                   class="form-control" value="<%= fechaFormateada %>" required>
        </div>

        <!-- Precio -->
        <div class="form-group">
            <label for="precio">Precio:</label>
            <input type="number" step="0.01" name="precio" id="precio" 
                   class="form-control" value="<%= peli.getPrecio() %>" required>
        </div>

        <!-- Tráiler -->
        <div class="form-group">
            <label for="trailerUrl">Enlace del Tráiler (YouTube, Vimeo, etc.):</label>
            <input type="url" name="trailerUrl" id="trailerUrl" 
                   class="form-control" placeholder="https://www.youtube.com/watch?v=..." 
                   value="<%= peli.getTrailerUrl() != null ? peli.getTrailerUrl() : "" %>">
            <small class="form-text text-muted">
                Si ya tiene un tráiler, puedes reemplazar el enlace o dejarlo igual.
            </small>
            <% if (peli.getTrailerUrl() != null && !peli.getTrailerUrl().isEmpty()) { %>
                <div class="mt-3">
                    <label>Vista previa:</label><br>
                    <iframe width="400" height="225"
                        src="<%= peli.getTrailerUrl().replace("watch?v=", "embed/") %>"
                        frameborder="0" allowfullscreen>
                    </iframe>
                </div>
            <% } %>
        </div>

        <!-- Foto -->
        <div class="form-group">
            <label for="foto">Foto actual:</label><br>
            <!-- 👇 Cambio aquí: se agrega un parámetro dinámico para evitar caché -->
            <img src="ImageServlet?id=<%= peli.getIdPelicula() %>&t=<%= System.currentTimeMillis() %>" 
                 width="150" class="mb-3"><br>
            <label for="foto">Cambiar foto (opcional):</label>
            <input type="file" class="form-control-file" name="foto" id="foto" accept="image/*">
            <small class="form-text text-muted">
                Si no seleccionas ninguna, se mantendrá la foto actual.
            </small>
        </div>

        <!-- Botones -->
        <button type="submit" class="btn btn-primary">Actualizar Película</button>
        <a href="PeliculaServlet?action=listar" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

</body>
</html>
