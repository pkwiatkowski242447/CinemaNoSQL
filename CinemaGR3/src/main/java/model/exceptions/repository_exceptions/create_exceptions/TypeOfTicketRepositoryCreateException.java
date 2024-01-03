package model.exceptions.repository_exceptions.create_exceptions;

public class TypeOfTicketRepositoryCreateException extends RepositoryCreateException {
    public TypeOfTicketRepositoryCreateException(String message) {
        super(message);
    }

    public TypeOfTicketRepositoryCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
