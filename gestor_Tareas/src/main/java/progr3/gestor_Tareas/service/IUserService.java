package progr3.gestor_Tareas.service;

import java.util.List;

import progr3.gestor_Tareas.entity.Usuario;
import progr3.gestor_Tareas.util.UpdateUserDTO;

public interface IUserService {

	Usuario registerUser(Usuario user);

	Usuario findUserById(Long userId);

	void deleteUser(Long id);

	Usuario updateUser(Long userId, UpdateUserDTO updatedUserDTO);

	long countUsers();

	List<Usuario> getAllUsers();

	Usuario findByEmail(String email);

}
