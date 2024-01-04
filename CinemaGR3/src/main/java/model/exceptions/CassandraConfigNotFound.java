package model.exceptions;

import java.io.FileNotFoundException;

public class CassandraConfigNotFound extends FileNotFoundException {
    public CassandraConfigNotFound(String s) {
        super(s);
    }
}
