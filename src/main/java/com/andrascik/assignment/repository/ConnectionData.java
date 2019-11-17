package com.andrascik.assignment.repository;

/**
 * Internal representation of connection.
 */
public class ConnectionData {
    private final Long id;
    private final String name;
    private final String hostname;
    private final Integer port;
    private final String databaseName;
    private final String userName;
    private final String password;

    public ConnectionData(
            Long id,
            String name,
            String hostname,
            Integer port,
            String databaseName,
            String userName,
            String password) {
        this.id = id;
        this.name = name;
        this.hostname = hostname;
        this.port = port;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
    }

    public ConnectionData(ConnectionDataDao dao) {
        this.id = dao.getId();
        this.name = dao.getName();
        this.hostname = dao.getHostname();
        this.port = dao.getPort();
        this.databaseName = dao.getDatabaseName();
        this.userName = dao.getUserName();
        this.password = dao.getPassword();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHostname() {
        return hostname;
    }

    public Integer getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public ConnectionDataDao toDao() {
        final var dao = new ConnectionDataDao(name, hostname, port, databaseName, userName, password);
        if (id != null) {
            dao.setId(id);
        }
        return dao;
    }
}
