package model.exceptions;

public class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
