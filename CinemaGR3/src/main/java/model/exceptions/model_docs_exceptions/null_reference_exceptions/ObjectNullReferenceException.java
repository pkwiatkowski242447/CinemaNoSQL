package model.exceptions.model_docs_exceptions.null_reference_exceptions;

public class ObjectNullReferenceException extends Exception {
    public ObjectNullReferenceException(String message) {
        super(message);
    }

    public ObjectNullReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
