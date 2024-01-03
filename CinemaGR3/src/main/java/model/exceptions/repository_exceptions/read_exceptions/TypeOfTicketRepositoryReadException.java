package model.exceptions.repository_exceptions.read_exceptions;

public class TypeOfTicketRepositoryReadException extends RepositoryReadException {
    public TypeOfTicketRepositoryReadException(String message) {
        super(message);
    }

    public TypeOfTicketRepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
