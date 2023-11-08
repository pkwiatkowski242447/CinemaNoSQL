package model.exceptions.model_docs_exceptions;

import com.mongodb.MongoException;

public class DocNotFoundException extends MongoException {
    public DocNotFoundException(String msg) {
        super(msg);
    }

    public DocNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
