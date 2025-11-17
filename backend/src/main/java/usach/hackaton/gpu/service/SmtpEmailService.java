package usach.hackaton.gpu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Profile("!noemail")
@Service
public class SmtpEmailService implements EmailService {
    private final JavaMailSender mailSender;
    private final String from;
    private final String baseUrl;

    public SmtpEmailService(JavaMailSender mailSender, @Value("${spring.mail.properties.mail.from}") String from,
        @Value("${app.url}") String baseUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.baseUrl = baseUrl;
    }

    @Override
    public void sendActivationEmail(String to, String token) {
        String activationLink = baseUrl + "/api/auth/activate?token=%s".formatted(token);
        final String bodyMessage = templateActivationEmail(activationLink);
        sendEmail(to, "Activacion de Cuenta", bodyMessage);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    private String templateActivationEmail(String activationLink) {
        return """
                ¡Bienvenido a la Ludoteca!

                Gracias por registrarte. Para activar tu cuenta, haz click en el siguiente enlace:

                %s

                Ten en cuenta que este enlace expira en 24 horas. Y si no solicitaste esta cuenta, ignora este correo.

                Saludos,
                GPU Developers.
            """.formatted(activationLink);
    }
}
