/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.sql.vector;

import org.lealone.common.exceptions.DbException;
import org.lealone.db.api.ErrorCode;
import org.lealone.db.value.Value;
import org.lealone.db.value.ValueInt;
import org.lealone.db.value.ValueLong;
import org.lealone.sql.expression.condition.Comparison;

public class IntVector extends ValueVector {

    private int[] values;

    public IntVector(int[] values) {
        this.values = values;
    }

    @Override
    public int getValueType() {
        return Value.INT;
    }

    @Override
    public ValueVector convertTo(int targetType) {
        if (getValueType() == targetType)
            return this;
        try {
            switch (targetType) {
            case Value.BOOLEAN: {
                int len = values.length;
                boolean[] a = new boolean[len];
                for (int i = 0; i < len; i++) {
                    a[i] = Integer.signum(values[i]) != 0;
                }
                return new BooleanVector(a);
            }
            case Value.BYTE: {
                int len = values.length;
                byte[] a = new byte[len];
                for (int i = 0; i < len; i++) {
                    long x = values[i];
                    if (x > Byte.MAX_VALUE || x < Byte.MIN_VALUE) {
                        throw DbException.get(ErrorCode.NUMERIC_VALUE_OUT_OF_RANGE_1, Long.toString(x));
                    }
                    a[i] = (byte) x;
                }
                return new ByteVector(a);
            }
            case Value.SHORT: {
                // return ValueShort.get(convertToShort(getInt()));
            }
            case Value.INT: {
                return this;
            }
            case Value.LONG: {
                int len = values.length;
                long[] a = new long[len];
                for (int i = 0; i < len; i++) {
                    a[i] = values[i];
                }
                return new LongVector(a);
            }
            case Value.DECIMAL: {
                // return ValueDecimal.get(BigDecimal.valueOf(getInt()));
            }
            case Value.DOUBLE: {
                // return ValueDouble.get(getInt());
            }
            case Value.FLOAT: {
                // return ValueFloat.get(getInt());
            }
            case Value.DATE: {
                break;
            }
            case Value.TIME: {
                break;
            }
            case Value.TIMESTAMP: {
                break;
            }
            case Value.BYTES: {
                // int x = getInt();
                // return ValueBytes.getNoCopy(
                // new byte[] { (byte) (x >> 24), (byte) (x >> 16), (byte) (x >> 8), (byte) x });
            }
            case Value.JAVA_OBJECT: {
                break;
            }
            case Value.BLOB: {
                break;
            }
            case Value.UUID: {
            }
            }
            // // conversion by parsing the string value
            // String s = getString();
            // switch (targetType) {
            // case Value.NULL:
            // return ValueNull.INSTANCE;
            // case Value.BOOLEAN: {
            // if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("t") || s.equalsIgnoreCase("yes")
            // || s.equalsIgnoreCase("y")) {
            // return ValueBoolean.get(true);
            // } else if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("f")
            // || s.equalsIgnoreCase("no") || s.equalsIgnoreCase("n")) {
            // return ValueBoolean.get(false);
            // } else {
            // // convert to a number, and if it is not 0 then it is true
            // return ValueBoolean.get(new BigDecimal(s).signum() != 0);
            // }
            // }
            // case Value.BYTE:
            // return ValueByte.get(Byte.parseByte(s.trim()));
            // case Value.SHORT:
            // return ValueShort.get(Short.parseShort(s.trim()));
            // case Value.INT:
            // return ValueInt.get(Integer.parseInt(s.trim()));
            // case Value.LONG:
            // return ValueLong.get(Long.parseLong(s.trim()));
            // case Value.DECIMAL:
            // return ValueDecimal.get(new BigDecimal(s.trim()));
            // case Value.TIME:
            // return ValueTime.parse(s.trim());
            // case Value.DATE:
            // return ValueDate.parse(s.trim());
            // case Value.TIMESTAMP:
            // return ValueTimestamp.parse(s.trim());
            // case Value.BYTES:
            // return ValueBytes.getNoCopy(StringUtils.convertHexToBytes(s.trim()));
            // case Value.JAVA_OBJECT:
            // return ValueJavaObject.getNoCopy(null, StringUtils.convertHexToBytes(s.trim()));
            // case Value.STRING:
            // return ValueString.get(s);
            // case Value.STRING_IGNORECASE:
            // return ValueStringIgnoreCase.get(s);
            // case Value.STRING_FIXED:
            // return ValueStringFixed.get(s);
            // case Value.DOUBLE:
            // return ValueDouble.get(Double.parseDouble(s.trim()));
            // case Value.FLOAT:
            // return ValueFloat.get(Float.parseFloat(s.trim()));
            // case Value.CLOB:
            // return ValueLob.createSmallLob(CLOB, s.getBytes(Constants.UTF8));
            // case Value.BLOB:
            // return ValueLob.createSmallLob(BLOB, StringUtils.convertHexToBytes(s.trim()));
            // case Value.ARRAY:
            // return ValueArray.get(new Value[] { ValueString.get(s) });
            // case Value.RESULT_SET: {
            // SimpleResultSet rs = new SimpleResultSet();
            // rs.addColumn("X", Types.VARCHAR, s.length(), 0);
            // rs.addRow(s);
            // return ValueResultSet.get(rs);
            // }
            // case Value.UUID:
            // return ValueUuid.get(s);
            // default:
            // throw DbException.getInternalError("type=" + targetType);
            // }
        } catch (NumberFormatException e) {
            // throw DbException.get(ErrorCode.DATA_CONVERSION_ERROR_1, e, getString());
        }
        return this;
    }

