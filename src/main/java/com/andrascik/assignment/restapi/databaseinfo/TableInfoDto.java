package com.andrascik.assignment.restapi.databaseinfo;

public class TableInfoDto {
    private String tableName;
    private String tableType;

    public TableInfoDto(String tableName, String tableType) {
        this.tableName = tableName;
        this.tableType = tableType;
    }

    public TableInfoDto() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }
}
