package com.andrascik.assignment.restapi.databaseinfo;

import java.util.List;

public class TablePreviewDto {
    private List<String> columnNames;
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
