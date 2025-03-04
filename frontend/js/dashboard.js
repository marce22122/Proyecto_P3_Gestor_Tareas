import { fetchWithAuth, isAuthenticated, logout, showSessionExpiredModal } from './auth.js';

document.addEventListener("DOMContentLoaded", async function () {
    if (!isAuthenticated()) {
        showSessionExpiredModal();
        return;
    }

    document.getElementById("logoutBtn").addEventListener("click", () => {
        logout();
    });

    try {
        const [responseTasks, responseUsers] = await Promise.all([
            fetchWithAuth("http://localhost:8080/tasks/count"),
            fetchWithAuth("http://localhost:8080/users/count")
        ]);

        const taskData = await responseTasks.json();
        const userData = await responseUsers.json();

        document.getElementById("taskCount").textContent = taskData.data || "0";
        document.getElementById("userCount").textContent = userData.data || "0";

    } catch (error) {
        console.error("Error en la solicitud:", error);
        showSessionExpiredModal();
    }
});

