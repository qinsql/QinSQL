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
package org.lealone.qinsql.engine.server;

import java.util.ArrayList;

import org.lealone.db.result.Result;
import org.lealone.db.value.Value;

public class QinBatchResult implements Result {

    private final ArrayList<QinResult> list = new ArrayList<>();
    private QinResult current;

    int index;
    private int rowCount;

    void addResult(QinResult result) {
        list.add(result);
        rowCount += result.getRowCount();
        if (current == null)
            current = result;
    }

    @Override
    public int hashCode() {
        return current.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return current.equals(obj);
    }

    @Override
    public void reset() {
        current.reset();
    }

    @Override
    public Value[] currentRow() {
        return current.currentRow();
    }

    @Override
    public boolean next() {
        if (hasNext()) {
            return current.next();
        }
        return false;
    }

    @Override
    public int getRowId() {
        return current.getRowId();
    }

    public boolean isAfterLast() {
        return current.isAfterLast();
    }

    @Override
    public int getVisibleColumnCount() {
        return current.getVisibleColumnCount();
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    public boolean hasNext() {
        boolean hasNext = current.hasNext();
        while (!hasNext && ++index < list.size()) {
            current = list.get(index);
            hasNext = current.hasNext();
        }
        return hasNext;
    }

    @Override
    public boolean needToClose() {
        return current.needToClose();
    }

    @Override
    public void close() {
        current.close();
    }

    @Override
    public String getAlias(int i) {
        return current.getAlias(i);
    }

    @Override
    public String getSchemaName(int i) {
        return current.getSchemaName(i);
    }

    @Override
    public String getTableName(int i) {
        return current.getTableName(i);
    }

    @Override
    public String getColumnName(int i) {
        return current.getColumnName(i);
    }

    @Override
    public int getColumnType(int i) {
        return current.getColumnType(i);
    }

    @Override
    public long getColumnPrecision(int i) {
        return current.getColumnPrecision(i);
    }

    @Override
    public int getColumnScale(int i) {
        return current.getColumnScale(i);
    }

    @Override
    public int getDisplaySize(int i) {
        return current.getDisplaySize(i);
    }

    @Override
    public boolean isAutoIncrement(int i) {
        return current.isAutoIncrement(i);
    }

    @Override
    public int getNullable(int i) {
        return current.getNullable(i);
    }

    @Override
    public void setFetchSize(int fetchSize) {
        current.setFetchSize(fetchSize);
    }

    @Override
    public int getFetchSize() {
        return current.getFetchSize();
    }

    @Override
    public String toString() {
        return current.toString();
    }
}
