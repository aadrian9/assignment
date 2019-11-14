package com.andrascik.assignment.restapi.databaseinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Information about database column")
public class ColumnInfoDto {
    @ApiModelProperty(example = "id")
    private String columnName;
    @ApiModelProperty(example = "integer")
    private String columnDataType;
    @ApiModelProperty(example = "true")
    private boolean primaryKey;
    @ApiModelProperty(example = "false")
    private boolean foreignKey;

    public ColumnInfoDto(String columnName, String columnDataType, boolean primaryKey, boolean foreignKey) {
        this.columnName = columnName;
        this.columnDataType = columnDataType;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
    }

    public ColumnInfoDto() {
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }
}
