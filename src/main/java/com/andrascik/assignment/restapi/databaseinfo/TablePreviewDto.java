package com.andrascik.assignment.restapi.databaseinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "Data preview for a database table including list of column names")
public class TablePreviewDto {
    @ApiModelProperty
    private List<String> columnNames;
    @ApiModelProperty
    private List<RowDataDto> rows;

    public TablePreviewDto(List<String> columnNames, List<RowDataDto> rows) {
        this.columnNames = columnNames;
        this.rows = rows;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<RowDataDto> getRows() {
        return rows;
    }

    public void setRows(List<RowDataDto> rows) {
        this.rows = rows;
    }
}
