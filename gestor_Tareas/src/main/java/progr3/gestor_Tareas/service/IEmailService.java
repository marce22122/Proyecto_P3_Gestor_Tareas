package progr3.gestor_Tareas.service;

public interface IEmailService {

	void sendTaskEmail(String to, String subject, String body);

}
