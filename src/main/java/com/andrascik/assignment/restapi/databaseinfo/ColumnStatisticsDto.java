package com.andrascik.assignment.restapi.databaseinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Statistics for a database column")
public class ColumnStatisticsDto {
    @ApiModelProperty(example = "id")
    private String name;
    @ApiModelProperty(example = "connections")
    private String table;
    @ApiModelProperty(example = "1")
    private String minValue;
    @ApiModelProperty(example = "3")
    private String maxValue;
    @ApiModelProperty(example = "2")
    private String averageValue;
    @ApiModelProperty(example = "2")
    private String medianValue;

    public ColumnStatisticsDto(
            String name, String table, String minValue, String maxValue, String averageValue, String medianValue) {
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

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(String averageValue) {
        this.averageValue = averageValue;
    }

    public String getMedianValue() {
        return medianValue;
    }

    public void setMedianValue(String medianValue) {
        this.medianValue = medianValue;
    }
}
