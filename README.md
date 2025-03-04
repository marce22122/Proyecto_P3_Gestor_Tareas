# 📝 Proyecto_P3_Gestor_Tareas  

**TaskAdmin - Sistema de Gestión de Tareas para Administradores**  

## 📌 Descripción  
Sistema de gestión de tareas desarrollado en **Java con Spring Boot** para el backend y **HTML, CSS, JavaScript y Bootstrap** para el frontend. Esta aplicación permite a los administradores gestionar tareas y usuarios de manera eficiente, enviando la tarea asignada al correo electrónico del usuario específico.  

---

## ✨ Características principales  

✅ **Gestión de usuarios** (crear, obtener, actualizar, eliminar).<br>
✅ **Asignación de tareas** a usuarios específicos.<br>
✅ **Notificación por correo** al usuario asignado a una tarea.<br>
✅ **Autenticación y autorización con JWT**.<br>
✅ **Interfaz moderna y responsiva con Bootstrap**.<br>
✅ **Seguridad mejorada** con control de acceso por roles (`ROLE_ADMIN`).<br>
✅ **Manejo de errores centralizado** con `GlobalExceptionHandler`.<br>
✅ **Persistencia de datos con MySQL**.<br>  

---

## 📂 Estructura del Proyecto  

### 🔹 Backend (Spring Boot)  
📁 `controller/` → Define los endpoints de la API REST.<br>
📁 `service/` → Contiene la lógica de negocio.<br>
📁 `repository/` → Interfaces para acceso a datos con JPA.<br>
📁 `entity/` → Entidades de la base de datos.<br>
📁 `util/` → Configuración de autenticación con JWT y DTO.<br>

### 🔹 Frontend (HTML, CSS, JavaScript, Bootstrap)  
📄 `login.html` → Acceso al sistema.<br>
📄 `dashboard.html` → Vista principal del sistema.<br>
📄 `task.html` → Sección de tareas.<br>
📄 `user.html` → Gestión de usuarios.<br>
📄 `settings.html` → Configuración de perfil y preferencias.<br>
📁 `js/` → Contiene los scripts en ES6 para llamadas a la API.<br>
📁 `css/` → Contiene los estilos y animaciones de los HTML.<br>
📁 `icons/` → Contiene iconos y fondos de pantalla de los HTML.<br>

---

## ⚙️ Requisitos  

🔹 **Java 21**<br>
🔹 **Spring Boot**<br>
🔹 **MySQL**<br>
🔹 **Node.js** *(opcional, para desarrollo frontend avanzado)*<br>

---

## 🚀 Instalación y Ejecución  

### 1️⃣ Clonar el Repositorio
git clone https://github.com/marce22122/Proyecto_P3_Gestor_Tareas.git  

### 2️⃣ Configurar la Base de Datos
Crear una base de datos en MySQL:

CREATE DATABASE taskadmin_db; <br>
Luego, edita el archivo application.properties con tus credenciales de MySQL: Ejemplo

spring.datasource.url=jdbc:mysql://localhost:3306/taskadmin_db <br>
spring.datasource.username=tu_usuario <br>
spring.datasource.password=tu_contraseña <br>

### 3️⃣ Configurar el Correo Electrónico
Para enviar notificaciones cuando se asigna una tarea, debes configurar un servidor SMTP en application.properties: <br>

spring.mail.username=tu_correo@gmail.com <br>
spring.mail.password=tu_contraseña <br>

#Nota: Si usas Gmail, debes habilitar el acceso a aplicaciones menos seguras o generar una contraseña de aplicación desde tu cuenta de Google. <br>

### 4️⃣ Configurar la Clave Secreta JWT
En application.properties, debes definir una clave secreta de 32 caracteres para la generación de tokens JWT: <br>

jwt.secret=clave_secreta_de_32_caracteres <br>
jwt.expirationMs=3600000 (expira en 1 hora) <br>

### 5️⃣ Ejecutar el Backend

mvn spring-boot:run <br>
La API normalmente estará disponible en http://localhost:8080. <br>
### Registra un usuario administrador usando Postman: <br>
Abrir postman y en POST /users/register asignamos un usuario Admin para usarlo en el Frontend <br>
![image](https://github.com/user-attachments/assets/d7dc21c8-4b5d-4943-8ec1-07857f8274d6)

json de ejemplo:
{
  "email": "admin.gt@gmail.com",
  "name": "Administrador GT",
  "password": "AdminSecure123",
  "role": "ROLE_ADMIN"
}

### 6️⃣ Ejecutar el Frontend
Abre login.html en tu navegador o usa Live Server en VS Code y accedemos mediante el email y contraseña.

---

## 🔗Endpoints Principales
🔹 Usuarios(users)

POST /users/register → Registra un nuevo usuario. <br>
GET /users → Obtiene la lista de usuarios. <br>
PUT /users/{id} → Actualiza un usuario. <br>
DELETE /users/{id} → Elimina un usuario. <br>
GET /users/count → Obtiene cantidad de usuarios. <br>
GET /users/me → Obtiene el Administrador que inicio sesión. <br>

🔹 Tareas(tasks)

POST /tasks/{userId} → Crea una nueva tarea. <br>
GET /tasks → Lista todas las tareas. <br>
GET /tasks/{id} → Obtiene una tarea por ID. <br>
PUT /tasks/{id} → Actualiza una tarea. <br>
DELETE /tasks/{id} → Elimina una tarea. <br>
GET /tasks/count → Obtiene cantidad de tareas. <br>

---

## 🔐Seguridad y Autenticación

🔹 Se utiliza JWT para autenticar a los administradores.<br> 
🔹 Los endpoints están protegidos según el rol del usuario (ROLE_ADMIN).<br> 
🔹 Se requiere un token para acceder a la API (una vez iniciada sesión, el token se almacena y se usa en las peticiones).<br>

---

## 🌟Mejoras Futuras
✔️ Implementar paginación y filtros de búsqueda avanzada.<br> 
✔️ Agregar logs de actividad para mayor trazabilidad.<br> 
✔️ Optimizar el rendimiento del frontend con AJAX.<br>

---

## 📜Licencia
Este proyecto está bajo la licencia MIT.

---

## 🎓 Créditos
"Este proyecto fue desarrollado como parte de mi formación en el Instituto Manuel Belgrano, aplicando mis conocimientos en desarrollo de software. Implementé tecnologías como Java, Spring Boot, HTML, CSS, Bootstrap, JavaScript y MySQL para crear una solución funcional y eficiente. El objetivo fue fortalecer mis habilidades en backend y frontend, aplicando buenas prácticas de desarrollo y estructura de código."
