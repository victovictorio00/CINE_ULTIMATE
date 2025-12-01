async function hashPasswordIfNeeded() {
    const passwordField = document.querySelector('input[name="password"]');

    // Si está vacío → NO cambiar contraseña → no hash
    if (!passwordField.value.trim()) return true;

    // HASH SHA-256
    const encoder = new TextEncoder();
    const data = encoder.encode(passwordField.value);
    const hashBuffer = await crypto.subtle.digest("SHA-256", data);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, "0")).join("");

    passwordField.value = hashHex;
    return true;
}

// Reemplazar el submit del formulario
document.querySelector("form").addEventListener("submit", async (e) => {
    e.preventDefault();
    await hashPasswordIfNeeded();
    e.target.submit();
});
