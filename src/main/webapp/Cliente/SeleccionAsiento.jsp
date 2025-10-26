<%@page import="modelo.Funcion"%>
<%@page import="modelo.Sala"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
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
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            html, body {
                height: 100%;
                font-family: Arial, sans-serif;
            }

            body {
                display: flex;
                flex-direction: column;
                background-color: #f5f5f5;
            }

            /* Cabecera personalizada */
            .custom-header {
                background-color: #343a40;
                color: white;
                display: flex;
                align-items: center;
                padding: 15px 30px;
                font-weight: bold;
                font-size: 18px;
                position: sticky;
                top: 0;
                z-index: 20;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }
            .back-link {
                color: white;
                text-decoration: none;
                margin-right: 20px;
                font-size: 16px;
                transition: color 0.3s;
            }
            .back-link:hover {
                color: #FF5733;
            }
            .custom-header h1 {
                margin: 0;
                flex-grow: 1;
                text-align: center;
                font-size: 24px;
            }

            /* Contenedor principal con dos columnas */
            .main-container {
                display: flex;
                gap: 30px;
                padding: 30px;
                max-width: 1400px;
                margin: 0 auto;
                width: 100%;
                flex: 1;
            }

            /* Columna izquierda - Selección de asientos */
            .left-column {
                flex: 2;
                background: white;
                padding: 25px;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }

            .screen {
                background: #343a40;
                color: white;
                padding: 12px;
                text-align: center;
                font-weight: bold;
                border-radius: 5px;
                margin-bottom: 30px;
                font-size: 16px;
            }

            .seats-row {
                display: flex;
                justify-content: center;
                margin-bottom: 8px;
            }

            .row-label {
                width: 30px;
                text-align: center;
                font-weight: bold;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #343a40;
            }

            .seats-group {
                display: flex;
                justify-content: center;
                gap: 40px;
            }

            .seats-column {
                display: flex;
                gap: 8px;
            }

            .seat {
                width: 28px;
                height: 28px;
                border-radius: 50%;
                border: 2px solid #FF5733;
                background-color: white;
                cursor: pointer;
                transition: all 0.3s;
            }

            .seat.available:hover {
                background-color: #FFE5DF;
                transform: scale(1.1);
            }

            .seat.occupied {
                background-color: #6c757d;
                cursor: not-allowed;
                border-color: #495057;
            }

            .seat.selected {
                background-color: #FF5733;
                border-color: #d44729;
            }

            /* Leyenda */
            .legend {
                display: flex;
                justify-content: space-around;
                margin-top: 30px;
                padding: 20px;
                background-color: #f8f9fa;
                border-radius: 8px;
                flex-wrap: wrap;
                gap: 15px;
            }

            .legend div {
                display: flex;
                align-items: center;
                gap: 8px;
                font-size: 14px;
                color: #343a40;
            }

            .legend .box {
                width: 24px;
                height: 24px;
                border-radius: 50%;
            }

            .legend .available {
                background-color: white;
                border: 2px solid #FF5733;
            }

            .legend .occupied {
                background-color: #6c757d;
                border: 2px solid #495057;
            }

            .legend .selected {
                background-color: #FF5733;
                border: 2px solid #d44729;
            }

            /* Columna derecha - Resumen */
            .right-column {
                flex: 1;
                display: flex;
                flex-direction: column;
                gap: 20px;
            }

            .summary-card {
                background: white;
                padding: 25px;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }

            .summary-card h2 {
                color: #343a40;
                font-size: 20px;
                margin-bottom: 20px;
                border-bottom: 3px solid #FF5733;
                padding-bottom: 10px;
            }

            .movie-info {
                margin-bottom: 20px;
            }

            .movie-info p {
                margin: 10px 0;
                color: #495057;
                line-height: 1.6;
            }

            .movie-info strong {
                color: #343a40;
                display: inline-block;
                min-width: 100px;
            }

            .selected-seats-section {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin: 20px 0;
            }

            .selected-seats-section h3 {
                color: #343a40;
                font-size: 16px;
                margin-bottom: 10px;
            }

            #selected-seats-list {
                color: #FF5733;
                font-weight: bold;
                min-height: 20px;
            }

            .price-section {
                border-top: 2px solid #e9ecef;
                padding-top: 20px;
                margin-top: 20px;
            }

            .price-row {
                display: flex;
                justify-content: space-between;
                margin: 10px 0;
                color: #495057;
            }

            .price-row.total {
                font-size: 20px;
                font-weight: bold;
                color: #343a40;
                border-top: 2px solid #FF5733;
                padding-top: 15px;
                margin-top: 15px;
            }

            .price-row.total .amount {
                color: #FF5733;
            }

            .btn-continue {
                background-color: #FF5733;
                color: white;
                border: none;
                padding: 15px 30px;
                font-size: 16px;
                font-weight: bold;
                border-radius: 8px;
                cursor: pointer;
                width: 100%;
                margin-top: 20px;
                transition: background-color 0.3s;
                text-decoration: none;
                display: block;
                text-align: center;
            }

            .btn-continue:hover {
                background-color: #d44729;
            }

            /* Pie de página */
            footer {
                background-color: #343a40;
                color: white;
                text-align: center;
                padding: 20px;
                margin-top: auto;
            }

            footer p {
                margin: 5px 0;
            }

            footer a {
                color: #FF5733;
                text-decoration: none;
                margin: 0 10px;
                transition: color 0.3s;
            }

            footer a:hover {
                color: #FFE5DF;
                text-decoration: underline;
            }

            /* Responsive */
            @media (max-width: 1024px) {
                .main-container {
                    flex-direction: column;
                }

                .seats-group {
                    gap: 20px;
                }
            }
        </style>
    </head>
    <body>

        <!-- Cabecera personalizada -->
        <header class="custom-header">
            <h1>Selecciona tus butacas</h1>
        </header>

        <div class="main-container">
            <!-- Columna izquierda: Selección de asientos -->
            <div class="left-column">
                <div class="screen">PANTALLA</div>

                <%-- Recuperar lista --%>
                <%
                    List<Asiento> asientos = (List<Asiento>) request.getAttribute("asientos");
                    if (asientos == null)
                        asientos = new ArrayList<>();
                %>

                <%-- Pintar asientos --%>
                <%
                    Map<String, List<Asiento>> porFila = new LinkedHashMap<>();

                    // Agrupar por letra de fila 
                    for (Asiento a : asientos) {
                        String fila = a.getCodigo().substring(0, 1);
                        porFila.computeIfAbsent(fila, k -> new ArrayList<>()).add(a);
                    }

                    // Recorrer filas
                    for (Map.Entry<String, List<Asiento>> e : porFila.entrySet()) {
                        //  Ordenar por número de asiento dentro de la fila
                        e.getValue().sort((a1, a2) -> {
                            int num1 = Integer.parseInt(a1.getCodigo().substring(1));
                            int num2 = Integer.parseInt(a2.getCodigo().substring(1));
                            return Integer.compare(num1, num2);
                        });
                %>
                <div class="seats-row">
                    <div class="row-label"><%= e.getKey()%></div>
                    <div class="seats-column">
                        <%
                            for (Asiento a : e.getValue()) {
                                String cls = a.isOcupado() ? "occupied" : "available";
                        %>
                        <div class="seat <%= cls%>"
                             data-seat="<%= a.getCodigo()%>"
                             title="<%= a.getCodigo()%>"></div>
                        <%
                            }
                        %>
                    </div>
                    <div class="row-label"><%= e.getKey()%></div>
                </div>
                <%
                    }
                %>

                <!-- Leyenda -->
                <div class="legend">
                    <div><div class="box available"></div><span>Disponible</span></div>
                    <div><div class="box occupied"></div><span>Ocupada</span></div>
                    <div><div class="box selected"></div><span>Seleccionada</span></div>
                </div>
            </div>

            <!-- Columna derecha: Resumen de compra -->
            <div class="right-column">
                <div class="summary-card">
                    <h2>Resumen de compra</h2>

                    <div class="movie-info">
                        <p><strong>Película:</strong> <%= ((Pelicula) request.getAttribute("pelicula")).getNombre()%></p>
                        <p><strong>Sala:</strong> <%= ((Sala) request.getAttribute("sala")).getNombre()%></p>
                        <p><strong>Fecha:</strong>
                            <%
                                Funcion f = (Funcion) request.getAttribute("funcion");
                                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"));
                                out.print(df.format(f.getFechaInicio()));
                            %>
                        </p>
                        <p><strong>Horario:</strong>
                            <%
                                java.text.SimpleDateFormat hf = new java.text.SimpleDateFormat("HH:mm");
                                out.print(hf.format(f.getFechaInicio()));
                            %>
                        </p>
                        <p><strong>Duración:</strong> <%= request.getAttribute("duracionMin")%> min</p>
                        <p><strong>Género:</strong> <%= request.getAttribute("genero")%></p>
                    </div>

                    <div class="selected-seats-section">
                        <h3>Butacas seleccionadas</h3>
                        <div id="selected-seats-list">Ninguna butaca seleccionada</div>
                    </div>

                    <div class="price-section">
                        <div class="price-row">
                            <span>Butaca (<span id="selected-count">0</span>):</span>
                            <span>S/. <span id="unit-price"><%= request.getAttribute("precioButaca")%></span></span>
                        </div>
                        <div class="price-row total">
                            <span>Total:</span>
                            <span class="amount">S/. <span id="total">0.00</span></span>
                        </div>
                    </div>
                    <button id="btnContinue" class="btn-continue" type="button" disabled>Continuar</button>
                </div>
            </div>
        </div>

        <!-- Pie de página -->
        <footer>
            <p>© 2025 Cine Online | Todos los derechos reservados</p>
            <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
        </footer>

        <script>
            (function(){
                // Variables del DOM
                const pricePerSeat = parseFloat('<%= request.getAttribute("precioButaca")%>') || 0;
                const selectedSeats = new Set();
                const selectedSeatsText = document.getElementById('selected-seats-list');
                const selectedCountSpan = document.getElementById('selected-count');
                const totalSpan = document.getElementById('total');
                const btnContinue = document.getElementById('btnContinue');

                // Helper: actualizar resumen y estado del botón
                function updateSummary() {
                    const count = selectedSeats.size;
                    const total = count * pricePerSeat;
                    selectedCountSpan.textContent = count;
                    totalSpan.textContent = total.toFixed(2);
                    selectedSeatsText.textContent = count ? Array.from(selectedSeats).join(', ') : 'Ninguna butaca seleccionada';

                    // Habilitar el botón SOLO si hay al menos 1 butaca seleccionada
                    if (count > 0) {
                        btnContinue.disabled = false;
                        // Mostrar total en el texto del botón para mejor UX
                        btnContinue.textContent = 'Continuar · S/. ' + total.toFixed(2);
                    } else {
                        btnContinue.disabled = true;
                        btnContinue.textContent = 'Continuar';
                    }
                }

                // Toggle selección en click/teclado
                function toggleSeatElement(el) {
                    const seatId = el.dataset.seat;
                    if (!seatId) return;
                    if (el.classList.contains('seat-occupied')) return; // no interactuar con ocupadas
                    if (selectedSeats.has(seatId)) {
                        selectedSeats.delete(seatId);
                        el.classList.remove('selected');
                    } else {
                        selectedSeats.add(seatId);
                        el.classList.add('selected');
                    }
                    updateSummary();
                }

                // Attach listeners a todas las butacas disponibles
                document.querySelectorAll('.seat.available').forEach(seat => {
                    seat.addEventListener('click', () => toggleSeatElement(seat));
                    // keyboard support: space/enter toggles
                    seat.addEventListener('keydown', (e) => {
                        if (e.key === 'Enter' || e.key === ' ') {
                            e.preventDefault();
                            toggleSeatElement(seat);
                        }
                    });
                });

                // Acción del botón: si habilitado, redirige pasando selectedSeats y total (GET)
                btnContinue.addEventListener('click', function(){
                    if (btnContinue.disabled) return;
                    const seatsCSV = encodeURIComponent(Array.from(selectedSeats).join(','));
                    const total = (selectedSeats.size * pricePerSeat).toFixed(2);
                    // Redirigir al servlet que necesites. Ahora uso DulceriaServlet como en tu original.
                    // Cambia la URL si prefieres POST o enviar a otro servlet.
                    const url = '<%=request.getContextPath()%>/DulceriaServlet?selectedSeats=' + seatsCSV + '&total=' + total;
                    window.location.href = url;
                });

                // Inicializar estado
                updateSummary();
            })();
        </script>
    </body>
</html>