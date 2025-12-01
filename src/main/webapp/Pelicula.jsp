<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pelicula" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Películas</title>

    <!-- Bootstrap 4 CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosAdmin/Pelicula.css">
</head>
<body>

    <!-- Barra lateral -->
    <nav class="sidebar">
        <div class="sidebar-header">CINEMAX</div>

        <div class="profile">
            <img src="Cliente/images/User.png" alt="Administrador" />
            <h5>Administrador</h5>
            <small>Admin</small>
        </div>

        <nav class="nav flex-column">
            <a href="AdminDashboardServlet" class="nav-link">
                <i class="fas fa-th-large mr-2"></i>Dashboard
            </a>
            <a href="UsuarioServlet?action=listar" class="nav-link">
                <i class="fas fa-users mr-2"></i>Usuarios
            </a>
            <a href="ProductoServlet?action=listar" class="nav-link">
                <i class="fas fa-box mr-2"></i>Productos
            </a>
            <a href="EmpleadoServlet?action=listar" class="nav-link">
                <i class="fas fa-user-tie mr-2"></i>Empleados
            </a>
            <a href="PeliculaServlet?action=listar" class="nav-link active">
                <i class="fas fa-film mr-2"></i>Películas
            </a>
            <a href="FuncionServlet?action=listar" class="nav-link">
                <i class="fas fa-clock mr-2"></i>Funciones
            </a>            
            <a href="<%= request.getContextPath() %>/LogoutServlet" class="nav-link">
                <i class="fas fa-sign-out-alt mr-2"></i> Cerrar Sesión
            </a>
        </nav>
    </nav>
    <!-- Contenido principal -->
    <main class="content">
        <div class="table-container">
            <h3 class="text-center">Lista de Películas</h3>

            <!-- Botón para agregar nueva película -->
            <a href="PeliculaServlet?action=nuevo" class="btn btn-success btn-agregar mb-3">
                <i class="fas fa-plus"></i> Agregar Película
            </a>
            <!-- Tabla de películas -->
            <table class="table table-bordered table-striped table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Sinopsis</th>
                        <th>Horario</th>
                        <th>Foto</th>
                        <th>Tráiler</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Pelicula> listaPeliculas = (List<Pelicula>) request.getAttribute("listaPeliculas");
                        if (listaPeliculas != null && !listaPeliculas.isEmpty()) {
                            for (Pelicula pelicula : listaPeliculas) {
                    %>
                    <tr>
                        <td><%= pelicula.getIdPelicula() %></td>
                        <td><%= pelicula.getNombre() %></td>
                        <td><%= pelicula.getSinopsis() %></td>
                        <td><%= pelicula.getFechaEstreno() %></td>
                        <td>
                            <%
                                byte[] foto = pelicula.getFoto();
                                if (foto != null) {
                                    String base64Image = java.util.Base64.getEncoder().encodeToString(foto);
                            %>
                            <img src="data:image/jpeg;base64,<%= base64Image %>" alt="Foto" style="width: 60px; height: auto;" />
                            <%
                                } else {
                            %>
                            Sin foto
                            <% } %>
                        </td>
                        <td><!-- Aquí podrías agregar un link o ícono de tráiler --></td>
                        <td class="text-center">
                            <a href="PeliculaServlet?action=editar&id=<%= pelicula.getIdPelicula() %>" 
                               class="btn btn-primary btn-sm d-block mb-2" style="width: 100px;">
                               Editar
                            </a>
                            <a href="PeliculaServlet?action=eliminar&id=<%= pelicula.getIdPelicula() %>" 
                               class="btn btn-danger btn-sm d-block" style="width: 100px;"
                               onclick="return confirm('¿Está seguro de eliminar esta película?');">
                               Eliminar
                            </a>
                        </td>
                    </tr>
                    <% 
                            }
                        } else { 
                    %>
                    <tr>
                        <td colspan="7" class="text-center">No hay películas disponibles.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </main>
    <!-- Bootstrap JS y dependencias -->
        <script src="<%= request.getContextPath() %>/Cliente/lib/jquery/jquery-3.6.4.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/popper/popper.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/bootstrap/js/bootstrap-4.6.2.min.js"></script>
</body>
</html>
