package com.andrascik.assignment.restapi.databaseinfo;

public class TableStatisticsDto {
    private String name;
    private int rowCount;
    private int columnCount;

    public TableStatisticsDto(String name, int rowCount, int columnCount) {
        this.name = name;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public TableStatisticsDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }
}
