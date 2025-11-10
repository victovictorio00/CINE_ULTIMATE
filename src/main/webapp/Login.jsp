<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // ==========================================================
    // Variables recibidas desde el LoginServlet
    // ==========================================================
    // - "error": mensaje de error en caso de credenciales incorrectas.
    // - "lastUsername": último nombre de usuario ingresado (para mantenerlo en el input).
    String error = (String) request.getAttribute("error");
    String lastUsername = (String) request.getAttribute("lastUsername");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <title>Login</title>

        <!-- ==========================================================
             Librerías CSS externas
             ========================================================== -->
        <!-- Bootstrap: para estructura y estilos -->
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" />
        <!-- Hoja de estilos personalizada -->
        <link rel="stylesheet" href="Estilos/loginStyle.css">
        <!-- Script de Google reCAPTCHA para validación antirrobótica -->
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    </head>

    <body>
        <!-- ==========================================================
             VIDEO DE FONDO (ambientación visual)
             ========================================================== -->
        <div class="video-bg">
            <video autoplay muted loop>
                <source src="Cliente/videos/video.mp4" type="video/mp4" />
                Tu navegador no soporta video de fondo.
            </video>
        </div>

        <!-- Capa oscura/roja semitransparente sobre el video -->
        <div class="overlay-red"></div>

        <!-- ==========================================================
             CONTENEDOR PRINCIPAL DEL LOGIN
             ========================================================== -->
        <div class="login-container">
            <div class="login-card">
                <h3>Ingresa con tu cuenta CINEMAX</h3>

                <!-- Muestra mensaje de error si el servlet lo envió -->
                <% if (error != null) { %>
                <div class="alert alert-danger" role="alert"><%= error %></div>
                <% } %>

                <!-- ==========================================================
                     FORMULARIO DE LOGIN
                     ==========================================================
                     - Envía datos por método POST a LoginServlet.
                     - Usa el contextPath para funcionar en cualquier entorno (producción o desarrollo).
                     - Incluye Google reCAPTCHA para validación adicional.
                ========================================================== -->
                <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
                    
                    <!-- Campo de usuario -->
                    <input type="text"
                           name="username"
                           class="form-control"
                           placeholder="Usuario"
                           required
                           autocomplete="username"
                           value="<%= (lastUsername != null) ? lastUsername : "" %>" />

                    <!-- Campo de contraseña -->
                    <input type="password"
                           name="password"
                           class="form-control"
                           placeholder="Contraseña"
                           required
                           autocomplete="current-password" />

                    <!-- Enlace de recuperación de contraseña (por implementar) -->
                    <a href="#" class="forgot-link">Olvidé mi contraseña</a>

                    <!-- Integración de reCAPTCHA v2 -->
                    <div class="g-recaptcha" 
                         data-sitekey="6Lf6HOorAAAAAOh0rkyVn0DXPsJklpcECHSygiHf">
                    </div>

                    <!-- Campo oculto para redirigir tras el login (si aplica) -->
                    <input type="hidden" name="redirect"
                           value="${param.redirect != null ? param.redirect : ''}">

                    <!-- Botones de acción -->
                    <button type="submit" class="btn btn-login">ENTRAR</button>
                    <button type="button" class="btn btn-register" onclick="window.location.href = 'Register.jsp'">
                        REGISTRARSE
                    </button>
                </form>
            </div>
        </div>
    </body>
</html>
