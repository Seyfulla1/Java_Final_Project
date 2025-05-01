package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.dao.UserDAO;
import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.exception.UserNotFoundException;
import az.edu.bhos.finalProject.logging.LoggingService;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final LoggingService loggingService;
    private User currentUser;

    public UserServiceImpl(UserDAO userDAO, LoggingService loggingService) {
        this.userDAO = userDAO;
        this.loggingService = loggingService;
    }

    @Override
    public boolean authenticate(String username, String password) throws UserNotFoundException {
        try {
            User user = userDAO.getByUsername(username);
            if (user.getPassword().equals(password)) {
                currentUser = user;
                loggingService.logAction("User " + username + " logged in successfully.");
                return true;
            } else {
                loggingService.logAction("Failed login attempt for user " + username);
                return false;
            }
        } catch (UserNotFoundException e) {
            loggingService.logAction("Failed login attempt for user " + username);
            throw new UserNotFoundException("User not found: " + username);
        }
    }

    @Override
    public void endSession() {
        if (currentUser != null) {
            loggingService.logAction("User " + currentUser.getUsername() + " logged out.");
            currentUser = null;
        }
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void logUserAction(String action) {
        if (currentUser != null) {
            loggingService.logAction("User " + currentUser.getUsername() + ": " + action);
        } else {
            loggingService.logAction("Unauthenticated user performed an action: " + action);
        }
    }
}
