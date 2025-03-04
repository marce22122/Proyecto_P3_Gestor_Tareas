package progr3.gestor_Tareas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import progr3.gestor_Tareas.entity.Usuario;
import progr3.gestor_Tareas.service.jpa.UserServiceImpl;
import progr3.gestor_Tareas.util.JwtUtil;
import progr3.gestor_Tareas.util.ResponseDTO;
import progr3.gestor_Tareas.util.UpdateUserDTO;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<ResponseDTO<Usuario>> registerUser(@Valid @RequestBody Usuario user, BindingResult result) {
		if (result.hasErrors()) {
			String errorMsg = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).findFirst()
					.orElse("Error de validación");
			return ResponseEntity.badRequest().body(ResponseDTO.error(errorMsg));
		}

		Usuario savedUser = userService.registerUser(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseDTO.success(savedUser, "Usuario registrado con éxito"));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseDTO<Usuario>> updateUser(@PathVariable Long id,
			@Valid @RequestBody UpdateUserDTO updatedUserDTO, BindingResult result) {

		if (result.hasErrors()) {
			String errorMsg = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).findFirst()
					.orElse("Error de validación");
			return ResponseEntity.badRequest().body(ResponseDTO.error(errorMsg));
		}

		Usuario updatedUser = userService.updateUser(id, updatedUserDTO);
		return ResponseEntity.ok(ResponseDTO.success(updatedUser, "Usuario actualizado correctamente"));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDTO<Void>> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(ResponseDTO.success(null, "Usuario eliminado correctamente"));
	}

	@GetMapping
	public ResponseEntity<ResponseDTO<List<Usuario>>> getAllUsers() {
		List<Usuario> users = userService.getAllUsers();
		return ResponseEntity.ok(ResponseDTO.success(users, "Usuarios obtenidos correctamente"));
	}

	@GetMapping("/count")
	public ResponseEntity<ResponseDTO<Long>> getUserCount() {
		long count = userService.countUsers();
		return ResponseEntity.ok(ResponseDTO.success(count, "Cantidad de usuarios obtenida"));
	}

	@GetMapping("/me")
	public ResponseEntity<ResponseDTO<Usuario>> getMyProfile(@RequestHeader("Authorization") String token) {
		String email = jwtUtil.extractUsername(token.substring(7)); // Extraer el email desde el JWT
		Usuario user = userService.findByEmail(email);

		// Crear un nuevo objeto Usuario con solo el nombre y el rol
		Usuario userProfile = new Usuario();
		userProfile.setName(user.getName());
		userProfile.setRole(user.getRole());

		// Devolvemos los datos del usuario sin permitir la modificación de nombre y rol
		return ResponseEntity.ok(ResponseDTO.success(userProfile, "Perfil recuperado correctamente"));
	}

}
