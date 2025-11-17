package usach.hackaton.gpu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.utils.LinkBuilder;

@Service
@RequiredArgsConstructor
@Profile("noemail")
@Slf4j
public class NoopEmailService implements EmailService {
    private final LinkBuilder linkBuilder;

    @Value("${spring.mail.properties.mail.from}")
    private String from;

    @Override
    public void sendActivationEmail(String to, String token) {
        String activationLink = linkBuilder.buildActivation(token);
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
