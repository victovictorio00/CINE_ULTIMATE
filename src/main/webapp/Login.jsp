<%-- 
    Document   : Login
    Created on : 26 may. 2025, 15:25:16
    Author     : Proyecto
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Login</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" />
    <style>
        /* Video de fondo */
        body, html {
            height: 100%;
            margin: 0;
            font-family: 'Arial', sans-serif;
        }
        .video-bg {
            position: fixed;
            top: 0; left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            z-index: -2;
        }
        .video-bg video {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        /* Superposición roja suave */
        .overlay-red {
            position: fixed;
            top: 0; left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(178, 0, 0, 0.7); /* rojo con transparencia */
            z-index: -1;
        }

        /* Contenedor formulario centrado */
        .login-container {
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 15px;
        }

        /* Card del formulario */
        .login-card {
            background: white;
            padding: 60px 50px;
            border-radius: 8px;
            max-width: 450px;
            width: 100%;
            box-shadow: 0 8px 25px rgba(0,0,0,0.3);
        }

        .login-card h3 {
            font-weight: 700;
            text-align: center;
            margin-bottom: 25px;
            color: #3b0000;
        }

        .form-control {
            border-radius: 0;
            margin-bottom: 20px;
            box-shadow: none;
        }

        .forgot-link {
            font-size: 0.85rem;
            color: #b20000;
            display: block;
            margin-bottom: 25px;
            text-decoration: none;
        }

        .forgot-link:hover {
            text-decoration: underline;
        }

        .btn-login {
            background-color: #b20000;
            border: none;
            width: 100%;
            padding: 12px;
            font-weight: bold;
            color: white;
            border-radius: 0;
            transition: background-color 0.3s ease;
        }

        .btn-login:hover {
            background-color: #7f0000;
        }

        .btn-register {
            border: 1px solid #b20000;
            background: transparent;
            color: #b20000;
            width: 100%;
            padding: 12px;
            font-weight: bold;
            margin-top: 15px;
            border-radius: 0;
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        .btn-register:hover {
            background-color: #b20000;
            color: white;
        }
    </style>
</head>
<body>

<div class="video-bg">
    <video autoplay muted loop>
        <source src="Cliente/videos/video.mp4" type="video/mp4" />
       
    </video>
</div>

<div class="overlay-red"></div>

<div class="login-container">
    <div class="login-card">
        <h3>Ingresa con tu cuenta CineOnline</h3>
        <form action="LoginServlet" method="post">
            <input type="text" name="username" class="form-control" placeholder="Usuario" required />
            <input type="password" name="password" class="form-control" placeholder="Contraseña" required />
<!--          <a href="#" class="forgot-link">Olvidé mi contraseña</a>-->
            <button type="submit" class="btn btn-login">ENTRAR</button>
            <!--<button type="button" class="btn btn-register" onclick="window.location.href='Registro.jsp'">REGISTRARSE</button>-->
        </form>
    </div>
</div>

</body>
</html>
