package progr3.gestor_Tareas.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import progr3.gestor_Tareas.util.AuthRequest;
import progr3.gestor_Tareas.util.JwtUtil;
import progr3.gestor_Tareas.util.ResponseDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final UserDetailsService userDetailsService;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseDTO<String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		try {
			UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
			if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(ResponseDTO.error("Credenciales incorrectas"));
			}
			String token = jwtUtil.generateToken(userDetails);
			return ResponseEntity.ok(ResponseDTO.success(token, "Token generado exitosamente"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDTO.error("Error en autenticación"));
		}
	}

	@GetMapping("/validate")
	public ResponseEntity<Map<String, String>> validateToken(@RequestHeader("Authorization") String token) {
		token = token.replace("Bearer ", "");
		String role = jwtUtil.extractRole(token);
		return ResponseEntity.ok(Map.of("role", role));
	}
}
