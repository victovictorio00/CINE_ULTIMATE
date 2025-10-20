<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="modelo.Producto" %> 
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.LinkedHashMap" %>

<%
    // Obtener el mapa de productos categorizados del Servlet
    Map<String, List<modelo.Producto>> productosCategorizados
            = (Map<String, List<modelo.Producto>>) request.getAttribute("productosCategorizados");

    // Inicializar mapa de respaldo si el Servlet no envió datos o falló.
    if (productosCategorizados == null) {
        productosCategorizados = new LinkedHashMap<>();
        productosCategorizados.put("COMBOS", new ArrayList<>());
        productosCategorizados.put("BEBIDAS", new ArrayList<>());
        productosCategorizados.put("DULCES", new ArrayList<>());
        productosCategorizados.put("PALOMITAS", new ArrayList<>());
        productosCategorizados.put("SNACKS", new ArrayList<>());
        productosCategorizados.put("OTROS", new ArrayList<>());
    }
    
    // Suponiendo que el carrito de la sesión se guarda como: Map<Integer (ID Producto), Integer (Cantidad)>
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
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">

        <link rel="stylesheet" href="<%= request.getContextPath()%>/Estilos/peliculaClienteStyle.css"> 

        <style>
            :root {
                --accent: #FF5733;
                --bg-dark: #1d1f20;
                --card-bg: #2b2b2b;
                --muted: #b0b0b0;
                --border: #3a3a3a;
            }

            /* ... (Tus estilos de Tabs y Product Card se mantienen) ... */
            
            /* Tabs */
            .category-nav {
                border-bottom: none;
                margin-bottom: 20px;
            }
            .category-nav .nav-link {
                color: var(--muted) !important;
                border-bottom: 2px solid transparent;
                font-weight: 600;
                padding: 10px 18px;
                border-radius: 6px 6px 0 0;
            }
            .category-nav .nav-link.active {
                color: var(--accent) !important;
                border-bottom-color: var(--accent);
                background: rgba(255,87,51,0.06);
            }

            /* Product card */
            .product-card {
                display: flex;
                flex-direction: column;
                background-color: var(--card-bg);
                border: 1px solid var(--border);
                border-radius: 12px;
                overflow: hidden;
                transition: transform .18s ease, box-shadow .18s ease;
                width: 100%;
            }
            .product-card:hover {
                transform: translateY(-6px);
                box-shadow: 0 12px 28px rgba(0,0,0,0.45);
            }
            .product-image-wrap {
                width: 100%;
                aspect-ratio: 4 / 3;
                background: linear-gradient(180deg, rgba(0,0,0,0.02), rgba(0,0,0,0.06));
                display: flex;
                align-items: center;
                justify-content: center;
                overflow: hidden;
            }
            .product-image {
                width: 100%;
                height: 100%;
                object-fit: cover;
                display: block;
            }
            .product-body {
                padding: 12px 14px;
                display: flex;
                flex-direction: column;
                flex: 1 1 auto;
                justify-content: space-between;
            }
            .product-title {
                font-size: 1rem;
                color: #fff;
                margin: 0 0 6px 0;
                line-height: 1.15;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .product-desc {
                font-size: 0.85rem;
                color: var(--muted);
                margin: 0;
                height: 2.4em; /* 2 lines aprox */
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .product-footer {
                margin-top: 10px;
                flex-grow: 1; /* Permite que el footer empuje el control de cantidad hacia abajo si es necesario */
                display: flex;
                flex-direction: column;
                justify-content: flex-end;
                align-items: center; /* Centrar el control de cantidad */
            }
            .product-price {
                font-weight: 700;
                color: var(--accent);
                font-size: 1.05rem;
                margin-bottom: 8px; /* Espacio antes del control de cantidad */
            }

            /* =================== ESTILOS NUEVOS PARA CONTADOR Y BOTÓN AGREGAR =================== */

            /* Controles para cantidad */
            .quantity-control {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 10px;
                width: 100%; /* Opcional: hacer que ocupe todo el ancho si lo deseas */
            }
            .quantity-control button {
                border: none;
                background-color: var(--border);
                color: white;
                border-radius: 50%;
                width: 32px;
                height: 32px;
                font-size: 18px;
                font-weight: bold;
                cursor: pointer;
                line-height: 1;
                transition: background-color 0.2s, transform 0.1s;
            }
            .quantity-control button:hover {
                background-color: var(--accent);
            }
            .quantity-control button:active {
                transform: scale(0.95);
            }
            .quantity-control span {
                font-size: 1.2rem;
                width: 30px;
                text-align: center;
                display: inline-block;
                color: #fff;
            }
            
            /* Boton Flotante "Agregar al Carrito" */
            .btn-flotante {
                position: fixed;
                bottom: 20px;
                right: 20px;
                background-color: var(--accent); 
                color: white;
                border: none;
                padding: 14px 30px;
                font-size: 16px;
                border-radius: 25px;
                cursor: pointer;
                box-shadow: 0 4px 10px rgba(255,87,51,0.6);
                z-index: 1100;
                transition: background-color 0.3s ease, opacity 0.3s;
                text-decoration: none; 
            }
            .btn-flotante:hover {
                background-color: #e54d2f; 
                text-decoration: none;
                color: white;
            }
            .btn-flotante:disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }
        </style>
    </head>
    <body style="background-color: var(--main-bg, #1e1e1e);">

        <%-- ==========================================================
        1. NAV BAR COMPLETO (Estructura de DashboardCliente)
        ============================================================== --%>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="#">CineOnline</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
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
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                             Hola, <%= (nombreCompleto != null && !nombreCompleto.isEmpty()) ? nombreCompleto.split(" ")[0] : username%>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#">Mi Perfil</a>
                            <a class="dropdown-item" href="#">Mis Reservas</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="<%= request.getContextPath()%>/LogoutServlet">Cerrar Sesión</a>
                        </div>
                    </li>
                    <% }%>
                </ul>
            </div>
        </nav>

        <%-- Botón flotante para confirmar la selección y enviarla al carrito --%>
        <button id="btnAgregar" class="btn-flotante" disabled>
            <i class="fas fa-shopping-cart"></i> Agregar al Carrito (0)
        </button>
        
        <%-- Formulario oculto para enviar los datos del carrito al Servlet --%>
        <form id="carritoForm" method="POST" action="<%= request.getContextPath()%>/CarritoDulceriaServlet" style="display: none;">
            <input type="hidden" name="accion" value="actualizarDulceria">
            <input type="hidden" name="carritoData" id="carritoData">
        </form>


        <div class="container py-4">
            <div class="container">
                <h1 class="page-header mt-4">Dulcería</h1>
            </div>

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
                    isFirstTab = true; // Reiniciar para el contenido
                    for (Map.Entry<String, List<modelo.Producto>> entry : productosCategorizados.entrySet()) {
                        String categoria = entry.getKey();
                        List<modelo.Producto> listaProductos = entry.getValue();
                        String tabId = "tab-" + categoria.replaceAll("[^a-zA-Z0-9]", "");
                        String activeContent = isFirstTab ? "show active" : "";
                %>
                <div class="tab-pane fade <%= activeContent%>" id="<%= tabId%>" role="tabpanel" aria-labelledby="tab-<%= tabId%>">
                    <div class="row pt-4">
                        <%
                            if (listaProductos != null) {
                                for (modelo.Producto producto : listaProductos) {
                                    // Obtener la cantidad que ya estaba en la sesion
                                    int cantidadInicial = carritoExistente.getOrDefault(producto.getIdProducto(), 0);
                                    
                                    // Convertir el byte array de la foto a base64 para mostrarla
                                    String fotoBase64 = java.util.Base64.getEncoder().encodeToString(producto.getFoto());
                        %>
                        <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-4 d-flex align-items-stretch">
                            <article class="product-card shadow-sm">
                                <div class="product-image-wrap">
                                    <img src="data:image/jpeg;base64,<%= fotoBase64%>" 
                                            class="product-image" alt="<%= producto.getNombre()%>" loading="lazy">
                                </div>

                                <div class="product-body">
                                    <h3 class="product-title" title="<%= producto.getNombre()%>"><%= producto.getNombre()%></h3>
                                    <p class="product-desc text-muted"><%= producto.getDescripcion()%></p>

                                    <div class="product-footer">
                                        <div class="price-wrap text-center">
                                            <span class="product-price">S/ <%= String.format("%.2f", producto.getPrecio())%></span>
                                        </div>
                                        
                                        <div class="quantity-control" data-id="<%= producto.getIdProducto()%>">
                                            <button type="button" class="decrease" title="Quitar">
                                                <i class="fas fa-minus"></i>
                                            </button>
                                            <span class="quantity-value"><%= cantidadInicial %></span>
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

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

        <script>
            // Mapa para almacenar el estado del carrito: {idProducto: cantidad, ...}
            let carritoState = {};
            
            // Inicializar carritoState con los valores que vienen de la sesion (si los hay)
            // Esto asegura que si el usuario vuelve, su seleccion anterior se mantiene.
            <% 
                for (Map.Entry<Integer, Integer> entry : carritoExistente.entrySet()) {
                    if (entry.getValue() > 0) {
            %>
                        carritoState[<%= entry.getKey() %>] = <%= entry.getValue() %>;
            <%
                    }
                }
            %>

            /**
             * Calcula el total de ítems seleccionados y actualiza el botón flotante.
             */
            function actualizarBotonFlotante() {
                const totalItems = Object.values(carritoState).reduce((sum, count) => sum + count, 0);
                const btn = document.getElementById('btnAgregar');
                
                if (totalItems > 0) {
                    btn.textContent = `Agregar al Carrito (${totalItems})`;
                    btn.disabled = false;
                } else {
                    btn.textContent = `Agregar al Carrito (0)`;
                    btn.disabled = true;
                }
            }

            /**
             * Prepara el formulario oculto con los datos del carrito y lo envía al Servlet.
             */
            function enviarCarritoAlServlet() {
                // 1. Convertir el carritoState de JavaScript a una cadena JSON
                const carritoJson = JSON.stringify(carritoState);
                
                // 2. Poner el JSON en el campo oculto
                document.getElementById('carritoData').value = carritoJson;
                
                // 3. Enviar el formulario
                document.getElementById('carritoForm').submit();
            }


            document.addEventListener("DOMContentLoaded", () => {
                
                // Inicializa el botón flotante con el estado del carrito existente
                actualizarBotonFlotante();

                // 1. Manejar los clics de incremento/decremento
                document.querySelectorAll('.quantity-control button').forEach(button => {
                    button.addEventListener('click', e => {
                        e.preventDefault();
                        
                        // Encontrar el contenedor de control de cantidad
                        const controlContainer = e.target.closest('.quantity-control');
                        const idProducto = parseInt(controlContainer.dataset.id);
                        const span = controlContainer.querySelector('.quantity-value');
                        let value = parseInt(span.textContent);
                        
                        // Determinar el cambio
                        // Se usa e.target.parentNode para soportar el clic en el icono (fas fa-minus/plus)
                        const clickedElement = e.target.classList.contains('decrease') || e.target.parentNode.classList.contains('decrease') ? 'decrease' :
                                               e.target.classList.contains('increase') || e.target.parentNode.classList.contains('increase') ? 'increase' : null;

                        if (clickedElement === 'decrease') {
                            if(value > 0) value--;
                        } else if (clickedElement === 'increase') {
                            value++;
                        }
                        
                        // 2. Actualizar el DOM y el estado JS
                        span.textContent = value;
                        
                        if (value > 0) {
                            carritoState[idProducto] = value;
                        } else {
                            delete carritoState[idProducto]; // Eliminar el producto si la cantidad llega a 0
                        }

                        // 3. Actualizar el boton flotante
                        actualizarBotonFlotante();
                    });
                });

                // 4. Manejar el clic del boton "Agregar al Carrito"
                document.getElementById('btnAgregar').addEventListener('click', function(e) {
                    e.preventDefault(); 
                    // Enviar la data al Servlet
                    enviarCarritoAlServlet();
                });
            });
        </script>
    </body>
</html>