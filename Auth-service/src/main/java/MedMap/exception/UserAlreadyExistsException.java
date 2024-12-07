package MedMap.exception;

/**
 * Exceção lançada quando já existe uma UBS registrada com o CNES fornecido.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
