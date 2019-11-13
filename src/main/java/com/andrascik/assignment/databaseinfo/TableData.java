package com.andrascik.assignment.databaseinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Data of a table.
 */
public class TableData {
    private final List<String> columnNames;
    private final List<RowData> rows = new ArrayList<>();

    public TableData(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void addRow(RowData row) {
        rows.add(row);
    }

    public List<RowData> getRows() {
        return rows;
    }
}
