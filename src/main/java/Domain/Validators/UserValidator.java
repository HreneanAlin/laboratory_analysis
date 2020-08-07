package Domain.Validators;

import Domain.Entities.User;
import Domain.Exceptions.InvalidUserException;

/**
 * Represents a Validator which validates Users
 */
public class UserValidator implements IValidator<User> {
    /**
     * @param entity The user to be validated.
     *               Throws InvalidUserException if invalid.
     */

    @Override
    public void validateBeforeRegistration(User entity) {
        String messages = "";
        if (entity.getPassword().length() < 5) {
            messages += "The password is too short\n";
        }
        if (entity.getPassword().equals(entity.getPassword().toLowerCase())) {
            messages += "The password must contain at least an uppercase character\n";
        }

        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            messages += "you must enter a name\n";
        }
        if (entity.getJobTitle() == null || entity.getName().trim().isEmpty()) {
            messages += "you must enter a job title\n";
        }

        if (entity.isAdmin() && (!entity.isAcceptToValidate() || !entity.isAcceptToRegister())) {
            messages += "an admin must have registrate and validate permission\n";
        }

        if (entity.isAcceptToValidate() && !entity.isAcceptToRegister()) {
            messages += "a user who validates must have register permission\n";
        }

        if (!messages.equals("")) {
            throw new InvalidUserException(messages);
        }

    }

    /**
     * method not required for user validation.
     */
    @Override
    public void validateAfter(User entity) {

    }
}
