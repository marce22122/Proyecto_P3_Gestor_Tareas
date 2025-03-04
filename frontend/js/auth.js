
// Guardar token en localStorage
function setToken(token) {
    localStorage.setItem("token", token);
}

// Obtener token de localStorage
function getToken() {
    return localStorage.getItem("token");
}

// Eliminar token de localStorage (cerrar sesión)
function removeToken() {
    localStorage.removeItem("token");
}

// Verificar si el token existe (usuario autenticado)
function isAuthenticated() {
    return getToken() !== null;
}

// Realizar peticiones fetch con el token (si existe)
async function fetchWithAuth(url, options = {}) {
    const token = getToken();
    options.headers = options.headers || {};

    if (token) {
        options.headers["Authorization"] = `Bearer ${token}`;
    }
    options.headers["Content-Type"] = "application/json";

    try {
        const response = await fetch(url, options);

        if (response.status === 401) {
            showSessionExpiredModal();  //
            throw new Error("Sesión expirada. Por favor, inicie sesión nuevamente.");
        }

        return response;
    } catch (error) {
        showSessionExpiredModal();
        throw error;
    }
}
function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}
// Muestra un modal cuando la sesión expira y redirige al login
function showSessionExpiredModal() {
    const modalElement = document.getElementById("sessionExpiredModal");

    // Asegurarse de que el modal exista antes de intentar mostrarlo
    if (modalElement) {
        const modal = new bootstrap.Modal(modalElement, { backdrop: 'static', keyboard: false });
        modal.show();

        localStorage.removeItem("token");

        setTimeout(() => {
            window.location.href = "login.html";
        }, 5000);

        document.getElementById("redirectLoginBtn").onclick = () => {
            window.location.href = "login.html";
        };
    } else {
        console.error("El modal de sesión expirada no se encontró en el DOM.");
    }
}

export { setToken, getToken, removeToken, isAuthenticated, fetchWithAuth, logout, showSessionExpiredModal };
