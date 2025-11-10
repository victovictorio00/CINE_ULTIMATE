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
        <title>Detalle de Película | CineMax</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosCliente/DetallePelicula.css">
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
            <a class="navbar-brand" href="<%= request.getContextPath()%>/DashboardServlet">CineMax</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav mx-auto">
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
                        <div class="dropdown-menu bg-dark" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/Cliente/PerfilCliente.jsp">Mi Perfil</a>
                            <a class="dropdown-item text-white bg-dark" href="#">Mis Reservas</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/LogoutServlet">Cerrar Sesión</a>
                        </div>
                    </li>
                    <% } %>
                </ul>
            </div>
        </nav>
        <!--  TRAILER -->
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
        <div class="movie-video" style="position:relative; margin-top:80px; width:100%; height:60vh;">
            <!-- Miniatura -->
            <img id="videoThumb" 
                 src="https://img.youtube.com/vi/<%= trailer.substring(trailer.lastIndexOf('/')+1) %>/hqdefault.jpg"
                 style="width:100%; height:100%; object-fit:cover; border-radius:12px;">

            <div id="playerContainer" style="position:absolute; top:0; left:0; width:100%; height:100%;"></div>

            <div id="playOverlay" 
                 style="position:absolute; top:0; left:0; width:100%; height:100%; 
                        display:flex; justify-content:center; align-items:center; 
                        cursor:pointer;">
                <div style="width:60px; height:60px; border-radius:50%; background:rgba(255,87,51,0.85);
                            display:flex; justify-content:center; align-items:center;">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="white">
                        <path d="M8 5V19L19 12L8 5Z"/>
                    </svg>
                </div>
            </div>
        </div>
        <script src="https://www.youtube.com/iframe_api"></script>
        <script>
            const overlay = document.getElementById('playOverlay');
            const container = document.getElementById('playerContainer');
            const videoId = "<%= trailer.substring(trailer.lastIndexOf('/')+1) %>";
            let player;

            overlay.addEventListener('click', () => {
                player = new YT.Player('playerContainer', {
                    videoId: videoId,
                    width: '100%',
                    height: '100%',
                    playerVars: {
                        autoplay: 1,
                        controls: 0,
                        rel: 0,
                        modestbranding: 1,
                        disablekb: 1,
                        playsinline: 1,
                        loop: 1,
                        playlist: videoId
                    },
                    events: {
                        'onStateChange': onPlayerStateChange
                    }
                });

                overlay.style.display = 'none';
            });

            function onPlayerStateChange(event) {
                if (event.data === YT.PlayerState.ENDED) {
                    player.seekTo(0);
                    player.playVideo();
                }
            }
        </script>
        <div class="container movie-details-container">
            <div class="row d-flex align-items-start justify-content-center">
                <!-- Imagen -->
                <div class="col-12 col-md-5 mb-4 mb-md-0 movie-poster text-center">
                    <img 
                        src="<%= (pelicula.getFoto() != null && pelicula.getFoto().length > 0)
            ? (request.getContextPath() + "/ImageServlet?id=" + pelicula.getIdPelicula() + "&t=" + System.currentTimeMillis())
            : (request.getContextPath() + "/Cliente/images/pelicula6.jpg") %>" 
                        alt="Póster de <%= pelicula.getNombre()%>" 
                        class="img-fluid poster-img"
                        style="border-radius:12px; box-shadow:0 10px 30px rgba(0,0,0,0.35); max-height:520px; object-fit:cover;">
                </div>
                <div class="col-12 col-md-7 movie-details" style="padding-left:18px;">
                    <%
                        // Formatear precio con locale de Perú
                        java.text.NumberFormat fmt = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "PE"));
                        String precioFormateado = fmt.format(pelicula.getPrecio());
                    %>
                    <!-- Titulo -->
                    <div class="d-flex align-items-center justify-content-between" style="gap:12px;">
                        <div style="flex:1;">
                            <h1 class="mb-1" style="font-size:1.9rem;"><%= pelicula.getNombre()%></h1>
                            <h5 class="text-muted mb-3"><%= pelicula.getIdGenero().getNombre()%></h5>
                        </div>
                        <!-- Precio -->
                        <div style="display:inline-block; text-align:center; background: linear-gradient(90deg,#FF6B3A,#FF8A61); 
                            padding:12px 18px; border-radius:16px; box-shadow:0 6px 18px rgba(255,107,58,0.15);">
                            <div style="font-size:0.85rem; color:rgba(255,255,255,0.9); font-weight:500;">Entrada</div>
                            <div style="font-weight:700; font-size:1.3rem; color:white; margin-top:2px;"><%= precioFormateado %></div>
                        </div>
                    </div>
                    <h3 style="margin-top:12px; margin-bottom:8px;">Sinopsis</h3>
                    <p style="margin-bottom:18px; text-align:justify; color:#c7d4df;"><%= pelicula.getSinopsis()%></p>
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
                        <!-- Tarjeta pequeña con precio (opcional, repetido para énfasis) -->
                        <div style="display:flex; align-items:center; justify-content:space-between; margin-bottom:12px; gap:12px;">
                            <div style="color:var(--muted); font-weight:600;">
                                Precio por entrada
                            </div>
                            <div style="font-weight:800; font-size:1.05rem; color:var(--accent);">
                                <%= precioFormateado%>
                            </div>
                        </div>
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
                    <div class="mt-3 d-flex flex-wrap justify-content-start align-items-center gap-2">
                        <form id="reservarForm" action="<%= request.getContextPath()%>/ClienteServlet" method="post"
                              class="m-0 p-0 d-flex align-items-center">
                            <input type="hidden" name="action" value="reservar_p">
                            <input type="hidden" name="id" value="<%= pelicula.getIdPelicula()%>">
                            <input type="hidden" name="idFuncion" id="inputIdFuncion" value="">
                            <button id="btnReservar" type="submit" class="btn btn-dark"
                                    style="height:40px; min-width:160px; border-radius:6px; font-weight:600; line-height:1;"
                                    disabled>
                                🎟 Reservar
                            </button>
                        </form>

                        <a href="<%= request.getContextPath()%>/CarteleraServlet"
                           class="btn btn-outline-secondary d-flex align-items-center justify-content-center"
                           style="height:40px; min-width:160px; border-radius:6px; font-weight:600; line-height:1; margin-top:11px;">
                           ← Volver a cartelera
                        </a>
                    </div>
                    <div style="height:18px;"></div>
                </div>
            </div>
        </div>
        <!-- FOOTER -->
        <footer>
            <p>© 2025 Cine Online | Todos los derechos reservados</p>
            <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
        </footer>

        <script>
            (function () {
                const horarioBtns = Array.from(document.querySelectorAll('.horario-btn'));
                const btnReservar = document.getElementById('btnReservar');
                const inputIdFuncion = document.getElementById('inputIdFuncion');
                const precioServidor = "<%= precioFormateado.replace("\"", "\\\"")%>"; 
                if (!btnReservar || !inputIdFuncion)
                    return;
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
                    btnReservar.innerHTML = "🎟 Reservar";
                }

                clearSelection();
                horarioBtns.forEach(btn => {
                    btn.addEventListener('click', function () {
                        const wasSelected = this.classList.contains('active');
                        clearSelection();
                        if (!wasSelected) {
                            this.classList.add('active');
                            this.style.background = window.getComputedStyle(document.documentElement).getPropertyValue('--accent') || '#FF5733';
                            this.style.color = '#fff';
                            this.style.borderColor = 'transparent';

                            const id = this.dataset.idfuncion;
                            inputIdFuncion.value = id;

                            btnReservar.disabled = false;
                            btnReservar.removeAttribute('aria-disabled');
                            btnReservar.title = "Reservar " + (this.dataset.label || '');
                            // Actualizar texto del botón con el precio
                            btnReservar.innerHTML = "🎟 Reservar · " + precioServidor;
                            btnReservar.focus();
                        }
                    });
                    btn.addEventListener('keydown', function (e) {
                        if (e.key === 'Enter' || e.key === ' ') {
                            e.preventDefault();
                            this.click();
                        }
                    });
                });

            })();
        </script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </body>
</html>

