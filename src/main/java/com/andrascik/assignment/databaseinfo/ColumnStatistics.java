package com.andrascik.assignment.databaseinfo;

/**
 * Basic statistics for data in a single database column.
 */
public class ColumnStatistics {
    private final String minValue;
    private final String maxValue;
    private final String averageValue;
    private final String medianValue;

    public ColumnStatistics(String minValue, String maxValue, String averageValue, String medianValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.medianValue = medianValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public String getAverageValue() {
        return averageValue;
    }

    public String getMedianValue() {
        return medianValue;
    }
}
