package model.exceptions.repository_exceptions;

import java.io.FileNotFoundException;

public class RedisConfigNotFoundException extends FileNotFoundException {
    public RedisConfigNotFoundException(String s) {
        super(s);
    }
}
