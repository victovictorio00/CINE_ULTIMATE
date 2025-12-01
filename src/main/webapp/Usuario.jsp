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
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosAdmin/Usuario.css">
</head>
<body>
    <jsp:include page="/Cliente/accesibilidad/accesibilidad.jsp" />
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

            <!-- Botón para agregar usuario -->
            <a href="UsuarioServlet?action=nuevo" class="btn btn-success btn-agregar">
                <i class="fas fa-plus"></i> Agregar Usuario
            </a>

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
                    <td class="acciones text-center">
                        <a href="UsuarioServlet?action=editar&idUsuario=<%= usuario.getIdUsuario() %>" 
                           class="btn btn-primary btn-sm">Editar</a>
                        <a href="UsuarioServlet?action=eliminar&id=<%= usuario.getIdUsuario() %>" 
                           class="btn btn-danger btn-sm" 
                           onclick="return confirm('¿Está seguro de eliminar este usuario?');">Eliminar</a>
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

    <!-- Bootstrap JS -->
        <script src="<%= request.getContextPath() %>/Cliente/lib/jquery/jquery-3.6.4.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/popper/popper.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/bootstrap/js/bootstrap-4.6.2.min.js"></script>
</body>
</html>
