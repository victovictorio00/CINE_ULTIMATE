
<%--
    Document : Peliculas
    Author     : Proyecto
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pelicula" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="modelo.Genero" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Cartelera - CineOnline</title>

        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="Estilos/peliculaClienteStyle.css">

    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="<%= request.getContextPath()%>/DashboardServlet">CineMax</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/DashboardServlet">Inicio</a></li>
                    <li class="nav-item active"><a class="nav-link" href="<%= request.getContextPath()%>/CarteleraServlet">Películas</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/DulceriaServlet">Dulcería</a></li>

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
                    <% }%>
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
                        <h6>Fecha de Función</h6>
                        <form action="<%= request.getContextPath()%>/CarteleraServlet" method="GET" class="mb-3">
                            <div class="form-group">
                                <input type="date" 
                                       class="form-control form-control-sm" 
                                       name="fechaSeleccionada" 
                                       id="fechaSeleccionada"
                                       value="<%= (request.getParameter("fechaSeleccionada") != null ? request.getParameter("fechaSeleccionada") : "")%>"
                                       style="color: var(--main-text); background-color: var(--main-bg); border-color: var(--border-color);" 
                                       required>
                            </div>
                            <button type="submit" class="btn btn-sm btn-block" 
                                    style="background-color: var(--accent); color: white; border: none;">
                                <i class="fas fa-search"></i> Filtrar Día
                            </button>
                        </form>
                    </div>

                    <div class="filter-section">
                        <h6>Género</h6>
                        <ul class="list-unstyled filter-list">
                            <%
                                // Obtener el filtro de fecha activo del Request (enviado desde el Servlet)
                                String fechaActiva = (String) request.getAttribute("filtroActivoFecha");
                                // Crear el segmento de URL para mantener el filtro de fecha (si existe)
                                String paramFecha = (fechaActiva != null && !fechaActiva.isEmpty()) ? "?fechaSeleccionada=" + fechaActiva : "";
                            %>
                            <a href="<%= request.getContextPath()%>/CarteleraServlet<%= paramFecha%>" class="active">
                                Todos los Géneros
                            </a>

                            <%
                                // 2. Obtener la lista de Géneros del Request
                                List<modelo.Genero> listaGeneros = (List<modelo.Genero>) request.getAttribute("generos");

                                if (listaGeneros != null) {
                                    for (modelo.Genero genero : listaGeneros) {
                                        // Obtiene el ID y el Nombre directamente del objeto Genero
                                        int idGenero = genero.getIdGenero();
                                        String nombreGenero = genero.getNombre(); // Asumiendo que Genero tiene getNombre()
                            %>
                            <li>
                                <a href="<%= request.getContextPath()%>/CarteleraServlet?genero=<%= idGenero%>" data-genero="<%= nombreGenero.toLowerCase()%>">
                                    <%= nombreGenero%>
                                </a>
                            </li>
                            <%
                                }
                            } else {
                            %>
                            <li><a href="#">Error al cargar géneros</a></li>
                                <% }%>
                        </ul>
                    </div>

                    <div class="filter-section mt-3">
                        <a href="<%= request.getContextPath()%>/CarteleraServlet" 
                           class="btn btn-block btn-outline-light" 
                           style="border-color: var(--accent); color: var(--accent); font-weight: 600;">
                            <i class="fas fa-times-circle mr-2"></i> Limpiar Filtros
                        </a>
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