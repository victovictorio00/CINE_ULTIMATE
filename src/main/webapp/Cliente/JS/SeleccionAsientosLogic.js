(function () {
    'use strict';

    // === OBTENER DATOS DE CONFIGURACIÓN ===
    const config = window.asientosData || {};
    const PRECIO_UNITARIO = config.precioUnitario || 0;
    const MAX_ASIENTOS = config.maxAsientos || 8;

    // === CREAR SET DE ASIENTOS SELECCIONADOS ===
    const selectedSeats = new Set(); // ✅ Crear aquí, no reutilizar del config

    // === ELEMENTOS DEL DOM ===
    const selectedSeatsDiv = document.getElementById('selected-seats-list');
    const selectedCountSpan = document.getElementById('selected-count');
    const subtotalSpan = document.getElementById('subtotal');
    const totalSpan = document.getElementById('total');
    const btnContinue = document.getElementById('btnContinue');
    const inputSelectedSeats = document.getElementById('inputSelectedSeats');
    const formAsientos = document.getElementById('formAsientos');

    // === VALIDACIÓN DE ELEMENTOS ===
    if (!selectedSeatsDiv || !btnContinue || !inputSelectedSeats) {
        console.error('❌ Error: Elementos del DOM no encontrados');
        return;
    }

    // === FUNCIONES ===
    function updateSummary() {
        const count = selectedSeats.size;
        const total = count * PRECIO_UNITARIO;

        // Actualizar contadores
        if (selectedCountSpan) selectedCountSpan.textContent = count;
        if (subtotalSpan) subtotalSpan.textContent = total.toFixed(2);
        if (totalSpan) totalSpan.textContent = total.toFixed(2);

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

    function toggleSeat(seatElement) {
        const seatCode = seatElement.dataset.seat;
        const isAvailable = seatElement.dataset.available === 'true';

        console.log('🔍 Click en asiento:', seatCode, 'Disponible:', isAvailable);

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

    console.log('🎫 Asientos disponibles encontrados:', asientosDisponibles.length);

    if (asientosDisponibles.length === 0) {
        console.warn('⚠️ No hay asientos disponibles');
        selectedSeatsDiv.textContent = 'No hay asientos disponibles para esta función';
    }

    asientosDisponibles.forEach(function (seat) {
        // Click
        seat.addEventListener('click', function (e) {
            e.preventDefault();
            console.log('👆 Click detectado en:', this.dataset.seat);
            toggleSeat(this);
        });

        // Teclado (accesibilidad)
        seat.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggleSeat(this);
            }
        });

        // Hacer focusable y agregar cursor pointer
        seat.setAttribute('tabindex', '0');
        seat.style.cursor = 'pointer';
    });

    // Validación al enviar formulario
    if (formAsientos) {
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
    }

    // === INICIALIZACIÓN ===
    updateSummary();
    console.log('✅ Sistema de selección de asientos listo');
    console.log('💰 Precio unitario: S/.', PRECIO_UNITARIO);
    console.log('🎯 Máximo de asientos:', MAX_ASIENTOS);

})();