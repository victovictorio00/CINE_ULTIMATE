<%-- 
    Document   : DashboardCliente
    Created on : 28 may. 2025, 11:38:38
    Author     : Proyecto
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pelicula" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Dashboard Cliente - CineOnline</title>

        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/Estilos/dashClienteStyle.css">

        <style>
            .pelicula-card {
                border-radius: 8px;
                overflow: hidden;
            }
            .pelicula-card .card-img-top {
                height: 240px;
                object-fit: cover;
                display:block;
                width:100%;
            }
            .placeholder-img {
                background: #eee;
                display:none;
                align-items:center;
                justify-content:center;
                height:240px;
                color:#777;
                font-size:14px;
            }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand" href="#">CineOnline</a>
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
                        <a class="nav-link" href="<%= request.getContextPath()%>/Login.jsp">Iniciar Sesión</a>
                    </li>
                    <% } else {%>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Hola, <%= (nombreCompleto != null && !nombreCompleto.isEmpty()) ? nombreCompleto : username%>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
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

        <div class="container mt-5">
            <h3>Películas Disponibles</h3>

            <%
                List<Pelicula> peliculas = (List<Pelicula>) request.getAttribute("peliculas");
                if (peliculas == null || peliculas.isEmpty()) {
            %>
            <div class="alert alert-info">No hay películas disponibles actualmente.</div>
            <%
            } else {
            %>
            <div class="row mb-4">
                <%-- Asume: import modelo.Pelicula y que "peliculas" está en requestScope --%>
                <% for (modelo.Pelicula pelicula : peliculas) {
                        String title = pelicula.getNombre() == null ? "Sin título" : pelicula.getNombre();
                        int id = pelicula.getIdPelicula();
                %>
                <div class="col-md-3 mb-4">
                    <div class="card pelicula-card">
                        <!-- src apunta al servlet que ahora sirve JPEG/PNG desde BLOB -->
                        <img
                            src="<%= request.getContextPath()%>/ImageServlet?id=<%= id%>"
                            data-id="<%= id%>"
                            class="card-img-top"
                            alt="<%= title%>"
                            loading="lazy"
                            onerror="this.style.display='none'; var ph = this.nextElementSibling; if(ph) ph.style.display='flex'; console.warn('Image load failed for id=<%= id%>');" />
                        <div class="placeholder-img" role="img" aria-label="Imagen no disponible">Imagen no disponible (id=<%= id%>)</div>

                        <div class="card-body">
                            <h5 class="card-title"><%= title%></h5>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
            <% }%>
        </div>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

        <script>
            // Debug en cliente: imprime src y hace HEAD para comprobar status
            (function () {
                var imgs = document.querySelectorAll('.pelicula-card img.card-img-top');
                imgs.forEach(function (img) {
                    console.log('IMG SRC ->', img.src, ' data-id=', img.dataset.id);
                    // HEAD request para ver status (mismo origen, no CORS)
                    fetch(img.src, {method: 'HEAD'})
                            .then(function (r) {
                                console.log('HEAD', img.dataset.id, r.status, r.headers.get('Content-Type'));
                            })
                            .catch(function (e) {
                                console.warn('HEAD failed for', img.dataset.id, e);
                            });
                });
            })();
        </script>

    </body>
</html>
