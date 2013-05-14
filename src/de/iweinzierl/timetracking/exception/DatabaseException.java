package de.iweinzierl.timetracking.exception;

public class DatabaseException extends Exception {

    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DatabaseException(Throwable throwable) {
        super(throwable);
    }
}
