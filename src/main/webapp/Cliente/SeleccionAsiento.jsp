<%--
    Document   : SeleccionAsiento
    Created on : 28 may. 2025, 3:47:59
    Author     : Proyecto
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Asiento" %>
<%@ page import="modelo.Pelicula" %>


<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Seleccionar Butacas</title>
    <style>
        /* Cabecera personalizada */
        .custom-header {
            background-color: #004080; /* Azul */
            color: white;
            display: flex;
            align-items: center;
            padding: 10px 20px;
            font-family: Arial, sans-serif;
            font-weight: bold;
            font-size: 18px;
            position: sticky;
            top: 0;
            z-index: 20;
        }
        .back-link {
            color: white;
            text-decoration: none;
            margin-right: 20px;
            font-size: 16px;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .custom-header h1 {
            margin: 0;
            flex-grow: 1;
            text-align: center;
        }

        .seats-container {
            width: 90%;
            margin: auto;
            background: #f0f4f7;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px #ccc;
            font-family: Arial, sans-serif;
        }
        .screen {
            background: #ccc;
            padding: 10px;
            text-align: center;
            font-weight: bold;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .seats-row {
            display: flex;
            justify-content: center; /* Centrar horizontalmente */
            width: 100%; /* Asegura que ocupe todo el ancho posible */
        }
        .row-label {
            width: 20px;
            text-align: center;
            margin: 0 10px;
            font-weight: bold;
        }
        .seats-group {
            display: flex;
            justify-content: center; /* Centra horizontalmente las 3 columnas */
            gap: 50px; /* Mantiene el espacio entre columnas */
            flex-wrap: nowrap;
        }

        .seat {
            width: 25px;
            height: 25px;
            border-radius: 50%;
            border: 2px solid #1d4ed8;
            background-color: white;
            cursor: pointer;
            position: relative;
        }
        .seat.available:hover {
            background-color: #bfdbfe;
        }
        .seat.occupied {
            background-color: #ef4444;
            cursor: not-allowed;
            border-color: #b91c1c;
        }
        .seat.selected {
            background-color: #1d4ed8;
            border-color: #1e40af;
        }
        .seats-column {
            display: flex;
            gap: 6px;
        }
        /* Separación entre las 3 columnas */
        .seats-group {
            gap: 50px;
        }

        /* Leyenda */
        .legend {
            display: flex;
            justify-content: space-around;
            margin-top: 20px;
            font-size: 14px;
        }
        .legend div {
            display: flex;
            align-items: center;
            gap: 6px;
        }
        .legend .box {
            width: 20px;
            height: 20px;
            border-radius: 4px;
        }
        .legend .available { background-color: white; border: 2px solid #1d4ed8; }
        .legend .occupied { background-color: #ef4444; border: 2px solid #b91c1c; }
        .legend .selected { background-color: #1d4ed8; border: 2px solid #1e40af; }

        /* Botón continuar */
        .btn-continue {
            margin-top: 20px;
            background-color: #ec4899;
            color: white;
            border: none;
            padding: 12px 30px;
            font-size: 16px;
            border-radius: 25px;
            cursor: pointer;
            display: block;
            margin-left: auto;
        }

        /* Pie de página pegado hacia abajo */
        footer {
            background-color: #343a40;
            color: white;
            text-align: center;
            padding: 10px;
            position: relative;
            bottom: 0;
            width: 100%;
        }

        footer a {
            color: #ffffff;
            text-decoration: none;
            margin: 0 5px;
        }

/* Asegura que el pie de página se quede pegado al fondo */
html, body {
    height: 100%;
    margin: 0;
    display: flex;
    flex-direction: column;
}

footer a:hover {
            text-decoration: underline;
        }
        
        .btn-link-continue {
    background-color: #ec4899;
    color: white;
    border: none;
    padding: 12px 30px;
    font-size: 16px;
    border-radius: 25px;
    cursor: pointer;

    display: inline-block;
    float: right;
    text-align: center;
    box-sizing: border-box;
    margin: 20px 0 0 0;
    width: auto; /* Que no tenga ancho 100% */
    text-decoration: none; /* Quitar subrayado del link */
}
.btn-link-continue:hover {
    background-color: #d63483; /* Opcional: cambio de color hover */
}
    </style>
    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const seats = document.querySelectorAll(".seat.available");
            const selectedSeats = new Set();
            const selectedSeatsText = document.getElementById("selected-seats");

            seats.forEach(seat => {
                seat.addEventListener("click", () => {
                    const seatId = seat.getAttribute("data-seat");
                    if (seat.classList.contains("selected")) {
                        seat.classList.remove("selected");
                        selectedSeats.delete(seatId);
                    } else {
                        seat.classList.add("selected");
                        selectedSeats.add(seatId);
                    }
                    selectedSeatsText.textContent = "Butacas seleccionadas: " + Array.from(selectedSeats).join(", ");
                });
            });
        });
    </script>
</head>
<body>

    <!-- Cabecera personalizada -->
    <header class="custom-header">
    <a href="http://localhost:8080/CineJ3/Cliente/DetallePelicula.jsp?id=1" class="back-link">

        ← Atrás
      </a>
      <h1>Selecciona tus butacas</h1>
    </header>

    <div class="seats-container">
        <div class="screen">Pantalla</div>

        <%
            String[] rows = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O"};
            for(int i = 0; i < rows.length; i++) {
                String row = rows[i];
        %>
        <div class="seats-row">
            <div class="row-label"><%= row %></div>

            <div class="seats-group">
                <div class="seats-column">
                    <%-- Primer grupo: 4 asientos --%>
                       <% for(int j=1; j <=4; j++) {
                String seatId = row + j;
                // Ejemplo: algunos asientos ocupados
                boolean occupied = seatId.equals("A3") || seatId.equals("B7") || seatId.equals("M15")||seatId.equals("A9");
            %>
                <div class="seat <%= occupied ? "occupied" : "available" %>" data-seat="<%= seatId %>" title="<%= seatId %>"></div>
            <% } %>
        </div>

                <div class="seats-column">
                    <%-- Segundo grupo: 9 asientos --%>
                    <% for(int j=5; j <= 13; j++) {
                String seatId = row + j;
                boolean occupied = seatId.equals("A3") || seatId.equals("B7") || seatId.equals("M15")||seatId.equals("A9");
            %>
                        <div class="seat <%= occupied ? "occupied" : "available" %>" data-seat="<%= seatId %>" title="<%= seatId %>"></div>
                    <% } %>
                </div>

                <div class="seats-column">
                    <%-- Tercer grupo: 4 asientos --%>
                    <% for(int j=14; j <= 17; j++) {
                String seatId = row + j;
                boolean occupied = seatId.equals("A3") || seatId.equals("B7") || seatId.equals("M15")||seatId.equals("A9");
            %>
                        <div class="seat <%= occupied ? "occupied" : "available" %>" data-seat="<%= seatId %>" title="<%= seatId %>"></div>
                    <% } %>
                </div>
            </div>

            <div class="row-label"><%= row %></div>
        </div>
        <% } %>

        <!-- Leyenda -->
        <div class="legend">
            <div><div class="box available"></div>Disponible</div>
            <div><div class="box occupied"></div>Ocupada</div>
            <div><div class="box selected"></div>Seleccionada</div>
            <div><i class="fa fa-wheelchair"></i> No Disponibles</div>
        </div>

        <!-- Butacas seleccionadas -->
        <div id="selected-seats" style="margin-top: 20px; font-weight: bold;">Butacas seleccionadas:</div>
        
        <!-- Botón continuar -->
       <a href="<%= request.getContextPath()%>/DulceriaServlet" class="btn-link-continue">Continuar</a>

    </div>

    <!-- Pie de página -->
    <footer>
        <p>© 2025 Cine Online | Todos los derechos reservados</p>
        <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
    </footer>

</body>
</html>