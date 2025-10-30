package usach.hackaton.gpu.exception;

public class AccountNotVerificatedException extends RuntimeException {
    public AccountNotVerificatedException() {
        super("Tu cuenta no está verificada, por favor, verificala");
    }
}
