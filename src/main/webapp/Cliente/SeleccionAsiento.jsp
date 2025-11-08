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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seleccionar Butacas</title>
    <link rel="stylesheet" href="<%= request.getContextPath()%>/Estilos/asientosStyle.css">
    <style>
        /* Estilos para numeración */
        .column-numbers {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            margin: 8px 0;
            padding: 0 50px;
        }

        .column-number {
            width: 32px;
            height: 32px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            color: #666;
            font-size: 0.85rem;
        }

        /* CRÍTICO: Estilos para asientos - garantizan funcionalidad */
        .seat {
            width: 32px;
            height: 32px;
            margin: 4px;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.2s ease;
            border: 2px solid transparent;
        }

        .seat.available {
            background-color: #e0e0e0;
            border-color: #bdbdbd;
        }

        .seat.available:hover {
            background-color: #90caf9;
            transform: scale(1.15);
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        }

        .seat.occupied {
            background-color: #f44336;
            border-color: #d32f2f;
            cursor: not-allowed;
            opacity: 0.6;
        }

        .seat.selected {
            background-color: #4CAF50 !important;
            border-color: #388E3C !important;
            transform: scale(1.1);
            box-shadow: 0 4px 12px rgba(76, 175, 80, 0.5);
        }

        /* Estilos del panel de resumen */
        .selected-seats-section h3 {
            margin-bottom: 10px;
            color: #333;
        }

        #selected-seats-list {
            min-height: 40px;
            padding: 12px;
            background: #f5f5f5;
            border-radius: 5px;
            color: #999;
            font-size: 14px;
            line-height: 1.6;
        }

        #selected-seats-list.has-selection {
            color: #4CAF50;
            font-weight: 600;
            background: #f1f8f4;
        }

        .price-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }

        .price-row.total {
            border-top: 2px solid #333;
            border-bottom: none;
            margin-top: 10px;
            padding-top: 10px;
            font-size: 1.2em;
            font-weight: bold;
        }

        .btn-continue {
            width: 100%;
            padding: 15px;
            margin-top: 15px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-continue:hover:not(:disabled) {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-continue:disabled {
            opacity: 0.5;
            cursor: not-allowed;
            background: #ccc;
        }
    </style>
</head>
<body>
    <%
    List<Asiento> chk = (List<Asiento>) request.getAttribute("asientosFuncion");
    System.out.println("DEBUG JSP ENTRADA: asientosFuncion size = " + (chk == null ? "null" : chk.size()));
%>
    <header class="custom-header">
        <h1>Selecciona tus butacas</h1>
    </header>

    <div class="main-container">
        <div class="left-column">
            <div class="screen">PANTALLA</div>

            <%-- Validar y recuperar datos --%>
            <%
                Pelicula pelicula = (Pelicula) request.getAttribute("pelicula");
                Sala sala = (Sala) request.getAttribute("sala");
                Funcion funcion = (Funcion) request.getAttribute("funcion");
                List<Asiento> asientos = (List<Asiento>) request.getAttribute("asientosFuncion");
                Object precioObj = request.getAttribute("precioButaca");

                if (pelicula == null || sala == null || funcion == null || precioObj == null) {
                    response.sendRedirect(request.getContextPath() + "/Cliente/Error.jsp");
                    return;
                }

                if (asientos == null) {
                    asientos = new ArrayList<>();
                }

                double precioButaca = 0.0;
                try {
                    precioButaca = Double.parseDouble(precioObj.toString());
                } catch (Exception e) {
                    precioButaca = 10.0;
                }
            %>

            <%-- Crear matriz de asientos --%>
            <%
                Map<String, List<Asiento>> porFila = new LinkedHashMap<>();
                System.out.println("DEBUG JSP: porFila.size = " + porFila.size());
                int maxColumnas = 0;

                for (Asiento a : asientos) {
                    String codigo = a.getCodigo();
                    if (codigo == null || codigo.isEmpty()) {
                        continue;
                    }

                    String fila = codigo.substring(0, 1);
                    porFila.computeIfAbsent(fila, k -> new ArrayList<>()).add(a);

                    try {
                        int numCol = Integer.parseInt(codigo.substring(1));
                        if (numCol > maxColumnas) {
                            maxColumnas = numCol;
                        }
                    } catch (Exception e) {
                    }
                }

                // Ordenar asientos por número
                for (List<Asiento> asientosFila : porFila.values()) {
                    asientosFila.sort((a1, a2) -> {
                        try {
                            int num1 = Integer.parseInt(a1.getCodigo().substring(1));
                            int num2 = Integer.parseInt(a2.getCodigo().substring(1));
                            return Integer.compare(num1, num2);
                        } catch (Exception e) {
                            return 0;
                        }
                    });
                }
            %>

            <!-- Numeración superior -->
            <div class="column-numbers">
                <div class="column-number" style="width: 30px;"></div>
                <% for (int i = 1; i <= maxColumnas; i++) {%>
                    <div class="column-number"><%= i%></div>
                <% }%>
                <div class="column-number" style="width: 30px;"></div>
            </div>

            <!-- Matriz de asientos -->
            <%
                for (Map.Entry<String, List<Asiento>> entry : porFila.entrySet()) {
                    String letraFila = entry.getKey();
                    List<Asiento> asientosFila = entry.getValue();
            %>
            <div class="seats-row">
                <div class="row-label"><%= letraFila%></div>
                <div class="seats-column">
                    <%
                        for (Asiento asiento : asientosFila) {
                            boolean disponible = asiento.estaDisponible();
                            String codigo = asiento.getCodigo();
                            String claseBase = "seat";
                            String estadoClase = disponible ? "available" : "occupied";
                    %>
                    <div class="<%= claseBase%> <%= estadoClase%>"
                         data-seat="<%= codigo%>"
                         data-available="<%= disponible%>"
                         title="Asiento <%= codigo%> - <%= disponible ? "Disponible" : "Ocupado"%>">
                    </div>
                    <%
                        }
                    %>
                </div>
                <div class="row-label"><%= letraFila%></div>
            </div>
            <%
                }
            %>

            <!-- Numeración inferior -->
            <div class="column-numbers">
                <div class="column-number" style="width: 30px;"></div>
                <% for (int i = 1; i <= maxColumnas; i++) {%>
                    <div class="column-number"><%= i%></div>
                <% }%>
                <div class="column-number" style="width: 30px;"></div>
            </div>

            <!-- Leyenda -->
            <div class="legend">
                <div><div class="box available"></div><span>Disponible</span></div>
                <div><div class="box occupied"></div><span>Ocupado</span></div>
                <div><div class="box selected"></div><span>Seleccionado</span></div>
            </div>
        </div>

        <!-- Columna derecha: Resumen -->
        <div class="right-column">
            <div class="summary-card">
                <h2>Resumen de compra</h2>

                <div class="movie-info">
                    <p><strong>Película:</strong> <%= pelicula.getNombre()%></p>
                    <p><strong>Sala:</strong> <%= sala.getNombre()%></p>
                    <p><strong>Fecha:</strong>
                        <%
                            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"));
                            out.print(df.format(funcion.getFechaInicio()));
                        %>
                    </p>
                    <p><strong>Horario:</strong>
                        <%
                            java.text.SimpleDateFormat hf = new java.text.SimpleDateFormat("HH:mm");
                            out.print(hf.format(funcion.getFechaInicio()) + " - " + hf.format(funcion.getFechaFin()));
                        %>
                    </p>
                    <p><strong>Duración:</strong> <%= request.getAttribute("duracionMin")%> min</p>
                    <p><strong>Género:</strong> <%= request.getAttribute("genero")%></p>
                </div>

                <div class="selected-seats-section">
                    <h3>Butacas seleccionadas</h3>
                    <div id="selected-seats-list">
                        Ninguna butaca seleccionada
                    </div>
                </div>

                <div class="price-section">
                    <div class="price-row">
                        <span>Precio unitario:</span>
                        <span>S/. <%= String.format("%.2f", precioButaca)%></span>
                    </div>
                    <div class="price-row">
                        <span>Cantidad:</span>
                        <span id="selected-count">0</span>
                    </div>
                    <div class="price-row">
                        <span>Subtotal:</span>
                        <span>S/. <span id="subtotal">0.00</span></span>
                    </div>
                    <div class="price-row total">
                        <span>Total:</span>
                        <span class="amount">S/. <span id="total">0.00</span></span>
                    </div>
                </div>

                <!-- Formulario POST al servlet -->
                <form id="formAsientos" method="POST" action="<%= request.getContextPath()%>/ClienteServlet">
                    <input type="hidden" name="action" value="seleccionarAsientos">
                    <input type="hidden" name="selectedSeats" id="inputSelectedSeats" value="">
                    <button type="submit" class="btn-continue" id="btnContinue" disabled>
                        Continuar
                    </button>
                </form>
            </div>
        </div>
    </div>

    <footer>
        <p>© 2025 Cine Online | Todos los derechos reservados</p>
        <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p>
    </footer>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            console.log('🎬 Sistema de selección de asientos iniciado');

            // === CONFIGURACIÓN ===
            const PRECIO_UNITARIO = <%= precioButaca %>;
            const MAX_ASIENTOS = 8;

            // === ELEMENTOS DEL DOM ===
            const selectedSeats = new Set();
            const selectedSeatsDiv = document.getElementById('selected-seats-list');
            const selectedCountSpan = document.getElementById('selected-count');
            const subtotalSpan = document.getElementById('subtotal');
            const totalSpan = document.getElementById('total');
            const btnContinue = document.getElementById('btnContinue');
            const inputSelectedSeats = document.getElementById('inputSelectedSeats');
            const formAsientos = document.getElementById('formAsientos');

            // Verificar elementos
            if (!selectedSeatsDiv || !btnContinue || !inputSelectedSeats) {
                console.error('❌ Error: Elementos del DOM no encontrados');
                return;
            }

            // === FUNCIONES ===

            /**
             * Actualiza el resumen de compra en el panel derecho
             */
            function updateSummary() {
                const count = selectedSeats.size;
                const total = count * PRECIO_UNITARIO;

                // Actualizar contadores
                selectedCountSpan.textContent = count;
                subtotalSpan.textContent = total.toFixed(2);
                totalSpan.textContent = total.toFixed(2);

                // Actualizar lista de asientos
                if (count > 0) {
                    const asientosOrdenados = Array.from(selectedSeats).sort();
                    selectedSeatsDiv.textContent = asientosOrdenados.join(', ');
                    selectedSeatsDiv.classList.add('has-selection');
                } else {
                    selectedSeatsDiv.textContent = 'Ninguna butaca seleccionada';
                    selectedSeatsDiv.classList.remove('has-selection');
                }

                // Actualizar input oculto para el formulario
                inputSelectedSeats.value = Array.from(selectedSeats).join(',');

                // Habilitar/deshabilitar botón
                if (count > 0) {
                    btnContinue.disabled = false;
                    btnContinue.textContent = 'Continuar · S/. ' + total.toFixed(2);
                } else {
                    btnContinue.disabled = true;
                    btnContinue.textContent = 'Continuar';
                }

                console.log('📊 Resumen:', {
                    asientos: Array.from(selectedSeats),
                    cantidad: count,
                    total: 'S/. ' + total.toFixed(2)
                });
            }

            /**
             * Alterna la selección de un asiento
             */
            function toggleSeat(seatElement) {
                const seatCode = seatElement.dataset.seat;
                const isAvailable = seatElement.dataset.available === 'true';

                console.log('🎯 Click en asiento:', seatCode, '| Disponible:', isAvailable);

                // Validar disponibilidad
                if (!isAvailable) {
                    alert('⚠️ Este asiento no está disponible');
                    return;
                }

                // Toggle selección
                if (selectedSeats.has(seatCode)) {
                    // Deseleccionar
                    selectedSeats.delete(seatCode);
                    seatElement.classList.remove('selected');
                    console.log('➖ Deseleccionado:', seatCode);
                } else {
                    // Validar límite máximo
                    if (selectedSeats.size >= MAX_ASIENTOS) {
                        alert('⚠️ Solo puedes seleccionar hasta ' + MAX_ASIENTOS + ' asientos por compra');
                        return;
                    }

                    // Seleccionar
                    selectedSeats.add(seatCode);
                    seatElement.classList.add('selected');
                    console.log('➕ Seleccionado:', seatCode);
                }

                updateSummary();
            }

            // === EVENT LISTENERS ===

            // Adjuntar eventos a todos los asientos disponibles
            const asientosDisponibles = document.querySelectorAll('.seat.available');
            console.log('✅ Asientos disponibles encontrados:', asientosDisponibles.length);

            if (asientosDisponibles.length === 0) {
                console.warn('⚠️ No hay asientos disponibles');
                selectedSeatsDiv.textContent = 'No hay asientos disponibles para esta función';
            }

            asientosDisponibles.forEach((seat, index) => {
                // Click
                seat.addEventListener('click', function () {
                    toggleSeat(this);
                });

                // Teclado (accesibilidad)
                seat.addEventListener('keydown', function (e) {
                    if (e.key === 'Enter' || e.key === ' ') {
                        e.preventDefault();
                        toggleSeat(this);
                    }
                });

                // Hacer focusable
                seat.setAttribute('tabindex', '0');
            });

            // Validación al enviar formulario
            formAsientos.addEventListener('submit', function (e) {
                if (selectedSeats.size === 0) {
                    e.preventDefault();
                    alert('⚠️ Debe seleccionar al menos un asiento para continuar');
                    return false;
                }

                // Confirmación
                const asientosStr = Array.from(selectedSeats).sort().join(', ');
                const total = (selectedSeats.size * PRECIO_UNITARIO).toFixed(2);
                const confirmMsg = '¿Confirmar la reserva de ' + selectedSeats.size + ' asiento(s)?\n\n' +
                        'Asientos: ' + asientosStr + '\n' +
                        'Total: S/. ' + total;

                if (!confirm(confirmMsg)) {
                    e.preventDefault();
                    return false;
                }

                // Deshabilitar botón para evitar doble submit
                btnContinue.disabled = true;
                btnContinue.textContent = 'Procesando...';
                console.log('✅ Enviando formulario:', inputSelectedSeats.value);
            });

            // === INICIALIZACIÓN ===
            updateSummary();
            console.log('✅ Sistema listo');
            console.log('💰 Precio unitario: S/.', PRECIO_UNITARIO);
        });
    </script>
</body>
</html>