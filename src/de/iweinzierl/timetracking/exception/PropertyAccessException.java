package de.iweinzierl.timetracking.exception;

public class PropertyAccessException extends Exception {

    public PropertyAccessException() {
    }

    public PropertyAccessException(String s) {
        super(s);
    }

    public PropertyAccessException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PropertyAccessException(Throwable throwable) {
        super(throwable);
    }
}
