package model.exceptions.repository_exceptions.create_exceptions;

public class MovieRepositoryCreateException extends RepositoryCreateException {

    public MovieRepositoryCreateException(String message) {
        super(message);
    }

    public MovieRepositoryCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
