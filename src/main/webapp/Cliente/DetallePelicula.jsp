<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.Cliente.Pelicula" %>
<%@ page import="modelo.Cliente.PeliculaDaoCliente" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle de Película | CineOnline</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f7f7f7;
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
        }

        /* === NAVBAR === */
        .navbar {
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 10;
            background-color: rgba(0, 0, 0, 0.85);
            border-bottom: 1px solid #fff;
            height: 70px;
        }

        .navbar-brand {
            font-weight: bold;
            font-size: 1.4rem;
        }

        /* === VIDEO === */
        .movie-video {
            width: 100%;
            height: 60vh;
            overflow: hidden;
            margin-top: 70px; /* evita solaparse con el navbar */
        }

        .movie-video iframe {
            width: 100%;
            height: 100%;
            border: none;
            object-fit: cover;
        }

        /* === DETALLES === */
        .movie-details-container {
            margin-top: 40px;
            margin-bottom: 80px;
        }

        .movie-details {
            background: white;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            padding: 30px;
        }

        .movie-details h1 {
            font-size: 2rem;
            font-weight: 600;
        }

        .movie-details h3 {
            margin-top: 20px;
            color: #333;
        }

        .movie-details p {
            text-align: justify;
            color: #555;
        }

        .movie-poster {
            text-align: center;
        }

        .movie-poster img {
            width: 80%;
            border-radius: 10px;
            box-shadow: 0 0 25px rgba(0,0,0,0.3);
        }

        /* === BOTONES === */
        .btn-primary, .btn-dark {
            margin-right: 10px;
            margin-top: 10px;
            border-radius: 8px;
        }

        /* === FOOTER === */
        footer {
            background-color: #212529;
            color: white;
            text-align: center;
            padding: 15px;
            width: 100%;
        }

        footer a {
            color: #bbb;
            text-decoration: none;
        }

        footer a:hover {
            color: white;
        }
    </style>
</head>

<body>

<%
    String peliculaId = request.getParameter("id");
    int id = Integer.parseInt(peliculaId);

    PeliculaDaoCliente dao = new PeliculaDaoCliente();
    Pelicula pelicula = dao.getPeliculaById(id);

    if (pelicula == null) {
        out.println("<div class='container mt-5'><h2>Película no encontrada.</h2></div>");
        return;
    }
%>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/ClienteServlet?action=listar">🎬 CineOnline</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item"><a class="nav-link" href="#">Inicio</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Películas</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Dulcería</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Mi Cuenta</a></li>
            </ul>
        </div>
    </div>
</nav>

<!-- VIDEO TRAILER -->
<div class="movie-video">
    <%
    String trailer = pelicula.getTrailerUrl();

    // Si el trailer no es nulo, convertir enlace normal de YouTube a formato embed
    if (trailer != null && trailer.contains("watch?v=")) {
        trailer = trailer.replace("watch?v=", "embed/");
    } else if (trailer == null || trailer.trim().isEmpty()) {
        // Si no tiene trailer, usar uno por defecto
        trailer = "https://www.youtube.com/embed/HeTE7j9dcGg";
    }
%>

<div class="movie-video">
    <iframe src="<%= trailer %>" 
            frameborder="0" 
            allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" 
            allowfullscreen>
    </iframe>
</div>

</div>

<!-- DETALLES -->
<div class="container movie-details-container">
    <div class="row align-items-center">
        <div class="col-md-6 movie-poster">
            <img src="<%= pelicula.getFoto() != null && !pelicula.getFoto().isEmpty() 
                ? pelicula.getFoto() 
                : request.getContextPath() + "/Cliente/images/pelicula6.jpg" %>" 
                alt="<%= pelicula.getNombre() %>">
        </div>
        <div class="col-md-6 movie-details">
            <h1><%= pelicula.getNombre() %></h1>
            <h5 class="text-muted"><%= pelicula.getGenero() %></h5>
            <h3>Sinopsis</h3>
            <p><%= pelicula.getSinopsis() %></p>

            <h3>Horarios</h3>
            <button class="btn btn-primary">09:30 PM</button>
            <button class="btn btn-primary">12:00 PM</button>
            <button class="btn btn-primary">02:30 PM</button>

            <a href="<%= request.getContextPath() %>/ClienteServlet?action=reservar&id=<%= pelicula.getIdPelicula() %>" 
               class="btn btn-dark mt-3">🎟 Reservar</a>
        </div>
    </div>
</div>

<!-- FOOTER -->
<footer>
    <p>© 2025 Cine Online | Todos los derechos reservados</p>
    <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
</footer>

<!-- Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>
