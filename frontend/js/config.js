import { fetchWithAuth, getToken, isAuthenticated, logout, showSessionExpiredModal } from './auth.js';

document.addEventListener("DOMContentLoaded", async function () {
    if (!isAuthenticated()) {
        showSessionExpiredModal(); //Muestra el modal si no hay token
        return;
    }

    document.getElementById("logoutBtn").addEventListener("click", () => {
        logout();  // Usa logout desde auth.js
    });

    try {
        const response = await fetchWithAuth("http://localhost:8080/users/me", {
            method: "GET",
            headers: { "Authorization": `Bearer ${getToken}` }
        });

        if (response.ok) {
            const result = await response.json();
            document.getElementById("name").value = result.data.name || "";
            document.getElementById("role").value = result.data.role || "";
        } else {
            console.error("No se pudo obtener el perfil:", response.statusText);
        }
    } catch (error) {
        console.error("Error al obtener el perfil:", error.message);
    }
});

// Función para aplicar el tema desde localStorage
function applyTheme() {
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme) {
        document.documentElement.classList.add(savedTheme); // Aplicar el tema guardado
        document.getElementById('theme').value = savedTheme; // Cambiar el valor del select
    } else {
        // Si no hay tema guardado, se aplica el tema claro por defecto
        document.documentElement.classList.add("light");
    }
}

// Función para guardar el tema seleccionado en localStorage
function saveTheme() {
    const selectedTheme = document.getElementById('theme').value;
    document.documentElement.classList.remove('light', 'dark'); // Eliminar clases previas
    document.documentElement.classList.add(selectedTheme); // Aplicar el nuevo tema
    localStorage.setItem("theme", selectedTheme); // Guardar en localStorage
}

// Llamada a applyTheme para que se cargue el tema cuando se acceda a la página
applyTheme();

// Añadir evento al botón para guardar el tema seleccionado
document.getElementById('saveTheme').addEventListener('click', () => {
    saveTheme();
});


