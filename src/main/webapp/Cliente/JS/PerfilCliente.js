async function hashPasswordChange(e) {
    e.preventDefault();
    const pass1 = document.querySelector("input[name='nuevaPass']");
    const pass2 = document.querySelector("input[name='confirmPass']");

    if (pass1.value !== pass2.value) {
        alert("Las contraseñas no coinciden.");
        return false;
    }

    const txt = pass1.value;
    const encoder = new TextEncoder();
    const hashBuffer = await crypto.subtle.digest("SHA-256", encoder.encode(txt));
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, "0")).join("");

    // Reemplazar por el hash
    pass1.value = hashHex;
    pass2.value = hashHex;

    e.target.submit();
}

document.querySelector("form[action$='CambiarPasswordServlet']")
        .addEventListener("submit", hashPasswordChange);
