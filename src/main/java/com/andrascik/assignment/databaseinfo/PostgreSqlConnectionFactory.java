package com.andrascik.assignment.databaseinfo;

/**
 * Factory for {@link PostgreSqlConnection}.
 */
public interface PostgreSqlConnectionFactory {

    /**
     * Create instance of {@link PostgreSqlConnection}.
     * @param hostName
     * @param port
     * @param databaseName
     * @param userName
     * @param password
     * @return
     */
    PostgreSqlConnection create(String hostName, Integer port, String databaseName, String userName, String password);
}
