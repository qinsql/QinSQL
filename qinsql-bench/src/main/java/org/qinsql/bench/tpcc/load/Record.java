/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.load;

import java.util.Arrays;

/**
 * Simple object to represent a single row of data being loaded to the database (or written to a CSV file).
 */
public class Record {

    /**
     * Column values.
     */
    private final Object field[];

    /**
     * Index of next column to write value to.
     */
    private int index;

    /**
     * Re-usable buffer for building string representations of the row.
     */
    // private final StringBuilder toStringBuilder = new StringBuilder();

    public Record(int columnCount) {
        this.field = new Object[columnCount];
    }

    public Record(Object[] field) {
        this.field = field;
    }

    public void reset() {
        index = 0;
    }

    public void add(Object value) {
        field[index++] = value;
    }

    public Object getField(int i) {
        return field[i];
    }

    public Object[] getField() {
        return field;
    }

    public int getColumnCount() {
        return field.length;
    }

    public Record copy() {
        Object field[] = new Object[index];
        System.arraycopy(this.field, 0, field, 0, index);
        return new Record(field);
    }

    @Override
    public String toString() {
        return Arrays.toString(field);
    }

}
