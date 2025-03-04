package progr3.gestor_Tareas.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email debe ser válido")
	@Column(unique = true)
	private String email;
	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
	private String name;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

	@NotBlank(message = "La contraseña no puede estar vacía")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore // Evita que se muestre el usuario dentro de cada tarea
	private List<Task> tasks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public enum Role {
		ROLE_USER, ROLE_ADMIN
	}

	public Usuario() {
	}

	public Usuario(String email, String name, String password, Role role) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.role = role;
	}

}
