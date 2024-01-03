package model.exceptions.repository_exceptions.delete_exceptions;

public class MovieRepositoryDeleteException extends RepositoryDeleteException {

    public MovieRepositoryDeleteException(String message) {
        super(message);
    }

    public MovieRepositoryDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
