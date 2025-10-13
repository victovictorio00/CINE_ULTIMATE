<%--
    Document : Peliculas
    Author     : Proyecto
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pelicula" %>
<%@ page import="java.util.Arrays" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Cartelera - CineOnline</title>

        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

        <style>
            /* PALETA DE COLORES AJUSTADA PARA FONDO BLANCO */
            :root {
                --accent: #FF5733;              /* Tu color de acento: Naranja/Rojo */
                --main-bg: #FFFFFF;             /* Fondo principal: Blanco */
                --main-text: #333333;           /* Color de texto oscuro para alto contraste */
                --secondary-text: #6c757d;      /* Gris para texto secundario */
                --border-color: #dee2e6;        /* Color de borde claro */
                --card-bg: #f8f9fa;             /* Fondo sutil para tarjetas o secciones */
                --shadow: 0 4px 12px rgba(0,0,0,0.1); /* Sombra suave para el modo claro */
            }

            /* CUERPO */
            body {
                background-color: var(--main-bg);
                color: var(--main-text);
                font-family: 'Poppins', sans-serif;
                padding-top: 80px;
            }

            /* NAVBAR (Mantenemos tu estilo de fondo transparente oscuro, pero ajustamos colores internos) */
            .navbar {
                position: fixed; /* Cambiado a fixed para que el contenido empiece debajo */
                top: 0;
                width: 100%;
                z-index: 1000;
                background-color: rgba(0,0,0,0.8); /* Tu color oscuro semitransparente */
                border-bottom: 1px solid white;
                height: 80px;
                padding-top: 10px;
            }
            .navbar-brand, .nav-link { color: white !important; transition: color .3s ease; }
            .nav-link:hover { color: var(--accent) !important; }
            .nav-item.active .nav-link { color: var(--accent) !important; }
            .nav-item.active::after {
                content: ''; position: absolute; width: 100%; height: 3px;
                background-color: var(--accent); bottom: 0; left: 0;
            }
            .navbar-nav { width:100%; display:flex; justify-content:center; }
            .nav-item { margin:0 15px; position:relative; }
            
            /* Dropdown de la cuenta (Ajustado para el color oscuro del navbar) */
            .navbar .dropdown-menu {
                background-color: #343a40 !important; /* Gris muy oscuro */
                border: 1px solid rgba(255, 255, 255, 0.15);
            }
            .navbar .dropdown-item {
                color: #f8f9fa !important;
                background-color: transparent !important;
            }
            .navbar .dropdown-item:hover {
                background-color: var(--accent) !important;
                color: white !important;
            }

            /* ESTRUCTURA PRINCIPAL (Filtros + Cartelera) */
            .page-header {
                color: var(--main-text); /* Color de texto principal */
                font-weight: 700;
                font-size: 2.2rem;
                margin-bottom: 20px;
                border-bottom: 2px solid var(--accent); /* Línea de acento */
                padding-bottom: 10px;
            }
            .main-content {
                display: flex;
                padding: 20px 0;
            }

            /* COLUMNA DE FILTROS (IZQUIERDA) */
            .sidebar {
                width: 250px;
                padding-right: 20px;
                /* Borde claro para separación */
                border-right: 1px solid var(--border-color); 
                flex-shrink: 0;
            }
            .sidebar h5 {
                color: var(--main-text);
                font-size: 1.2rem;
                font-weight: 700;
                padding-bottom: 5px;
                border-bottom: 1px solid var(--border-color);
            }
            .sidebar h6 {
                color: var(--secondary-text); /* Gris oscuro para subtítulos */
                margin-top: 20px;
                font-size: 1rem;
                font-weight: 600;
            }
            
            /* ESTILO DE LOS ENLACES DE FILTRO */
            .filter-list a {
                display: block;
                color: var(--main-text); /* Enlaces de color oscuro */
                padding: 3px 0;
                transition: color 0.2s;
                font-size: 0.95rem;
            }
            .filter-list a:hover,
            .filter-list a.active {
                color: var(--accent);
                text-decoration: none;
                font-weight: 600;
            }

            /* COLUMNA DE CONTENIDO (DERECHA) */
            .movie-list-container {
                flex-grow: 1;
                padding-left: 30px;
            }

            /* TARJETAS DE PELÍCULAS (Mantenemos tu efecto de HOVER) */
            .pelicula-card {
                position: relative;
                overflow: hidden;
                border: 1px solid #eee; /* Borde sutil en modo claro */
                box-shadow: 0 2px 4px rgba(0,0,0,0.05); /* Sombra más sutil */
                transition: box-shadow .3s ease, transform .3s ease;
                border-radius: 8px;
                background: var(--main-bg);
            }

            .pelicula-card img.card-img-top {
                width: 100%;
                height: 400px;
                object-fit: cover;
                display: block;
                transition: transform .3s ease-in-out;
            }
            .pelicula-card:hover img.card-img-top {
                transform: scale(1.05);
            }

            /* Card body aparece en hover: Mantenemos el fondo oscuro para contraste */
            .pelicula-card .card-body {
                display: none;
                position: absolute;
                bottom: 0;
                left: 0;
                background-color: rgba(0,0,0,0.85); 
                color: white;
                padding: 15px;
                width: 100%;
                box-sizing: border-box;
                z-index: 5;
                min-height: 150px;
                justify-content: flex-end;
                flex-direction: column;
            }
            .pelicula-card:hover .card-body { display:flex; }
            
            .pelicula-card .card-body .card-title {
                color: var(--accent);
            }
            .pelicula-card .card-body .card-text {
                font-size: 0.9rem;
                max-height: 80px;
                overflow: hidden;
                text-overflow: ellipsis;
            }

            .pelicula-card .card-body a.btn-primary {
                background-color: var(--accent);
                border-color: var(--accent);
                color: white;
            }

            .pelicula-card:hover {
                box-shadow: var(--shadow); /* Sombra ajustada */
                transform: translateY(-4px);
            }

            /* Etiqueta de Promoción */
            .movie-tag {
                position: absolute;
                top: 10px;
                left: -15px;
                background-color: var(--accent);
                color: white;
                padding: 5px 15px 5px 25px;
                font-weight: 700;
                font-size: 0.8rem;
                transform: rotate(-5deg);
                z-index: 5;
                box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3);
            }

            /* Placeholder (Mantenemos un fondo oscuro para consistencia con el estilo de la tarjeta) */
            .placeholder-img {
                background: #6c757d; /* Gris oscuro para el placeholder */
                display: none; 
                align-items: center;
                justify-content: center;
                height: 400px;
                color: #f8f9fa;
                font-size: 16px;
                text-align: center;
                border-radius: 8px 8px 0 0;
            }

            /* Botón Ver Más */
            .btn-ver-mas {
                color: var(--accent);
                border-color: var(--accent);
            }
            .btn-ver-mas:hover {
                background-color: var(--accent);
                color: white; /* Blanco en hover para un contraste limpio */
            }

            /* Footer (Ajustado para que combine con el tema claro) */
            .footer {
                background-color: var(--card-bg) !important; /* Gris muy claro */
                color: var(--secondary-text);
                border-top: 1px solid var(--border-color);
            }
            .footer a {
                color: var(--secondary-text);
            }
            .footer a:hover {
                color: var(--accent);
            }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="#">CineOnline</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/DashboardServlet">Inicio</a></li>
                    <li class="nav-item active"><a class="nav-link" href="#">Películas</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">Dulcería</a></li>

                    <%
                        String username = (String) session.getAttribute("username");
                        String nombreCompleto = (String) session.getAttribute("nombreCompleto");
                        if (username == null || username.isEmpty()) {
                    %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath()%>/Login.jsp">Mi Cuenta</a>
                    </li>
                    <% } else {%>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                            Hola, <%= (nombreCompleto != null && !nombreCompleto.isEmpty()) ? nombreCompleto.split(" ")[0] : username%>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#">Mi Perfil</a>
                            <a class="dropdown-item" href="#">Mis Reservas</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="<%= request.getContextPath()%>/LogoutServlet">Cerrar Sesión</a>
                        </div>
                    </li>
                    <% } %>
                </ul>
            </div>
        </nav>
        
        <div class="container-fluid mt-4">
            <div class="container">
                <h1 class="page-header">Cartelera</h1>
            </div>

            <div class="main-content container">
                <div class="sidebar">
                    <h5 class="mb-0"><i class="fas fa-filter mr-2"></i> Filtrar Por</h5>

                    <div class="filter-section">
                        <h6>Fecha de Estreno</h6>
                        <ul class="list-unstyled filter-list">
                            <li><a href="#" class="active">En Cartelera</a></li>
                            <li><a href="#">Preventa</a></li>
                            <li><a href="#">Próximos Estrenos</a></li>
                        </ul>
                    </div>

                    <div class="filter-section">
                        <h6>Género</h6>
                        <ul class="list-unstyled filter-list">
                            <%
                                List<String> generos = Arrays.asList("Acción", "Comedia", "Drama", "Terror", "Animación", "Thriller", "Ciencia Ficción", "Fantasía");
                                for (String genero : generos) {
                            %>
                            <li><a href="#" data-genero="<%= genero.toLowerCase()%>"><%= genero%></a></li>
                            <% } %>
                        </ul>
                    </div>

                </div>

                <div class="movie-list-container">
                    <div class="row">
                        <%
                            List<Pelicula> peliculas = (List<Pelicula>) request.getAttribute("peliculas");
                            if (peliculas == null || peliculas.isEmpty()) {
                        %>
                        <div class="alert alert-info col-12" style="background-color: var(--card-bg); color: var(--secondary-text); border-color: var(--border-color);">No hay películas que coincidan con los filtros.</div>
                        <%
                        } else {
                            for (modelo.Pelicula pelicula : peliculas) {
                                String title = pelicula.getNombre() == null ? "Sin título" : pelicula.getNombre();
                                String sinopsis = pelicula.getSinopsis() == null || pelicula.getSinopsis().isEmpty() ? "Sin sinopsis disponible." : pelicula.getSinopsis();
                                int id = pelicula.getIdPelicula();
                                String imageUrl = request.getContextPath() + "/ImageServlet?id=" + id;
                                String detailUrl = request.getContextPath() + "/DetallePeliculaServlet?id=" + id;
                            %>

                            <div class="col-6 col-sm-4 col-md-3 mb-4">
                                <div class="card pelicula-card">

                                    <span class="movie-tag">ESTRENO</span>

                                    <a href="<%= detailUrl%>" class="d-block">
                                        <img
                                            src="<%= imageUrl%>"
                                            class="card-img-top"
                                            alt="<%= title%>"
                                            loading="lazy"
                                            onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';"
                                            />
                                    </a>

                                    <div class="placeholder-img" role="img" aria-label="Imagen no disponible">
                                        <%= title%><br/>(Imagen no disponible)
                                    </div>

                                    <div class="card-body">
                                        <h5 class="card-title"><%= title%></h5>
                                        <p class="card-text">
                                            <%= sinopsis.length() > 100 ? sinopsis.substring(0, 100) + "..." : sinopsis%>
                                        </p>
                                        <a href="<%= detailUrl%>" class="btn btn-primary">Seleccionar</a>
                                    </div>
                                </div>
                            </div>
                            <%
                                } // for peliculas
                            %>

                            <div class="col-12 text-center mt-4">
                                <button class="btn btn-outline-light btn-ver-mas"><i class="fas fa-arrow-down mr-2"></i> Ver más películas</button>
                            </div>

                        <% } // else peliculas %>
                    </div>
                </div>
            </div>
        </div>

        <footer class="footer text-center py-4 mt-5">
            <p>© 2025 Cine Online | Todos los derechos reservados</p>
            <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
        </footer>

        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </body>
</html>