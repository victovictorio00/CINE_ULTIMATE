<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <title>Registro</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosAdmin/Register.css">
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

        <div class="login-container mt-2">
            <div class="login-card">
                <h3>Crear cuenta</h3>

                <!-- Mensaje de error opcional del servidor -->
                <%
                    String err = (String) request.getAttribute("error");
                    if (err != null) {
                %>
                <div class="alert alert-danger" role="alert"><%= err%></div>
                <% }%>

                <!-- IMPORTANTE: ajusta action si tu servlet usa otra ruta -->
                <form id="registroForm" action="UsuarioServlet?action=insertarcliente" method="post" novalidate>
                    <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">

                    <!-- Ocultos: id_rol (cliente), id_estado_usuario (activo), numeroIntentos (3) -->
                    <input type="hidden" name="idRol" value="1" />
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

                    <div class="g-recaptcha" 
                         data-sitekey="6LcDkRosAAAAAD7Ig-GC9IWUsxRQiKjVL8zCJA05">
                    </div>
                    <button type="submit" class="btn btn-login mt-2">Registrar</button>
                    <button type="button" class="btn btn-register" onclick="window.location.href = 'Login.jsp'">Regresar</button>
                    <div class="small-note">Al registrarte aceptas nuestros términos y políticas.</div>
            </div>
        </form>
    </div>
</div>

<script src="<%= request.getContextPath() %>/Cliente/lib/sha256/sha256.min.js"></script>
<script src="${pageContext.request.contextPath}/Cliente/JS/Register.js"></script>

</body>
</html>