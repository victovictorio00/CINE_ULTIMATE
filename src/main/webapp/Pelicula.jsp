<%-- 
    Document   : Pelicula
    Created on : 27 may. 2025, 23:29:04
    Author     : Proyecto
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pelicula" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Películas</title>

    <!-- Bootstrap 4 CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

    <style>
        /* Barra lateral fija */
        body {
            min-height: 100vh;
            display: flex;
            overflow-x: hidden;
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
        }
        .sidebar {
            min-width: 250px;
            max-width: 250px;
            background-color: #0d6efd;
            color: white;
            min-height: 100vh;
            position: fixed;
            top: 0; left: 0;
            padding-top: 1rem;
        }
        .sidebar .sidebar-header {
            text-align: center;
            font-weight: bold;
            font-size: 1.5rem;
            margin-bottom: 2rem;
        }
        .sidebar .profile {
            text-align: center;
            margin-bottom: 2rem;
        }
        .sidebar .profile img {
            width: 80px;
            border-radius: 50%;
            margin-bottom: 0.5rem;
        }
        .sidebar .profile h5, .sidebar .profile small {
            margin: 0;
        }
        .sidebar .nav-link {
            color: white;
            padding: 1rem 1.5rem;
            font-weight: 500;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: #084298;
            color: white;
        }

        /* Contenido principal */
        .content {
            margin-left: 250px;
            padding: 2rem;
            width: 100%;
        }

        /* Tabla de películas */
        .table-container {
            background: white;
            padding: 1.5rem;
            border-radius: 0.5rem;
            box-shadow: 0 0 12px rgb(0 0 0 / 0.1);
            max-width: 1000px;
            margin: auto;
        }
        .btn-agregar {
            margin-bottom: 1rem;
        }
    </style>
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
            <a href="AdminDashboard.jsp" class="nav-link">
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
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
