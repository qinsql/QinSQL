/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.qinsql.engine.storage;

import java.util.List;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.drill.exec.planner.logical.DynamicDrillTable;
import org.apache.drill.shaded.guava.com.google.common.collect.Lists;
import org.lealone.db.table.Column;
import org.lealone.db.table.Table;
import org.lealone.db.value.DataType;

public class LealoneTable extends DynamicDrillTable {

    private final Table table;

    public LealoneTable(Table table, LealoneStoragePlugin plugin, LealoneScanSpec scanSpec) {
        super(plugin, plugin.getName(), scanSpec);
        this.table = table;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        List<String> names = Lists.newArrayList();
        List<RelDataType> types = Lists.newArrayList();
        for (Column column : table.getColumns()) {
            names.add(column.getName());
            RelDataType type = getSqlTypeFromLealoneType(typeFactory, column.getType());
            type = typeFactory.createTypeWithNullability(type, column.isNullable());
            types.add(type);
        }

        return typeFactory.createStructType(types, names);
    }

    private RelDataType getSqlTypeFromLealoneType(RelDataTypeFactory typeFactory, int type) {
        int sqlType = DataType.convertTypeToSQLType(type);
        SqlTypeName typeName = SqlTypeName.getNameForJdbcType(sqlType);
        return typeFactory.createSqlType(typeName);
    }
}
