package Repository;

import Domain.Entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONFileRepositoryTest {
    IRepository<User> userIRepository = new JSONFileRepository<>("src/test/java/Repository/usersTest.txt", User.class);
    User user = new User("Admin", "AdminTest", "tester", true, true, true);



    @BeforeEach
    void setUp(){
        userIRepository.clearAll();
        user.setId(0);
    }

    @Test
    void AddingNewEntity_Should_SaveThatEntityToRepository() {
        userIRepository.create(user);
        assertEquals(1, userIRepository.read().size());
        assertEquals(user,userIRepository.read(0));

    }

    @Test
    void UpdatingEntity_Should_UpdateTheEntityFromRepository(){
        userIRepository.create(user);
        user.setName("OtherAdmin");
        user.setPassword("OtherPass");
        user.setJobTitle("other");
        userIRepository.update(user);
        assertEquals(user,userIRepository.read(0));
    }

    @Test
    void DeletingEntity_Should_DeleteTheEntityFromRepository(){
        userIRepository.create(user);
        userIRepository.delete(0);
        assertNotEquals(user,userIRepository.read(0));
        assertTrue(userIRepository.read().isEmpty());
    }


}