<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <title>Método de Pago</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet"/>
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
                font-family: Arial, sans-serif;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
                margin: 0;
            }

            /* Cabecera personalizada */
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
            .back-link {
                color: var(--white);
                text-decoration: none;
                margin-right: 20px;
                font-size: 16px;
                transition: color 0.3s;
            }
            .back-link:hover {
                color: var(--orange);
            }
            .custom-header h1 {
                margin: 0;
                flex-grow: 1;
                text-align: center;
                font-size: 24px;
            }

            /* Contenedor principal */
            .payment-container {
                flex: 1;
                max-width: 700px;
                margin: 30px auto;
                background: var(--white);
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            /* Pasos */
            .step-progress {
                display: flex;
                justify-content: center;
                margin-bottom: 30px;
                gap: 20px;
            }
            .step-progress .step {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                background: var(--gray);
                color: var(--white);
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                font-size: 18px;
            }
            .step-progress .step.active {
                background: var(--orange);
            }

            /* Títulos */
            h2 {
                color: var(--dark);
                font-weight: 700;
                margin-bottom: 20px;
                text-align: center;
            }

            /* Inputs mejorados */
            .form-group {
                margin-bottom: 1.5rem;
            }
            .form-control {
                border: none;
                border-bottom: 2px solid #ccc;
                border-radius: 0;
                padding: 10px 5px 5px 0;
                font-size: 16px;
                background: transparent;
                transition: border-color 0.3s;
            }
            .form-control:focus {
                border-color: var(--orange);
                box-shadow: none;
            }
            /* Línea animada */
            .form-group {
                position: relative;
            }
            .form-group::after {
                content: '';
                position: absolute;
                bottom: 0;
                left: 0;
                width: 0;
                height: 2px;
                background: var(--orange);
                transition: width 0.3s;
            }
            .form-control:focus ~ .form-group::after,
            .form-control:not(:placeholder-shown) ~ .form-group::after {
                width: 100%;
            }

            /* Tarjetas de pago */
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
            }
            .payment-options input[type="radio"] {
                margin: 0;
            }
            .payment-options label {
                margin: 0;
                font-weight: 600;
                color: var(--dark);
                flex-grow: 1;
            }
            .payment-options img {
                max-height: 25px;
            }

            /* Checkboxes */
            .checkbox-group {
                font-size: 14px;
                margin-bottom: 10px;
            }
            .checkbox-group a {
                color: var(--orange);
            }

            /* Notas */
            .notes {
                font-size: 12px;
                color: var(--gray);
                margin-top: 15px;
            }

            /* Botón estilo CineJ3 */
            .btn-continue {
                background-color: var(--orange);
                color: var(--white);
                border: none;
                padding: 14px 40px;
                font-size: 16px;
                font-weight: bold;
                border-radius: 25px;
                cursor: pointer;
                box-shadow: 0 4px 10px rgba(255, 87, 51, 0.6);
                display: block;
                margin: 30px auto 0 auto;
                transition: background-color 0.3s, opacity 0.3s;
            }
            .btn-continue:disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }
            .btn-continue:hover:not(:disabled) {
                background-color: #d44729;
            }

            /* Footer */
            footer {
                text-align: center;
                font-size: 12px;
                color: var(--gray);
                padding: 20px 0;
                margin-top: auto;
            }
            footer a {
                color: var(--gray);
                margin: 0 5px;
                text-decoration: none;
            }
            footer a:hover {
                color: var(--orange);
            }
        </style>
    </head>
    <body>
        <header class="custom-header">
            <h1>Pago</h1>
        </header>

        <div class="payment-container">

            <div class="step-progress">
                <div class="step">1</div>
                <div class="step">2</div>
                <div class="step">3</div>
                <div class="step active">4</div>
            </div>

            <h2>Elige una forma de Pago</h2>

            <form action="ClienteServlet" method="post">
                <div class="form-group">
                    <input type="text" name="nombreCompleto" placeholder="Nombre completo" required/>
                </div>
                <div class="form-group">
                    <input type="email" name="correoElectronico" placeholder="Correo electrónico" required/>
                </div>

                <div class="payment-options">
                    <input type="radio" id="tarjeta" name="metodoPago" value="tarjeta" required/>
                    <label for="tarjeta">Tarjeta de Crédito o Débito</label>
                    <img src="http://localhost:8080/CineJ3/Cliente/images/pago1.png" alt="Visa"/>
                    <img src="http://localhost:8080/CineJ3/Cliente/images/pago3.png"/>
                    <img src="http://localhost:8080/CineJ3/Cliente/images/pago2.png" alt="Mastercard"/>
                    <img src="http://localhost:8080/CineJ3/Cliente/images/pago4.png" alt="Diners Club"/>
                </div>

                <div class="payment-options">
                    <input type="radio" id="appAgora" name="metodoPago" value="appAgora" />
                    <label for="appAgora">App agora</label>
                    <img src="http://localhost:8080/CineJ3/Cliente/images/pago5.png" alt="App Agora"/>
                </div>

                <div class="payment-options">
                    <input type="radio" id="billeteras" name="metodoPago" value="billeteras" />
                    <label for="billeteras">Billeteras Electrónicas</label>
                    <img src="http://localhost:8080/CineJ3/Cliente/images/pago6.jpg" alt="Yape"/>
                    <img src="http://localhost:8080/CineJ3/Cliente/images/pago7.png" alt="Plin"/>
                    <img src="http://localhost:8080/CineJ3/Cliente/images/pago8.png" alt="Payme"/>
                </div>

                <div class="checkbox-group">
                    <input type="checkbox" id="terminos" name="terminos" required />
                    <label for="terminos">Acepto los <a href="#">Términos y Condiciones</a> y <a href="#">Política de Privacidad</a>.</label>
                </div>
                <div class="checkbox-group">
                    <input type="checkbox" id="finalidades" name="finalidades" required />
                    <label for="finalidades">He leído y acepto las finalidades de <a href="#">Tratamiento necesario de datos</a>.</label>
                </div>
                <div class="checkbox-group">
                    <input type="checkbox" id="opcionales" name="opcionales" />
                    <label for="opcionales">Acepto el tratamiento opcional de datos.</label>
                </div>

                <div class="notes">
                    * No se hacen cambios ni devoluciones<br/>
                    * Toda la información de pago es segura<br/>
                    * Algunas de las tarjetas de débito con CVV podrían ser rechazadas por la plataforma de pago que utilizamos debido a las políticas de seguridad del banco
                </div>

                <div class="text-center mt-4">
                    <button type="submit" class="btn-continue">Continuar</button>
                </div>
            </form>
        </div>
        <footer>
            © 2025 Cine Online | Todos los derechos reservados
            <br />
            <a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a>
        </footer>
        <script>
            const pricePerSeat = 0; // no lo usamos aquí, pero lo dejamos por compatibilidad
            const btnContinuar = document.querySelector('.btn-continue');

            /* Campos que deben estar OK */
            const nombre = document.querySelector('[name="nombreCompleto"]');
            const email = document.querySelector('[name="correoElectronico"]');
            const metodoPago = document.querySelectorAll('[name="metodoPago"]');
            const terminos = document.getElementById('terminos');
            const finalidades = document.getElementById('finalidades');

            function validarTodo() {
                const nombreOk = nombre.value.trim().length > 0;
                const emailOk = email.value.trim().length > 0;
                const metodoOk = Array.from(metodoPago).some(r => r.checked);
                const terminosOk = terminos.checked;
                const finalidadesOk = finalidades.checked;

                const todoOk = nombreOk && emailOk && metodoOk && terminosOk && finalidadesOk;
                btnContinuar.disabled = !todoOk;
            }

            /* Escuchar cambios */
            [nombre, email, terminos, finalidades].forEach(el =>
                el.addEventListener('input', validarTodo));
            metodoPago.forEach(r => r.addEventListener('change', validarTodo));

            /* Estado inicial */
            btnContinuar.disabled = true;
        </script>
    </body>
</html>