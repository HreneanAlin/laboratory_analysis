package Domain.Exceptions;
/**
 * Throwen when a Sample is invalid.
 */

public class InvalidSampleException extends RuntimeException {
    public InvalidSampleException(String message) {
        super(message);
    }
}
