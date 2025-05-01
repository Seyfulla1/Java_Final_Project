package az.edu.bhos.finalProject.controller;

import az.edu.bhos.finalProject.service.UserService;

public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    public void logout() {
        userService.endSession();
    }
    public void logUserAction(String action) {
        userService.logUserAction(action);
    }
    public boolean authenticate(String username, String password) {
        try {
            if(userService.authenticate(username, password)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    public boolean isAuthenticated() {
        return userService.isAuthenticated();
    }

}
