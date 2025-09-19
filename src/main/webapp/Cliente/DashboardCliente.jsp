<%-- 
    Document   : DashboardCliente
    Created on : 28 may. 2025, 11:38:38
    Author     : Proyecto
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Cliente.Pelicula" %>
<%@ page import="javax.servlet.RequestDispatcher" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Cliente</title>
    <!-- Vinculamos Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Slider */
        .carousel-item img {
            width: 100%;
            height: 100vh;
            max-height: 700px;
            object-fit: cover;
        }

        /* Cabecera */
        .navbar {
            position: absolute;
            top: 0;
            width: 100%;
            z-index: 1;
            background-color: rgba(0, 0, 0, 0.3); /* Sombra más suave */
            border-bottom: 1px solid white; /* Línea blanca debajo de la cabecera */
            height: 80px;
            padding-top: 10px;
        }

        .navbar-brand, .nav-link {
            color: white !important;
        }

        .navbar-nav {
            width: 100%;
            display: flex;
            justify-content: center; /* Centra los elementos de la cabecera */
        }

        .nav-item {
            margin: 0 15px; /* Espaciado entre los items */
            position: relative; /* Necesario para la línea roja */
        }

        /* Línea roja al hacer clic en el item de la cabecera */
        .nav-link:hover {
            color: #FF5733 !important; /* Color del texto al pasar el mouse */
        }

        .nav-link:active {
            color: #FF5733 !important; /* Color del texto al hacer clic */
        }

        .nav-item.active::after {
            content: '';
            position: absolute;
            width: 100%;
            height: 3px;
            background-color: #FF5733; /* Línea roja */
            bottom: 0;
            left: 0;
        }

        /* Películas */
        .pelicula-card {
            position: relative;
            overflow: hidden;
        }

        .pelicula-card img {
            width: 100%;
            height: 400px;
            object-fit: cover;
            transition: transform 0.3s ease-in-out;
        }

        /* Efecto de agrandado al pasar el mouse sobre la imagen */
        .pelicula-card:hover img {
            transform: scale(1.05);
        }

        .pelicula-card .card-body {
            display: none;
        }

        /* Mostrar la información y el botón al hacer hover */
        .pelicula-card:hover .card-body {
            display: block;
            position: absolute;
            bottom: 10px;
            left: 10px;
            background-color: rgba(0, 0, 0, 0.6);  /* Fondo oscuro con opacidad */
            color: white;
            padding: 10px;
            width: 100%;
            box-sizing: border-box;  /* Asegura que el contenido no se desborde */
            transition: background-color 0.3s ease;
        }

        .pelicula-card .card-body a {
            background-color: #FF5733;
            color: white;
            padding: 8px 15px;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 10px;
        }

        /* Efecto hover sobre la tarjeta (sombra y transformación) */
        .pelicula-card:hover {
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* Sombra que aparece al pasar el mouse */
        }

        /* Espacio entre las filas de las películas */
        .container .row.mb-4 {
            margin-bottom: 30px !important; /* Asegúrate de que este estilo no sea sobreescrito */
        }
    </style>
</head>
<body>

<!-- Slider -->
<div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
    <ol class="carousel-indicators">
        <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
    </ol>
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="Cliente/images/slide1.jpg" class="d-block w-100" alt="Blade Runner">
        </div>
        <div class="carousel-item">
            <img src="Cliente/images/slide2.jpg" class="d-block w-100" alt="Blade Runner">
        </div>
        <div class="carousel-item">
            <img src="Cliente/images/slide3.jpg" class="d-block w-100" alt="Blade Runner">
        </div>
    </div>
    <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>

<!-- Barra de navegación -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <a class="navbar-brand" href="#">CineOnline</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mx-auto">
            <li class="nav-item active"><a class="nav-link" href="#">Inicio</a></li>
            <li class="nav-item"><a class="nav-link" href="#">Películas</a></li>
            <li class="nav-item"><a class="nav-link" href="#">Dulcería</a></li>
            <li class="nav-item"><a class="nav-link" href="http://localhost:8080/CineJ3/Login.jsp">Mi Cuenta</a></li>
        </ul>
    </div>
</nav>

<!-- Películas Disponibles -->
<div class="container mt-5">
    <h3>Películas Disponibles</h3>
    <div class="row mb-4">
        <div class="col-md-3">
            <div class="card pelicula-card">
                <img src="Cliente/images/pelicula6.jpg" class="card-img-top" alt="Blade Runner">
                <div class="card-body">
                    <h5 class="card-title">Blade Runner</h5>
                    <p class="card-text">16+ | 2h 44m | Una historia futurista en un mundo distópico...</p>
                    <a href="Cliente/DetallePelicula.jsp?id=1" class="btn btn-primary">Seleccionar</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card pelicula-card">
                <img src="Cliente/images/pelicula1.jpg" class="card-img-top" alt="Frozen">
                <div class="card-body">
                    <h5 class="card-title">Frozen</h5>
                    <p class="card-text">Todo está congelado en el reino de Arendelle...</p>
                    <a href="#" class="btn btn-primary">Seleccionar</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card pelicula-card">
                <img src="Cliente/images/pelicula2.jpg" class="card-img-top" alt="Mario Bros">
                <div class="card-body">
                    <h5 class="card-title">Mario Bros</h5>
                    <p class="card-text">Una aventura animada con Mario y Luigi...</p>
                    <a href="#" class="btn btn-primary">Seleccionar</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card pelicula-card">
                <img src="Cliente/images/pelicula3.jpg" class="card-img-top" alt="Spiderman">
                <div class="card-body">
                    <h5 class="card-title">Spiderman</h5>
                    <p class="card-text">Peter Parker salva el mundo una vez más...</p>
                    <a href="#" class="btn btn-primary">Seleccionar</a>
                </div>
            </div>
        </div>
    </div>
    <div class="row mb-4">
        <div class="col-md-3">
            <div class="card pelicula-card">
                <img src="Cliente/images/pelicula5.jpg" class="card-img-top" alt="Inception">
                <div class="card-body">
                    <h5 class="card-title">Inception</h5>
                    <p class="card-text">16+ | 2h 28m | Un thriller psicológico...</p>
                    <a href="Cliente/DetallePelicula.jsp?id=5" class="btn btn-primary">Seleccionar</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card pelicula-card">
                <img src="Cliente/images/pelicula7.jpg" class="card-img-top" alt="Avengers">
                <div class="card-body">
                    <h5 class="card-title">Avengers</h5>
                    <p class="card-text">El destino del universo está en juego...</p>
                    <a href="#" class="btn btn-primary">Seleccionar</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card pelicula-card">
                <img src="Cliente/images/pelicula8.jpeg" class="card-img-top" alt="Cars">
                <div class="card-body">
                    <h5 class="card-title">Cars</h5>
                    <p class="card-text">Rayo McQueen en nuevas carreras increíbles...</p>
                    <a href="#" class="btn btn-primary">Seleccionar</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card pelicula-card">
                <img src="Cliente/images/pelicula4.jpg" class="card-img-top" alt="Toy Story">
                <div class="card-body">
                    <h5 class="card-title">Toy Story</h5>
                    <p class="card-text">Los juguetes vuelven para una nueva aventura...</p>
                    <a href="#" class="btn btn-primary">Seleccionar</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Pie de página -->
<footer class="bg-dark text-white text-center py-4 mt-5">
    <p>© 2025 Cine Online | Todos los derechos reservados</p>
    <p><a href="#" class="text-white">Política de Privacidad</a> | <a href="#" class="text-white">Términos y Condiciones</a></p>
</footer>

<!-- Scripts de Bootstrap -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>
