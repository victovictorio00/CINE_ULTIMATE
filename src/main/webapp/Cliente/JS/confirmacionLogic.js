(function () {
    'use strict';

    console.log('✅ Inicializando sistema de confirmación de compra...');

    // Obtener datos de configuración
    const config = window.confirmacionData;

    if (!config || config.totalGeneral === undefined) {
        console.error('❌ Error: totalGeneral no encontrado');
        return;
    }

    // Elementos del DOM
    const formConfirmar = document.getElementById('formConfirmar');
    const btnConfirmar = document.getElementById('btnConfirmar');

    // Validar elementos
    if (!formConfirmar || !btnConfirmar) {
        console.error('❌ Error: Elementos del formulario no encontrados');
        return;
    }

    console.log('💰 Total a pagar: S/.', config.totalGeneral);

    /**
     * Maneja el envío del formulario de confirmación
     */
    formConfirmar.addEventListener('submit', function (e) {
        e.preventDefault();

        // Mostrar confirmación al usuario
        const confirmacion = confirm(
            '¿Confirmar la compra por S/. ' + config.totalGeneral + '?\n\n' +
            'Esta acción es irreversible.'
        );

        if (confirmacion) {
            // Deshabilitar botón para evitar doble submit
            btnConfirmar.disabled = true;
            btnConfirmar.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Procesando...';
            
            console.log('✅ Compra confirmada, enviando formulario...');
            
            // Enviar formulario
            formConfirmar.submit();
        } else {
            console.log('❌ Compra cancelada por el usuario');
        }
    });

    console.log('✅ Sistema de confirmación listo');

})();