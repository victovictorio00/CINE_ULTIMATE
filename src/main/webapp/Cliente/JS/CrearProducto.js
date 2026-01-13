// Validación de tamaño de imagen antes de enviar
function validarFormulario() {
    const inputFoto = document.getElementById("foto");
    const archivo = inputFoto.files[0];
    const maxSize = 1024 * 1024; // 1 MB

    if (archivo && archivo.size > maxSize) {
        alert("La imagen es demasiado grande. El tamaño máximo permitido es 1 MB.");
        inputFoto.value = "";
        return false;
    }
    return true;
}