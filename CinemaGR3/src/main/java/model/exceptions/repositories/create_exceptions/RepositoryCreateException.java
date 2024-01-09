package model.exceptions.repositories.create_exceptions;

import model.exceptions.repositories.GeneralRepositoryException;

public class RepositoryCreateException extends GeneralRepositoryException {

    public RepositoryCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
