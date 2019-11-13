package com.andrascik.assignment.databaseinfo;

import java.util.List;

/**
 * Represents a single row in a database.
 */
public class RowData {
    private final List<String> data;

    public RowData(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }
}
