package com.andrascik.assignment.databaseinfo;

import java.util.ArrayList;
import java.util.List;

public class TablePreview {
    private final List<String> columnNames;
    private final List<RowData> rows = new ArrayList<>();

    public TablePreview(List<String> columnNames) {
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
