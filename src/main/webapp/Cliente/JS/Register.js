(function () {
    const form = document.getElementById("registroForm");
    form.addEventListener("submit", function (event) {
        const pass = document.getElementById("password").value.trim();
        const confirm = document.getElementById("passwordconfirm").value.trim();

        // Validación HTML5 general
        //if (!form.checkValidity()) {
        //  event.preventDefault();
        //event.stopPropagation();
        //alert("Revisa los campos resaltados. Asegúrate de completar los obligatorios y con el formato correcto.");
        //return;
        //}
        if (pass !== confirm) {
            event.preventDefault();
            alert("Las contraseñas no coinciden.");
            return;
        }

        if (pass.length < 6) {
            event.preventDefault();
            alert("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        // Generar SHA-256 y guardarlo en el input oculto
        document.getElementById("password").value = sha256(pass);
        document.getElementById("passwordconfirm").value = "";
    }, false);
})();


