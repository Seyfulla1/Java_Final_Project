package az.edu.bhos.finalProject.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String message){
        super(message);
    }
}
