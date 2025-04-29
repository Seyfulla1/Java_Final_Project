package az.edu.bhos.finalProject.dao;
import java.io.IOException;
import java.util.List;

public interface DAO<T> {
    T getById(String id) throws RuntimeException; // might create custom exception here too.

    List<T> getAll();

    void save() throws IOException;

    boolean insert(T t) throws IOException, RuntimeException; // will change runtime ex to custom exception

    //boolean update(T t) throws IOException;

    boolean delete(T t) throws IOException, RuntimeException; //might create custom exception here too.

    boolean deleteById(String id) throws IOException, RuntimeException; // might create custom exception here too.

}
