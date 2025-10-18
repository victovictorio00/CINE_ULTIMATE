<%@page import="java.util.List"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.Rol"%>
<%@page import="modelo.EstadoUsuario"%>
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // 🔐 Verificación de sesión y rol
    HttpSession sesion = request.getSession(false);

    if (sesion == null || sesion.getAttribute("rol") == null ||
        !"admin".equals(sesion.getAttribute("rol"))) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Lista de Usuarios</title>

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

        /* Tabla de usuarios */
        .table-container {
            background: white;
            padding: 1.5rem;
            border-radius: 0.5rem;
            box-shadow: 0 0 12px rgb(0 0 0 / 0.1);
            max-width: 1100px;
            margin: auto;
        }
        .btn-agregar {
            margin-bottom: 1rem;
        }
        .acciones a {
            margin-right: 10px;
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
            <a href="UsuarioServlet?action=listar" class="nav-link active">
                <i class="fas fa-users mr-2"></i>Usuarios
            </a>
            <a href="ProductoServlet?action=listar" class="nav-link">
                <i class="fas fa-box mr-2"></i>Productos
            </a>
            <a href="EmpleadoServlet?action=listar" class="nav-link">
                <i class="fas fa-user-tie mr-2"></i>Empleados
            </a>
            <a href="PeliculaServlet?action=listar" class="nav-link">
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
            <h3 class="text-center">Lista de Usuarios</h3>

            <!-- Botón para agregar nuevo usuario -->
            <a href="UsuarioServlet?action=nuevo" class="btn btn-primary btn-agregar">Agregar Usuario</a>

            <table class="table table-striped table-bordered table-hover">
                <thead class="thead-dark">
                <tr>
                    <th>ID</th>
                    <th>Rol</th>
                    <th>Estado</th>
                    <th>Nombre Completo</th>
                    <th>DNI</th>
                    <th>Usuario</th>
                    <th>Teléfono</th>
                    <th>Email</th>
                    <th>Dirección</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
                    if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                        for (Usuario usuario : listaUsuarios) {
                %>
                <tr>
                    <td><%= usuario.getIdUsuario() %></td>
                    <td><%= usuario.getIdRol() != null ? usuario.getIdRol().getNombre() : "Sin rol" %></td>
                    <td><%= usuario.getIdEstadoUsuario() != null ? usuario.getIdEstadoUsuario().getNombre() : "Sin estado" %></td>
                    <td><%= usuario.getNombreCompleto() %></td>
                    <td><%= usuario.getDni() %></td>
                    <td><%= usuario.getUsername() %></td>
                    <td><%= usuario.getTelefono() %></td>
                    <td><%= usuario.getEmail() %></td>
                    <td><%= usuario.getDireccion() %></td>
                    <td class="acciones">
                        <a href="UsuarioServlet?action=editar&idUsuario=<%= usuario.getIdUsuario() %>" class="btn btn-sm btn-info">Editar</a>
                        <a href="UsuarioServlet?action=eliminar&id=<%= usuario.getIdUsuario() %>" class="btn btn-sm btn-danger" onclick="return confirm('¿Está seguro de eliminar este usuario?');">Eliminar</a>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="10" class="text-center">No hay usuarios registrados.</td>
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
