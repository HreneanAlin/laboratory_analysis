package Domain.Exceptions;
/**
 * Throwen when an Entity Id is found or is already given.
 */

public class WrongIdException extends  RuntimeException{
    public WrongIdException(String message) {
        super(message);
    }
}
