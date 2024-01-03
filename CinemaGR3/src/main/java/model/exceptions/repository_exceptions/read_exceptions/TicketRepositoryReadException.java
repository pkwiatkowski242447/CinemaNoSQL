package model.exceptions.repository_exceptions.read_exceptions;

public class TicketRepositoryReadException extends RepositoryReadException {

    public TicketRepositoryReadException(String message) {
        super(message);
    }

    public TicketRepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
