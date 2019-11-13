package com.andrascik.assignment.databaseinfo;

import java.util.List;

public class RowData {
    private final List<String> data;

    public RowData(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }
}
