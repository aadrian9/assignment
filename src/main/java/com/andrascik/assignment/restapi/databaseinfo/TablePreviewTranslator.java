package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.RowData;
import com.andrascik.assignment.databaseinfo.TablePreview;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TablePreviewTranslator {
    static TablePreviewDto translate(TablePreview tablePreview) {
        final var allRows = tablePreview.getRows()
                .stream()
                .map(row -> translateSingleRow(row, tablePreview.getColumnNames()))
                .collect(Collectors.toList());
        return new TablePreviewDto(tablePreview.getColumnNames(), allRows);
    }

    private static RowDataDto translateSingleRow(RowData rowData, List<String> columnNames) {
        final var translated = IntStream.range(0, columnNames.size())
                .boxed()
                .collect(Collectors.toMap(columnNames::get, i -> rowData.getData().get(i)));
        return new RowDataDto(translated);
    }
}
