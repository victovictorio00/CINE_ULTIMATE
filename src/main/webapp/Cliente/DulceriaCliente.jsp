<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="modelo.Producto" %> 
<%-- Asegúrate de que tu clase Producto exista y tenga getPrecio() y getFoto() --%>

<%
    // Obtener el mapa de productos categorizados del Servlet
    // Se asume que el Servlet envía esto: Map<String, List<modelo.Producto>> 
    Map<String, List<modelo.Producto>> productosCategorizados
            = (Map<String, List<modelo.Producto>>) request.getAttribute("productosCategorizados");

    // Inicializar mapa de respaldo si el Servlet no envió datos o falló.
    if (productosCategorizados == null) {
        productosCategorizados = new java.util.LinkedHashMap<>();
        productosCategorizados.put("COMBOS", new ArrayList<>());
        productosCategorizados.put("BEBIDAS", new ArrayList<>());
        productosCategorizados.put("DULCES", new ArrayList<>());
        productosCategorizados.put("PALOMITAS", new ArrayList<>());
        productosCategorizados.put("SNACKS", new ArrayList<>());
        productosCategorizados.put("OTROS", new ArrayList<>());
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

            /* Image wrap mantiene aspecto consistente sin fijar ancho absoluto */
            .product-image-wrap {
                width: 100%;
                /* uso aspect-ratio si el navegador lo permite, cae al padding-top en fallback */
                aspect-ratio: 4 / 3;
                background: linear-gradient(180deg, rgba(0,0,0,0.02), rgba(0,0,0,0.06));
                display: flex;
                align-items: center;
                justify-content: center;
                overflow: hidden;
            }
            @supports not (aspect-ratio: 1/1) {
                .product-image-wrap {
                    position: relative;
                    padding-top: 75%;
                } /* 4:3 fallback */
                .product-image-wrap img {
                    position: absolute;
                    top:0;
                    left:0;
                    height:100%;
                    width:100%;
                    object-fit:cover;
                }
            }

            .product-image {
                width: 100%;
                height: 100%;
                object-fit: cover;
                display: block;
            }

            /* Body */
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

            /* Footer */
            .product-footer {
                margin-top: 10px;
            }
            .product-price {
                font-weight: 700;
                color: var(--accent);
                font-size: 1.05rem;
            }

            /* Button */
            .add-button {
                background: var(--accent);
                color: #fff;
                border: none;
                border-radius: 8px;
                width: 40px;
                height: 40px;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                box-shadow: 0 6px 18px rgba(255,87,51,0.16);
                transition: transform .12s ease, box-shadow .12s ease, opacity .12s ease;
            }
            .add-button:hover {
                transform: translateY(-2px);
            }
            .add-button:active {
                transform: translateY(0);
                opacity: .92;
            }

            /* Responsive tweaks */
            @media (max-width: 767px) {
                .product-image-wrap {
                    aspect-ratio: 3/2;
                }
                .product-title {
                    font-size: .95rem;
                }
            }

            /* small polish: remove default outline but keep accessible focus */
            .add-button:focus {
                outline: 3px solid rgba(255,87,51,0.25);
                outline-offset: 2px;
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


        <div class="container py-4">
            <div class="container">
                <h1 class="page-header mt-4">Dulcería</h1>
            </div>

            <%
                // Usado para marcar la primera pestaña como activa
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
                                    // Convertir el byte array de la foto a base64 para mostrarla
                                    String fotoBase64 = java.util.Base64.getEncoder().encodeToString(producto.getFoto());
                        %>
                        <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-4 d-flex align-items-stretch">
                            <article class="product-card shadow-sm">
                                <div class="product-image-wrap">
                                    <!-- nota: sin espacio después de la coma en data URI -->
                                    <img src="data:image/jpeg;base64,<%= fotoBase64%>" 
                                         class="product-image" alt="<%= producto.getNombre()%>" loading="lazy">
                                </div>

                                <div class="product-body">
                                    <h3 class="product-title" title="<%= producto.getNombre()%>"><%= producto.getNombre()%></h3>
                                    <p class="product-desc text-muted"><%= producto.getDescripcion()%></p>

                                    <div class="product-footer d-flex align-items-center justify-content-between">
                                        <div class="price-wrap">
                                            <span class="product-price">S/ <%= String.format("%.2f", producto.getPrecio())%></span>
                                        </div>
                                        <button class="add-button btn" aria-label="Agregar <%= producto.getNombre()%>" title="Agregar al carrito">
                                            <i class="fas fa-plus"></i>
                                        </button>
                                    </div>
                                </div>
                            </article>
                        </div>
                        <%      }
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

    </body>
</html>