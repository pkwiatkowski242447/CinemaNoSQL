package model.exceptions.read_exceptions;

import model.exceptions.GeneralRepositoryException;

public class RepositoryReadException extends GeneralRepositoryException {

    public RepositoryReadException(String message) {
        super(message);
    }

    public RepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
