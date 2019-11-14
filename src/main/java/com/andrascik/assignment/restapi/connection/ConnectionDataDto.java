package com.andrascik.assignment.restapi.connection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "All details about the database connection")
public class ConnectionDataDto {
    @ApiModelProperty(example = "3")
    private Long id;
    @ApiModelProperty(example = "connection1")
    @NotNull
    private String name;
    @ApiModelProperty(example = "localhost")
    @NotNull
    private String hostname;
    @ApiModelProperty(example = "1234")
    @NotNull
    private Integer port;
    @ApiModelProperty(example = "mydb")
    @NotNull
    private String databaseName;
    @ApiModelProperty(example = "user")
    @NotNull
    private String userName;
    @ApiModelProperty(example = "Password1")
    @NotNull
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