    @Override
    public BooleanVector compare(ValueVector vv, int compareType) {
        switch (compareType) {
        case Comparison.EQUAL: {
            if (vv instanceof SingleValueVector) {
                int[] values1 = this.values;
                int v = ((SingleValueVector) vv).getValue().getInt();
                boolean[] values = new boolean[values1.length];
                for (int i = 0; i < values1.length; i++) {
                    values[i] = values1[i] == v;
                }
                return new BooleanVector(values);
            }
            int[] values1 = this.values;
            int[] values2 = ((IntVector) vv).values;
            boolean[] values = new boolean[values1.length];
            for (int i = 0; i < values1.length; i++) {
                values[i] = values1[i] == values2[i];
            }
            return new BooleanVector(values);
        }
        case Comparison.EQUAL_NULL_SAFE:
            return null;
        case Comparison.BIGGER_EQUAL:
            return null;
        case Comparison.BIGGER: {
            if (vv instanceof SingleValueVector) {
                int[] values1 = this.values;
                int v = ((SingleValueVector) vv).getValue().getInt();
                boolean[] values = new boolean[values1.length];
                for (int i = 0; i < values1.length; i++) {
                    values[i] = values1[i] > v;
                }
                return new BooleanVector(values);
            }
            return null;
        }
        case Comparison.SMALLER_EQUAL:
            return null;
        case Comparison.SMALLER:
            return null;
        case Comparison.NOT_EQUAL:
            return null;
        case Comparison.NOT_EQUAL_NULL_SAFE:
            return null;
        default:
            throw DbException.getInternalError("compareType=" + compareType);
        }
    }

    @Override
    public ValueVector add(ValueVector vv) {
        if (vv instanceof SingleValueVector) {
            int[] values1 = this.values;
            int v = ((SingleValueVector) vv).getValue().getInt();
            int[] values = new int[values1.length];
            for (int i = 0; i < values1.length; i++) {
                values[i] = values1[i] + v;
            }
            return new IntVector(values);
        }
        int[] values1 = this.values;
        int[] values2 = ((IntVector) vv).values;
        int[] values = new int[values1.length];
        int len = Math.min(values1.length, values2.length);
        int i = 0;
        for (; i < len; i++) {
            values[i] = values1[i] + values2[i]; // TODO 可能会溢出
        }
        for (; i < values1.length; i++) {
            values[i] = values1[i];
        }
        return new IntVector(values);
    }

    @Override
    public ValueVector add(ValueVector bvv0, ValueVector vv, ValueVector bvv) {
        return null;
    }

    @Override
    public ValueVector subtract(ValueVector vv) {
        if (vv instanceof SingleValueVector) {
            int[] values1 = this.values;
            int v = ((SingleValueVector) vv).getValue().getInt();
            int[] values = new int[values1.length];
            for (int i = 0; i < values1.length; i++) {
                values[i] = values1[i] - v;
            }
            return new IntVector(values);
        }
        int[] values1 = this.values;
        int[] values2 = ((IntVector) vv).values;
        int[] values = new int[values1.length];
        int len = Math.min(values1.length, values2.length);
        int i = 0;
        for (; i < len; i++) {
            values[i] = values1[i] - values2[i]; // TODO 可能会溢出
        }
        for (; i < values1.length; i++) {
            values[i] = values1[i];
        }
        return new IntVector(values);
    }

