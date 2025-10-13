<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pelicula" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Dashboard Cliente - CineOnline</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

    <style>
        :root {
            --accent: #FF5733;
            --dark-bg: #212529;
            --card-shadow: 0 8px 16px rgba(0,0,0,0.5);
        }
        .carousel-item img {
            width: 100%;
            height: 100vh;
            max-height: 700px;
            object-fit: cover;
        }
        .navbar {
            position: absolute;
            top: 0;
            width: 100%;
            z-index: 10;
            background-color: rgba(0,0,0,0.3);
            border-bottom: 1px solid white;
            height: 80px;
            padding-top: 10px;
        }
        .navbar-brand, .nav-link { color: white !important; transition: color .3s ease; }
        .navbar-nav { width:100%; display:flex; justify-content:center; }
        .nav-item { margin:0 15px; position:relative; }
        .nav-link:hover { color: var(--accent) !important; }
        .nav-item.active .nav-link { color: var(--accent) !important; }
        .nav-item.active::after {
            content: '';
            position: absolute;
            width: 100%;
            height: 3px;
            background-color: var(--accent);
            bottom: 0;
            left: 0;
        }
        .pelicula-card {
            position: relative;
            overflow: hidden;
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            transition: box-shadow .3s ease, transform .3s ease;
            border-radius: 8px;
            background: transparent;
        }

        .pelicula-card img.card-img-top {
            width: 100%;
            height: 400px;
            object-fit: cover;
            display: block; /* importante: mostrar por defecto */
            transition: transform .3s ease-in-out;
        }
        .pelicula-card:hover img.card-img-top {
            transform: scale(1.05);
        }

        /* Card body aparece en hover */
        .pelicula-card .card-body {
            display: none;
            position: absolute;
            bottom: 0;
            left: 0;
            background-color: rgba(0,0,0,0.6);
            color: white;
            padding: 15px;
            width: 100%;
            box-sizing: border-box;
            z-index: 5;
        }
        .pelicula-card:hover .card-body { display:block; }

        .pelicula-card .card-body a.btn-primary {
            background-color: var(--accent);
            border-color: var(--accent);
            color: white;
            margin-top: 10px;
            display: inline-block;
        }

        .pelicula-card:hover { box-shadow: var(--card-shadow); transform: translateY(-4px); }

        /* Placeholder: oculto por defecto y solo visible en error */
        .placeholder-img {
            background: #222;
            display: none; /* oculto por defecto */
            align-items: center;
            justify-content: center;
            height: 400px;
            color: #ccc;
            font-size: 16px;
            text-align: center;
        }

        /* Marca cuando no hay imagen (opcional para estilos) */
        .pelicula-card.no-image {
            /* puedes añadir bordes o efecto si quieres */
        }

        .h3 {
            margin-bottom: 10px;
        }
        
        /* Footer */
        .footer { background-color: var(--dark-bg) !important; color: #fff; }
    </style>
</head>
<body>

    <!-- CARRUSEL -->
    <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel" style="margin-top:0;">
        <ol class="carousel-indicators">
            <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
        </ol>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="<%= request.getContextPath() %>/Cliente/images/slide1.jpg" class="d-block w-100" alt="Slide 1">
            </div>
            <div class="carousel-item">
                <img src="<%= request.getContextPath() %>/Cliente/images/slide2.jpg" class="d-block w-100" alt="Slide 2">
            </div>
            <div class="carousel-item">
                <img src="<%= request.getContextPath() %>/Cliente/images/slide3.jpg" class="d-block w-100" alt="Slide 3">
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

    <!-- NAVBAR -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <a class="navbar-brand" href="#">CineOnline</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item active"><a class="nav-link" href="#">Inicio</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Películas</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Dulcería</a></li>

                <%
                    String username = (String) session.getAttribute("username");
                    String nombreCompleto = (String) session.getAttribute("nombreCompleto");
                    if (username == null || username.isEmpty()) {
                %>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath()%>/Login.jsp">Mi Cuenta</a>
                </li>
                <% } else { %>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        Hola, <%= (nombreCompleto != null && !nombreCompleto.isEmpty()) ? nombreCompleto.split(" ")[0] : username %>
                    </a>
                    <div class="dropdown-menu bg-dark" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item text-white bg-dark" href="#">Mi Perfil</a>
                        <a class="dropdown-item text-white bg-dark" href="#">Mis Reservas</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/LogoutServlet">Cerrar Sesión</a>
                    </div>
                </li>
                <% } %>
            </ul>
        </div>
    </nav>

    <!-- CONTENIDO: PELÍCULAS -->
    <div class="container mt-5">
        <div class="container mb-5"><h3>Películas Disponibles</h3></div>

        <%
            List<Pelicula> peliculas = (List<Pelicula>) request.getAttribute("peliculas");
            if (peliculas == null || peliculas.isEmpty()) {
        %>
        <div class="alert alert-info">No hay películas disponibles actualmente.</div>
        <%
            } else {
        %>
        <div class="row mb-4">
            <%
                for (modelo.Pelicula pelicula : peliculas) {
                    String title = pelicula.getNombre() == null ? "Sin título" : pelicula.getNombre();
                    String sinopsis = pelicula.getSinopsis() == null || pelicula.getSinopsis().isEmpty()
                            ? "Sin sinopsis disponible." : pelicula.getSinopsis();
                    int id = pelicula.getIdPelicula();
                    String imageUrl = request.getContextPath() + "/ImageServlet?id=" + id;
                    String detailUrl = request.getContextPath() + "/DetallePeliculaServlet?id=" + id;
            %>
            <div class="col-md-3 mb-4">
                <div class="card pelicula-card">
                    <!-- img visible por defecto. onerror mostrará el placeholder -->
                    <img
                        src="<%= imageUrl %>"
                        data-id="<%= id %>"
                        class="card-img-top"
                        alt="<%= title %>"
                        loading="lazy"
                        onerror="(function(img){ img.style.display='none'; var ph=img.nextElementSibling; if(ph) ph.style.display='flex'; img.closest('.pelicula-card').classList.add('no-image'); })(this);"
                        onload="(function(img){ var ph=img.nextElementSibling; if(ph) ph.style.display='none'; img.closest('.pelicula-card').classList.add('has-image'); })(this);" />

                    <!-- Placeholder oculto inicialmente; solo aparece si onerror o fallback lo activa -->
                    <div class="placeholder-img" role="img" aria-label="Imagen no disponible">
                        <% if (title.length() > 20) { %>
                            <%= title.substring(0, 20) + "..." %>
                        <% } else { %>
                            <%= title %>
                        <% } %>
                        <br/>(Imagen no disponible)
                    </div>

                    <div class="card-body">
                        <h5 class="card-title"><%= title %></h5>
                        <p class="card-text">
                            <%= sinopsis.length() > 100 ? sinopsis.substring(0, 100) + "..." : sinopsis %>
                        </p>
                        <a href="<%= detailUrl %>" class="btn btn-primary">Seleccionar</a>
                    </div>
                </div>
            </div>
            <%
                } // for peliculas
            %>
        </div>
        <% } // else peliculas %>
    </div>

    <!-- FOOTER -->
    <footer class="footer bg-dark text-white text-center py-4 mt-5">
        <p>© 2025 Cine Online | Todos los derechos reservados</p>
        <p><a href="#" class="text-white">Política de Privacidad</a> | <a href="#" class="text-white">Términos y Condiciones</a></p>
    </footer>

    <!-- SCRIPTS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

    <script>
        // Fallback/robusto: comprueba estado de cada imagen y ajusta placeholder si hace falta
        document.addEventListener('DOMContentLoaded', function () {
            document.querySelectorAll('.pelicula-card').forEach(function(card){
                var img = card.querySelector('img.card-img-top');
                var ph  = card.querySelector('.placeholder-img');

                if (!img) return;

                // Si ya cargó con éxito
                if (img.complete && img.naturalHeight > 0) {
                    card.classList.add('has-image');
                    if (ph) ph.style.display = 'none';
                }
                // Si completó pero no tiene altura (contenido inválido)
                else if (img.complete && img.naturalHeight === 0) {
                    img.style.display = 'none';
                    if (ph) ph.style.display = 'flex';
                    card.classList.add('no-image');
                }

                // Eventos (en caso no se usen inline)
                img.addEventListener('load', function(){
                    card.classList.add('has-image');
                    if (ph) ph.style.display = 'none';
                });
                img.addEventListener('error', function(){
                    img.style.display = 'none';
                    if (ph) ph.style.display = 'flex';
                    card.classList.add('no-image');
                });
            });
        });
    </script>
</body>
</html>
