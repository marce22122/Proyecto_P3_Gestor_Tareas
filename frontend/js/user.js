import { fetchWithAuth, getToken, isAuthenticated, logout, showSessionExpiredModal } from './auth.js';

const addUserBtn = document.getElementById('addUserBtn');
const listUserBtn = document.getElementById('listUserBtn');
const userFormContainer = document.getElementById('userFormContainer');
const userTableContainer = document.getElementById('userTableContainer');
const formTitle = document.getElementById('formTitle');
const cancelUserBtn = document.getElementById('cancelUserBtn');
const userForm = document.getElementById('userForm');

const userNameInput = document.getElementById('userName');
const userEmailInput = document.getElementById('userEmail');
const userPasswordInput = document.getElementById('userPassword');
const userRoleInput = document.getElementById('userRole');
const userPasswordLabel = document.querySelector('label[for="userPassword"]');

let isEditing = false;
let currentUserId = null;
document.addEventListener("DOMContentLoaded", async function () {
    if (!isAuthenticated()) {
        showSessionExpiredModal();
        return;
    }
});
addUserBtn.addEventListener('click', () => {
    isEditing = false;
    currentUserId = null;
    userForm.reset();
    formTitle.textContent = 'Añadir Usuario';

    userNameInput.disabled = false;
    userRoleInput.disabled = false;
    userPasswordLabel.textContent = 'Contraseña';

    userFormContainer.classList.remove('d-none');
    userTableContainer.classList.add('d-none');
});

listUserBtn.addEventListener('click', () => {
    userFormContainer.classList.add('d-none');
    userTableContainer.classList.remove('d-none');
    loadUsers();
});

cancelUserBtn.addEventListener('click', () => {
    userFormContainer.classList.add('d-none');
    userTableContainer.classList.remove('d-none');
    userForm.reset();
    isEditing = false;
    currentUserId = null;
});
const loadUsers = async () => {
    try {
        const response = await fetchWithAuth('http://localhost:8080/users', {
            headers: { Authorization: `Bearer ${getToken()}` }
        });

        const data = await response.json();
        const userTable = document.querySelector('#Table tbody');
        userTable.innerHTML = '';

        if (response.ok) {
            data.data.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role === 'ROLE_ADMIN' ? 'Administrador' : 'Usuario'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editUser(${user.id}, '${user.name}', '${user.email}', '${user.role}')">✏️</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">🗑️</button>
                    </td>
                `;
                userTable.appendChild(row);
            });
        } else {
            alert(data.message || 'Error al cargar usuarios');
        }
    } catch (error) {
        console.error('Error al cargar usuarios:', error);
    }
};

// CREAR Y MODIFICAR USUARIO
userForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const user = {
        name: userNameInput.value,
        email: userEmailInput.value,
        password: userPasswordInput.value || null,
        role: userRoleInput.value
    };

    const method = isEditing ? 'PUT' : 'POST';
    const url = isEditing ? `http://localhost:8080/users/${currentUserId}` : 'http://localhost:8080/users/register';

    try {
        const response = await fetchWithAuth(url, {
            method,
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${getToken()}`
            },
            body: JSON.stringify(user)
        });

        const data = await response.json();
        if (response.ok) {
            alert(data.message || 'Usuario guardado con éxito');
            cancelUserBtn.click();
            loadUsers();
        } else {
            alert(data.message || 'Error al guardar usuario');
        }
    } catch (error) {
        console.error('Error al guardar usuario:', error);
    }
});

window.editUser = (id, name, email, role) => {
    isEditing = true;
    currentUserId = id;

    formTitle.textContent = 'Editar Usuario';
    userNameInput.value = name;
    userEmailInput.value = email;
    userRoleInput.value = role;

    userNameInput.disabled = true;
    userRoleInput.disabled = true;
    userPasswordLabel.textContent = 'Nueva Contraseña';

    userFormContainer.classList.remove('d-none');
    userTableContainer.classList.add('d-none');
};

window.deleteUser = async (id) => {
    if (!confirm('¿Seguro que deseas eliminar este usuario?')) return;

    try {
        const response = await fetchWithAuth(`http://localhost:8080/users/${id}`, {
            method: 'DELETE',
            headers: { Authorization: `Bearer ${getToken()}` }
        });

        const data = await response.json();
        if (response.ok) {
            alert(data.message || 'Usuario eliminado correctamente');
            loadUsers();
        } else {
            alert(data.message || 'Error al eliminar usuario');
        }
    } catch (error) {
        console.error('Error al eliminar usuario:', error);
    }
};
loadUsers();

document.getElementById("logoutBtn").addEventListener("click", () => {
    logout();
});
