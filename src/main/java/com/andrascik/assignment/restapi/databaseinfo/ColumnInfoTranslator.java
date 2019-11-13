package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.ColumnInfo;

import java.util.List;
import java.util.stream.Collectors;

class ColumnInfoTranslator {
    private ColumnInfoTranslator() {
    }

    static List<ColumnInfoDto> translate(List<ColumnInfo> columnInfos) {
        return columnInfos
                .stream()
                .map(ColumnInfoTranslator::translateSingleColumn)
                .collect(Collectors.toList());
    }

    private static ColumnInfoDto translateSingleColumn(ColumnInfo columnInfo) {
        return new ColumnInfoDto(
                columnInfo.getName(),
                columnInfo.getDataType(),
                columnInfo.isPrimaryKey(),
                columnInfo.isForeignKey()
        );
    }
}
