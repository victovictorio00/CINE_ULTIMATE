(function () {
    'use strict';

    // Obtener datos de configuración
    const data = window.detallePeliculaData;

    if (!data || !data.precioFormateado) {
        return;
    }

    // Elementos del DOM
    const horarioBtns = Array.from(document.querySelectorAll('.horario-btn'));
    const btnReservar = document.getElementById('btnReservar');
    const inputIdFuncion = document.getElementById('inputIdFuncion');

    // Validar elementos
    if (!btnReservar || !inputIdFuncion) {
        return;
    }

    // Estado inicial
    btnReservar.disabled = true;
    btnReservar.setAttribute('aria-disabled', 'true');

    /**
     * Limpia la selección de todos los horarios
     */
    function clearSelection() {
        horarioBtns.forEach(function (b) {
            b.classList.remove('active');
            b.style.background = '';
            b.style.color = '';
            b.style.borderColor = '';
        });

        inputIdFuncion.value = '';
        btnReservar.disabled = true;
        btnReservar.setAttribute('aria-disabled', 'true');
        btnReservar.title = "Selecciona un horario";
        btnReservar.innerHTML = "🎟 Reservar";
    }

    // Inicializar con selección limpia
    clearSelection();

    // Adjuntar eventos a cada botón de horario
    horarioBtns.forEach(function (btn) {
        // Click
        btn.addEventListener('click', function () {
            const wasSelected = this.classList.contains('active');
            clearSelection();

            if (!wasSelected) {
                // Seleccionar este botón
                this.classList.add('active');

                // Aplicar estilos de selección
                const accentColor = window.getComputedStyle(document.documentElement)
                        .getPropertyValue('--accent') || '#FF5733';
                this.style.background = accentColor;
                this.style.color = '#fff';
                this.style.borderColor = 'transparent';

                // Guardar ID de función
                const id = this.dataset.idfuncion;
                inputIdFuncion.value = id;

                // Habilitar botón de reserva
                btnReservar.disabled = false;
                btnReservar.removeAttribute('aria-disabled');
                btnReservar.title = "Reservar " + (this.dataset.label || '');
                btnReservar.innerHTML = "🎟 Reservar · " + data.precioFormateado;
                btnReservar.focus();
            }
        });

        // Teclado (accesibilidad)
        btn.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }
        });
    });
})();