package model.exceptions.repository_exceptions.delete_exceptions;

import model.exceptions.repository_exceptions.GeneralRepositoryException;

public class RepositoryDeleteException extends GeneralRepositoryException {

    public RepositoryDeleteException(String message) {
        super(message);
    }

    public RepositoryDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
