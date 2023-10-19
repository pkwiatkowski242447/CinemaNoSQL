package model.exceptions.repository_exceptions;

public class ClientRepositoryReadException extends RepositoryReadException {
    public ClientRepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
