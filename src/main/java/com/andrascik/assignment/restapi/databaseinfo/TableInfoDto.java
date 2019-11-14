package com.andrascik.assignment.restapi.databaseinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Information about database table")
public class TableInfoDto {
    @ApiModelProperty(example = "connections")
    private String tableName;
    @ApiModelProperty(example = "table")
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
