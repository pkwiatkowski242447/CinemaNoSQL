package model.exceptions.repository_exceptions.update_exceptions;

public class ClientRepositoryUpdateException extends RepositoryUpdateException {

    public ClientRepositoryUpdateException(String message) {
        super(message);
    }

    public ClientRepositoryUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
