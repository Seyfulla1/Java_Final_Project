package az.edu.bhos.finalProject.service;

import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.exception.UserNotFoundException;

public interface UserService {
    boolean authenticate(String username, String password) throws UserNotFoundException;
    void endSession();
    boolean isAuthenticated();
    User getCurrentUser();
    void logUserAction(String action);
}
