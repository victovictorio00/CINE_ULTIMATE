<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.ArrayList, java.util.Collections" %>

<%
    HttpSession sesion = request.getSession(false);
    if (sesion == null || sesion.getAttribute("rol") == null ||
        !"admin".equals(sesion.getAttribute("rol"))) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }

    double totalVentas = (request.getAttribute("totalVentas") != null)
            ? (Double) request.getAttribute("totalVentas") : 0.0;
    int totalProductos = (request.getAttribute("totalProductos") != null)
            ? (Integer) request.getAttribute("totalProductos") : 0;
    int totalEmpleados = (request.getAttribute("totalEmpleados") != null)
            ? (Integer) request.getAttribute("totalEmpleados") : 0;
    int totalPeliculas = (request.getAttribute("totalPeliculas") != null)
            ? (Integer) request.getAttribute("totalPeliculas") : 0;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Dashboard Administrador</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
    <script src="<%= request.getContextPath() %>/Cliente/lib/chart/chart.min.js"></script>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosAdmin/AdminDashboard.css">
</head>

<body>
    <!-- SIDEBAR -->
    <nav class="sidebar">
        <div class="sidebar-header">CINEMAX</div>
        <div class="profile">
            <img src="Cliente/images/User.png" alt="Administrador" />
            <h5>Administrador</h5>
            <small>Admin</small>
        </div>
        <nav class="nav flex-column">
            <a href="AdminDashboard.jsp" class="nav-link active">
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

    <!-- CONTENIDO -->
    <main class="content">
        <h2 class="mb-5">Dashboard</h2>

        <div class="row text-center">
            <div class="col-md-3 mb-3">
                <div class="stats-card">
                    <h5>Total Ventas</h5>
                    <p>S/ <%= String.format("%.2f", totalVentas) %></p>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stats-card">
                    <h5>Total Productos</h5>
                    <p><%= totalProductos %></p>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stats-card">
                    <h5>Total Empleados</h5>
                    <p><%= totalEmpleados %></p>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stats-card">
                    <h5>Películas en Inventario</h5>
                    <p><%= totalPeliculas %></p>
                </div>
            </div>
        </div>

        <div id="chart-container">
            <canvas id="ventasChart"></canvas>
        </div>
    </main>

    <!-- JS de Bootstrap -->
        <script src="<%= request.getContextPath() %>/Cliente/lib/jquery/jquery-3.6.4.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/popper/popper.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/bootstrap/js/bootstrap-4.6.2.min.js"></script>

    <%-- Preparar datos de ventas mensuales --%>
    <%
        List<Double> ventasMensuales = (List<Double>) request.getAttribute("ventasMensuales");
        if (ventasMensuales == null) {
            ventasMensuales = java.util.Collections.nCopies(12, 0.0);
        }
    %>

    <%-- Inicializar datos del dashboard --%>
    <script>
        window.adminDashboardData = {
            ventasMensuales: <%= ventasMensuales.toString() %>,
            meses: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic']
        };
    </script>

    <%-- Cargar lógica del gráfico --%>
    <script src="<%= request.getContextPath() %>/Admin/JS/adminDashboardChart.js"></script>
</body>
</html>