package Domain.Exceptions;
/**
 * Throwen when a Patient is invalid.
 */

public class InvalidPatientException extends RuntimeException {
    public InvalidPatientException(String message) {
        super(message);
    }
}
