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
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/Cliente/EstilosCliente/DetallePelicula.css">
    </head>
    <body>
        <jsp:include page="/Cliente/accesibilidad/accesibilidad.jsp" />
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
                            <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/ClienteServlet?action=misReservas">Mis Reservas</a>
                            <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/Cliente/PerfilCliente.jsp">Mi Perfil</a>
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
        <div class="movie-video">
            <!-- Miniatura -->
            <img id="videoThumb" 
                 src="https://img.youtube.com/vi/<%= trailer.substring(trailer.lastIndexOf('/') + 1)%>/hqdefault.jpg"
                 alt="Video thumbnail">

            <div id="playerContainer"></div>

            <div id="playOverlay">
                <div>
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="white">
                        <path d="M8 5V19L19 12L8 5Z"/>
                    </svg>
                </div>
            </div>
        </div>
        
        <div class="container movie-details-container">
            <div class="row d-flex align-items-start justify-content-center">
                <!-- Imagen -->
                <div class="col-12 col-md-5 mb-4 mb-md-0 movie-poster text-center">
                    <img 
                        src="<%= (pelicula.getFoto() != null && pelicula.getFoto().length > 0)
                                ? (request.getContextPath() + "/ImageServlet?id=" + pelicula.getIdPelicula() + "&t=" + System.currentTimeMillis())
                                : (request.getContextPath() + "/Cliente/images/pelicula6.jpg")%>" 
                        alt="Póster de <%= pelicula.getNombre()%>" 
                        class="img-fluid poster-img">
                </div>
                <div class="col-12 col-md-7 movie-details">
                    <%
                        // Formatear precio con locale de Perú
                        java.text.NumberFormat fmt = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "PE"));
                        String precioFormateado = fmt.format(pelicula.getPrecio());
                    %>
                    <!-- Titulo -->
                    <div class="d-flex align-items-center justify-content-between">
                        <div>
                            <h1 class="mb-1"><%= pelicula.getNombre()%></h1>
                            <h5 class="text-muted mb-3"><%= pelicula.getIdGenero().getNombre()%></h5>
                        </div>
                        <!-- Precio -->
                        <div>
                            <div>Entrada</div>
                            <div><%= precioFormateado%></div>
                        </div>
                    </div>
                    <h3>Sinopsis</h3>
                    <p><%= pelicula.getSinopsis()%></p>
                    <h3>Horarios</h3>
                    <div class="mb-3">
                        <%
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");

                            // Contar funciones activas
                            int contadorActivas = 0;
                            if (funciones != null && !funciones.isEmpty()) {
                                for (modelo.Funcion f : funciones) {
                                    if (f.getActiva() == 1) {
                                        contadorActivas++;
                                    }
                                }
                            }

                            if (funciones == null || funciones.isEmpty() || contadorActivas == 0) {
                        %>
                        <div class="alert alert-info w-100" role="alert">
                            No hay funciones disponibles
                        </div>
                        <%
                        } else {
                        %>
                        <div id="horariosWrapper" class="d-flex flex-wrap align-items-center">
                            <%
                                for (modelo.Funcion funcion : funciones) {
                                    // Solo renderizamos botones para funciones activas
                                    if (funcion.getActiva() != 1) {
                                        continue;
                                    }

                                    String horarioInicio = sdf.format(funcion.getFechaInicio());
                                    String horarioFin = sdf.format(funcion.getFechaFin());
                                    int idFuncion = funcion.getIdFuncion();
                            %>
                            <button type="button" 
                                    class="btn btn-outline-primary horario-btn"
                                    data-idfuncion="<%= idFuncion%>"
                                    data-label="<%= horarioInicio%> - <%= horarioFin%>"
                                    data-activa="1"
                                    title="Seleccionar horario">
                                <%= horarioInicio%> - <%= horarioFin%>
                            </button>
                            <%
                                }
                            %>
                        </div>
                        <%
                            }
                        %>
                    </div>
                    <div class="mt-3 d-flex flex-wrap justify-content-start align-items-center gap-2">
                        <form id="reservarForm" action="<%= request.getContextPath()%>/ClienteServlet" method="post"
                              class="m-0 p-0 d-flex align-items-center">
                            <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="reservar_p">
                            <input type="hidden" name="id" value="<%= pelicula.getIdPelicula()%>">
                            <input type="hidden" name="idFuncion" id="inputIdFuncion" value="">
                            <button id="btnReservar" type="submit" class="btn btn-dark" disabled>
                                🎟 Reservar
                            </button>
                        </form>

                        <a href="<%= request.getContextPath()%>/CarteleraServlet"
                           class="btn btn-outline-secondary d-flex align-items-center justify-content-center">
                            ← Volver a cartelera
                        </a>
                    </div>
                    <div></div>
                </div>
            </div>
        </div>
        
        <!-- FOOTER -->
        <footer>
            <p>© 2025 Cine Online | Todos los derechos reservados</p>
            <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
        </footer>

        <%-- Inicializar datos de configuración para JavaScript --%>
        <script>
            window.detallePeliculaData = {
                videoId: "<%= trailer.substring(trailer.lastIndexOf('/') + 1)%>",
                precioFormateado: "<%= precioFormateado.replace("\"", "\\\"")%>"
            };
        </script>

        <%-- API de YouTube (debe cargarse primero) --%>
        <script src="https://www.youtube.com/iframe_api"></script>
        
        <%-- Scripts externos de la aplicación --%>
        <script src="<%= request.getContextPath()%>/Cliente/JS/youtubePlayer.js"></script>
        <script src="<%= request.getContextPath()%>/Cliente/JS/seleccionHorario.js"></script>
        
        <%-- Bootstrap y dependencias --%>
        <script src="<%= request.getContextPath() %>/Cliente/lib/jquery/jquery-3.6.4.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/popper/popper.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/bootstrap/js/bootstrap-4.6.2.min.js"></script>
    </body>
</html>