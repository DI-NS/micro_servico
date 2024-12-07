package MedMap.exception;

/**
 * Exceção lançada quando credenciais inválidas são fornecidas no login.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
