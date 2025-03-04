package progr3.gestor_Tareas.entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "El título no puede estar vacío")
	@Size(max = 100, message = "El título debe tener máximo 100 caracteres")
	private String title;
	@NotBlank(message = "La descripción no puede estar vacía")
	@Size(max = 255, message = "La descripción debe tener máximo 255 caracteres")
	private String description;
	@Enumerated(EnumType.STRING)
	@NotNull(message = "El estado de la tarea es obligatorio")
	private TaskStatus status;
	@FutureOrPresent(message = "La fecha de vencimiento no puede ser en el pasado")
	private LocalDate dueDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnoreProperties({ "tasks", "hibernateLazyInitializer", "handler" }) // Evita la referencia cíclica
	private Usuario user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public enum TaskStatus {
		PENDING, IN_PROGRESS, COMPLETED
	}
}
