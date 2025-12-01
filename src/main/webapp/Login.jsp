<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
    String lastUsername = (String) request.getAttribute("lastUsername");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="Estilos/loginStyle.css">
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
</head>
<body>
<jsp:include page="/Cliente/accesibilidad/accesibilidad.jsp" />
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

        <% if (error != null) { %>
        <div class="alert alert-danger" role="alert"><%= error %></div>
        <% } %>

        <form id="loginForm" action="<%= request.getContextPath()%>/LoginServlet" method="post">
            <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">
            
            <input type="text"
                   name="username"
                   class="form-control"
                   placeholder="Usuario"
                   required
                   autocomplete="username"
                   value="<%= (lastUsername != null) ? lastUsername : "" %>" />

            <!-- Cambiamos el nombre a password_temp para que no se envíe al servidor -->
            <input type="password"
                   name="password"
                   class="form-control"
                   placeholder="Contraseña"
                   required
                   autocomplete="current-password" />

            

            <a href="#" class="forgot-link">Olvidé mi contraseña</a>

            <input type="hidden" name="redirect"
                   value="${param.redirect != null ? param.redirect : ''}">
            <button type="submit" class="btn btn-login">Entrar</button>
            <button type="button" class="btn btn-register" onclick="window.location.href = 'Register.jsp'">Registrarse</button>
        </form>
    </div>
</div>
<script src="<%= request.getContextPath() %>/Cliente/lib/sha256/sha256.min.js"></script>
<script src="${pageContext.request.contextPath}/Cliente/JS/Login.js"></script>
</body>
</html>