<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="modelo.Funcion" %>
<%@ page import="java.util.Map" %>

<%
    // Validar que existan datos previos en sesión
    Funcion funcionSeleccionada = (Funcion) session.getAttribute("funcionSeleccionada");
    String asientosSeleccionados = (String) session.getAttribute("asientosSeleccionados");
    Double totalAsientos = (Double) session.getAttribute("totalAsientos");
    Double totalDulces = (Double) session.getAttribute("totalDulces");
    Map<Integer, Integer> carritoDulceria = (Map<Integer, Integer>) session.getAttribute("carritoDulceria");
    
    // Si no hay datos de compra, redirigir
    if (funcionSeleccionada == null || asientosSeleccionados == null || totalAsientos == null) {
        response.sendRedirect(request.getContextPath() + "/CarteleraServlet");
        return;
    }
    
    // Calcular total general
    if (totalDulces == null) totalDulces = 0.0;
    double totalGeneral = totalAsientos + totalDulces;
    
    // Obtener datos del usuario si ya están en sesión (por si regresa)
    String nombrePrevio = (String) session.getAttribute("nombreCompleto");
    String correoPrevio = (String) session.getAttribute("correoElectronico");
    String metodoPagoPrevio = (String) session.getAttribute("metodoPago");
    
    if (nombrePrevio == null) nombrePrevio = "";
    if (correoPrevio == null) correoPrevio = "";
    if (metodoPagoPrevio == null) metodoPagoPrevio = "";
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Método de Pago</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        :root {
            --dark: #343a40;
            --orange: #FF5733;
            --light: #f5f5f5;
            --white: #ffffff;
            --gray: #6c757d;
        }

        body {
            background-color: var(--light);
            font-family: 'Segoe UI', Arial, sans-serif;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            margin: 0;
        }

        .custom-header {
            background-color: var(--dark);
            color: var(--white);
            display: flex;
            align-items: center;
            padding: 15px 30px;
            font-weight: bold;
            font-size: 18px;
            position: sticky;
            top: 0;
            z-index: 20;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .custom-header h1 {
            margin: 0 auto;
            font-size: 24px;
        }

        .payment-container {
            flex: 1;
            max-width: 650px;
            margin: 40px auto;
            background: var(--white);
            padding: 35px 40px;
            border-radius: 10px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
        }

        /* Resumen de compra */
        .resumen-compra {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 25px;
        }
        
        .resumen-compra h4 {
            margin-bottom: 15px;
            font-weight: bold;
        }
        
        .resumen-item {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid rgba(255,255,255,0.2);
        }
        
        .resumen-item:last-child {
            border-bottom: none;
            font-size: 1.3em;
            font-weight: bold;
            margin-top: 10px;
            padding-top: 10px;
            border-top: 2px solid rgba(255,255,255,0.5);
        }

        .step-progress {
            display: flex;
            justify-content: center;
            margin-bottom: 30px;
            gap: 20px;
        }
        .step {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: var(--gray);
            color: var(--white);
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
        }
        .step.active {
            background: var(--orange);
        }
        .step.completed {
            background: #28a745;
        }

        h2 {
            color: var(--dark);
            font-weight: 700;
            margin-bottom: 25px;
            text-align: center;
        }

        .form-group {
            position: relative;
            margin-bottom: 1.8rem;
        }

        .form-control {
            width: 100%;
            font-size: 16px;
            border: none;
            border-bottom: 2px solid #ccc;
            padding: 10px 0 5px 0;
            background: transparent;
            transition: border-color 0.3s;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--orange);
        }

        .form-group label {
            position: absolute;
            top: 10px;
            left: 0;
            color: #777;
            font-size: 16px;
            transition: all 0.3s ease;
            pointer-events: none;
        }

        .form-control:focus + label,
        .form-control:not(:placeholder-shown) + label {
            top: -12px;
            font-size: 13px;
            color: var(--orange);
        }

        .payment-options {
            border: 2px solid #ddd;
            border-radius: 8px;
            padding: 12px 15px;
            margin-bottom: 1rem;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 12px;
            transition: border-color 0.3s;
        }
        .payment-options:hover,
        .payment-options:has(input:checked) {
            border-color: var(--orange);
            background: rgba(255, 87, 51, 0.05);
        }
        .payment-options input[type="radio"] {
            margin: 0;
            cursor: pointer;
        }
        .payment-options label {
            margin: 0;
            font-weight: 600;
            color: var(--dark);
            flex-grow: 1;
            cursor: pointer;
        }
        .payment-options img {
            max-height: 25px;
        }

        .checkbox-group {
            font-size: 14px;
            margin-bottom: 10px;
        }
        .checkbox-group a {
            color: var(--orange);
        }

        .notes {
            font-size: 12px;
            color: var(--gray);
            margin-top: 15px;
            padding: 10px;
            background: #f8f9fa;
            border-radius: 5px;
        }

        .btn-continue {
            background-color: var(--orange);
            color: var(--white);
            border: none;
            padding: 12px 35px;
            font-size: 16px;
            font-weight: bold;
            border-radius: 25px;
            cursor: pointer;
            box-shadow: 0 4px 10px rgba(255, 87, 51, 0.4);
            display: block;
            margin: 30px auto 0 auto;
            transition: background-color 0.3s, opacity 0.3s;
        }
        .btn-continue:disabled {
            opacity: 0.5;
            cursor: not-allowed;
            background-color: #ccc;
        }
        .btn-continue:hover:not(:disabled) {
            background-color: #d44729;
        }

        footer {
            text-align: center;
            font-size: 12px;
            color: var(--gray);
            padding: 20px 0;
            margin-top: auto;
        }
        footer a {
            color: var(--gray);
            text-decoration: none;
        }
        footer a:hover {
            color: var(--orange);
        }
    </style>
