package progr3.gestor_Tareas.service.jpa;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import progr3.gestor_Tareas.entity.Usuario;
import progr3.gestor_Tareas.repository.UsuarioRepository;
import progr3.gestor_Tareas.service.IUserService;
import progr3.gestor_Tareas.util.ResourceNotFoundException;
import progr3.gestor_Tareas.util.UpdateUserDTO;

@Service
public class UserServiceImpl implements IUserService {
	private final UsuarioRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<Usuario> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Usuario registerUser(Usuario user) {
		userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
			throw new IllegalArgumentException("El email ya está registrado: " + user.getEmail());
		});

		if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("La contraseña no puede estar vacía");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Asignar un rol por defecto si no se proporciona
		if (user.getRole() == null) {
			user.setRole(Usuario.Role.ROLE_USER);
		}

		return userRepository.save(user);
	}

	@Override
	public long countUsers() {
		return userRepository.count();
	}

	@Override
	public Usuario findUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
	}

	@Override
	public Usuario updateUser(Long userId, UpdateUserDTO updatedUserDTO) {
		Usuario existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

		if (updatedUserDTO.getEmail() != null && !updatedUserDTO.getEmail().equals(existingUser.getEmail())) {
			if (userRepository.existsByEmail(updatedUserDTO.getEmail())) {
				throw new IllegalArgumentException("El correo ya está en uso");
			}
			existingUser.setEmail(updatedUserDTO.getEmail());
		}

		if (updatedUserDTO.getPassword() != null && !updatedUserDTO.getPassword().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(updatedUserDTO.getPassword()));
		}

		return userRepository.save(existingUser);
	}

	@Override
	public void deleteUser(Long id) {
		Usuario user = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
		userRepository.delete(user);
	}

	@Override
	public Usuario findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
	}

}
