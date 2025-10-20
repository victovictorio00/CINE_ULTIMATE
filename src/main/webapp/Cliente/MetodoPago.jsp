<%-- 
    Document   : MetodoPago
    Created on : 28 may. 2025, 3:59:31
    Author     : Proyecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<title>Método de Pago</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet"/>
<style>
  body {
    background-color: #f0f4f7;
    font-family: Arial, sans-serif;
    padding-top: 80px;
    padding-bottom: 80px;
  }
  /* Cabecera personalizada */
  .custom-header {
    background-color: #004080;
    color: white;
    font-weight: bold;
    font-size: 18px;
    padding: 12px 20px;
    position: fixed;
    top: 0; left: 0; right: 0;
    z-index: 1050;
    display: flex;
    align-items: center;
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
    flex-grow: 1;
    text-align: center;
    margin: 0;
  }

  /* Contenido principal */
  .payment-container {
    max-width: 700px;
    margin: auto;
    background: white;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 0 15px rgba(0,0,0,0.1);
  }

  .step-progress {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
    gap: 20px;
  }
  .step-progress .step {
    width: 40px;
    height: 40px;
    border-bottom: 3px solid #ddd;
    position: relative;
    text-align: center;
    font-size: 24px;
    color: #ccc;
    line-height: 40px;
  }
  .step-progress .step.active {
    color: #004080;
    border-color: #004080;
    font-weight: bold;
  }
  .step-progress .step::after {
    content: '';
    position: absolute;
    bottom: -3px;
    left: 0;
    width: 100%;
    border-bottom: 3px solid currentColor;
  }

  h2 {
    color: #004080;
    font-weight: 700;
    margin-bottom: 20px;
    text-align: center;
  }

  form .form-group {
    margin-bottom: 1.5rem;
  }
  form input[type="text"],
  form input[type="email"] {
    border: none;
    border-bottom: 2px solid #ccc;
    border-radius: 0;
    padding: 8px 5px;
    width: 100%;
    font-size: 16px;
    outline: none;
  }
  form input[type="text"]:focus,
  form input[type="email"]:focus {
    border-color: #004080;
  }

  .payment-options {
    border: 1px solid #ccc;
    border-radius: 6px;
    margin-bottom: 1rem;
    cursor: pointer;
    display: flex;
    align-items: center;
    padding: 10px 15px;
    transition: border-color 0.3s ease;
  }
  .payment-options:hover {
    border-color: #004080;
  }
  .payment-options input[type="radio"] {
    margin-right: 15px;
  }
  .payment-options label {
    margin: 0;
    flex-grow: 1;
    font-weight: 600;
    color: #004080;
  }
  .payment-options img {
    max-height: 25px;
    margin-left: 10px;
  }

  .checkbox-group {
    font-size: 14px;
    margin-bottom: 10px;
  }
  .checkbox-group label {
    margin-left: 5px;
    color: #444;
    cursor: pointer;
  }
  .checkbox-group input[type="checkbox"] {
    cursor: pointer;
  }

  .notes {
    font-size: 12px;
    color: #666;
    margin-top: 15px;
  }

  /* Botón continuar */
  .btn-continue {
    background-color: #d81071;
    color: white;
    border: none;
    padding: 14px 40px;
    font-size: 16px;
    border-radius: 25px;
    cursor: pointer;
    box-shadow: 0 4px 10px rgba(216,16,113,0.6);
    display: block;
    margin: 30px auto 0 auto;
    transition: background-color 0.3s ease;
  }
  .btn-continue:hover {
    background-color: #ad0b58;
  }

  /* Footer */
  footer {
    text-align: center;
    font-size: 12px;
    color: #555;
    margin-top: 50px;
  }
  footer a {
    color: #555;
    margin: 0 5px;
    text-decoration: none;
  }
  footer a:hover {
    text-decoration: underline;
  }
  
.btn-link-continue {
    display: inline-block;
    background-color: #ec4899;
    color: white;
    border: none;
    padding: 12px 30px;
    font-size: 16px;
    border-radius: 25px;
    cursor: pointer;
    text-align: center;
    text-decoration: none;
    box-shadow: 0 4px 10px rgba(236, 72, 153, 0.6);
    transition: background-color 0.3s ease;
}

.btn-link-continue:hover {
    background-color: #be1f73;
    text-decoration: none;
    color: white;
}


</style>
</head>
<body>

<header class="custom-header">
  <a href="http://localhost:8080/CineJ3/Cliente/SeleccionCombo.jsp" class="back-link">← Atrás</a>
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

<div style="text-align: center;">
  <a href="http://localhost:8080/CineJ3/Cliente/Voucher.jsp" class="btn-link-continue">Continuar</a>
</div>

</div>

<footer>
  © 2025 Cine Online | Todos los derechos reservados
  <br />
  <a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a>
</footer>

</body>
</html>