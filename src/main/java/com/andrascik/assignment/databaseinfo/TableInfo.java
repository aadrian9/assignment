package com.andrascik.assignment.databaseinfo;

/**
 * Basic information about a database table.
 */
public class TableInfo {
    private final String name;
    private final String type;

    public TableInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
