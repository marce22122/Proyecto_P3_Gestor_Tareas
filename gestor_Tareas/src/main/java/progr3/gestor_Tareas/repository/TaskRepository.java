package progr3.gestor_Tareas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import progr3.gestor_Tareas.entity.Task;
import progr3.gestor_Tareas.entity.Usuario;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByUserId(Long userId);

	boolean existsByTitleAndUser(String title, Usuario user);

	@Query("SELECT t FROM Task t WHERE t.dueDate = :tomorrow")
	List<Task> findTasksDueTomorrow(@Param("tomorrow") LocalDate tomorrow);// Esto busca las tareas que vencen mañana.
}
