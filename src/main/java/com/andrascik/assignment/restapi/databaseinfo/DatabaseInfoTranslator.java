package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DatabaseInfoTranslator {
    private DatabaseInfoTranslator() {
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

    static ColumnInfoDto translate(ColumnInfo columnInfo) {
        return new ColumnInfoDto(
                columnInfo.getName(),
                columnInfo.getDataType(),
                columnInfo.isPrimaryKey(),
                columnInfo.isForeignKey()
        );
    }

    static TableInfoDto translate(TableInfo tableInfo) {
        return new TableInfoDto(tableInfo.getName(), tableInfo.getType());
    }

    static TablePreviewDto translate(TableData tableData) {
        final var allRows = tableData.getRows()
                .stream()
                .map(row -> translateSingleRow(row, tableData.getColumnNames()))
                .collect(Collectors.toList());
        return new TablePreviewDto(tableData.getColumnNames(), allRows);
    }

    //=================================================================================================================

    private static RowDataDto translateSingleRow(RowData rowData, List<String> columnNames) {
        final var translated = IntStream.range(0, columnNames.size())
                .boxed()
                .collect(Collectors.toMap(columnNames::get, i -> rowData.getData().get(i)));
        return new RowDataDto(translated);
    }
}
