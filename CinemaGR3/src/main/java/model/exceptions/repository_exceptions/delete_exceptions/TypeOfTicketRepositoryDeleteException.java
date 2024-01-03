package model.exceptions.repository_exceptions.delete_exceptions;

public class TypeOfTicketRepositoryDeleteException extends RepositoryDeleteException {
    public TypeOfTicketRepositoryDeleteException(String message) {
        super(message);
    }

    public TypeOfTicketRepositoryDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
