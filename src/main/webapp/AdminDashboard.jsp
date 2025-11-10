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
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body {
            min-height: 100vh;
            display: flex;
            overflow-x: hidden;
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
        .sidebar .nav-link {
            color: white;
            padding: 1rem 1.5rem;
            font-weight: 500;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: #084298;
            color: white;
        }
        .content {
            margin-left: 250px;
            padding: 2rem;
            width: 100%;
            background-color: #f8f9fa;
        }
        .stats-card {
            border-radius: 0.5rem;
            padding: 1.5rem;
            background: #ffffff;
            box-shadow: 0 0 10px rgb(0 0 0 / 0.05);
            text-align: center;
        }
        .stats-card h5 {
            font-weight: 600;
            color: #343a40;
        }
        .stats-card p {
            font-size: 1.2rem;
            font-weight: bold;
            margin: 0;
            color: #0d6efd;
        }
        #chart-container {
            background: #fff;
            border-radius: 0.5rem;
            box-shadow: 0 0 10px rgb(0 0 0 / 0.1);
            padding: 1rem 2rem;
            margin-top: 2rem;
        }
    </style>
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
        <h2>Dashboard</h2>

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

    <!-- JS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

    <%
    List<Double> ventasMensuales = (List<Double>) request.getAttribute("ventasMensuales");
    if (ventasMensuales == null) {
        ventasMensuales = java.util.Collections.nCopies(12, 0.0);
    }
%>

<div id="chart-container">
    <canvas id="ventasChart"></canvas>
</div>

<script>
    const ctx = document.getElementById('ventasChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
            datasets: [{
                label: 'Ventas Mensuales (S/)',
                data: <%= ventasMensuales.toString() %>,
                backgroundColor: 'rgba(13, 110, 253, 0.8)',
                borderColor: '#0d6efd',
                borderWidth: 2,
                hoverBackgroundColor: '#084298'
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true },
                x: {}
            },
            plugins: {
                legend: { display: false },
                title: {
                    display: true,
                    text: 'Ventas Mensuales - 2025',
                    color: '#084298',
                    font: { size: 18, weight: 'bold' }
                }
            }
        }
    });
</script>


</body>
</html>
