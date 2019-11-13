package com.andrascik.assignment.databaseinfo;

public class ColumnStatistics {
    private final long minValue;
    private final long maxValue;
    private final double averageValue;
    private final long medianValue;

    public ColumnStatistics(long minValue, long maxValue, double averageValue, long medianValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.medianValue = medianValue;
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public long getMedianValue() {
        return medianValue;
    }
}