    @Override
    public ValueVector multiply(ValueVector vv) {
        if (vv instanceof SingleValueVector) {
            int[] values1 = this.values;
            int v = ((SingleValueVector) vv).getValue().getInt();
            int[] values = new int[values1.length];
            for (int i = 0; i < values1.length; i++) {
                values[i] = values1[i] * v;
            }
            return new IntVector(values);
        }
        int[] values1 = this.values;
        int[] values2 = ((IntVector) vv).values;
        int[] values = new int[values1.length];
        int len = Math.min(values1.length, values2.length);
        int i = 0;
        for (; i < len; i++) {
            values[i] = values1[i] * values2[i]; // TODO 可能会溢出
        }
        for (; i < values1.length; i++) {
            values[i] = values1[i];
        }
        return new IntVector(values);
    }

    @Override
    public ValueVector divide(ValueVector vv) {
        if (vv instanceof SingleValueVector) {
            int[] values1 = this.values;
            int v = ((SingleValueVector) vv).getValue().getInt();
            int[] values = new int[values1.length];
            for (int i = 0; i < values1.length; i++) {
                values[i] = values1[i] / v;
            }
            return new IntVector(values);
        }
        int[] values1 = this.values;
        int[] values2 = ((IntVector) vv).values;
        int[] values = new int[values1.length];
        int len = Math.min(values1.length, values2.length);
        int i = 0;
        for (; i < len; i++) {
            values[i] = values1[i] / values2[i]; // TODO 可能会溢出
        }
        for (; i < values1.length; i++) {
            values[i] = values1[i];
        }
        return new IntVector(values);
    }

    @Override
    public ValueVector modulus(ValueVector vv) {
        if (vv instanceof SingleValueVector) {
            int[] values1 = this.values;
            int v = ((SingleValueVector) vv).getValue().getInt();
            int[] values = new int[values1.length];
            for (int i = 0; i < values1.length; i++) {
                values[i] = values1[i] % v;
            }
            return new IntVector(values);
        }
        int[] values1 = this.values;
        int[] values2 = ((IntVector) vv).values;
        int[] values = new int[values1.length];
        int len = Math.min(values1.length, values2.length);
        int i = 0;
        for (; i < len; i++) {
            values[i] = values1[i] % values2[i]; // TODO 可能会溢出
        }
        for (; i < values1.length; i++) {
            values[i] = values1[i];
        }
        return new IntVector(values);
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Value getValue(int index) {
        return ValueInt.get(values[index]);
    }

    @Override
    public Value[] getValues(ValueVector bvv) {
        int size;
        if (bvv == null)
            size = values.length;
        else
            size = bvv.trueCount();
        Value[] a = new Value[size];
        int j = 0;
        for (int i = 0, len = values.length; i < len; i++) {
            if (bvv == null || bvv.isTrue(i))
                a[j++] = getValue(i);
        }
        return a;
    }

    @Override
    public Value sum() {
        int sum = 0;
        for (int i = 0, len = values.length; i < len; i++) {
            sum += values[i];
        }
        return ValueLong.get(sum);
    }

    @Override
    public Value sum(ValueVector bvv) {
        if (bvv == null)
            return sum();
        int sum = 0;
        for (int i = 0, len = values.length; i < len; i++) {
            if (bvv.isTrue(i))
                sum += values[i];
        }
        return ValueLong.get(sum);
    }

    @Override
    public Value min() {
        int min = Integer.MAX_VALUE;
        for (int i = 0, len = values.length; i < len; i++) {
            if (min > values[i])
                min = values[i];
        }
        return ValueInt.get(min);
    }

    @Override
    public Value min(ValueVector bvv) {
        if (bvv == null)
            return min();
        int min = Integer.MAX_VALUE;
        for (int i = 0, len = values.length; i < len; i++) {
            if (bvv.isTrue(i) && min > values[i])
                min = values[i];
        }
        return ValueInt.get(min);
    }

    @Override
    public Value max() {
        int max = Integer.MIN_VALUE;
        for (int i = 0, len = values.length; i < len; i++) {
            if (max < values[i])
                max = values[i];
        }
        return ValueInt.get(max);
    }

    @Override
    public Value max(ValueVector bvv) {
        if (bvv == null)
            return min(bvv);
        int max = Integer.MIN_VALUE;
        for (int i = 0, len = values.length; i < len; i++) {
            if (bvv.isTrue(i) && max < values[i])
                max = values[i];
        }
        return ValueInt.get(max);
    }

    @Override
    public ValueVector filter(ValueVector bvv) {
        if (bvv == null)
            return new IntVector(values);
        int size = bvv.trueCount();
        int[] a = new int[size];
        int j = 0;
        for (int i = 0, len = values.length; i < len; i++) {
            if (bvv == null || bvv.isTrue(i))
                a[j++] = values[i];
        }
        return new IntVector(a);
    }
}
