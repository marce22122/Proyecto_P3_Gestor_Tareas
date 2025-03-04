package progr3.gestor_Tareas.util;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserDTO {
	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email debe ser válido")
	private String email;

	@NotBlank(message = "La contraseña no puede estar vacía")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
