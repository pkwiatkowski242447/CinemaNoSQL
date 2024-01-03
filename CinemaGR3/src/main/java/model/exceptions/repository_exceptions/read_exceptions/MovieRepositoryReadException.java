package model.exceptions.repository_exceptions.read_exceptions;

public class MovieRepositoryReadException extends RepositoryReadException {

    public MovieRepositoryReadException(String message) {
        super(message);
    }

    public MovieRepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
