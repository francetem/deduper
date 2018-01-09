package org.ehu.dedupe;

public class InvalidDataRowConstruction extends RuntimeException {
    public InvalidDataRowConstruction(Exception e) {
        super(e);
    }
}
