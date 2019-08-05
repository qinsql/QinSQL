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
package org.lealone.bats.engine.storage;

import java.util.List;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.drill.exec.planner.logical.DynamicDrillTable;
import org.apache.drill.shaded.guava.com.google.common.collect.Lists;
import org.lealone.db.schema.Schema;
import org.lealone.db.table.Column;
import org.lealone.db.value.DataType;

public class LealoneTable extends DynamicDrillTable {
    // private final Schema schema;
    org.lealone.db.table.Table table;

    public LealoneTable(org.lealone.db.table.Table table, String storageEngineName, LealoneStoragePlugin plugin,
            Schema schema, LealoneScanSpec scanSpec) {
        super(plugin, storageEngineName, scanSpec);
        // this.schema = schema;
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
        // throw new UnsupportedOperationException("Unsupported type.");
    }
}
