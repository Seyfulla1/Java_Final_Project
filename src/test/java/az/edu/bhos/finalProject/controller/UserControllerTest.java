package az.edu.bhos.finalProject.controller;

import az.edu.bhos.finalProject.dao.UserDAOImpl;
import az.edu.bhos.finalProject.entity.Passenger;
import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.logging.LoggingService;
import az.edu.bhos.finalProject.service.UserService;
import az.edu.bhos.finalProject.service.UserServiceImpl;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private static final String testFilePath = "src/test/java/az/edu/bhos/finalProject/controller/test_users.json";
    private UserController userController;
    private UserService userService;

    @BeforeEach
    void setUp() throws IOException {
        File file = new File(testFilePath);
        if (file.exists()) file.delete();
        file.createNewFile();

        UserDAOImpl userDAO = new UserDAOImpl(testFilePath);
        LoggingService loggingService = new LoggingService();
        userService = new UserServiceImpl(userDAO, loggingService);
        userController = new UserController(userService);

        userDAO.insert(new User(new Passenger("Test", "User"), "testuser", "pass123"));
    }

    @AfterEach
    void tearDown() {
        File file = new File(testFilePath);
        if (file.exists()) file.delete();

        File logFile = new File("user_actions.log");
        if (logFile.exists()) logFile.delete();
    }

    @Test
    void testAuthenticateSuccess() {
        boolean result = userController.authenticate("testuser", "pass123");
        assertTrue(result);
        assertTrue(userController.isAuthenticated());
    }

    @Test
    void testAuthenticateWrongPassword() {
        boolean result = userController.authenticate("testuser", "wrongpass");
        assertFalse(result);
        assertFalse(userController.isAuthenticated());
    }

    @Test
    void testAuthenticateUserNotFound() {
        boolean result = userController.authenticate("noUser", "pass");
        assertFalse(result);
        assertFalse(userController.isAuthenticated());
    }

    @Test
    void testLogout() {
        userController.authenticate("testuser", "pass123");
        assertTrue(userController.isAuthenticated());
        userController.logout();
        assertFalse(userController.isAuthenticated());
    }

    @Test
    void testLogUserActionWithoutLogin() {
        userController.logUserAction("Attempted unauthorized access");
        File logFile = new File("user_actions.log");
        assertTrue(logFile.exists());
    }

    @Test
    void testLogUserActionWithLogin() {
        userController.authenticate("testuser", "pass123");
        userController.logUserAction("Performed booking");
        File logFile = new File("user_actions.log");
        assertTrue(logFile.exists());
    }
}
