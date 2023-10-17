package model.exceptions.repository_exceptions;

public class GeneralRepositoryException extends RuntimeException {
    public GeneralRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
