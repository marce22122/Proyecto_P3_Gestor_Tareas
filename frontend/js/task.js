import { fetchWithAuth, getToken, isAuthenticated, logout, showSessionExpiredModal } from './auth.js';

const newTaskBtn = document.getElementById('newTaskBtn');
const listTaskBtn = document.getElementById('listTaskBtn');
const taskFormContainer = document.getElementById('taskFormContainer');
const taskTableContainer = document.getElementById('taskTableContainer');
const formTitle = document.getElementById('formTitle');
const cancelTaskBtn = document.getElementById('cancelTaskBtn');
const form = document.getElementById('createTaskForm');

let isEditing = false;
let currentTaskId = null;
document.addEventListener("DOMContentLoaded", async function () {
    if (!isAuthenticated()) {
        showSessionExpiredModal(); // ✅ Muestra el modal si no hay token
        return;
    }
});

// FUNCIONES DE INTERFAZ
newTaskBtn.addEventListener('click', () => {
    isEditing = false;
    currentTaskId = null;
    form.reset();
    formTitle.textContent = 'Añadir Tarea';

    taskFormContainer.classList.remove('d-none');
    taskTableContainer.classList.add('d-none');
});

listTaskBtn.addEventListener('click', () => {
    taskFormContainer.classList.add('d-none');
    taskTableContainer.classList.remove('d-none');
    loadTasks();
});

cancelTaskBtn.addEventListener('click', () => {
    taskFormContainer.classList.add('d-none');
    taskTableContainer.classList.remove('d-none');
    form.reset();
    isEditing = false;
    currentTaskId = null;
});

// CRUD DE TAREAS

const loadTasks = async () => {
    try {
        const response = await fetchWithAuth('http://localhost:8080/tasks', {
            headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        const data = await response.json();
        const taskTable = document.getElementById('Table').getElementsByTagName('tbody')[0];

        taskTable.innerHTML = '';

        if (data.success) {
            data.data.forEach(task => {
                const row = taskTable.insertRow();
                row.innerHTML = `
                    <td>${task.id}</td>
                    <td>${task.title}</td>
                    <td>${task.description}</td>
                    <td>${task.status}</td>
                    <td>${task.dueDate}</td>
                    <td>${task.user.name}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editTask(${task.id})">✏️</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteTask(${task.id})">🗑️</button>
                    </td>
                `;
            });
        } else {
            alert('Error al cargar las tareas: ' + data.message);
        }
    } catch (error) {
    }
};

const getTaskById = async (id) => {
    try {
        const response = await fetchWithAuth(`http://localhost:8080/tasks/${id}`, {
            headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        const data = await response.json();
        if (response.ok) return data;
        else alert(`Error al obtener la tarea: ${data.message}`);
    } catch (error) {
        alert(`Error al conectar con el servidor: ${error.message}`);
    }
};

const createTask = async (taskData) => {
    const userId = document.getElementById('userId').value;
    try {
        const response = await fetchWithAuth(`http://localhost:8080/tasks/${userId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(taskData)
        });

        if (response.ok) {
            alert('Tarea creada exitosamente');
            loadTasks();
            cancelTaskBtn.click();
        } else {
            const errorData = await response.json();
            alert(`Error al crear la tarea: ${errorData.message}`);
        }
    } catch (error) {
        alert(`Error al conectar con el servidor: ${error.message}`);
    }
};

const updateTask = async (id, updatedTask) => {
    try {
        const response = await fetchWithAuth(`http://localhost:8080/tasks/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedTask)
        });

        const data = await response.json();
        if (response.ok) {
            alert(data.message || 'Tarea actualizada correctamente');
            loadTasks();
            cancelTaskBtn.click();
        } else {
            alert(`Error al actualizar la tarea: ${data.message}`);
        }
    } catch (error) {
        alert(`Error al conectar con el servidor: ${error.message}`);
    }
};

window.deleteTask = async (id) => {
    if (!confirm('¿Estás seguro de eliminar esta tarea?')) return;

    try {
        const response = await fetchWithAuth(`http://localhost:8080/tasks/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${getToken()}` }
        });

        const data = await response.json();
        if (response.ok) {
            alert(data.message || 'Tarea eliminada correctamente');
            loadTasks();
        } else {
            alert(`Error al eliminar la tarea: ${data.message}`);
        }
    } catch (error) {
        alert(`Error al conectar con el servidor: ${error.message}`);
    }
};

window.editTask = async (id) => {
    const task = await getTaskById(id);
    if (task) {
        isEditing = true;
        currentTaskId = id;

        formTitle.textContent = 'Editar Tarea';
        document.getElementById('taskTitle').value = task.title;
        document.getElementById('taskDescription').value = task.description;
        document.getElementById('taskDeadline').value = task.dueDate;
        document.getElementById('taskStatus').value = task.status;
        document.getElementById('userId').value = task.user.id;

        document.getElementById('taskTitle').setAttribute('disabled', 'true');
        document.getElementById('userId').setAttribute('disabled', 'true');

        taskFormContainer.classList.remove('d-none');
        taskTableContainer.classList.add('d-none');
    }
};


newTaskBtn.addEventListener('click', () => {
    isEditing = false;
    currentTaskId = null;
    form.reset();
    formTitle.textContent = 'Añadir Tarea';

    document.getElementById('taskTitle').removeAttribute('disabled');
    document.getElementById('userId').removeAttribute('disabled');

    taskFormContainer.classList.remove('d-none');
    taskTableContainer.classList.add('d-none');
});
// MANEJADOR DE FORMULARIO

form.addEventListener('submit', (e) => {
    e.preventDefault();

    const taskData = {
        title: document.getElementById('taskTitle').value,
        description: document.getElementById('taskDescription').value,
        dueDate: document.getElementById('taskDeadline').value,
        status: document.getElementById('taskStatus').value
    };

    if (isEditing && currentTaskId) {
        updateTask(currentTaskId, taskData);
    } else {
        createTask(taskData);
    }
});

// INICIO
loadTasks();
document.getElementById("logoutBtn").addEventListener("click", () => {
    logout();
});