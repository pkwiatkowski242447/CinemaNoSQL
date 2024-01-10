package exceptions;

public class MongoConfigNotFoundException extends Exception {
    public MongoConfigNotFoundException(String message) {
        super(message);
    }

    public MongoConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
