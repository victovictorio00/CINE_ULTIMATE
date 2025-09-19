<%-- 
    Document   : SeleccionCombo
    Created on : 28 may. 2025, 3:56:36
    Author     : Proyecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<title>Seleccionar Combo</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet"/>
<style>
    body {
        background-color: #f0f4f7;
        font-family: Arial, sans-serif;
        padding-top: 80px;
        padding-bottom: 80px;
    }
    /* Header similar */
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
    /* Cards container */
    .combo-container {
        max-width: 1200px;
        margin: auto;
    }
    /* Individual card style */
    .combo-card {
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 12px rgb(0 0 0 / 0.1);
        padding: 15px;
        margin-bottom: 30px;
        text-align: center;
        position: relative;
        height: 100%;
        display: flex;
        flex-direction: column;
    }
    .combo-card img {
        max-width: 100%;
        border-radius: 8px 8px 0 0;
        margin-bottom: 12px;
    }
    .combo-title {
        font-weight: 700;
        text-transform: uppercase;
        margin-bottom: 8px;
        color: #0b2c5a;
        font-size: 14px;
    }
    .combo-desc {
        font-size: 12px;
        color: #444;
        margin-bottom: 20px;
        flex-grow: 1;
    }
    .combo-price {
        font-weight: 600;
        font-size: 16px;
        margin-bottom: 12px;
        color: #0b2c5a;
    }
    /* Controls for quantity */
    .quantity-control {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;
        margin-bottom: 10px;
    }
    .quantity-control button {
        border: none;
        background-color: #eee;
        border-radius: 50%;
        width: 28px;
        height: 28px;
        font-size: 18px;
        font-weight: bold;
        cursor: pointer;
        line-height: 1;
    }
    .quantity-control span {
        font-size: 16px;
        width: 30px;
        text-align: center;
        display: inline-block;
    }
    /* Botón continuar fijo */
    .btn-continue {
        position: fixed;
        bottom: 20px;
        right: 20px;
        background-color: #d81071;
        color: white;
        border: none;
        padding: 14px 30px;
        font-size: 16px;
        border-radius: 25px;
        cursor: pointer;
        box-shadow: 0 4px 10px rgba(216,16,113,0.6);
        z-index: 1100;
        transition: background-color 0.3s ease;
    }
    .btn-continue:hover {
        background-color: #ad0b58;
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
    // Código para controlar el incremento/decremento de combos
    document.addEventListener("DOMContentLoaded", () => {
        document.querySelectorAll('.quantity-control button').forEach(button => {
            button.addEventListener('click', e => {
                const container = e.target.closest('.quantity-control');
                const span = container.querySelector('span');
                let value = parseInt(span.textContent);
                if(e.target.classList.contains('decrease')) {
                    if(value > 0) span.textContent = value - 1;
                } else if(e.target.classList.contains('increase')) {
                    span.textContent = value + 1;
                }
            });
        });
    });
</script>
</head>
<body>

<header class="custom-header">
    <a href="SeleccionAsiento.jsp" class="back-link">← Atrás</a>
    <h1>Dulcería</h1>
</header>

<div class="combo-container container">
    <div class="row mb-4">
        <%-- Ejemplo 5 combos --%>
        <%-- Aquí idealmente sacarías combos de tu DAO --%>
        <div class="col-md-4">
            <div class="combo-card">
                <img src="http://localhost:8080/CineJ3/Cliente/images/combo1.png" alt="Combo 1"/>
                <div class="combo-title">COMBO 2 SALADO + 2 KIT KAT OL</div>
                <div class="combo-desc">1 Canchita Gigante (Salado) + 2 Bebidas (32oz) + 2 KIT KAT.<br>*Sabor bebidas sujeto a stock / canchita sin refill</div>
                <div class="combo-price">S/42.00</div>
                <div class="quantity-control">
                    <button class="decrease">-</button>
                    <span>0</span>
                    <button class="increase">+</button>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="combo-card">
                <img src="http://localhost:8080/CineJ3/Cliente/images/combo2.png" alt="Combo 2"/>
                <div class="combo-title">COMBO 2 MIX + 2 KIT KAT OL</div>
                <div class="combo-desc">1 Canchita Gigante (Mix) + 2 Bebidas (32oz) + 2 KIT KAT.<br>*Sabor bebidas sujeto a stock / canchita sin refill</div>
                <div class="combo-price">S/45.00</div>
                <div class="quantity-control">
                    <button class="decrease">-</button>
                    <span>1</span>
                    <button class="increase">+</button>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="combo-card">
                <img src="http://localhost:8080/CineJ3/Cliente/images/combo3.png" alt="Combo 3"/>
                <div class="combo-title">COMBO 2 DULCE + 2 KIT KAT OL</div>
                <div class="combo-desc">1 Canchita Gigante (Dulce) + 2 Bebidas (32oz) + 2 KIT KAT.<br>*Sabor bebidas sujeto a stock / canchita sin refill</div>
                <div class="combo-price">S/45.00</div>
                <div class="quantity-control">
                    <button class="decrease">-</button>
                    <span>0</span>
                    <button class="increase">+</button>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-4">
            <div class="combo-card">
                <img src="http://localhost:8080/CineJ3/Cliente/images/combo1.png" alt="Combo 4"/>
                <div class="combo-title">COMBO 1 DULCE + KIT KAT OL</div>
                <div class="combo-desc">1 Canchita Grande (Dulce) + 1 Bebida (32oz) + 1 Kit Kat.<br>*Sabor bebida sujeto a stock / canchita sin refill</div>
                <div class="combo-price">S/24.50</div>
                <div class="quantity-control">
                    <button class="decrease">-</button>
                    <span>0</span>
                    <button class="increase">+</button>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="combo-card">
                <img src="http://localhost:8080/CineJ3/Cliente/images/combo1.png" alt="Combo 5"/>
                <div class="combo-title">COMBO 1 SALADO + KIT KAT OL</div>
                <div class="combo-desc">1 Canchita Grande (Salada) + 1 Bebida (32oz) + 1 Kit Kat.<br>*Sabor bebida sujeto a stock / canchita sin refill</div>
                <div class="combo-price">S/21.50</div>
                <div class="quantity-control">
                    <button class="decrease">-</button>
                    <span>1</span>
                    <button class="increase">+</button>
                </div>
            </div>
        </div>
    </div>
</div>


<a href="http://localhost:8080/CineJ3/Cliente/MetodoPago.jsp" class="btn-link-continue">Continuar</a>

</body>
</html>