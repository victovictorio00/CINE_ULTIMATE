<%-- 
    Document   : AdminDashboard
    Created on : 26 may. 2025, 16:02:00
    Author     : Proyecto
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Dashboard Administrador</title>

    <!-- Bootstrap 4 CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />

    <!-- FontAwesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

    <style>
        /* Barra lateral fija */
        body {
            min-height: 100vh;
            display: flex;
            overflow-x: hidden;
        }
        .sidebar {
            min-width: 250px;
            max-width: 250px;
            background-color: #0d6efd; /* azul bootstrap */
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
            background-color: #084298; /* azul más oscuro */
            color: white;
        }

        /* Contenido principal */
        .content {
            margin-left: 250px;
            padding: 2rem;
            width: 100%;
        }

        /* Tarjetas resumen */
        .stats-card {
            border-radius: 0.5rem;
            padding: 1.5rem;
            background: #f8f9fa;
            box-shadow: 0 0 10px rgb(0 0 0 / 0.05);
            text-align: center;
        }
        .stats-card h5 {
            font-weight: 600;
        }
        .stats-card p {
            font-size: 1.2rem;
            font-weight: bold;
            margin: 0;
        }

        /* Gráfico simulado */
        #chart-placeholder {
            height: 300px;
            background: #ffe5e5;
            border-radius: 0.5rem;
            padding: 1rem;
            margin-top: 2rem;
            color: #b30000;
            font-weight: bold;
            text-align: center;
            line-height: 300px;
            font-size: 1.25rem;
            user-select: none;
        }
    </style>
</head>
<body>

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
            <a href="http://localhost:8080/CineJ3/ClienteServlet?action=listar" class="nav-link">
                <i class="fas fa-sign-out-alt mr-2"></i>Cerrar Sesión
            </a>
        </nav>
    </nav>

    <main class="content">
        <h2>Dashboard</h2>

        <div class="row text-center">
            <div class="col-md-3 mb-3">
                <div class="stats-card">
                    <h5>Total Ventas</h5>
                    <p>$ 10,000</p>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stats-card">
                    <h5>Total Productos</h5>
                    <p>150</p>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stats-card">
                    <h5>Total Empleados</h5>
                    <p>25</p>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="stats-card">
                    <h5>Películas en Inventario</h5>
                    <p>30</p>
                </div>
            </div>
        </div>

        <div id="chart-placeholder">
            Ventas Mensuales (Gráfico de ejemplo)
        </div>

        <!-- Aquí puedes agregar más contenido, tablas, gráficos, etc -->
    </main>

    <!-- JS y dependencias de Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