</head>
<body>
    <header class="custom-header">
        <h1>Método de Pago</h1>
    </header>

    <div class="payment-container">
        <!-- Indicador de progreso -->
        <div class="step-progress">
            <div class="step completed" title="Película"><i class="fas fa-check"></i></div>
            <div class="step completed" title="Asientos"><i class="fas fa-check"></i></div>
            <div class="step completed" title="Dulcería"><i class="fas fa-check"></i></div>
            <div class="step active" title="Pago">4</div>
        </div>

        <!-- Resumen de compra -->
        <div class="resumen-compra">
            <h4><i class="fas fa-receipt"></i> Resumen de tu Compra</h4>
            <div class="resumen-item">
                <span>Película:</span>
                <span><strong><%= funcionSeleccionada.getPelicula().getNombre() %></strong></span>
            </div>
            <div class="resumen-item">
                <span>Asientos:</span>
                <span><strong><%= asientosSeleccionados %></strong></span>
            </div>
            <div class="resumen-item">
                <span>Entradas:</span>
                <span><strong>S/. <%= String.format("%.2f", totalAsientos) %></strong></span>
            </div>
            <% if (totalDulces > 0) { %>
            <div class="resumen-item">
                <span>Dulcería:</span>
                <span><strong>S/. <%= String.format("%.2f", totalDulces) %></strong></span>
            </div>
            <% } %>
            <div class="resumen-item">
                <span>TOTAL:</span>
                <span><strong>S/. <%= String.format("%.2f", totalGeneral) %></strong></span>
            </div>
        </div>

        <h2>Completa tus Datos</h2>

        <!-- Formulario -->
        <form id="formPago" action="<%= request.getContextPath() %>/ClienteServlet" method="post">
            <input type="hidden" name="action" value="ingresarDatosPago">
            
            <div class="form-group">
                <input type="text" 
                       id="nombreCompleto" 
                       name="nombreCompleto" 
                       class="form-control" 
                       placeholder=" " 
                       value="<%= nombrePrevio %>"
                       required 
                       maxlength="100"/>
                <label for="nombreCompleto">Nombre completo</label>
            </div>

            <div class="form-group">
                <input type="email" 
                       id="correoElectronico" 
                       name="correoElectronico" 
                       class="form-control" 
                       placeholder=" " 
                       value="<%= correoPrevio %>"
                       required 
                       maxlength="100"/>
                <label for="correoElectronico">Correo electrónico</label>
            </div>

            <h5 style="margin-top: 25px; margin-bottom: 15px; color: var(--dark);">
                Selecciona Método de Pago
            </h5>

            <div class="payment-options">
                <input type="radio" 
                       id="tarjeta" 
                       name="metodoPago" 
                       value="Tarjeta" 
                       <%= metodoPagoPrevio.equals("Tarjeta") ? "checked" : "" %>
                       required/>
                <label for="tarjeta">Tarjeta de Crédito o Débito</label>
                <img src="<%= request.getContextPath() %>/Cliente/images/pago1.png" alt="Visa"/>
                <img src="<%= request.getContextPath() %>/Cliente/images/pago3.png" alt="Logo"/>
                <img src="<%= request.getContextPath() %>/Cliente/images/pago2.png" alt="Mastercard"/>
                <img src="<%= request.getContextPath() %>/Cliente/images/pago4.png" alt="Diners Club"/>
            </div>

            <div class="payment-options">
                <input type="radio" 
                       id="appAgora" 
                       name="metodoPago" 
                       value="App Agora"
                       <%= metodoPagoPrevio.equals("AppAgora") ? "checked" : "" %> />
                <label for="appAgora">App Agora</label>
                <img src="<%= request.getContextPath() %>/Cliente/images/pago5.png" alt="App Agora"/>
            </div>

            <div class="payment-options">
                <input type="radio" 
                       id="billeteras" 
                       name="metodoPago" 
                       value="Billeteras"
                       <%= metodoPagoPrevio.equals("Billetera") ? "checked" : "" %> />
                <label for="billeteras">Billeteras Electrónicas</label>
                <img src="<%= request.getContextPath() %>/Cliente/images/pago6.jpg" alt="Yape"/>
                <img src="<%= request.getContextPath() %>/Cliente/images/pago7.png" alt="Plin"/>
                <img src="<%= request.getContextPath() %>/Cliente/images/pago8.png" alt="Payme"/>
            </div>

            <div class="checkbox-group">
                <input type="checkbox" id="terminos" name="terminos" required />
                <label for="terminos">
                    Acepto los <a href="#" target="_blank">Términos y Condiciones</a> y 
                    <a href="#" target="_blank">Política de Privacidad</a>.
                </label>
            </div>
            <div class="checkbox-group">
                <input type="checkbox" id="finalidades" name="finalidades" required />
                <label for="finalidades">
                    He leído y acepto las finalidades de 
                    <a href="#" target="_blank">Tratamiento de datos</a>.
                </label>
            </div>
            <div class="checkbox-group">
                <input type="checkbox" id="opcionales" name="opcionales" />
                <label for="opcionales">Acepto el tratamiento opcional de datos.</label>
            </div>

            <div class="notes">
                <i class="fas fa-info-circle"></i> <strong>Notas importantes:</strong><br/>
                • No se hacen cambios ni devoluciones<br/>
                • Toda la información de pago es segura<br/>
                • Algunas tarjetas pueden ser rechazadas según políticas bancarias
            </div>

            <button type="submit" class="btn-continue" id="btnContinuar" disabled>
                Continuar a Confirmación
            </button>
        </form>
    </div>

    <footer>
        © 2025 Cine Online | Todos los derechos reservados
        <br />
        <a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a>
    </footer>

    <script>
        const formPago = document.getElementById('formPago');
        const btnContinuar = document.getElementById('btnContinuar');
        
        const campos = {
            nombre: document.getElementById('nombreCompleto'),
            email: document.getElementById('correoElectronico'),
            metodoPago: document.getElementsByName('metodoPago'),
            terminos: document.getElementById('terminos'),
            finalidades: document.getElementById('finalidades')
        };

        /**
         * Valida todos los campos y habilita/deshabilita el botón
         */
        function validarFormulario() {
            const nombreValido = campos.nombre.value.trim().length >= 3;
            const emailValido = campos.email.value.trim().includes('@');
            const metodoSeleccionado = Array.from(campos.metodoPago).some(r => r.checked);
            const terminosAceptados = campos.terminos.checked;
            const finalidadesAceptadas = campos.finalidades.checked;

            const todoValido = nombreValido && 
                             emailValido && 
                             metodoSeleccionado && 
                             terminosAceptados && 
                             finalidadesAceptadas;

            btnContinuar.disabled = !todoValido;
            
            // Actualizar texto del botón
            if (todoValido) {
                btnContinuar.textContent = 'Continuar a Confirmación';
            } else {
                btnContinuar.textContent = 'Completa todos los campos';
            }
        }

        // Adjuntar eventos
        campos.nombre.addEventListener('input', validarFormulario);
        campos.email.addEventListener('input', validarFormulario);
        campos.terminos.addEventListener('change', validarFormulario);
        campos.finalidades.addEventListener('change', validarFormulario);
        
        Array.from(campos.metodoPago).forEach(radio => {
            radio.addEventListener('change', validarFormulario);
        });

        // Validación al enviar
        formPago.addEventListener('submit', function(e) {
            if (btnContinuar.disabled) {
                e.preventDefault();
                alert('Por favor, completa todos los campos obligatorios');
                return false;
            }
            
            // Deshabilitar botón para evitar doble submit
            btnContinuar.disabled = true;
            btnContinuar.textContent = 'Procesando...';
        });

        // Validar al cargar (por si hay datos previos)
        validarFormulario();
        
        console.log('Formulario de pago inicializado');
        console.log('Total a pagar: S/. <%= String.format("%.2f", totalGeneral) %>');
    </script>
</body>
</html>