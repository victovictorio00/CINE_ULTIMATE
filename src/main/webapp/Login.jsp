
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Mensajes y valores que envía el servlet con request.setAttribute("error", ...);
    String error = (String) request.getAttribute("error");
    String lastUsername = (String) request.getAttribute("lastUsername");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <title>Login</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="Estilos/loginStyle.css">
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    </head>
    <body>
        <div class="video-bg">
            <video autoplay muted loop>
                <source src="Cliente/videos/video.mp4" type="video/mp4" />
                Tu navegador no soporta video de fondo.
            </video>
        </div>
        <div class="overlay-red"></div>
        <div class="login-container">
            <div class="login-card">
                <h3>Ingresa con tu cuenta CINEMAX</h3>

                <% if (error != null) {%>
                <div class="alert alert-danger" role="alert"><%= error%></div>
                <% }%>

                <!-- Usa el contextPath para que funcione en cualquier contexto -->
                <form action="<%= request.getContextPath()%>/LoginServlet" method="post">
                    <input type="text"
                           name="username"
                           class="form-control"
                           placeholder="Usuario"
                           required
                           autocomplete="username"
                           value="<%= (lastUsername != null) ? lastUsername : ""%>" />

                    <input type="password"
                           name="password"
                           class="form-control"
                           placeholder="Contraseña"
                           required
                           autocomplete="current-password" />

                    <a href="#" class="forgot-link">Olvidé mi contraseña</a>

                    <div class="g-recaptcha" 
                         data-sitekey="6Lf6HOorAAAAAOh0rkyVn0DXPsJklpcECHSygiHf">
                    </div>

                    <% 
        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl != null) {
            // El valor a enviar debe ser el mismo que se recibió.
    %>
        <input type="hidden" name="redirectUrl" value="<%= redirectUrl %>">
    <% } %>
                    <button type="submit" class="btn btn-login">ENTRAR</button>
                    <button type="button" class="btn btn-register" onclick="window.location.href = 'Register.jsp'">REGISTRARSE</button>
                </form>
            </div>
        </div>
    </body>
</html>
