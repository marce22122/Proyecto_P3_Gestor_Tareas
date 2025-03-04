import { setToken } from "./auth.js";

document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");
    const errorMessage = document.getElementById("errorMessage");

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password })
            });

            if (!response.ok) throw new Error("Credenciales incorrectas");

            const data = await response.json();
            const token = data.data;

            const payload = JSON.parse(atob(token.split('.')[1]));
            const role = payload.role;

            if (role !== "ROLE_ADMIN") {
                showErrorMessage("Acceso denegado: Solo los administradores pueden ingresar.");
                setTimeout(() => { window.location.href = "login.html"; }, 3000);
                return;
            }

            setToken(token);  // Usa la función de auth.js
            window.location.href = "dashboard.html";

        } catch (error) {
            showErrorMessage(error.message);
        }
    });

    function showErrorMessage(message) {
        errorMessage.textContent = message;
        errorMessage.style.display = "block";
        setTimeout(() => { errorMessage.style.display = "none"; }, 4000);
    }
});

