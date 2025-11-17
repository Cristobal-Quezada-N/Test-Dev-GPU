package usach.hackaton.gpu.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("noemail")
@Slf4j
public class NoopEmailService implements EmailService {
    @Value("${spring.mail.properties.mail.from}")
    private String from;

    @Override
    public void sendActivationEmail(String to, String activationLink) {
        sendEmail(to, "Activacion de Cuenta", activationLink);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("""
            [NOOP EMAIL] Subject: %s
                From: %s
                To: %s
                Body: %s""".formatted(subject, from, to, body));
    }
}
