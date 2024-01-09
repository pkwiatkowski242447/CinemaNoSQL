package model.exceptions.repositories.read_exceptions;

public class ClientRepositoryReadException extends RepositoryReadException {

    public ClientRepositoryReadException(String message) {
        super(message);
    }

    public ClientRepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
