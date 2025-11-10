<%-- 
    Document   : AdminDashboard
    Created on : 26 may. 2025, 16:02:00
    Author     : Proyecto
    Descripción: Página principal del panel de administración del sistema CineMax.
                 Permite al administrador visualizar métricas globales y navegar
                 entre los módulos del sistema.
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%
    // ==========================================================
    // VALIDACIÓN DE SESIÓN Y ROL
    // ==========================================================
    // Se obtiene la sesión actual. Si no existe o el usuario no tiene el rol "admin",
    // se redirige al Login.jsp para evitar accesos no autorizados.
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
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Dashboard Administrador</title>

    <!-- ==========================================================
         LIBRERÍAS CSS
         ========================================================== -->
    <!-- Bootstrap (estructura y estilos base) -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />
    <!-- Font Awesome (iconos del menú) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

    <style>
        /* ==========================================================
           ESTILOS DEL PANEL ADMINISTRATIVO
           ========================================================== */

        /* Estructura general del cuerpo */
        body {
            min-height: 100vh;
            display: flex;
            overflow-x: hidden;
        }

        /* Barra lateral (menú fijo a la izquierda) */
        .sidebar {
            min-width: 250px;
            max-width: 250px;
            background-color: #0d6efd; /* azul principal */
            color: white;
            min-height: 100vh;
            position: fixed;
            top: 0; left: 0;
            padding-top: 1rem;
        }

        /* Encabezado del menú lateral */
        .sidebar .sidebar-header {
            text-align: center;
            font-weight: bold;
            font-size: 1.5rem;
            margin-bottom: 2rem;
        }

        /* Perfil de usuario dentro del menú */
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

        /* Estilo de los enlaces del menú */
        .sidebar .nav-link {
            color: white;
            padding: 1rem 1.5rem;
            font-weight: 500;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: #084298; /* azul oscuro al pasar el mouse */
            color: white;
        }

        /* Contenedor principal del contenido */
        .content {
            margin-left: 250px; /* espacio para el sidebar */
            padding: 2rem;
            width: 100%;
        }

        /* Tarjetas de estadísticas (resumen) */
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

        /* Sección del gráfico (simulada por un bloque visual) */
        #chart-placeholder {
            height: 300px;
            background: #ffe5e5; /* color de fondo */
            border-radius: 0.5rem;
            padding: 1rem;
            margin-top: 2rem;
            color: #b30000; /* texto rojo */
            font-weight: bold;
            text-align: center;
            line-height: 300px; /* centra el texto verticalmente */
            font-size: 1.25rem;
            user-select: none;
        }
    </style>
</head>
<body>

    <!-- ==========================================================
         BARRA LATERAL DE NAVEGACIÓN
         ========================================================== -->
    <nav class="sidebar">
        <div class="sidebar-header">CINEMAX</div>

        <!-- Perfil del administrador (imagen y nombre) -->
        <div class="profile">
            <img src="Cliente/images/User.png" alt="Administrador" />
            <h5>Administrador</h5>
            <small>Admin</small>
        </div>

        <!-- Enlaces de navegación -->
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

    <!-- ==========================================================
         CONTENIDO PRINCIPAL DEL DASHBOARD
         ========================================================== -->
    <main class="content">
        <h2>Dashboard</h2>

        <!-- Tarjetas de resumen con métricas globales -->
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

        <!-- Espacio para el gráfico (aún simulado) -->
        <div id="chart-placeholder">
            Ventas Mensuales (Gráfico de ejemplo)
        </div>
    </main>

    <!-- ==========================================================
         LIBRERÍAS JS (BOOTSTRAP Y DEPENDENCIAS)
         ========================================================== -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
