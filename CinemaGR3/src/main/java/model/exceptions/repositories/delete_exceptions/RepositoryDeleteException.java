package model.exceptions.repositories.delete_exceptions;

import model.exceptions.repositories.GeneralRepositoryException;

public class RepositoryDeleteException extends GeneralRepositoryException {

    public RepositoryDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
