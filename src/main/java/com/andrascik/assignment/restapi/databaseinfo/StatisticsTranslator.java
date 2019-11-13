package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.TableStatistics;

class StatisticsTranslator {
    private StatisticsTranslator() {
    }

    static TableStatisticsDto translate(String name, TableStatistics statistics) {
        return new TableStatisticsDto(name, statistics.getRowCount(), statistics.getColumnCount());
    }

}
