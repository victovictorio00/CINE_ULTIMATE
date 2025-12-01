document.getElementById("loginForm").addEventListener("submit", function(e){
    let passInput = this.querySelector('input[name="password"]');
    
    if(passInput.value) {
        // reemplazamos el valor por el hash SHA-256
        passInput.value = sha256(passInput.value);
    }
});
