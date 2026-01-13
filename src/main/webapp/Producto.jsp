<%@page import="java.util.Base64"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Producto" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
    <title>Lista de Productos</title>

    <!-- Bootstrap 4 -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosAdmin/Producto.css">
</head>
<body>
    <jsp:include page="/Cliente/accesibilidad/accesibilidad.jsp" />
    <!-- Sidebar -->
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
            <a href="ProductoServlet?action=listar" class="nav-link active">
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
            <h3 class="text-center mb-4">Lista de Productos</h3>

            <a href="ProductoServlet?action=nuevo" class="btn btn-success btn-agregar mb-3">
                <i class="fas fa-plus"></i> Agregar Producto
            </a>

            <table class="table table-striped table-bordered table-hover text-center">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Precio</th>
                        <th>Descripción</th>
                        <th>Foto</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Producto> listaProductos = (List<Producto>) request.getAttribute("listaProductos");
                        if (listaProductos != null && !listaProductos.isEmpty()) {
                            for (Producto producto : listaProductos) {
                    %>
                    <tr>
                        <td><%= producto.getIdProducto() %></td>
                        <td><%= producto.getNombre() %></td>
                        <td>S/ <%= producto.getPrecio() %></td>
                        <td><%= producto.getDescripcion() %></td>
                        <td>
                            <%
                                byte[] foto = producto.getFoto();
                                if (foto != null) {
                                    String base64Image = Base64.getEncoder().encodeToString(foto);
                            %>
                            <img src="data:image/jpeg;base64,<%= base64Image %>" alt="Foto" class="product-img" />

                            <%
                                } else {
                            %>
                            <small>Sin foto</small>
                            <% } %>
                        </td>
                        <td class="text-center acciones">
                            <a href="ProductoServlet?action=editar&id=<%= producto.getIdProducto() %>" 
                               class="btn btn-primary btn-sm">Editar</a>
                            <a href="ProductoServlet?action=eliminar&id=<%= producto.getIdProducto() %>" 
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('¿Está seguro de eliminar este producto?');">Eliminar</a>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="6" class="text-center text-muted">No hay productos disponibles.</td>
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