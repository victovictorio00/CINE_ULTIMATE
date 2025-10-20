<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Funcion" %>
<%@ page import="modelo.Pelicula" %>
<%@ page import="modelo.Sala" %>

<%
    // 🔐 Verificación de sesión administrador
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
    <meta charset="UTF-8">
    <title>Funciones - CINEMAX</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    <style>
        body { min-height: 100vh; display: flex; overflow-x: hidden; font-family: Arial, sans-serif; background-color: #f8f9fa; }
        .sidebar { min-width: 250px; max-width: 250px; background-color: #0d6efd; color: white; min-height: 100vh; position: fixed; top: 0; left: 0; padding-top: 1rem; }
        .sidebar .sidebar-header { text-align: center; font-weight: bold; font-size: 1.5rem; margin-bottom: 2rem; }
        .sidebar .profile { text-align: center; margin-bottom: 2rem; }
        .sidebar .profile img { width: 80px; border-radius: 50%; margin-bottom: 0.5rem; }
        .sidebar .nav-link { color: white; padding: 1rem 1.5rem; font-weight: 500; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background-color: #084298; color: white; }
        .content { margin-left: 250px; padding: 2rem; width: 100%; }
        .table-container { background: white; padding: 1.5rem; border-radius: 0.5rem; box-shadow: 0 0 12px rgb(0 0 0 / 0.1); max-width: 1100px; margin: auto; }
        .acciones a { margin-right: 10px; }
    </style>
</head>

<body>
    <!-- Sidebar -->
    <nav class="sidebar">
        <div class="sidebar-header">CINEMAX</div>
        <div class="profile">
            <img src="Cliente/images/User.png" alt="Administrador" />
            <h5>Administrador</h5>
            <small>Admin</small>
        </div>
        <nav class="nav flex-column">
            <a href="AdminDashboard.jsp" class="nav-link"><i class="fas fa-th-large mr-2"></i>Dashboard</a>
            <a href="UsuarioServlet?action=listar" class="nav-link"><i class="fas fa-users mr-2"></i>Usuarios</a>
            <a href="ProductoServlet?action=listar" class="nav-link"><i class="fas fa-box mr-2"></i>Productos</a>
            <a href="EmpleadoServlet?action=listar" class="nav-link"><i class="fas fa-user-tie mr-2"></i>Empleados</a>
            <a href="PeliculaServlet?action=listar" class="nav-link"><i class="fas fa-film mr-2"></i>Películas</a>
            <a href="FuncionServlet?action=listar" class="nav-link active"><i class="fas fa-clock mr-2"></i>Funciones</a>
            <a href="<%= request.getContextPath() %>/LogoutServlet" class="nav-link"><i class="fas fa-sign-out-alt mr-2"></i>Cerrar Sesión</a>
        </nav>
    </nav>

    <!-- Contenido -->
    <main class="content">
        <div class="table-container">
            <h3 class="text-center mb-4">Lista de Funciones</h3>

            <!-- Botón Nueva función -->
            <button type="button" class="btn btn-success mb-3" data-toggle="modal" data-target="#modalNuevaFuncion">
                <i class="fas fa-plus"></i> Nueva Función
            </button>

            <!-- Tabla de funciones -->
            <table class="table table-striped table-bordered table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Película</th>
                        <th>Sala</th>
                        <th>Inicio</th>
                        <th>Fin</th>
                        <th>Estado</th>
                        <th>Asientos</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Funcion> lista = (List<Funcion>) request.getAttribute("listaFunciones");
                        if (lista != null && !lista.isEmpty()) {
                            for (Funcion f : lista) {
                    %>
                    <tr>
                        <td><%= f.getIdFuncion() %></td>
                        <td><%= f.getPelicula() != null ? f.getPelicula().getNombre() : "Sin película" %></td>
                        <td><%= f.getSala() != null ? f.getSala().getNombre() : "Sin sala" %></td>
                        <td><%= f.getFechaInicio() %></td>
                        <td><%= f.getFechaFin() %></td>
                        <td><%= f.getEstadoFuncion() != null ? f.getEstadoFuncion().getNombre() : "Sin estado" %></td>
                        <td><%= f.getAsientosDisponibles() %></td>
                        <td class="acciones">
                            <a href="FuncionServlet?action=editar&id=<%= f.getIdFuncion() %>"
                               class="btn btn-primary btn-sm">
                               <i class="fas fa-edit"></i> Editar
                            </a>
                            <a href="FuncionServlet?action=eliminar&id=<%= f.getIdFuncion() %>"
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('¿Seguro que deseas eliminar esta función?');">
                               <i class="fas fa-trash"></i> Eliminar
                            </a>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="8" class="text-center">No hay funciones registradas.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </main>

    <!-- Modal Nueva Función -->
    <div class="modal fade" id="modalNuevaFuncion" tabindex="-1" role="dialog" aria-labelledby="modalNuevaFuncionLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <form action="FuncionServlet?action=insertar" method="post">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title" id="modalNuevaFuncionLabel">Agregar Nueva Función</h5>
                        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    </div>
                    <div class="modal-body">

                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label>Película</label>
                                <select name="id_pelicula" class="form-control" required>
                                  <%
                                    List<Pelicula> listaPeliculas = (List<Pelicula>) request.getAttribute("listaPeliculas");
                                    if (listaPeliculas != null) {
                                        for (Pelicula p : listaPeliculas) {
                                  %>
                                    <option value="<%= p.getIdPelicula() %>"><%= p.getNombre() %></option>
                                  <%  }
                                    }
                                  %>
                                </select>
                            </div>
                            <div class="form-group col-md-6">
                                <label>Sala</label>
                                <select name="id_sala" class="form-control" required>
                                  <%
                                    List<Sala> listaSalas = (List<Sala>) request.getAttribute("listaSalas");
                                    if (listaSalas != null) {
                                        for (Sala s : listaSalas) {
                                  %>
                                    <option value="<%= s.getIdSala() %>"><%= s.getNombre() %></option>
                                  <%  }
                                    }
                                  %>
                                </select>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label>Fecha Inicio</label>
                                <input type="datetime-local" name="fecha_inicio" class="form-control" required>
                            </div>
                            <div class="form-group col-md-6">
                                <label>Fecha Fin</label>
                                <input type="datetime-local" name="fecha_fin" class="form-control" required>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label>Estado</label>
                                <select name="id_estado_funcion" class="form-control" required>
                                  <option value="1">Disponible</option>
                                  <option value="2">En curso</option>
                                  <option value="3">Finalizada</option>
                                </select>
                            </div>
                            <div class="form-group col-md-6">
                                <label>Asientos Disponibles</label>
                                <input type="number" name="asientos_disponibles" class="form-control" required min="1">
                            </div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Guardar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
