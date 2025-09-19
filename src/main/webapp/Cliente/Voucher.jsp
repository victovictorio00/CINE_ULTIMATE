<%-- 
    Document   : Voucher
    Created on : 28 may. 2025, 4:02:17
    Author     : Proyecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Voucher</title>

<!-- Bootstrap CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" />

<!-- Font Awesome para iconos -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

<style>
body {
  margin: 0;
  padding: 0;
  font-family: 'Arial', sans-serif;
  background-color: #f8f9fa;
  color: #333;
}
 .voucher-container {
  max-width: 800px;
  background: white;
  margin: 0 auto;
  padding: 100px 40px 40px; /* padding-top para que no quede debajo de header */
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(0,0,0,0.1);
}
  /* Cabecera igual a la imagen */
 .voucher-header {
  background-color: #003366; /* azul oscuro */
  color: white;
  height: 50px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1050;
}
  .voucher-header .back-link {
    color: white;
    font-weight: 600;
    text-decoration: none;
    margin-right: 20px;
    font-size: 16px;
  }
  .voucher-header .back-link:hover {
    text-decoration: underline;
  }
  .voucher-header .title {
    flex-grow: 1;
    text-align: center;
    font-weight: 700;
    font-size: 18px;
  }
  .voucher-header .icons {
    display: flex;
    align-items: center;
    gap: 20px;
  }
  .voucher-header .icons i {
    cursor: pointer;
    font-size: 18px;
  }
  
  /* Resto del contenido (puedes usar lo que te di antes) */
  .header-voucher {
    display: flex;
    justify-content: space-between;
    margin-bottom: 30px;
    flex-wrap: wrap;
  }
  .order-info {
    flex: 1 1 45%;
  }
  .order-info h6 {
    font-weight: bold;
    margin-bottom: 5px;
  }
  .order-info .order-number {
    font-size: 1.2rem;
    font-weight: 700;
  }
  .qr-code {
    flex: 1 1 45%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .qr-code img {
    max-width: 160px;
    height: auto;
  }
  .details-info {
    flex: 1 1 45%;
    font-size: 0.9rem;
  }
  .details-info p {
    margin: 3px 0;
  }
  .table thead th {
    background-color: #e9ecef;
    font-weight: bold;
  }
  .total-section {
    margin-top: 20px;
    font-size: 1.2rem;
    font-weight: 700;
    color: #003366;
    display: flex;
    justify-content: flex-end;
    gap: 40px;
  }
  .total-section span {
    font-weight: normal;
    color: #555;
  }
  .notes {
    margin-top: 25px;
    font-size: 0.85rem;
    color: #666;
  }
  .notes p {
    margin-bottom: 10px;
  }
  .footer-thanks {
    margin-top: 40px;
    font-weight: bold;
    font-size: 1.1rem;
    text-align: center;
  }
</style>

</head>
<body>

<!-- Cabecera -->
<header class="voucher-header">
  <a href="http://localhost:8080/CineJ3/Cliente/MetodoPago.jsp" class="back-link">
    <i class="fas fa-arrow-left"></i> Atrás
  </a>
  <div class="title">Boleta</div>
  <div class="icons">
    <i class="fas fa-user-circle"></i>
    <i class="fas fa-times"></i>
  </div>
</header>

<div class="voucher-container">

  <div class="header-voucher">
    <div class="order-info">
      <p>N° de Orden de compra:</p>
      <p class="order-number">70-04106950</p>
    </div>

    <div class="qr-code">
      <img src="http://localhost:8080/CineJ3/Cliente/images/qr.png" alt="Código QR" />
    </div>

    <div class="details-info">
      <p><strong>Cine:</strong> Alcazar</p>
      <p><strong>Película:</strong> Asu Mare 2 2D DOB</p>
      <p><strong>Sala:</strong> 06</p>
      <p><strong>Función:</strong> 11/04/2015 18:00</p>
      <p><strong>Butacas:</strong> N10, N11</p>
      <p><strong>Cant. butacas:</strong> 2</p>
      <p><strong>Cliente:</strong> cesar becerra alvarado</p>
    </div>
  </div>

  <h5>Entrada</h5>
  <table class="table table-sm">
    <thead>
      <tr>
        <th>Tipo</th>
        <th>Precio</th>
        <th>Cantidad</th>
        <th>Total</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>General may 12 años</td>
        <td>S/. 22.00</td>
        <td>2</td>
        <td>S/. 44.00</td>
      </tr>
    </tbody>
  </table>

  <h5>Dulcería</h5>
  <table class="table table-sm">
    <thead>
      <tr>
        <th>Producto</th>
        <th>Precio</th>
        <th>Cantidad</th>
        <th>Total</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>
          Combo 3 Regular<br/>
          1 Chicha Morada Mediana<br/>
          1 Pepsi Mediana<br/>
          1 Pop Corn Grande
        </td>
        <td>S/. 20.50</td>
        <td>1</td>
        <td>S/. 20.50</td>
      </tr>
    </tbody>
  </table>

  <div class="total-section">
    <div><span>Total Compra</span> S/. 64.50</div>
    <div><span>Total en dólares</span> USD 20.80</div>
  </div>

  <div class="notes">
    <p><strong>• La compra y el canje de las entradas y/o combos, solo son válidos para el mismo día de la función.</strong></p>
    <p><strong>• Si utilizaste códigos promocionales o boletos corporativos debes presentar los cupones físicos en el ingreso a sala.</strong></p>
    <p><strong>• Si utilizaste vales de consumo corporativos debes presentar los cupones físicos en la zona de despacho de dulcería.</strong></p>
    <p><strong>• Esta compra no permite cambio de función, anulación y/o devolución de dinero.</strong></p>

    <p>Estimado cliente:<br/>
    Para un mejor servicio realiza los siguientes pasos:<br/>
    1. Imprime este documento o presenta tu smartphone con el código QR al ingreso a salas. No tiene que pasar por boletería.<br/>
    2. Si compraste un producto en dulcería, dirígete a la zona de despacho para recoger tu pedido.<br/>
    3. Si solo compraste entradas, dirígete al ingreso de tu sala.<br/>
    4. Este no es un comprobante de pago válido. Si lo necesitas, el mismo día de tu función acércate a la oficina de administración del cine para recogerlo.<br/>
    5. Cualquier duda respecto al pago, realízala directamente con su banco emisor.</p>
  </div>

  <div class="footer-thanks">
    Gracias por su compra
  </div>

</div>

<!-- Bootstrap JS y dependencias -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>
