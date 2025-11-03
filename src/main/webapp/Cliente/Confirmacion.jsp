<%@page import="modelo.ProductoDao"%>
<%@page import="modelo.Producto"%>
<%@page import="java.util.Map"%>
<%@page import="modelo.Funcion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="javax.servlet.http.HttpSession"%>

<%
    HttpSession sesion = request.getSession(false);
    if (sesion == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }
    String nombreCompleto = (String) sesion.getAttribute("nombreCompleto");
    String correoElectronico = (String) sesion.getAttribute("correoElectronico");
    String metodoPago = (String) sesion.getAttribute("metodoPago");
    Funcion funcion = (Funcion)sesion.getAttribute("funcionSeleccionada");
    Object butacasObj = sesion.getAttribute("butacasSeleccionadas");
    
    Object precioAsientosObj = sesion.getAttribute("totalAsientos");
    Map<Integer,Integer> carritoObj = (Map<Integer,Integer>)sesion.getAttribute("carritoDulceria");
    Object precioDulcesObj = sesion.getAttribute("precioDulces");
    
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmación de Compra</title>
</head>
<body>
    <h1>Detalles de la Compra</h1>

    <h3>Función seleccionada:</h3>
    <p><strong>Pelicula    : </strong> <%= funcion != null ? funcion.getPelicula().getNombre() : "No disponible" %></p>
    <p><strong>Hora        :</strong> <%= funcion != null ? funcion.getFechaInicio(): "No disponible" %></p>
    <p><strong>Sala        :</strong> <%= funcion != null ? funcion.getSala().getNombre(): "No disponible" %></p>
    <h3>Asientos seleccionados:</h3>
    <ul>
    <%
        if (butacasObj != null) {
            if (butacasObj instanceof java.util.List) {
                for (Object a : (java.util.List<?>) butacasObj) {
    %>
                    <li><%= a.toString() %></li>
    <%
                }
            } else {
    %>
                <li><%= butacasObj.toString() %></li>
    <%
            }
        } else {
    %>
            <li>No se seleccionaron asientos.</li>
    <%
        }
    %>
    </ul>
    <h3>Precio Total :<%= precioAsientosObj.toString() %></h3>
    
    
    <h3>Dulcería:</h3>
    <ul>
    <%
        if (carritoObj != null) {
            if (carritoObj instanceof java.util.List) {
                for (Object d : (java.util.List<?>) carritoObj) {
    %>
                    <li><%= d.toString() %></li>
    <%
                }
            } else {
                for (Map.Entry<Integer, Integer> entry : carritoObj.entrySet()) {
                    int idProducto = entry.getKey(); 
                    int cantidad = entry.getValue();
                    ProductoDao pdao = new ProductoDao();
                    Producto p = pdao.leer(idProducto);
    %>
                <li><%= p.getNombre().toString() %> | <%= cantidad %></li>
    <%          }
            }
        }else {
    %>
            <li>No se seleccionaron productos.</li>
    <%
        }
    %>
    </ul>
    <h3>Precio Total :<%= precioDulcesObj.toString() %></h3>
    
    
    
    <h3>Datos del cliente:</h3>
    <p><strong>Nombre:</strong> <%= nombreCompleto != null ? nombreCompleto : "No registrado" %></p>
    <p><strong>Correo:</strong> <%= correoElectronico != null ? correoElectronico : "No registrado" %></p>
    <p><strong>Método de pago:</strong> <%= metodoPago != null ? metodoPago : "No seleccionado" %></p>

    <h2>¿Confirmar compra?</h2>
    <form action="ClienteServlet" method="post">
        <input type="hidden" name="action" value="guardarVenta">
        <button type="submit">Confirmar y Guardar Venta</button>
    </form>
</body>
</html>
