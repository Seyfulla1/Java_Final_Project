package az.edu.bhos.finalProject.dao;
import java.io.IOException;
import java.util.List;

public interface DAO<T> {
    T getById(String id) throws RuntimeException;

    List<T> getAll();

    void save() throws IOException;

    boolean insert(T t) throws IOException, RuntimeException;

    //boolean update(T t) throws IOException;

    boolean delete(T t) throws IOException, RuntimeException;

    boolean deleteById(String id) throws IOException, RuntimeException;

}
