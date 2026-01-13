(function () {
    document.querySelectorAll('.quantity-control button').forEach(button => {
        button.addEventListener('click', e => {
            e.preventDefault();
            const clickedButton = e.currentTarget;
            const controlContainer = clickedButton.closest('.quantity-control');
            if (!controlContainer)
                return;

            const idProducto = parseInt(controlContainer.dataset.id);
            const precio = parseFloat(controlContainer.dataset.precio);
            const span = controlContainer.querySelector('.quantity-value');
            let value = parseInt(span.textContent);

            const isDecrease = clickedButton.classList.contains('decrease');
            const isIncrease = clickedButton.classList.contains('increase');

            if (isDecrease && value > 0) {
                value--;
            } else if (isIncrease) {
                value++;
            }

            span.textContent = value;

            // ✅ CORRECCIÓN: usar window.dulceriaData en lugar de data
            if (value > 0) {
                window.dulceriaData.carritoState[idProducto] = value;
            } else {
                delete window.dulceriaData.carritoState[idProducto];
            }

            actualizarBotonFlotante();
        });
    });
})();

function actualizarBotonFlotante() {
    // ✅ CORRECCIÓN: usar window.dulceriaData
    const data = window.dulceriaData;

    if (!data.esFlujoCompra)
        return;

    let totalItems = 0;
    let totalPrecio = 0;

    document.querySelectorAll('.quantity-control').forEach(control => {
        const idProducto = parseInt(control.dataset.id);
        const precio = parseFloat(control.dataset.precio);
        const cantidad = data.carritoState[idProducto] || 0;

        totalItems += cantidad;
        totalPrecio += precio * cantidad;
    });

    const spanTotal = document.getElementById('totalDulceria');
    const btnContinuar = document.getElementById('btnContinuar');

    if (totalItems > 0) {
        spanTotal.style.display = 'inline';
        spanTotal.textContent = ' · S/. ' + totalPrecio.toFixed(2);
        btnContinuar.innerHTML = '<i class="fas fa-arrow-right"></i> Continuar (' + totalItems + ' productos)' + spanTotal.outerHTML;
    } else {
        btnContinuar.innerHTML = '<i class="fas fa-arrow-right"></i> Continuar sin dulcería';
    }
}

function continuarConCompra() {
    // ✅ CORRECCIÓN: usar window.dulceriaData
    const data = window.dulceriaData;
    const form = document.getElementById('carritoForm');
    const container = document.getElementById('productosHidden');

    container.innerHTML = '';

    for (const [idProducto, cantidad] of Object.entries(data.carritoState)) {
        if (cantidad > 0) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'producto_' + idProducto;
            input.value = cantidad;
            container.appendChild(input);
        }
    }

    console.log('Enviando carrito:', data.carritoState);
    form.submit();
}