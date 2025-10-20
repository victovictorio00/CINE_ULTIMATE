<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="modelo.FuncionDao"%>
<%@page import="modelo.Funcion"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.Pelicula" %>
<%@ page import="modelo.PeliculaDao" %>

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
        .navbar {
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 10;
            background-color: rgba(0, 0, 0, 0.85);
            border-bottom: 1px solid #fff;
            height: 70px;
        }
        .navbar-brand { font-weight: bold; font-size: 1.4rem; }
        .movie-video {
            width: 100%;
            height: 60vh;
            overflow: hidden;
            margin-top: 70px;
        }
        .movie-video iframe {
            width: 100%;
            height: 100%;
            border: none;
            object-fit: cover;
        }
        .movie-details-container {
            margin-top: 60px;
            margin-bottom: 80px;
        }
        .movie-poster img.poster-img {
            max-width: 100%;
            height: auto;
            border-radius: 12px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.3);
            transition: transform 0.3s ease;
        }
        .movie-poster img.poster-img:hover { transform: scale(1.03); }
        .movie-details {
            background: #fff;
            border-radius: 12px;
            padding: 30px 40px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }
        .movie-details h1 { font-size: 2rem; font-weight: 600; }
        .movie-details h3 { margin-top: 20px; color: #333; }
        .movie-details p { text-align: justify; color: #555; }
        .btn-primary, .btn-dark { margin-right: 10px; margin-top: 10px; border-radius: 8px; }
        footer {
            background-color: #212529;
            color: white;
            text-align: center;
            padding: 15px;
            width: 100%;
        }
        footer a { color: #bbb; text-decoration: none; }
        footer a:hover { color: white; }
        @media (max-width: 768px) {
            .movie-details { margin-top: 30px; text-align: center; }
        }
    </style>
</head>

<body>

<%
    String peliculaId = request.getParameter("id");
    int id = Integer.parseInt(peliculaId);

    PeliculaDao dao = new PeliculaDao();
    Pelicula pelicula = dao.leer(id);
    
    FuncionDao dao2 = new FuncionDao();
    List<Funcion> funciones = new ArrayList<>();
    funciones = dao2.obtenerFunciones(id);

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
<%
    String trailer = pelicula.getTrailerUrl();
    if (trailer != null && !trailer.trim().isEmpty()) {
        String videoId = "";
        if (trailer.contains("watch?v=")) {
            videoId = trailer.substring(trailer.indexOf("watch?v=") + 8);
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
        } else {
            videoId = trailer;
        }
        trailer = "https://www.youtube.com/embed/" + videoId;
    } else {
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

<!-- DETALLES -->
<div class="container movie-details-container">
    <div class="row d-flex align-items-center justify-content-center">
        
        <!-- Imagen -->
        <div class="col-md-5 movie-poster text-center">
            <img 
                src="<%= (pelicula.getFoto() != null && pelicula.getFoto().length > 0)
                    ? (request.getContextPath() + "/ImageServlet?id=" + pelicula.getIdPelicula() + "&t=" + System.currentTimeMillis())
                    : (request.getContextPath() + "/Cliente/images/pelicula6.jpg") %>" 
                alt="Póster de <%= pelicula.getNombre() %>" 
                class="img-fluid poster-img">
        </div>

        <!-- Texto -->
        <div class="col-md-7 movie-details">
            <h1><%= pelicula.getNombre() %></h1>
            <h5 class="text-muted mb-3"><%= pelicula.getIdGenero().getNombre()%></h5>

            <h3>Sinopsis</h3>
            <p><%= pelicula.getSinopsis() %></p>

            <h3>Horarios</h3>
            <%
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
                if (funciones == null || funciones.isEmpty()) {
            %>
                <div class="alert alert-info col-12">
                    No hay funciones disponibles
                </div>
            <%
                } else {
                    for (modelo.Funcion funcion : funciones) {
                        String horarioInicio = sdf.format(funcion.getFechaInicio());
                        String horarioFin = sdf.format(funcion.getFechaFin());
            %>
                <button class="btn btn-primary"><%= horarioInicio %> - <%= horarioFin %></button>
            <%
                    }
                }
            %>

            <a href="<%= request.getContextPath() %>/ClienteServlet?action=reservar&id=<%= pelicula.getIdPelicula() %>" 
               class="btn btn-dark mt-2">🎟 Reservar</a>
        </div>
    </div>
</div>

<!-- FOOTER -->
<footer>
    <p>© 2025 Cine Online | Todos los derechos reservados</p>
    <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
</footer>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>
