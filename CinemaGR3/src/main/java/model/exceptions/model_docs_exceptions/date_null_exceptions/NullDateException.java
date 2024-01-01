package model.exceptions.model_docs_exceptions.date_null_exceptions;

import model.exceptions.model_docs_exceptions.null_reference_exceptions.ObjectNullReferenceException;

public class NullDateException extends ObjectNullReferenceException {
    public NullDateException(String s) {
        super(s);
    }
}
