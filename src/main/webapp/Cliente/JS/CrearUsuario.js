async function hashPassword() {
    const passField = document.getElementById("password");

    if (!passField.value.trim()) return true; // no debería pasar porque es required

    // Convertir a SHA-256
    const encoder = new TextEncoder();
    const data = encoder.encode(passField.value);
    const hashBuffer = await crypto.subtle.digest("SHA-256", data);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, "0")).join("");

    passField.value = hashHex;
    return true;
}

document.querySelector("form").addEventListener("submit", async (e) => {
    e.preventDefault();
    await hashPassword();
    e.target.submit();
});

