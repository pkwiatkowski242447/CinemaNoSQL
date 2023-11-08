package model.exceptions.model_docs_exceptions;

public class NullReferenceException extends IllegalArgumentException {
    public NullReferenceException(String s) {
        super(s);
    }

    public NullReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
