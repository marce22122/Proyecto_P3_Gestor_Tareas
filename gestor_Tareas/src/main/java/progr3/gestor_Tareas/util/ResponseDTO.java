package progr3.gestor_Tareas.util;

public class ResponseDTO<T> {
	private boolean success;
	private String message;
	private T data;

	public ResponseDTO(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public static <T> ResponseDTO<T> success(T data, String message) {
		return new ResponseDTO<>(true, message, data);
	}

	public static <T> ResponseDTO<T> error(String message) {
		return new ResponseDTO<>(false, message, null);
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}
}