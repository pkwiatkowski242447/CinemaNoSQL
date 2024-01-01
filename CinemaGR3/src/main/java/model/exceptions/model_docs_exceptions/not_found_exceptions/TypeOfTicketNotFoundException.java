package model.exceptions.model_docs_exceptions.not_found_exceptions;

import model.exceptions.model_docs_exceptions.not_found_exceptions.ObjectNotFoundException;

public class TypeOfTicketNotFoundException extends ObjectNotFoundException {
    public TypeOfTicketNotFoundException(String msg) {
        super(msg);
    }
}
