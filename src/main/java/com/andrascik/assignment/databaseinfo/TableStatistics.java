package com.andrascik.assignment.databaseinfo;

/**
 * Basic statistics of a table.
 */
public class TableStatistics {
    private final int rowCount;
    private final int columnCount;

    public TableStatistics(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }
}
