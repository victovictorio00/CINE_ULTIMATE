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
    if (funcionSeleccionada == null || asientosSeleccionados == null || totalAsientos == null) {
        response.sendRedirect(request.getContextPath() + "/CarteleraServlet");
        return;
    }
    if (totalDulces == null) totalDulces = 0.0;
    double totalGeneral = totalAsientos + totalDulces;
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
        <link rel="stylesheet" href="<%= request.getContextPath() %>/Cliente/EstilosCliente/MetodoPago.css">
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
                <form id="formPago" action="<%= request.getContextPath() %>/ClienteServlet" method="post">
                    <input type="hidden" name="csrf_token" value="${sessionScope.csrfToken}">
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
                    <h5>
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
                if (todoValido) {
                    btnContinuar.textContent = 'Continuar a Confirmación';
                } else {
                    btnContinuar.textContent = 'Completa todos los campos';
                }
            }
            campos.nombre.addEventListener('input', validarFormulario);
            campos.email.addEventListener('input', validarFormulario);
            campos.terminos.addEventListener('change', validarFormulario);
            campos.finalidades.addEventListener('change', validarFormulario);

            Array.from(campos.metodoPago).forEach(radio => {
                radio.addEventListener('change', validarFormulario);
            });
            formPago.addEventListener('submit', function(e) {
                if (btnContinuar.disabled) {
                    e.preventDefault();
                    alert('Por favor, completa todos los campos obligatorios');
                    return false;
                }
                btnContinuar.disabled = true;
                btnContinuar.textContent = 'Procesando...';
            });
            validarFormulario();
            console.log('Formulario de pago inicializado');
            console.log('Total a pagar: S/. <%= String.format("%.2f", totalGeneral) %>');
        </script>
    </body>
</html>