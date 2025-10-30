package usach.hackaton.gpu.exception;

public class AccountBannedException extends RuntimeException {
    public AccountBannedException() {
        super("Tu cuenta esta bloqueada. Contacta al administrador");
    }
}
