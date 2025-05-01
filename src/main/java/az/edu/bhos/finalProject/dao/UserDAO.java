package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.exception.DuplicateUserException;
import az.edu.bhos.finalProject.exception.UserNotFoundException;

import java.io.IOException;
import java.util.List;

public interface UserDAO {
    User getByUsername(String username) throws UserNotFoundException;
    List<User> getAll();
    boolean insert(User user) throws IOException, DuplicateUserException;
    boolean delete(User user) throws IOException, UserNotFoundException;
    boolean deleteByUsername(String username) throws IOException, UserNotFoundException;
    void save() throws IOException;
}
