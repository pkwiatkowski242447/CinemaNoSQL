package model.exceptions;

import java.io.FileNotFoundException;

public class MongoConfigNotFoundException extends FileNotFoundException {
    public MongoConfigNotFoundException(String s) {
        super(s);
    }
}