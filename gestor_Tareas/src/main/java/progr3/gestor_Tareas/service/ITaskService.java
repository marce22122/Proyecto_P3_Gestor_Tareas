package progr3.gestor_Tareas.service;

import java.util.List;
import progr3.gestor_Tareas.entity.Task;
import progr3.gestor_Tareas.entity.Usuario;

public interface ITaskService {

	List<Task> getTasksByUser(Usuario user);

	void deleteTask(Long taskId);

	Task update(Long id, Task task);

	Task findById(Long id);

	List<Task> getAllTasks();

	Task createTask(Task task, Long userId);

	long countTasks();

}
