package progr3.gestor_Tareas.service.jpa;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import progr3.gestor_Tareas.entity.Task;
import progr3.gestor_Tareas.entity.Usuario;
import progr3.gestor_Tareas.repository.TaskRepository;
import progr3.gestor_Tareas.repository.UsuarioRepository;
import progr3.gestor_Tareas.service.ITaskService;
import progr3.gestor_Tareas.util.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements ITaskService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private final UsuarioRepository userRepository;
	@Autowired
	private EmailServiceImpl emailService;

	public TaskServiceImpl(TaskRepository taskRepository, UsuarioRepository userRepository) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
	}

	public Task createTask(Task task, Long userId) {
	    Usuario user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

	    boolean exists = taskRepository.existsByTitleAndUser(task.getTitle(), user);
	    if (exists) {
	        throw new IllegalArgumentException("Ya existe una tarea con el mismo título para este usuario");
	    }

	    task.setUser(user);
	    Task savedTask = taskRepository.save(task);


        // Verifica si emailService no es nulo antes de llamar al método
        if (emailService != null) {
            String subject = "Nueva tarea asignada";
            String body = "🚀 ¡Hola " + user.getName() + "! \n\n"
                    + "📢 Se te ha asignado una nueva tarea en el sistema:\n\n"
                    + "📌 *Título:* " + task.getTitle() + "\n"
                    + "📝 *Descripción:* " + task.getDescription() + "\n"
                    + "📊 *Estado:* " + task.getStatus() + "\n"
                    + "📅 *Fecha límite:* " + task.getDueDate() + "\n\n"
                    + "⚡ Te recomendamos gestionarla a la brevedad.\n\n"
                    + "🌐 Saludos,*Equipo Gtro* ✅";

            emailService.sendTaskEmail(user.getEmail(), subject, body);
        } else {
            throw new IllegalStateException("El servicio de correo electrónico no está disponible.");
        }

        return savedTask;
	}

	@Override
	public List<Task> getTasksByUser(Usuario user) {
		return taskRepository.findByUserId(user.getId());
	}

	@Transactional
	@Override
	public Task update(Long id, Task taskDetails) {
		Task existingTask = taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el ID: " + id));

		existingTask.setTitle(taskDetails.getTitle());
		existingTask.setDescription(taskDetails.getDescription());
		existingTask.setStatus(taskDetails.getStatus());
		existingTask.setDueDate(taskDetails.getDueDate());

		if (taskDetails.getUser() != null && taskDetails.getUser().getId() != null) {
			Usuario user = userRepository.findById(taskDetails.getUser().getId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Usuario no encontrado con ID: " + taskDetails.getUser().getId()));
			existingTask.setUser(user);
		}

		return taskRepository.save(existingTask);
	}

	@Override
	public void deleteTask(Long taskId) {
		if (!taskRepository.existsById(taskId)) {
			throw new ResourceNotFoundException("La tarea no existe");
		}
		taskRepository.deleteById(taskId);
	}

	@Override
	public Task findById(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));
	}

	@Override
	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}
	@Override
	public long countTasks() {
	    return taskRepository.count();
	}
	
	@Scheduled(cron = "0 0 11 * * ?") // Se ejecutará todos los días a las 11 AM
	public void sendTaskReminders() {
	    LocalDate tomorrow = LocalDate.now().plusDays(1);
	    List<Task> tasksDueTomorrow = taskRepository.findTasksDueTomorrow(tomorrow);

	    for (Task task : tasksDueTomorrow) {
	        String userEmail = task.getUser().getEmail();
	        String subject = "📌 Recordatorio: " + task.getTitle() + " vence mañana!";
	        String body = "Hola " + task.getUser().getName() + ",\n\n"
	                    + "⚠️ Te recordamos que tienes una tarea pendiente para mañana:\n\n"
	                    + "📌 *Título:* " + task.getTitle() + "\n"
	                    + "📃 *Descripción:* " + task.getDescription() + "\n"
	                    + "📅 *Fecha de vencimiento:* " + task.getDueDate() + "\n"
	                    + "🔖 *Estado:* " + task.getStatus() + "\n\n"
	                    + "¡No la dejes para última hora! ⏳\n"
	                    + "--\n"
	                    + "🌐 Saludos,*Equipo Gtro* ✅";

	        emailService.sendTaskEmail(userEmail, subject, body);
	    }
	}

}
