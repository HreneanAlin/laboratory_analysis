package Domain.Exceptions;

/**
 * Throwen when an Analysis is invalid.
 */
public class InvalidAnalysisException extends RuntimeException {
    public InvalidAnalysisException(String message) {
        super(message);
    }
}
