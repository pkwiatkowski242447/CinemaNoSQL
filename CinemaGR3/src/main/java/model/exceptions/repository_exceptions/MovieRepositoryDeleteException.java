package model.exceptions.repository_exceptions;

public class MovieRepositoryDeleteException extends RepositoryDeleteException {
    public MovieRepositoryDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
