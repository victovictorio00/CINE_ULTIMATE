<%-- 
    Document   : Register
    Author     : Proyecto
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Registro</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" />
    <style>
        body, html { height: 100%; margin: 0; font-family: 'Arial', sans-serif; }
        .video-bg { position: fixed; top: 0; left: 0; width: 100%; height: 100%; overflow: hidden; z-index: -2; }
        .video-bg video { width: 100%; height: 100%; object-fit: cover; }
        .overlay-red { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(178,0,0,0.7); z-index: -1; }
        .login-container { height: 100vh; display: flex; justify-content: center; align-items: center; padding: 15px; }
        .login-card { background: #fff; padding: 50px 40px; border-radius: 8px; max-width: 520px; width: 100%; box-shadow: 0 8px 25px rgba(0,0,0,0.3); }
        .login-card h3 { font-weight: 700; text-align: center; margin-bottom: 25px; color: #3b0000; }
        .form-control { border-radius: 0; margin-bottom: 14px; box-shadow: none; }
        .btn-login { background-color: #b20000; border: none; width: 100%; padding: 12px; font-weight: bold; color: white; border-radius: 0; transition: .3s ease; }
        .btn-login:hover { background-color: #7f0000; }
        .btn-register { border: 1px solid #b20000; background: transparent; color: #b20000; width: 100%; padding: 12px; font-weight: bold; margin-top: 10px; border-radius: 0; transition: .3s ease; }
        .btn-register:hover { background-color: #b20000; color: #fff; }
        .row-compact .col { padding-right: 7px; padding-left: 7px; }
        .small-note { font-size: .85rem; color: #555; margin-top: 4px; }
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
        <h3>Crear cuenta</h3>

        <!-- Mensaje de error opcional del servidor -->
        <%
          String err = (String) request.getAttribute("error");
          if (err != null) {
        %>
          <div class="alert alert-danger" role="alert"><%= err %></div>
        <% } %>

        <!-- IMPORTANTE: ajusta action si tu servlet usa otra ruta -->
        <form id="registroForm" action="UsuarioServlet?action=insertarcliente" method="post" novalidate>

            <!-- Ocultos: id_rol (cliente), id_estado_usuario (activo), numeroIntentos (3) -->
            <input type="hidden" name="idRol" value="2" />
            <input type="hidden" name="idEstadoUsuario" value="1" />
            <input type="hidden" name="numeroIntentos" value="3" />

            <!-- nombre_completo -->
            <input type="text" name="nombreCompleto" class="form-control" placeholder="Nombre completo" required maxlength="120" />

            <!-- dni (Perú: 8 dígitos) -->
            <input type="text" name="dni" class="form-control" placeholder="DNI (8 dígitos)" required
                   pattern="\\d{8}" inputmode="numeric" />

            <!-- username -->
            <input type="text" name="username" class="form-control" placeholder="Usuario" required maxlength="50" />

            <!-- password + confirm -->
            <input type="password" id="password" name="password" class="form-control" placeholder="Contraseña" 
                   required minlength="6" autocomplete="new-password" />
            <input type="password" id="passwordconfirm" name="passwordconfirm" class="form-control" 
                   placeholder="Confirmar contraseña" required minlength="6" autocomplete="new-password" />

            <!-- teléfono (Perú mobile: 9 dígitos) -->
            <input type="text" name="telefono" class="form-control" placeholder="Teléfono (9 dígitos)" 
                   pattern="\\d{9}" inputmode="numeric" />

            <!-- email -->
            <input type="email" name="email" class="form-control" placeholder="Correo electrónico" required maxlength="120" />

            <!-- dirección -->
            <input type="text" name="direccion" class="form-control" placeholder="Dirección" maxlength="200" />

            <button type="submit" class="btn btn-login mt-2">Registrar</button>
            <button type="button" class="btn btn-register" onclick="window.location.href='Login.jsp'">Regresar</button>

            <div class="small-note">Al registrarte aceptas nuestros términos y políticas.</div>
        </form>
    </div>
</div>

<script>
(function() {
    const form = document.getElementById("registroForm");
    form.addEventListener("submit", function(event) {
        const pass = document.getElementById("password").value.trim();
        const confirm = document.getElementById("passwordconfirm").value.trim();

        // Validación HTML5 general
        //if (!form.checkValidity()) {
          //  event.preventDefault();
            //event.stopPropagation();
            //alert("Revisa los campos resaltados. Asegúrate de completar los obligatorios y con el formato correcto.");
            //return;
        //}

        // Contraseñas iguales
        if (pass !== confirm) {
            event.preventDefault();
            alert("Las contraseñas no coinciden.");
            return;
        }

        // Reglas mínimas de contraseña (ejemplo)
        if (pass.length < 6) {
            event.preventDefault();
            alert("La contraseña debe tener al menos 6 caracteres.");
            return;
        }
    }, false);
})();
</script>
</body>
</html>
