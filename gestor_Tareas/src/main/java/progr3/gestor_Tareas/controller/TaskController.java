package progr3.gestor_Tareas.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import progr3.gestor_Tareas.entity.Task;
import progr3.gestor_Tareas.service.jpa.TaskServiceImpl;
import progr3.gestor_Tareas.util.ResponseDTO;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	@Autowired
	private TaskServiceImpl taskService;

	@PostMapping("/{userId}")
	public ResponseEntity<ResponseDTO<Task>> createTask(@PathVariable Long userId, @Valid @RequestBody Task task,
			BindingResult result) {

		if (result.hasErrors()) {
			String errorMsg = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).findFirst()
					.orElse("Error de validación");
			return ResponseEntity.badRequest().body(ResponseDTO.error(errorMsg));
		}

		Task savedTask = taskService.createTask(task, userId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseDTO.success(savedTask, "Tarea creada exitosamente"));
	}


	@GetMapping("/{id}")
	public ResponseEntity<?> getTaskById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		Task task = taskService.findById(id);
		return ResponseEntity.ok(task);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseDTO<Task>> updateTask(@PathVariable Long id, @Valid @RequestBody Task updatedTask,
			BindingResult result) {
		if (result.hasErrors()) {
			String errorMsg = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).findFirst()
					.orElse("Error de validación");
			return ResponseEntity.badRequest().body(ResponseDTO.error(errorMsg));
		}

		Task savedTask = taskService.update(id, updatedTask);
		return ResponseEntity.ok(ResponseDTO.success(savedTask, "Tarea actualizada correctamente"));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDTO<Void>> deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.ok(ResponseDTO.success(null, "Tarea eliminada correctamente"));
	}

	@GetMapping
	public ResponseEntity<ResponseDTO<List<Task>>> getAllTasks() {
		List<Task> tasks = taskService.getAllTasks();
		return ResponseEntity.ok(ResponseDTO.success(tasks, "Lista de todas las tareas"));
	}

	@GetMapping("/count")
	public ResponseEntity<ResponseDTO<Long>> getTaskCount() {
		long count = taskService.countTasks();
		return ResponseEntity.ok(ResponseDTO.success(count, "Cantidad de tareas obtenida"));
	}
}
