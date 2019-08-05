/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.bats.engine.server;

import org.apache.drill.common.types.TypeProtos;
import org.apache.drill.common.types.TypeProtos.MajorType;
import org.apache.drill.common.types.Types;
import org.apache.drill.exec.expr.TypeHelper;
import org.apache.drill.exec.record.BatchSchema;
import org.apache.drill.exec.record.RecordBatch;
import org.apache.drill.exec.record.VectorWrapper;
import org.apache.drill.exec.vector.ValueVector;
import org.apache.drill.exec.vector.accessor.BoundCheckingAccessor;
import org.apache.drill.exec.vector.accessor.InvalidAccessException;
import org.apache.drill.exec.vector.accessor.SqlAccessor;
import org.lealone.db.result.Result;
import org.lealone.db.value.DataType;
import org.lealone.db.value.Value;
import org.lealone.db.value.ValueBoolean;
import org.lealone.db.value.ValueByte;
import org.lealone.db.value.ValueBytes;
import org.lealone.db.value.ValueDate;
import org.lealone.db.value.ValueDecimal;
import org.lealone.db.value.ValueDouble;
import org.lealone.db.value.ValueFloat;
import org.lealone.db.value.ValueInt;
import org.lealone.db.value.ValueLong;
import org.lealone.db.value.ValueNull;
import org.lealone.db.value.ValueShort;
import org.lealone.db.value.ValueString;
import org.lealone.db.value.ValueTime;
import org.lealone.db.value.ValueTimestamp;

public class BatsResult implements Result {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BatsResult.class);

    static class BatsSqlAccessor {
        final SqlAccessor accessor;
        final int valueType;

        public BatsSqlAccessor(SqlAccessor accessor, int valueType) {
            this.accessor = accessor;
            this.valueType = valueType;
        }

        Value getValue(int rowOffset) throws InvalidAccessException {
            if (accessor.isNull(rowOffset))
                return ValueNull.INSTANCE;

            switch (valueType) {
            case Value.BOOLEAN:
                return accessor.getBoolean(rowOffset) ? ValueBoolean.TRUE : ValueBoolean.FALSE;
            case Value.BYTE:
                return ValueByte.get(accessor.getByte(rowOffset));
            case Value.SHORT:
                return ValueShort.get(accessor.getShort(rowOffset));
            case Value.INT:
                return ValueInt.get(accessor.getInt(rowOffset));
            case Value.LONG:
                return ValueLong.get(accessor.getLong(rowOffset));
            case Value.DECIMAL:
                return ValueDecimal.get(accessor.getBigDecimal(rowOffset));
            case Value.DOUBLE:
                return ValueDouble.get(accessor.getDouble(rowOffset));
            case Value.FLOAT:
                return ValueFloat.get(accessor.getFloat(rowOffset));
            case Value.TIME:
                return ValueTime.get(accessor.getTime(rowOffset));
            case Value.DATE:
                return ValueDate.get(accessor.getDate(rowOffset));
            case Value.TIMESTAMP:
                return ValueTimestamp.get(accessor.getTimestamp(rowOffset));
            case Value.BYTES:
                return ValueBytes.get(accessor.getBytes(rowOffset));
            case Value.STRING:
            case Value.STRING_IGNORECASE:
            case Value.STRING_FIXED:
                return ValueString.get(accessor.getString(rowOffset));
            // 从BLOB到ENUM全转成字节
            default:
                return ValueByte.get(accessor.getByte(rowOffset));
            }
        }
    }

    // private final RecordBatch data;
    BatchSchema schema;
    int columnCount;
    int rowCount;
    int index;
    BatsSqlAccessor[] accessors;
    Value[] current;

    public BatsResult(RecordBatch data) {
        // this.data = data;
        schema = data.getSchema();
        columnCount = schema.getFieldCount();
        accessors = new BatsSqlAccessor[columnCount];
        int column = 0;
        for (VectorWrapper<?> vw : data) {
            final ValueVector vector = vw.getValueVector();
            final SqlAccessor acc = new BoundCheckingAccessor(vector, TypeHelper.getSqlAccessor(vector));
            MajorType majorType = acc.getType();
            final String sqlTypeName = Types.getSqlTypeName(majorType);
            final int jdbcTypeId = Types.getJdbcTypeCode(sqlTypeName);
            int valueType = DataType.convertSQLTypeToValueType(jdbcTypeId);

            accessors[column++] = new BatsSqlAccessor(acc, valueType);
        }
        rowCount = data.getRecordCount();
    }

    @Override
    public void reset() {
        index = 0;
    }

    @Override
    public Value[] currentRow() {
        return current;
    }

    @Override
    public boolean next() {
        if (hasNext()) {
            current = new Value[columnCount];
            for (int i = 0; i < columnCount; i++) {
                try {
                    current[i] = accessors[i].getValue(index);
                } catch (InvalidAccessException e) {
                    logger.warn(e.getMessage());
                    current[i] = ValueNull.INSTANCE;
                }
            }
            index++;
            return true;
        } else {
            current = null;
            return false;
        }
    }

    @Override
    public int getRowId() {
        return index;
    }

    // @Override
    public boolean isAfterLast() {
        return index >= rowCount;
    }

    @Override
    public int getVisibleColumnCount() {
        return columnCount;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    // @Override
    public boolean hasNext() {
        return index < rowCount;
    }

    @Override
    public boolean needToClose() {
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public String getAlias(int i) {
        return null;
    }

    @Override
    public String getSchemaName(int i) {
        return null;
    }

    @Override
    public String getTableName(int i) {
        return null;
    }

    @Override
    public String getColumnName(int i) {
        return schema.getColumn(i).getName();
    }

    @Override
    public int getColumnType(int i) {
        return accessors[i].valueType;
    }

    @Override
    public long getColumnPrecision(int i) {
        return schema.getColumn(i).getPrecision();
    }

    @Override
    public int getColumnScale(int i) {
        return schema.getColumn(i).getScale();
    }

    @Override
    public int getDisplaySize(int i) {
        return schema.getColumn(i).getWidth();
    }

    @Override
    public boolean isAutoIncrement(int i) {
        return false;
    }

    @Override
    public int getNullable(int i) {
        return schema.getColumn(i).getType().getMode() == TypeProtos.DataMode.OPTIONAL ? 1 : 0;
    }

    @Override
    public void setFetchSize(int fetchSize) {
    }

    @Override
    public int getFetchSize() {
        return 0;
    }
}
