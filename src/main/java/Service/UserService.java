package Service;

import Domain.Entities.User;
import Domain.Exceptions.InvalidUserException;
import Domain.Exceptions.WrongIdException;
import Domain.Validators.IValidator;
import Repository.IRepository;

/**
 * A service that provides functionality for User operations.
 */
public class UserService extends AbstractService<User> {
    /**
     * @param repository    The repository used for the service.
     * @param userValidator The validator used for user validation.
     */
    public UserService(IRepository<User> repository, IValidator<User> userValidator) {
        super(repository, userValidator);
    }

    /**
     * Creates a new user if is valid and saves it to repository.
     *
     * @param name             The name that a user is associated with.
     * @param password         The password that a user uses to log in.
     * @param jobTitle         The job that the user have.
     * @param acceptToRegister Clearence to register patients, view and print results (basic level of clearence).
     * @param acceptToValidate Basic level of clearence plus entering results, diagnostics.
     * @param admin            It is given all clearences.
     */
    public void addUser(String name, String password, String jobTitle, boolean acceptToRegister,
                        boolean acceptToValidate, boolean admin) {
        User user = new User(name, password, jobTitle, acceptToRegister, acceptToValidate, admin);
        validateUniqueUserName(name);
        validator.validateBeforeRegistration(user);
        this.repository.create(user);
    }

    /**
     * Checks if the name was already given to another user.
     * throws InvalidUserException if the name was already given.
     *
     * @param name The name that a user is associated with.
     */
    private void validateUniqueUserName(String name) {
        for (User currentUser : repository.read()) {
            if (currentUser.getName().equals(name)) throw new InvalidUserException("The name is already taken!");
        }
    }

    /**
     * Updates a user from repository.
     * throws WrongIdException if the Id is not found.
     * * @param name The name that a user is associated with.
     *
     * @param password         The password that a user uses to log in.
     * @param jobTitle         The job that the user have.
     * @param acceptToRegister Clearence to register patients, view and print results (basic level of clearence).
     * @param acceptToValidate Basic level of clearence plus entering results, diagnostics.
     * @param admin            It is given all clearences.
     */

    public void updateUser(int id, String name, String password, String jobTitle, boolean acceptToRegister,
                           boolean acceptToValidate, boolean admin) {

        for (User user : repository.read()) {
            if (user.getId() == id) {
                user.setName(name);
                user.setPassword(password);
                user.setJobTitle(jobTitle);
                user.setAcceptToRegister(acceptToRegister);
                user.setAcceptToValidate(acceptToValidate);
                user.setAdmin(admin);
                validator.validateBeforeRegistration(user);
                repository.update(user);
                return;
            }
        }
        throw new WrongIdException("The User Id does not exist!");
    }

    /**
     * Checks if the user name and the password exists and matches.
     *
     * @param userName The name that a user is associated with.
     * @param password The password that a user uses to log in.
     * @return true if the user name and the password exists and matches or false otherwise.
     */
    public boolean checkLogIn(String userName, String password) {
        for (User user : repository.read()) {
            if (user.getName().equals(userName) && user.getPassword().equals(password)) return true;
        }
        return false;
    }

    /**
     * @param userName The name that a user is associated with.
     * @param password password The password that a user uses to log in.
     * @return the user with that given name and password, or null if not found.
     */
    public User getUser(String userName, String password) {
        for (User user : repository.read()) {
            if (user.getName().equals(userName) && user.getPassword().equals(password)) return user;
        }
        return null;
    }
}
