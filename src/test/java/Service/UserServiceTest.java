package Service;

import Domain.Entities.User;
import Domain.Exceptions.InvalidUserException;
import Domain.Exceptions.WrongIdException;
import Domain.Validators.IValidator;
import Domain.Validators.UserValidator;
import Repository.IRepository;
import Repository.JSONFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    IRepository<User> userIRepository = new JSONFileRepository<>("src/test/java/Service/usersTest.txt", User.class);
    IValidator<User> userValidator = new UserValidator();
    UserService userService = new UserService(userIRepository, userValidator);
    User user = new User("Admin", "AdminTest", "tester", true, true, true);

    @BeforeEach
    void setUp() {
        user.setId(0);
        userIRepository.clearAll();
        userService.addUser(user.getName(), user.getPassword(), user.getJobTitle(),
                user.isAcceptToRegister(), user.isAcceptToValidate(), user.isAdmin());
    }

    @Test
    void addValidUser_should_SaveUserToRepository() {
        User otherUser = new User("OtherAdmin", "Abcdet", "tester", true, true, true);
        otherUser.setId(1);
        userService.addUser(otherUser.getName(), otherUser.getPassword(),
                otherUser.getJobTitle(), otherUser.isAcceptToRegister(), otherUser.isAcceptToValidate(), otherUser.isAdmin());
        assertEquals(2, userService.getAll().size());
        User sameUser = (User) userService.getOne(1);
        assertEquals(otherUser, sameUser);

    }

    @Test
    void addInvalidUser_should_throwInvalidUserException() {
        assertThrows(InvalidUserException.class, () -> userService.addUser("some", "ss", "",
                false, false, true)
        );
    }

    @Test
    void updateUser_should_saveTheUpdateIfValid() {
        userService.updateUser(user.getId(), "Other", "Otherpass",
                "otherTest", false, false, false);
        User updatedUser = (User) userService.getOne(user.getId());
        assertFalse(updatedUser.isAcceptToRegister());
        assertFalse(updatedUser.isAcceptToValidate());
        assertFalse(updatedUser.isAdmin());
        assertEquals("Other", updatedUser.getName());
        assertEquals("Otherpass", updatedUser.getPassword());
        assertEquals("otherTest", updatedUser.getJobTitle());
    }

    @Test
    void updateUser_should_throwInvalidUserExceptionIfInvalid() {
        User sameUser = (User) userService.getOne(0);
        assertThrows(InvalidUserException.class, () -> userService.updateUser(sameUser.getId(), "some", "ss",
                "", false, false, true)
        );

    }

    @Test
    void deleteUser_should_RemoveTheUserFromRepository() {
        User sameUser = (User) userService.getOne(0);
        userService.deleteOne(0);
        User nullUser = (User) userService.getOne(sameUser.getId());
        assertNull(nullUser);
    }

    @Test
    void checkLogIn_should_checkIfThePasswordAndUsernameMatchAndExists() {
        User sameUser = (User) userService.getOne(0);
        assertTrue(userService.checkLogIn(sameUser.getName(), sameUser.getPassword()));
        assertFalse(userService.checkLogIn("aaa", "pppp"));

    }

    @Test
    void getUserWithUserNameAndPassword_should_getTheCorrespondingUser() {
        User sameUser = (User) userService.getOne(0);
        User otherUser = userService.getUser(sameUser.getName(), sameUser.getPassword());
        assertEquals(sameUser, otherUser);
        User nullUser = userService.getUser("aaa", "cccc");
        assertNull(nullUser);

    }

    @Test
    void addingNewUserWithOccupiedName_should_throwInvalidUserException() {
        assertThrows(InvalidUserException.class, () -> userService.addUser("Admin", "ssdaasdS",
                "sdsd", true, true, true)
        );
    }

    @Test
    void updateInexistingUser_should_throwWrongIdException() {

        assertThrows(WrongIdException.class, () -> userService.updateUser(99, "Other", "Otherpass",
                "otherTest", false, false, false));
    }
}