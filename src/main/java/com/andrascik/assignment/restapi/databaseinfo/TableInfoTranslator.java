package com.andrascik.assignment.restapi.databaseinfo;

import com.andrascik.assignment.databaseinfo.TableInfo;

import java.util.List;
import java.util.stream.Collectors;

class TableInfoTranslator {
    private TableInfoTranslator() {
    }

    static List<TableInfoDto> translate(List<TableInfo> tableInfos) {
        return tableInfos
                .stream()
                .map(TableInfoTranslator::translateSingleTable)
                .collect(Collectors.toList());
    }

    private static TableInfoDto translateSingleTable(TableInfo tableInfo) {
        return new TableInfoDto(tableInfo.getName(), tableInfo.getType());
    }
}
