package model.exceptions.repository_exceptions.update_exceptions;

public class TicketRepositoryUpdateException extends RepositoryUpdateException {

    public TicketRepositoryUpdateException(String message) {
        super(message);
    }

    public TicketRepositoryUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
