package model.exceptions.repository_exceptions.update_exceptions;

import model.exceptions.repository_exceptions.GeneralRepositoryException;

public class RepositoryUpdateException extends GeneralRepositoryException {

    public RepositoryUpdateException(String message) {
        super(message);
    }

    public RepositoryUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
