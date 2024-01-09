package model.exceptions.repositories.update_exceptions;

import model.exceptions.repositories.GeneralRepositoryException;

public class RepositoryUpdateException extends GeneralRepositoryException {

    public RepositoryUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
