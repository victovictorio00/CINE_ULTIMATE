<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="modelo.Empleado" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // 🔐 VERIFICACIÓN DE SESIÓN ADMINISTRADOR
    HttpSession sesion = request.getSession(false);

    if (sesion == null || sesion.getAttribute("rol") == null ||
        !"admin".equals(sesion.getAttribute("rol"))) {
        // Si no hay sesión o el rol no es admin → redirige al login
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Empleados</title>

    <!-- Bootstrap 4 CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosAdmin/Empleado.css">
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
            <a href="EmpleadoServlet?action=listar" class="nav-link active">
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
            <h3 class="text-center">Lista de Empleados</h3>

            <!-- Botón para agregar nuevo empleado -->
            <a href="EmpleadoServlet?action=nuevo" class="btn btn-success btn-agregar mb-3">
                <i class="fas fa-plus"></i> Agregar Empleado
            </a>

            <table class="table table-striped table-bordered table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Dirección</th>
                        <th>Teléfono</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Empleado> lista = (List<Empleado>) request.getAttribute("listaEmpleados");
                        if (lista != null && !lista.isEmpty()) {
                            for (Empleado empleado : lista) {
                    %>
                    <tr>
                        <td><%= empleado.getIdEmpleado() %></td>
                        <td><%= empleado.getNombre() %></td>
                        <td><%= empleado.getDireccion() %></td>
                        <td><%= empleado.getTelefono() %></td>
                        <td class="text-center acciones">
                            <a href="EmpleadoServlet?action=editar&id=<%= empleado.getIdEmpleado() %>" 
                               class="btn btn-primary btn-sm">Editar</a>
                            <a href="EmpleadoServlet?action=eliminar&id=<%= empleado.getIdEmpleado() %>" 
                               class="btn btn-danger btn-sm" 
                               onclick="return confirm('¿Está seguro de eliminar este empleado?');">Eliminar</a>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="5" class="text-center">No hay empleados disponibles.</td>
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