package model.exceptions.repository_exceptions;

public class MovieRepositoryUpdateException extends RepositoryUpdateException {
    public MovieRepositoryUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
