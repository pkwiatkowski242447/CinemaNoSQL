package model.exceptions.read_exceptions;

public class TicketRepositoryReadException extends RepositoryReadException {

    public TicketRepositoryReadException(String message) {
        super(message);
    }

    public TicketRepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
