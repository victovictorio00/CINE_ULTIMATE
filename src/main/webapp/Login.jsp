<%-- 
    Document   : Login
    Author     : Proyecto
--%>
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
    <style>
        body, html { height: 100%; margin: 0; font-family: 'Arial', sans-serif; }
        .video-bg { position: fixed; top: 0; left: 0; width: 100%; height: 100%; overflow: hidden; z-index: -2; }
        .video-bg video { width: 100%; height: 100%; object-fit: cover; }
        .overlay-red { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(178,0,0,0.7); z-index: -1; }
        .login-container { height: 100vh; display: flex; justify-content: center; align-items: center; padding: 15px; }
        .login-card { background: #fff; padding: 50px 40px; border-radius: 8px; max-width: 420px; width: 100%; box-shadow: 0 8px 25px rgba(0,0,0,0.3); }
        .login-card h3 { font-weight: 700; text-align: center; margin-bottom: 25px; color: #3b0000; }
        .form-control { border-radius: 0; margin-bottom: 20px; box-shadow: none; }
        .forgot-link { font-size: .85rem; color: #b20000; display: block; margin-bottom: 25px; text-decoration: none; }
        .forgot-link:hover { text-decoration: underline; }
        .btn-login { background-color: #b20000; border: none; width: 100%; padding: 12px; font-weight: bold; color: #fff; border-radius: 0; transition: .3s ease; }
        .btn-login:hover { background-color: #7f0000; }
        .btn-register { border: 1px solid #b20000; background: transparent; color: #b20000; width: 100%; padding: 12px; font-weight: bold; margin-top: 15px; border-radius: 0; transition: .3s ease; }
        .btn-register:hover { background-color: #b20000; color: #fff; }
    </style>
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

        <% if (error != null) { %>
          <div class="alert alert-danger" role="alert"><%= error %></div>
        <% } %>

        <!-- Usa el contextPath para que funcione en cualquier contexto -->
        <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
            <input type="text"
                   name="username"
                   class="form-control"
                   placeholder="Usuario"
                   required
                   autocomplete="username"
                   value="<%= (lastUsername != null) ? lastUsername : "" %>" />

            <input type="password"
                   name="password"
                   class="form-control"
                   placeholder="Contraseña"
                   required
                   autocomplete="current-password" />

            <a href="#" class="forgot-link">Olvidé mi contraseña</a>

            <button type="submit" class="btn btn-login">ENTRAR</button>
            <button type="button" class="btn btn-register" onclick="window.location.href='Register.jsp'">REGISTRARSE</button>
        </form>
    </div>
</div>

</body>
</html>
