package model.exceptions.delete_exceptions;

public class ClientRepositoryDeleteException extends RepositoryDeleteException {

    public ClientRepositoryDeleteException(String message) {
        super(message);
    }

    public ClientRepositoryDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
