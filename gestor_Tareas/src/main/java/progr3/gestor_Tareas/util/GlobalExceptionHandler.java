package progr3.gestor_Tareas.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseDTO<String>> handleResourceNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error(ex.getMessage()));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseDTO<String>> handleRuntimeException(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.error("Error: " + ex.getMessage()));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ResponseDTO<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.error(ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseDTO<Void>> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
				.findFirst().orElse("Error de validación");

		return ResponseEntity.badRequest().body(ResponseDTO.error(errorMessage));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseDTO<Void>> handleInvalidRequestBody(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest()
				.body(ResponseDTO.error("Error en el formato de la solicitud. Verifica los datos enviados."));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDTO<Void>> handleGeneralException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ResponseDTO.error("Ocurrió un error inesperado. Contacta al administrador."));
	}
}
