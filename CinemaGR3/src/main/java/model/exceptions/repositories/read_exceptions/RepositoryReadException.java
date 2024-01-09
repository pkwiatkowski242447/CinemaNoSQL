package model.exceptions.repositories.read_exceptions;

import model.exceptions.repositories.GeneralRepositoryException;

public class RepositoryReadException extends GeneralRepositoryException {

    public RepositoryReadException(String message) {
        super(message);
    }

    public RepositoryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
