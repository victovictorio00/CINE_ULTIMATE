<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="modelo.Producto" %>
<%@ page import="modelo.Funcion" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.LinkedHashMap" %>

<%
    // ===== VALIDAR FLUJO DE COMPRA =====
    Funcion funcionSeleccionada = (Funcion) session.getAttribute("funcionSeleccionada");
    String asientosSeleccionados = (String) session.getAttribute("asientosSeleccionados");
    Double totalAsientos = (Double) session.getAttribute("totalAsientos");
    
    boolean esFlujoDeCompra = (funcionSeleccionada != null && asientosSeleccionados != null && totalAsientos != null);
    
    // ===== OBTENER PRODUCTOS CATEGORIZADOS =====
    Map<String, List<Producto>> productosCategorizados
            = (Map<String, List<Producto>>) request.getAttribute("productosCategorizados");

    if (productosCategorizados == null) {
        productosCategorizados = new LinkedHashMap<>();
        productosCategorizados.put("COMBOS", new ArrayList<>());
        productosCategorizados.put("BEBIDAS", new ArrayList<>());
        productosCategorizados.put("DULCES", new ArrayList<>());
        productosCategorizados.put("CANCHITA", new ArrayList<>());
        productosCategorizados.put("SNACKS", new ArrayList<>());
        productosCategorizados.put("OTROS", new ArrayList<>());
    }
    
    // ===== CARRITO EXISTENTE (para persistencia) =====
    Map<Integer, Integer> carritoExistente = (Map<Integer, Integer>) session.getAttribute("carritoDulceria");
    if (carritoExistente == null) {
        carritoExistente = new LinkedHashMap<>();
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dulcería | Productos</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/lib/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/Estilos/peliculaClienteStyle.css"> 
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosCliente/DulceriaCliente.css">
    </head>
    <body>
        <jsp:include page="/Cliente/accesibilidad/accesibilidad.jsp" />
        <%-- NAVBAR --%>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="#">CineMax</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/DashboardServlet">Inicio</a></li>
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath()%>/CarteleraServlet">Películas</a></li>
                    <li class="nav-item active"><a class="nav-link" href="<%= request.getContextPath()%>/DulceriaServlet">Dulcería</a></li>

                    <%
                        String username = (String) session.getAttribute("username");
                        String nombreCompleto = (String) session.getAttribute("nombreCompleto");
                        if (username == null || username.isEmpty()) {
                    %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath()%>/Login.jsp">Mi Cuenta</a>
                    </li>
                    <% } else {%>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown">
                            Hola, <%= (nombreCompleto != null && !nombreCompleto.isEmpty()) ? nombreCompleto.split(" ")[0] : username%>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right">
                            <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/ClienteServlet?action=misReservas">Mis Reservas</a>
                            <a class="dropdown-item text-white bg-dark" href="<%= request.getContextPath()%>/Cliente/PerfilCliente.jsp">Mi Perfil</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="<%= request.getContextPath()%>/LogoutServlet">Cerrar Sesión</a>
                        </div>
                    </li>
                    <% }%>
                </ul>
            </div>
        </nav>
        <div class="container py-4">
            <% if (esFlujoDeCompra) { %>
            <div class="resumen-compra">
                <h4><i class="fas fa-ticket-alt"></i> Tu Reserva</h4>
                <div class="info-item">
                    <span>Película:</span>
                    <span><strong><%= funcionSeleccionada.getPelicula().getNombre() %></strong></span>
                </div>
                <div class="info-item">
                    <span>Asientos:</span>
                    <span><strong><%= asientosSeleccionados %></strong></span>
                </div>
                <div class="info-item">
                    <span>Subtotal Entradas:</span>
                    <span><strong>S/. <%= String.format("%.2f", totalAsientos) %></strong></span>
                </div>
            </div>
            <div class="alert-info-compra">
                <i class="fas fa-info-circle"></i>
                <strong>Opcional:</strong> Puedes agregar productos de dulcería o continuar sin ellos.
            </div>
            <% } else { %>
            <div class="alert alert-warning text-center">
                <i class="fas fa-shopping-cart"></i>
                Estás viendo el catálogo. Para comprar, primero <a href="<%= request.getContextPath()%>/CarteleraServlet">selecciona una película</a>.
            </div>
            <% } %>
            <h1 class="page-header mt-4">Dulcería</h1>
            <%
                boolean isFirstTab = true;
            %>
            <nav class="category-nav">
                <div class="nav nav-tabs border-0" id="nav-tab" role="tablist">
                    <%
                        for (String categoria : productosCategorizados.keySet()) {
                            String tabId = "tab-" + categoria.replaceAll("[^a-zA-Z0-9]", "");
                            String activeClass = isFirstTab ? "active" : "";
                    %>
                    <a class="nav-link <%= activeClass%>" 
                       id="nav-<%= categoria%>-tab" 
                       data-toggle="tab" 
                       href="#<%= tabId%>" 
                       role="tab">
                        <%= categoria.toUpperCase()%>
                    </a>
                    <%
                            isFirstTab = false;
                        }
                    %>
                </div>
            </nav>
            <div class="tab-content" id="nav-tabContent">
                <%
                    isFirstTab = true;
                    for (Map.Entry<String, List<Producto>> entry : productosCategorizados.entrySet()) {
                        String categoria = entry.getKey();
                        List<Producto> listaProductos = entry.getValue();
                        String tabId = "tab-" + categoria.replaceAll("[^a-zA-Z0-9]", "");
                        String activeContent = isFirstTab ? "show active" : "";
                %>
                <div class="tab-pane fade <%= activeContent%>" id="<%= tabId%>" role="tabpanel">
                    <div class="row pt-4">
                        <%
                            if (listaProductos != null) {
                                for (Producto producto : listaProductos) {
                                    int cantidadInicial = carritoExistente.getOrDefault(producto.getIdProducto(), 0);
                                    String fotoBase64 = Base64.getEncoder().encodeToString(producto.getFoto());
                        %>
                        <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-4 d-flex align-items-stretch">
                            <article class="product-card shadow-sm">
                                <div class="product-image-wrap">
                                    <img src="data:image/jpeg;base64,<%= fotoBase64%>" 
                                         class="product-image" alt="<%= producto.getNombre()%>" loading="lazy">
                                </div>
                                <div class="product-body">
                                    <h3 class="product-title" title="<%= producto.getNombre()%>">
                                        <%= producto.getNombre()%>
                                    </h3>
                                    <p class="product-desc text-muted"><%= producto.getDescripcion()%></p>

                                    <div class="product-footer">
                                        <div class="price-wrap text-center">
                                            <span class="product-price">S/ <%= String.format("%.2f", producto.getPrecio())%></span>
                                        </div>

                                        <div class="quantity-control" 
                                             data-id="<%= producto.getIdProducto()%>"
                                             data-precio="<%= producto.getPrecio()%>">
                                            <button type="button" class="decrease" title="Quitar">
                                                <i class="fas fa-minus"></i>
                                            </button>
                                            <span class="quantity-value"><%= cantidadInicial%></span>
                                            <button type="button" class="increase" title="Agregar">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </article>
                        </div>
                        <%}
                            } %>
                    </div>
                </div>
                <%
                        isFirstTab = false;
                    }
                %>
            </div>
        </div>
        <% if (esFlujoDeCompra) { %>
        <button class="btn-flotante btn btn-success" id="btnContinuar" onclick="continuarConCompra()">
            <i class="fas fa-arrow-right"></i> Continuar
        </button>

        <%-- FORMULARIO OCULTO --%>
        <form id="carritoForm" method="POST" action="<%= request.getContextPath()%>/ClienteServlet">
            <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">
            <input type="hidden" name="action" value="seleccionarCombo">
            <div id="productosHidden"></div>
        </form>
        <% } %>

        <script src="<%= request.getContextPath() %>/Cliente/lib/jquery/jquery-3.6.4.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/popper/popper.min.js"></script>
        <script src="<%= request.getContextPath() %>/Cliente/lib/bootstrap/js/bootstrap-4.6.2.min.js"></script>
        <script>
            window.dulceriaData = {
                esFlujoCompra: <%= esFlujoDeCompra%>,
                carritoState: {}
            };
        </script>

        <%
            // Carga de la persistencia del carrito desde la sesión
            for (Map.Entry<Integer, Integer> entry : carritoExistente.entrySet()) {
                if (entry.getValue() > 0) {
        %>
        <script>
            window.dulceriaData.carritoState[<%= entry.getKey()%>] = <%= entry.getValue()%>;
        </script>
        <%
                }
            }
        %>

        <script src="<%= request.getContextPath()%>/Cliente/JS/DulceriaData.js"></script>
        <script src="<%= request.getContextPath()%>/Cliente/JS/DulceriaLogic.js"></script>
    </body>
</html>