package com.andrascik.assignment.restapi;

import java.util.Map;

public class RowDataDto {
    private Map<String, String> columns;

    public RowDataDto(Map<String, String> columns) {
        this.columns = columns;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public Map<String, String> getColumns() {
        return columns;
    }
}
