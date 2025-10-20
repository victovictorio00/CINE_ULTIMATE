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
            :root {
                --accent: #FF5733;              /* Tu color de acento: Naranja/Rojo */
            }
            body {
                background-color: #f7f7f7;
                margin: 0;
                font-family: 'Segoe UI', sans-serif;
            }

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
            .navbar-brand, .nav-link {
                color: white !important;
                transition: color .3s ease;
            }
            .nav-link:hover {
                color: var(--accent) !important;
            }
            .nav-item.active .nav-link {
                color: var(--accent) !important;
            }
            .nav-item.active::after {
                content: '';
                position: absolute;
                width: 100%;
                height: 3px;
                background-color: var(--accent);
                bottom: 0;
                left: 0;
            }
            .navbar-nav {
                width:100%;
                display:flex;
                justify-content:center;
            }
            .nav-item {
                margin:0 15px;
                position:relative;
            }

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

            .movie-poster img.poster-img:hover {
                transform: scale(1.03);
            }

            .movie-details {
                background: #fff;
                border-radius: 12px;
                padding: 30px 40px;
                box-shadow: 0 8px 25px rgba(0,0,0,0.1);
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

            @media (max-width: 768px) {
                .movie-details {
                    margin-top: 30px;
                    text-align: center;
                }
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
            <a class="navbar-brand" href="#">CineOnline</a>
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

        <!-- VIDEO TRAILER -->
        <%
            String trailer = pelicula.getTrailerUrl();
            if (trailer != null && !trailer.trim().isEmpty()) {
                String videoId = "";
                if (trailer.contains("watch?v=")) {
                    videoId = trailer.substring(trailer.indexOf("watch?v=") + 8);
                    if (videoId.contains("&")) { // eliminar parámetros extra
                        videoId = videoId.substring(0, videoId.indexOf("&"));
                    }
                } else {
                    videoId = trailer; // por si ya es un embed
                }
                trailer = "https://www.youtube.com/embed/" + videoId;
            } else {
                trailer = "https://www.youtube.com/embed/HeTE7j9dcGg";
            }
        %>

        <div class="movie-video"style="margin-top:80px">
            <iframe src="<%= trailer%>" 
                    frameborder="0" 
                    allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" 
                    allowfullscreen>
            </iframe>
        </div>

        <!-- DETALLES MEJORADOS -->
        <div class="container movie-details-container">
            <div class="row d-flex align-items-start justify-content-center">

                <!-- Imagen -->
                <div class="col-12 col-md-5 mb-4 mb-md-0 movie-poster text-center">
                    <img 
                        src="<%= (pelicula.getFoto() != null && pelicula.getFoto().length > 0)
                                ? (request.getContextPath() + "/ImageServlet?id=" + pelicula.getIdPelicula())
                                : (request.getContextPath() + "/Cliente/images/pelicula6.jpg")%>" 
                        alt="Póster de <%= pelicula.getNombre()%>" 
                        class="img-fluid poster-img"
                        style="border-radius:12px; box-shadow:0 10px 30px rgba(0,0,0,0.35); max-height:520px; object-fit:cover;">
                </div>

                <!-- Texto -->
                <div class="col-12 col-md-7 movie-details" style="padding-left:18px;">
                    <h1 class="mb-2" style="font-size:1.9rem;"><%= pelicula.getNombre()%></h1>
                    <h5 class="text-muted mb-3"><%= pelicula.getIdGenero().getNombre()%></h5>

                    <h3 style="margin-top:6px; margin-bottom:8px;">Sinopsis</h3>
                    <p style="margin-bottom:18px; text-align:justify; color:#666;"><%= pelicula.getSinopsis()%></p>

                    <h3 style="margin-top:10px; margin-bottom:10px;">Horarios</h3>

                    <div class="mb-3">
                        <%
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a"); // 12h + AM/PM
                            if (funciones == null || funciones.isEmpty()) {
                        %>
                        <div class="alert alert-info w-100" role="alert" style="background:#f2f4f7; color:#333; border-color:#e1e6ef;">
                            No hay funciones disponibles
                        </div>
                        <%
                        } else {
                        %>

                        <!-- Lista de horarios: espacio entre botones y scroll lateral en móviles -->
                        <div id="horariosWrapper" class="d-flex flex-wrap align-items-center" style="gap:10px;">
                            <%
                                for (modelo.Funcion funcion : funciones) {
                                    String horarioInicio = sdf.format(funcion.getFechaInicio());
                                    String horarioFin = sdf.format(funcion.getFechaFin());
                                    int idFuncion = funcion.getIdFuncion();
                            %>
                            <button type="button" 
                                    class="btn btn-outline-primary horario-btn"
                                    data-idfuncion="<%= idFuncion%>"
                                    data-label="<%= horarioInicio%> - <%= horarioFin%>"
                                    style="border-radius:999px; padding:10px 16px; font-weight:600;">
                                <%= horarioInicio%> - <%= horarioFin%>
                            </button>
                            <%
                                } // for
                            %>
                        </div>
                        <%
                    } // else
%>
                    </div>

                    <!-- Acciones: formulario que envía action=reservar&id=...&idFuncion=... -->
                    <div class="mt-3 d-flex flex-column flex-sm-row align-items-start align-items-sm-center" style="gap:12px;">
                        <form id="reservarForm" action="<%= request.getContextPath()%>/ClienteServlet" method="get" class="mb-2 mb-sm-0">
                            <input type="hidden" name="action" value="reservar">
                            <input type="hidden" name="id" value="<%= pelicula.getIdPelicula()%>">
                            <input type="hidden" name="idFuncion" id="inputIdFuncion" value="">
                            <button id="btnReservar" type="submit" class="btn btn-dark" disabled
                                    style="padding:10px 18px; border-radius:8px; font-weight:700;">
                                🎟 Reservar
                            </button>
                        </form>

                        <a href="<%= request.getContextPath()%>/CarteleraServlet" class="btn btn-outline-secondary"
                           style="padding:10px 16px; border-radius:8px;">← Volver a cartelera</a>
                    </div>

                    <!-- Espacio extra para separar contenido visualmente -->
                    <div style="height:18px;"></div>
                </div>
            </div>
        </div>

        <!-- FOOTER -->
        <footer>
            <p>© 2025 Cine Online | Todos los derechos reservados</p>
            <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
        </footer>

        <!-- Script que habilita Reservar solo tras seleccionar un horario -->
        <script>
            (function () {
                // Evitar ejecutar si no hay botones de horario
                const horarioBtns = Array.from(document.querySelectorAll('.horario-btn'));
                const btnReservar = document.getElementById('btnReservar');
                const inputIdFuncion = document.getElementById('inputIdFuncion');

                if (!btnReservar || !inputIdFuncion)
                    return;

                // Reset inicial: deshabilitado
                btnReservar.disabled = true;
                btnReservar.setAttribute('aria-disabled', 'true');

                function clearSelection() {
                    horarioBtns.forEach(b => {
                        b.classList.remove('active');
                        b.style.background = '';
                        b.style.color = '';
                        b.style.borderColor = '';
                    });
                    inputIdFuncion.value = '';
                    btnReservar.disabled = true;
                    btnReservar.setAttribute('aria-disabled', 'true');
                    btnReservar.title = "Selecciona un horario";
                }

                // Inicializar (por si)
                clearSelection();

                horarioBtns.forEach(btn => {
                    btn.addEventListener('click', function () {
                        // Toggle: si ya seleccionado, deselecciona
                        const wasSelected = this.classList.contains('active');
                        clearSelection();
                        if (!wasSelected) {
                            this.classList.add('active');
                            // Estilos visuales para selección
                            this.style.background = window.getComputedStyle(document.documentElement).getPropertyValue('--accent') || '#FF5733';
                            this.style.color = '#fff';
                            this.style.borderColor = 'transparent';

                            const id = this.dataset.idfuncion;
                            inputIdFuncion.value = id;

                            btnReservar.disabled = false;
                            btnReservar.removeAttribute('aria-disabled');
                            btnReservar.title = "Reservar " + (this.dataset.label || '');
                        }
                    });

                    // keyboard accessibility
                    btn.addEventListener('keydown', function (e) {
                        if (e.key === 'Enter' || e.key === ' ') {
                            e.preventDefault();
                            this.click();
                        }
                    });
                });

                // Optional: al recargar, si hay inputIdFuncion con valor (poco probable), marcar el botón correspondiente
                const existing = inputIdFuncion.value;
                if (existing) {
                    const found = horarioBtns.find(b => b.dataset.idfuncion === existing);
                    if (found)
                        found.click();
                }
            })();
        </script>

        <!-- Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

    </body>
</html>
