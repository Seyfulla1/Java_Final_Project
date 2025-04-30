package az.edu.bhos.finalProject.dao;

import static org.junit.jupiter.api.Assertions.*;

import az.edu.bhos.finalProject.entity.Passenger;
import az.edu.bhos.finalProject.entity.User;
import az.edu.bhos.finalProject.exception.DuplicateUserException;
import az.edu.bhos.finalProject.exception.UserNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class UserDAOImplTest {

    private static UserDAOImpl userDAO;
    private static final String testFilePath = "src\\test\\java\\az\\edu\\bhos\\finalProject\\dao\\test_users.json";
    @BeforeEach
    void setUp() throws IOException {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        userDAO= new UserDAOImpl(testFilePath);
        List<User> users=new ArrayList<>();
        users.add(new User(new Passenger("Ali", "Aliyev"), "ali", "123"));
        users.add(new User(new Passenger("Veli", "Veliev"), "veli", "1a23"));
        users.add(new User(new Passenger("Hasan", "Hasanov"), "hasan", "1b23"));
        users.add(new User(new Passenger("Huseyn", "Huseynov"), "huseyn", "1c23"));
        users.add(new User(new Passenger("Mehmet", "Mehmedov"), "mehmet", "asd4"));
        users.add(new User(new Passenger("Mika", "Mikaev"), "mika", "123"));
        try {
            for (User user : users) {
                userDAO.insert(user);
            }
        } catch (IOException e) {
            System.out.println("Error writing user data: " + e.getMessage());
        }
    }
    @Test
    void getByUsernameInvalid() {
        String username="mikaz";
        assertThrows(UserNotFoundException.class,()-> userDAO.getByUsername(username));
    }
    @Test
    void getByUsernameValid(){
        String username="mehmet";
        assertEquals(username,userDAO.getByUsername(username).getUsername());
    }

    @Test
    void getAll() {
        List<User> users = userDAO.getAll();
        assertEquals(6, users.size());
    }

    @Test
    void insertDuplicate() throws IOException {
        User user=new User(new Passenger("Alibayram","Valiyev"),"ali","12356");
        assertThrows(DuplicateUserException.class,()-> userDAO.insert(user));

    }
    @Test
    void insertValid() throws IOException {
        User user=new User(new Passenger("Ali","Valiyev"),"alivali","123");
        userDAO.insert(user);
        assertEquals(user.getUsername(),userDAO.getByUsername(user.getUsername()).getUsername());
    }


    @Test
    void deleteInvalid() throws IOException{
        User user=new User(new Passenger("Huseyn","Huseynov"),"huseyn1","1c23");
        assertThrows(UserNotFoundException.class,()-> userDAO.delete(user));
    }

    @Test
    void deleteValid() throws IOException {
        User user = new User(new Passenger("Ali", "Aliyev"), "ali", "123");
        userDAO.delete(user);
        assertThrows(UserNotFoundException.class, () -> userDAO.getByUsername(user.getUsername()));
    }

    @Test
    void deleteByUsernameInvalid() {
        String username="alma";
        assertThrows(UserNotFoundException.class,()-> userDAO.deleteByUsername(username));

    }

    @Test
    void deleteByUsernameValid() throws IOException{
        String username="veli";
        userDAO.deleteByUsername(username);
        assertThrows(UserNotFoundException.class,()-> userDAO.getByUsername(username));
    }

}