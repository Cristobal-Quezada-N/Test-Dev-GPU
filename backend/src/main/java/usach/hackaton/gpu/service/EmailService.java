package usach.hackaton.gpu.service;

public interface EmailService {
    public void sendActivationEmail(String to, String token);

    public void sendEmail(String to, String subject, String body);
}
