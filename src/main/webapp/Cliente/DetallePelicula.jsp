    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ page import="modelo.Cliente.Pelicula" %>
    <%@ page import="modelo.Cliente.PeliculaDaoCliente" %>
    <%@ page import="javax.servlet.RequestDispatcher" %>

    <!DOCTYPE html>
    <html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Detalle de la Película</title>
        <!-- Vinculamos Bootstrap CSS -->
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
        <style>
        /* Video en la parte superior */

    /* Espaciado entre el navbar y los detalles 
    .container {
        position: relative;
        z-index: 2;
        padding-top: 100px; /* Espaciado para que no se solape con la navbar 
    }
    */


    .movie-video {
        width: 100%; /* El video ocupa todo el ancho de la pantalla */
        height: 60vh;  /* El video ocupa el 60% de la altura de la pantalla */
        position: relative; /* Ya no es fixed, solo ocupa el espacio necesario */
    }

    .movie-video iframe {
        width: 100%; /* El video ocupa todo el ancho de la pantalla */
        height: 100%;  /* El video ocupa el 100% de la altura del contenedor */
        border: none;
        object-fit: cover; /* Asegura que el video se recorte adecuadamente para llenar el espacio */
    }



    /* Contenedor debajo del video (para detalles) */
    .movie-details-container {
        position: relative;
        z-index: 2;
        padding-top: 20px; /* Espaciado para no solaparse con el video */
    }

    /* Detalles de la película */
    .movie-details {
        padding: 30px;
        background-color: #fff;
        box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
        margin-top: 80px; /* Empuja los detalles hacia abajo, para no solaparse con el navbar */
    }


    /* Imagen de la película */
    .movie-details img {
        width: 100%; /* Ajusta el valor según el tamaño deseado */
        height: auto; /* Mantiene la proporción de la imagen */

    }



    .movie-details h3 {
        margin-bottom: 20px;
    }

    /* Estilos para los botones en el navbar */

    button.btn {
        margin: 5px; /* Espaciado entre botones */
    }


    /* Cabecera */
    .navbar {
        position: fixed; /* Fija el navbar en la parte superior */
        top: 0;
        width: 100%;
        z-index: 10; /* Asegura que el navbar esté por encima del video */
        background-color: rgba(0, 0, 0, 0.7); /* Fondo negro con transparencia */
        border-bottom: 1px solid white; /* Línea blanca debajo del navbar */
        height: 80px; /* Altura del navbar */
        padding-top: 10px;
    }

    /* Estilos para los botones en el navbar */
    .navbar-nav {
        width: 100%;
        display: flex;
        justify-content: center; /* Centra los elementos del navbar */
    }


    .nav-item {
        margin: 0 15px; /* Espaciado entre los items */
    }

    /* Pie de página pegado hacia abajo */
    footer {
        background-color: #343a40;
        color: white;
        text-align: center;
        padding: 10px;
        position: relative;
        bottom: 0;
        width: 100%;
    }

    /* Asegura que el pie de página se quede pegado al fondo */
    html, body {
        height: 100%;
        margin: 0;
        display: flex;
        flex-direction: column;
    }

    /* Cuerpo de la página */
    body {
        flex: 1; /* Esto hace que el cuerpo ocupe todo el espacio disponible */
    }



    /* Espacio entre el contenedor de detalles de la película y el pie de página */
    .movie-details-container {
        margin-bottom: 100px; /* Ajusta el valor a tu preferencia */
    }



        </style>
    </head>
    <body>

    <%
        // Obtener el parámetro de la URL
        String peliculaId = request.getParameter("id");

        // Convertir el parámetro a un entero (ID de la película)
        int id = Integer.parseInt(peliculaId);

        // Crear una instancia del DAO cliente y buscar la película
        PeliculaDaoCliente peliculaDaoCliente = new PeliculaDaoCliente();
        Pelicula pelicula = peliculaDaoCliente.getPeliculaById(id); // Pasa el ID como int

        // Si la película no existe, redirigir o mostrar un mensaje de error
        if (pelicula == null) {
            out.println("<h2>Pelicula no encontrada.</h2>");
            return;
        }
    %>

    <!-- Video en la parte superior -->
    <div class="movie-video">
        <iframe src="https://www.youtube.com/embed/HeTE7j9dcGg" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
    </div>

    <!-- Contenedor de los detalles de la película (debajo del video) -->
    <div class="container movie-details-container">
        <div class="row">
            <!-- Columna para la sinopsis -->
            <div class="col-md-6 movie-details">
                <h1><%= pelicula.getTitle() %> (<%= pelicula.getGenero() %>)</h1>
                <h3>Sinopsis</h3>
                <p><%= pelicula.getSinopsis() %></p>
                <h3>Horarios</h3>
                <!-- Aquí puedes agregar los horarios dinámicamente -->
                <button class="btn btn-primary">09:30 PM</button>
                <button class="btn btn-primary">12:00 PM</button>
                <button class="btn btn-primary">02:30 PM</button>


                <a href="<%= request.getContextPath() %>/ClienteServlet?action=reservar&id=<%= pelicula.getId() %>" class="btn btn-dark mt-3">Reservar</a>
            </div>




            <!-- Columna para la imagen -->
            <div class="col-md-6">
            <img src="http://localhost:8080/CineJ3/Cliente/images/pelicula6.jpg" class="card-img-top" alt="Blade Runner" style="max-width: 60%; height: auto; box-shadow: 0 0 30px 5px rgba(200, 200, 200, 0.7);">



            </div>
        </div>
    </div>



    <!-- Barra de navegación -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <a class="navbar-brand" href="http://localhost:8080/CineJ3/ClienteServlet?action=listar">CineOnline</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item active"><a class="nav-link" href="#">Inicio</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Películas</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Dulcería</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Mi Cuenta</a></li>
            </ul>
        </div>
    </nav>       

    <!-- Pie de página -->
    <footer>
        <p>© 2025 Cine Online | Todos los derechos reservados</p>
        <p><a href="#" class="text-white">Política de Privacidad</a> | <a href="#" class="text-white">Términos y Condiciones</a></p>
    </footer>

    <!-- Scripts de Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

    </body>
    </html>