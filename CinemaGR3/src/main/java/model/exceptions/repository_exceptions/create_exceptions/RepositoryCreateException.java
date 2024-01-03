package model.exceptions.repository_exceptions.create_exceptions;

import model.exceptions.repository_exceptions.GeneralRepositoryException;

public class RepositoryCreateException extends GeneralRepositoryException {

    public RepositoryCreateException(String message) {
        super(message);
    }

    public RepositoryCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
