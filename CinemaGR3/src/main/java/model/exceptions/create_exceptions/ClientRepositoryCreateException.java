package model.exceptions.create_exceptions;

public class ClientRepositoryCreateException extends RepositoryCreateException {

    public ClientRepositoryCreateException(String message) {
        super(message);
    }

    public ClientRepositoryCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
