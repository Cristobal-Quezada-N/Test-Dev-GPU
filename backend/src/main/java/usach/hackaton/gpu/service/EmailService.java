package usach.hackaton.gpu.service;

public interface EmailService {
    public void sendActivationEmail(String to, String activationLink);

    public void sendEmail(String to, String subject, String body);
}
