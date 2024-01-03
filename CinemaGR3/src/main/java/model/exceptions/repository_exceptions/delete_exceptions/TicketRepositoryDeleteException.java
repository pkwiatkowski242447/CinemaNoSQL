package model.exceptions.repository_exceptions.delete_exceptions;

public class TicketRepositoryDeleteException extends RepositoryDeleteException {

    public TicketRepositoryDeleteException(String message) {
        super(message);
    }

    public TicketRepositoryDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
