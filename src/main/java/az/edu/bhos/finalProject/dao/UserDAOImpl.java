package az.edu.bhos.finalProject.dao;

import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.exception.DuplicateUserException;
import az.edu.bhos.finalProject.exception.UserNotFoundException;
import az.edu.bhos.finalProject.util.Json;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;
public class UserDAOImpl implements UserDAO {
    private final String filepath;
    private List<User> userList;
    public UserDAOImpl(String filepath){
        this.filepath = filepath;
        try{
        userList = Json.readJsonFile(filepath, new TypeReference<List<User>>() {});
        }catch (IOException ie){
            System.out.println("Error reading user data: " + ie.getMessage());

        }
    }
    @Override
    public User getByUsername(String username) throws UserNotFoundException {
        return userList.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + username));
    }

    @Override
    public List<User> getAll() {
        return userList;
    }
    @Override
    public boolean insert(User user) throws IOException, DuplicateUserException {
        boolean duplicate=userList.stream()
                .anyMatch( u -> u.getUsername().equals(user.getUsername()));
        if(duplicate){
            throw new DuplicateUserException("User with username " + user.getUsername() + " already exists!");
        }
        userList.add(user);
        save();
        return true;
    }
    @Override
    public boolean delete(User user) throws IOException, UserNotFoundException {
        boolean success=userList.remove(user);
        if(!success) {
            throw new UserNotFoundException("User not found!");
        }
        save();
        return true;
    }
    @Override
    public boolean deleteByUsername(String username) throws IOException, UserNotFoundException {
       return delete(getByUsername(username));
    }
    @Override
    public void save() throws IOException {
        Json.writeJsonFile(filepath, userList);
    }

}
