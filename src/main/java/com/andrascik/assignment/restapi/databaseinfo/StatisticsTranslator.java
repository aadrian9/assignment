package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.ColumnStatistics;
import com.andrascik.assignment.databaseinfo.TableStatistics;

class StatisticsTranslator {
    private StatisticsTranslator() {
    }

    static TableStatisticsDto translate(String name, TableStatistics statistics) {
        return new TableStatisticsDto(name, statistics.getRowCount(), statistics.getColumnCount());
    }

    static ColumnStatisticsDto translate(String name, String table, ColumnStatistics statistics) {
        return new ColumnStatisticsDto(
                name,
                table,
                statistics.getMinValue(),
                statistics.getMaxValue(),
                statistics.getAverageValue(),
                statistics.getMedianValue()
        );
    }
}
