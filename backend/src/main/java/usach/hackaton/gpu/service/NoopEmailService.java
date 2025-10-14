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
    public void send(String to, String subject, String body) {
        log.info("[NOOP EMAIL] Subject: {}", subject);
        log.info("\tFrom: {}", from);
        log.info("\tTo: {}", to);
        log.info("\tBody: {}", body);
    }
}
