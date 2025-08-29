package usach.hackaton.gpu.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException() {
        super("El correo ya está registrado");
    }
}