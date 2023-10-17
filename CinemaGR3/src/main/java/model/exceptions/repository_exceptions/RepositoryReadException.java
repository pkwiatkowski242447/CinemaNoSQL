package model.exceptions.repository_exceptions;

public class RepositoryReadException extends GeneralRepositoryException {
    public RepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
