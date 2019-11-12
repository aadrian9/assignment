package com.andrascik.assignment.connection;

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
