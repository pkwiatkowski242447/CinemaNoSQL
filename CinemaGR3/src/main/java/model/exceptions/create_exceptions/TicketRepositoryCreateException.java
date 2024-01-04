package model.exceptions.create_exceptions;

public class TicketRepositoryCreateException extends RepositoryCreateException {

    public TicketRepositoryCreateException(String message) {
        super(message);
    }

    public TicketRepositoryCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
