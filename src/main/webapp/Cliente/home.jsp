<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pelicula" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>CineOnline</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="Estilos/dashClienteStyle.css">

    <style>
        /* Ajuste del carrusel */
        .carousel-item {
            background-color: #000; /* Fondo negro para posters verticales */
            text-align: center;
        }
        .carousel-item img {
            max-height: 70vh;   /* Altura fija */
            width: auto;        /* Mantiene proporción */
            object-fit: contain; /* No recorta, muestra imagen completa */
            margin: 0 auto;
        }
    </style>
</head>
<body>
    <!-- CARRUSEL DINÁMICO CON PELÍCULAS -->
    <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel" style="margin-top:0;">
        <ol class="carousel-indicators">
            <%
                List<Pelicula> peliculas = (List<Pelicula>) request.getAttribute("peliculas");
                if (peliculas != null && !peliculas.isEmpty()) {
                    for (int i = 0; i < peliculas.size(); i++) {
            %>
            <li data-target="#carouselExampleIndicators" data-slide-to="<%= i%>" class="<%= (i == 0) ? "active" : ""%>"></li>
            <%
                    }
                } else {
            %>
            <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
            <%
                }
            %>
        </ol>

        <div class="carousel-inner">
            <%
                if (peliculas != null && !peliculas.isEmpty()) {
                    for (int i = 0; i < peliculas.size(); i++) {
                        Pelicula pelicula = peliculas.get(i);
                        String imageUrl = request.getContextPath() + "/ImageServlet?id=" + pelicula.getIdPelicula() + "&t=" + System.currentTimeMillis();
                        String title = pelicula.getNombre() == null ? "Sin título" : pelicula.getNombre();
                        String sinopsis = (pelicula.getSinopsis() != null && !pelicula.getSinopsis().isEmpty())
                                ? pelicula.getSinopsis()
                                : "Sin sinopsis disponible.";
            %>
            <div class="carousel-item <%= (i == 0) ? "active" : ""%>">
                <img src="<%= imageUrl%>" alt="<%= title%>"
                     onerror="this.src='<%= request.getContextPath()%>/Cliente/images/fallback.jpg'">
                <div class="carousel-caption d-none d-md-block bg-dark bg-opacity-75 rounded p-3">
                    <h5><%= title%></h5>
                    <p><%= sinopsis.length() > 100 ? sinopsis.substring(0, 100) + "..." : sinopsis%></p>
                </div>
            </div>
            <%
                    }
                } else {
            %>
            <div class="carousel-item active">
                <img src="<%= request.getContextPath()%>/Cliente/images/slide1.jpg" alt="Slide 1">
            </div>
            <div class="carousel-item">
                <img src="<%= request.getContextPath()%>/Cliente/images/slide2.jpg" alt="Slide 2">
            </div>
            <div class="carousel-item">
                <img src="<%= request.getContextPath()%>/Cliente/images/slide3.jpg" alt="Slide 3">
            </div>
            <%
                }
            %>
        </div>

        <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Anterior</span>
        </a>
        <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Siguiente</span>
        </a>
    </div>

    <!-- NAVBAR -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <a class="navbar-brand" href="#">CineMax</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item active"><a class="nav-link" href="<%= request.getContextPath()%>/DashboardServlet">Inicio</a></li>
                <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/CarteleraServlet">Películas</a></li>
                <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/DulceriaServlet">Dulcería</a></li>

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
                    String imageUrl = request.getContextPath() + "/ImageServlet?id=" + id + "&t=" + System.currentTimeMillis();
                    String detailUrl = request.getContextPath() + "/DetallePeliculaServlet?id=" + id;
            %>
            <div class="col-md-3 mb-4">
                <div class="card pelicula-card">
                    <img src="<%= imageUrl%>" class="card-img-top" alt="<%= title%>">
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
                }
            %>
        </div>
        <% } %>
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
</body>
</html>
