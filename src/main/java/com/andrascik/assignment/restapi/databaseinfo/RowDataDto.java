package com.andrascik.assignment.restapi.databaseinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(description = "Data contents of a database row")
public class RowDataDto {
    @ApiModelProperty
    private Map<String, String> columns;

    public RowDataDto(Map<String, String> columns) {
        this.columns = columns;
    }

    public RowDataDto() {
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public Map<String, String> getColumns() {
        return columns;
    }
}
