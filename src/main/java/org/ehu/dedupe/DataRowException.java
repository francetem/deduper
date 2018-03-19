package org.ehu.dedupe;

public class DataRowException extends Exception {
    public DataRowException(ReflectiveOperationException e) {
        super(e);
    }
}
