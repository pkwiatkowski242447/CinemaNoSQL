package model.exceptions.repository_exceptions;

public class MovieRepositoryReadException extends RepositoryReadException {
    public MovieRepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
