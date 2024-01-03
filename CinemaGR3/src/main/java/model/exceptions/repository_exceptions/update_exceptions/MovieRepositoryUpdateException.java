package model.exceptions.repository_exceptions.update_exceptions;

public class MovieRepositoryUpdateException extends RepositoryUpdateException {

    public MovieRepositoryUpdateException(String message) {
        super(message);
    }

    public MovieRepositoryUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
