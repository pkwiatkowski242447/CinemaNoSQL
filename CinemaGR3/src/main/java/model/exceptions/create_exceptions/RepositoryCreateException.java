package model.exceptions.create_exceptions;

import model.exceptions.GeneralRepositoryException;

public class RepositoryCreateException extends GeneralRepositoryException {

    public RepositoryCreateException(String message) {
        super(message);
    }

    public RepositoryCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
