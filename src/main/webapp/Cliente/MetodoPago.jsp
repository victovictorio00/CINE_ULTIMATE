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
            font-family: 'Segoe UI', Arial, sans-serif;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            margin: 0;
        }

        /* Cabecera */
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

        /* Contenedor */
        .payment-container {
            flex: 1;
            max-width: 650px;
            margin: 40px auto;
            background: var(--white);
            padding: 35px 40px;
            border-radius: 10px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
        }

        /* Pasos */
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

        h2 {
            color: var(--dark);
            font-weight: 700;
            margin-bottom: 25px;
            text-align: center;
        }

        /* Inputs estilo moderno */
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
            transition: border-color 0.3s, background-color 0.3s;
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

        /* Opciones de pago */
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

        /* Botón */
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
            <input type="text" id="nombreCompleto" name="nombreCompleto" class="form-control" placeholder=" " required/>
            <label for="nombreCompleto">Nombre completo</label>
        </div>

        <div class="form-group">
            <input type="email" id="correoElectronico" name="correoElectronico" class="form-control" placeholder=" " required/>
            <label for="correoElectronico">Correo electrónico</label>
        </div>

        <div class="payment-options">
            <input type="radio" id="tarjeta" name="metodoPago" value="tarjeta" required/>
            <label for="tarjeta">Tarjeta de Crédito o Débito</label>
            <img src="Cliente/images/pago1.png" alt="Visa"/>
            <img src="Cliente/images/pago3.png" alt="Logo"/>
            <img src="Cliente/images/pago2.png" alt="Mastercard"/>
            <img src="Cliente/images/pago4.png" alt="Diners Club"/>
        </div>

        <div class="payment-options">
            <input type="radio" id="appAgora" name="metodoPago" value="appAgora" />
            <label for="appAgora">App Agora</label>
            <img src="Cliente/images/pago5.png" alt="App Agora"/>
        </div>

        <div class="payment-options">
            <input type="radio" id="billeteras" name="metodoPago" value="billeteras" />
            <label for="billeteras">Billeteras Electrónicas</label>
            <img src="Cliente/images/pago6.jpg" alt="Yape"/>
            <img src="Cliente/images/pago7.png" alt="Plin"/>
            <img src="Cliente/images/pago8.png" alt="Payme"/>
        </div>

        <div class="checkbox-group">
            <input type="checkbox" id="terminos" name="terminos" required />
            <label for="terminos">Acepto los <a href="#">Términos y Condiciones</a> y <a href="#">Política de Privacidad</a>.</label>
        </div>
        <div class="checkbox-group">
            <input type="checkbox" id="finalidades" name="finalidades" required />
            <label for="finalidades">He leído y acepto las finalidades de <a href="#">Tratamiento de datos</a>.</label>
        </div>
        <div class="checkbox-group">
            <input type="checkbox" id="opcionales" name="opcionales" />
            <label for="opcionales">Acepto el tratamiento opcional de datos.</label>
        </div>

        <div class="notes">
            * No se hacen cambios ni devoluciones<br/>
            * Toda la información de pago es segura<br/>
            * Algunas tarjetas pueden ser rechazadas según políticas bancarias
        </div>

        <button type="submit" class="btn-continue" disabled>Continuar</button>
    </form>
</div>

<footer>
    © 2025 Cine Online | Todos los derechos reservados
    <br />
    <a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a>
</footer>

<script>
    const btnContinuar = document.querySelector('.btn-continue');
    const formPago = document.querySelector('form');
    const campos = {
        nombre: document.getElementById('nombreCompleto'),
        email: document.getElementById('correoElectronico'),
        metodoPago: document.getElementsByName('metodoPago'),
        terminos: document.getElementById('terminos'),
        finalidades: document.getElementById('finalidades')
    };

    function validarTodo() {
        const ok = campos.nombre.value.trim() &&
                   campos.email.value.trim() &&
                   Array.from(campos.metodoPago).some(r => r.checked) &&
                   campos.terminos.checked &&
                   campos.finalidades.checked;

        btnContinuar.disabled = !ok;
    }

    Object.values(campos).forEach(el => {
        if (NodeList.prototype.isPrototypeOf(el)) el.forEach(r => r.addEventListener('change', validarTodo));
        else el.addEventListener('input', validarTodo);
    });

    btnContinuar.addEventListener('click', e => {
        e.preventDefault();
        if (!btnContinuar.disabled) {
            let input = formPago.querySelector('input[name="action"]');
            if (!input) {
                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'action';
                input.value = 'procesarPago';
                formPago.appendChild(input);
            }
            formPago.submit();
        }
    });
</script>
</body>
</html>
