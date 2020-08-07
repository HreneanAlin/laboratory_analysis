package Domain.Exceptions;
/**
 * Throwen when a User is invalid.
 */
public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }
}
