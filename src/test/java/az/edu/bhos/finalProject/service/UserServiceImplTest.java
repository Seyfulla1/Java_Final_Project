package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.dao.UserDAOImpl;
import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.exception.UserNotFoundException;
import az.edu.bhos.finalProject.logging.LoggingService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {

    private static final String TEST_FILE = "test_users.json";
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() throws IOException {
        UserDAOImpl userDAO = new UserDAOImpl(TEST_FILE);
        userDAO.getAll().clear();
        userDAO.getAll().add(new User("Test", "User", "testuser", "password123"));
        userDAO.save();

        LoggingService loggingService = new LoggingService();
        userService = new UserServiceImpl(userDAO, loggingService);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }

        File log = new File("user_actions.log");
        if (log.exists()) {
            log.delete();
        }
    }

    @Test
    void testAuthenticate_Success() throws UserNotFoundException {
        boolean result = userService.authenticate("testuser", "password123");
        assertTrue(result);
        assertNotNull(userService.getCurrentUser());
        assertTrue(userService.isAuthenticated());
    }

    @Test
    void testAuthenticate_WrongPassword() throws UserNotFoundException {
        boolean result = userService.authenticate("testuser", "wrongpassword");
        assertFalse(result);
        assertFalse(userService.isAuthenticated());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        assertThrows(UserNotFoundException.class, () ->
                userService.authenticate("nosuchuser", "pass"));
    }

    @Test
    void testEndSession() throws UserNotFoundException {
        userService.authenticate("testuser", "password123");
        userService.endSession();
        assertFalse(userService.isAuthenticated());
    }

    @Test
    void testLogUserAction() throws IOException, UserNotFoundException {
        userService.authenticate("testuser", "password123");
        userService.logUserAction("Test action");

        File log = new File("user_actions.log");
        assertTrue(log.exists());
    }
}
