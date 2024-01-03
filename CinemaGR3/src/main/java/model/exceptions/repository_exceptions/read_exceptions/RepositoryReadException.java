package model.exceptions.repository_exceptions.read_exceptions;

import model.exceptions.repository_exceptions.GeneralRepositoryException;

public class RepositoryReadException extends GeneralRepositoryException {

    public RepositoryReadException(String message) {
        super(message);
    }

    public RepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
