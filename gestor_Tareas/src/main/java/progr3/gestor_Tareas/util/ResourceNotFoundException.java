package progr3.gestor_Tareas.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND) // Esto hace que retorne 404 en lugar de 500
public class ResourceNotFoundException extends RuntimeException  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
	
    

    
}


