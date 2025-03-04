package progr3.gestor_Tareas.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import progr3.gestor_Tareas.service.IEmailService;

@Service
public class EmailServiceImpl implements IEmailService {
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendTaskEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}
}
