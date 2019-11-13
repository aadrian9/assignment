package com.andrascik.assignment.databaseinfo;

/**
 * Error during a PostgreSQL database connection.
 */
public class PostgreSqlConnectionException extends RuntimeException {
    public PostgreSqlConnectionException(Throwable cause) {
        super(cause);
    }

    public PostgreSqlConnectionException(String message) {
        super(message);
    }
}
