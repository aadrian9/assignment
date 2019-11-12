package com.andrascik.assignment.connection;

public class ColumnInfo {
    private final String name;
    private final String dataType;
    private final boolean primaryKey;
    private final boolean foreignKey;

    public ColumnInfo(String name, String dataType, boolean isPrimaryKey, boolean isForeignKey) {
        this.name = name;
        this.dataType = dataType;
        this.primaryKey = isPrimaryKey;
        this.foreignKey = isForeignKey;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }
}
