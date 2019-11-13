package com.andrascik.assignment.restapi.databaseinfo;

public class ColumnStatisticsDto {
    private String name;
    private String table;
    private long minValue;
    private long maxValue;
    private double averageValue;
    private long medianValue;

    public ColumnStatisticsDto(String name, String table, long minValue, long maxValue, double averageValue, long medianValue) {
        this.name = name;
        this.table = table;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.medianValue = medianValue;
    }

    public ColumnStatisticsDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(double averageValue) {
        this.averageValue = averageValue;
    }

    public long getMedianValue() {
        return medianValue;
    }

    public void setMedianValue(long medianValue) {
        this.medianValue = medianValue;
    }
}
