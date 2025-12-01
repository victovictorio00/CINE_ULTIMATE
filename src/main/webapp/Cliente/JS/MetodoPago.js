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
formPago.addEventListener('submit', function (e) {
    if (btnContinuar.disabled) {
        e.preventDefault();
        alert('Por favor, completa todos los campos obligatorios');
        return false;
    }
    btnContinuar.disabled = true;
    btnContinuar.textContent = 'Procesando...';
});
validarFormulario();
