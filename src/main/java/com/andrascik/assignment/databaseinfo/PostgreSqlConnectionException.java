package com.andrascik.assignment.databaseinfo;

public class PostgreSqlConnectionException extends RuntimeException {
    public PostgreSqlConnectionException(Throwable cause) {
        super(cause);
    }

    public PostgreSqlConnectionException(String message) {
        super(message);
    }
}
