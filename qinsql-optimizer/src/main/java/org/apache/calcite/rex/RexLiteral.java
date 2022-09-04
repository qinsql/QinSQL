/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.rex;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.List;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.ByteString;
import org.apache.calcite.util.DateString;
import org.apache.calcite.util.NlsString;
import org.apache.calcite.util.TimeString;
import org.apache.calcite.util.TimestampString;
import org.apache.calcite.util.Util;

/**
 * Constant value in a row-expression.
 *
 * <p>There are several methods for creating literals in {@link RexBuilder}:
 * {@link RexBuilder#makeLiteral(boolean)} and so forth.</p>
 *
 * <p>How is the value stored? In that respect, the class is somewhat of a black
 * box. There is a {@link #getValue} method which returns the value as an
 * object, but the type of that value is implementation detail, and it is best
 * that your code does not depend upon that knowledge. It is better to use
 * task-oriented methods such as {@link #getValue2} and
 * {@link #toJavaString}.</p>
 *
 * <p>The allowable types and combinations are:</p>
 *
 * <table>
 * <caption>Allowable types for RexLiteral instances</caption>
 * <tr>
 * <th>TypeName</th>
 * <th>Meaning</th>
 * <th>Value type</th>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#NULL}</td>
 * <td>The null value. It has its own special type.</td>
 * <td>null</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#BOOLEAN}</td>
 * <td>Boolean, namely <code>TRUE</code>, <code>FALSE</code> or <code>
 * UNKNOWN</code>.</td>
 * <td>{@link Boolean}, or null represents the UNKNOWN value</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#DECIMAL}</td>
 * <td>Exact number, for example <code>0</code>, <code>-.5</code>, <code>
 * 12345</code>.</td>
 * <td>{@link BigDecimal}</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#DOUBLE}</td>
 * <td>Approximate number, for example <code>6.023E-23</code>.</td>
 * <td>{@link BigDecimal}</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#DATE}</td>
 * <td>Date, for example <code>DATE '1969-04'29'</code></td>
 * <td>{@link Calendar};
 *     also {@link Calendar} (UTC time zone)
 *     and {@link Integer} (days since POSIX epoch)</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#TIME}</td>
 * <td>Time, for example <code>TIME '18:37:42.567'</code></td>
 * <td>{@link Calendar};
 *     also {@link Calendar} (UTC time zone)
 *     and {@link Integer} (milliseconds since midnight)</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#TIMESTAMP}</td>
 * <td>Timestamp, for example <code>TIMESTAMP '1969-04-29
 * 18:37:42.567'</code></td>
 * <td>{@link TimestampString};
 *     also {@link Calendar} (UTC time zone)
 *     and {@link Long} (milliseconds since POSIX epoch)</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#INTERVAL_DAY},
 *     {@link SqlTypeName#INTERVAL_DAY_HOUR},
 *     {@link SqlTypeName#INTERVAL_DAY_MINUTE},
 *     {@link SqlTypeName#INTERVAL_DAY_SECOND},
 *     {@link SqlTypeName#INTERVAL_HOUR},
 *     {@link SqlTypeName#INTERVAL_HOUR_MINUTE},
 *     {@link SqlTypeName#INTERVAL_HOUR_SECOND},
 *     {@link SqlTypeName#INTERVAL_MINUTE},
 *     {@link SqlTypeName#INTERVAL_MINUTE_SECOND},
 *     {@link SqlTypeName#INTERVAL_SECOND}</td>
 * <td>Interval, for example <code>INTERVAL '4:3:2' HOUR TO SECOND</code></td>
 * <td>{@link BigDecimal};
 *     also {@link Long} (milliseconds)</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#INTERVAL_YEAR},
 *     {@link SqlTypeName#INTERVAL_YEAR_MONTH},
 *     {@link SqlTypeName#INTERVAL_MONTH}</td>
 * <td>Interval, for example <code>INTERVAL '2-3' YEAR TO MONTH</code></td>
 * <td>{@link BigDecimal};
 *     also {@link Integer} (months)</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#CHAR}</td>
 * <td>Character constant, for example <code>'Hello, world!'</code>, <code>
 * ''</code>, <code>_N'Bonjour'</code>, <code>_ISO-8859-1'It''s superman!'
 * COLLATE SHIFT_JIS$ja_JP$2</code>. These are always CHAR, never VARCHAR.</td>
 * <td>{@link NlsString};
 *     also {@link String}</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#BINARY}</td>
 * <td>Binary constant, for example <code>X'7F34'</code>. (The number of hexits
 * must be even; see above.) These constants are always BINARY, never
 * VARBINARY.</td>
 * <td>{@link ByteBuffer};
 *     also {@code byte[]}</td>
 * </tr>
 * <tr>
 * <td>{@link SqlTypeName#SYMBOL}</td>
 * <td>A symbol is a special type used to make parsing easier; it is not part of
 * the SQL standard, and is not exposed to end-users. It is used to hold a flag,
 * such as the LEADING flag in a call to the function <code>
 * TRIM([LEADING|TRAILING|BOTH] chars FROM string)</code>.</td>
 * <td>An enum class</td>
 * </tr>
 * </table>
 */
@SuppressWarnings("rawtypes")
public interface RexLiteral extends RexNode {

    /**
     * Returns a string which concisely describes the definition of this
     * rex literal. Two literals are equivalent if and only if their digests are the same.
     *
     * <p>The digest does not contain the expression's identity, but does include the identity
     * of children.
     *
     * <p>Technically speaking 1:INT differs from 1:FLOAT, so we need data type in the literal's
     * digest, however we want to avoid extra verbosity of the {@link RelNode#getDigest()} for
     * readability purposes, so we omit type info in certain cases.
     * For instance, 1:INT becomes 1 (INT is implied by default), however 1:BIGINT always holds
     * the type
     *
     * <p>Here's a non-exhaustive list of the "well known cases":
     * <ul><li>Hide "NOT NULL" for not null literals
     * <li>Hide INTEGER, BOOLEAN, SYMBOL, TIME(0), TIMESTAMP(0), DATE(0) types
     * <li>Hide collation when it matches IMPLICIT/COERCIBLE
     * <li>Hide charset when it matches default
     * <li>Hide CHAR(xx) when literal length is equal to the precision of the type.
     * In other words, use 'Bob' instead of 'Bob':CHAR(3)
     * <li>Hide BOOL for AND/OR arguments. In other words, AND(true, null) means
     * null is BOOL.
     * <li>Hide types for literals in simple binary operations (e.g. +, -, *, /,
     * comparison) when type of the other argument is clear.
     * See {@link RexCall#computeDigest(boolean)}
     * For instance: =(true. null) means null is BOOL. =($0, null) means the type
     * of null matches the type of $0.
     * </ul>
     *
     * @param includeType whether the digest should include type or not
     * @return digest
     */
    public String computeDigest(RexDigestIncludeType includeType);

    /**
     * Returns true if {@link RexDigestIncludeType#OPTIONAL} digest would include data type.
     *
     * @see RexCall#computeDigest(boolean)
     * @return true if {@link RexDigestIncludeType#OPTIONAL} digest would include data type
     */
    public RexDigestIncludeType digestIncludesType();

    /**
     * Prints the value this literal as a Java string constant.
     */
    public void printAsJava(PrintWriter pw);

    public SqlTypeName getTypeName();

    /**
     * Returns whether this literal's value is null.
     */
    public boolean isNull();

    /**
     * Returns the value of this literal.
     *
     * <p>For backwards compatibility, returns DATE. TIME and TIMESTAMP as a
     * {@link Calendar} value in UTC time zone.
     */
    public Comparable getValue();

    /**
     * Returns the value of this literal, in the form that the calculator
     * program builder wants it.
     */
    public Object getValue2();

    /**
     * Returns the value of this literal, in the form that the rex-to-lix
     * translator wants it.
     */
    public Object getValue3();

    /**
     * Returns the value of this literal, in the form that {@link RexInterpreter}
     * wants it.
     */
    public Comparable getValue4();

    /** Returns the value of this literal as an instance of the specified class.
     *
     * <p>The following SQL types allow more than one form:
     *
     * <ul>
     * <li>CHAR as {@link NlsString} or {@link String}
     * <li>TIME as {@link TimeString},
     *   {@link Integer} (milliseconds since midnight),
     *   {@link Calendar} (in UTC)
     * <li>DATE as {@link DateString},
     *   {@link Integer} (days since 1970-01-01),
     *   {@link Calendar}
     * <li>TIMESTAMP as {@link TimestampString},
     *   {@link Long} (milliseconds since 1970-01-01 00:00:00),
     *   {@link Calendar}
     * <li>DECIMAL as {@link BigDecimal} or {@link Long}
     * </ul>
     *
     * <p>Called with {@code clazz} = {@link Comparable}, returns the value in
     * its native form.
     *
     * @param clazz Desired return type
     * @param <T> Return type
     * @return Value of this literal in the desired type
     */
    public <T> T getValueAs(Class<T> clazz);

    public static Comparable value(RexNode node) {
        return findValue(node);
    }

    public static int intValue(RexNode node) {
        final Comparable value = findValue(node);
        return ((Number) value).intValue();
    }

    public static String stringValue(RexNode node) {
        final Comparable value = findValue(node);
        return (value == null) ? null : ((NlsString) value).getValue();
    }

    public static Comparable findValue(RexNode node) {
        if (node instanceof RexLiteral) {
            return ((RexLiteral) node).getValue();
        }
        if (node instanceof RexCall) {
            final RexCall call = (RexCall) node;
            final SqlOperator operator = call.getOperator();
            if (operator == SqlStdOperatorTable.CAST) {
                return findValue(call.getOperands().get(0));
            }
            if (operator == SqlStdOperatorTable.UNARY_MINUS) {
                final BigDecimal value = (BigDecimal) findValue(call.getOperands().get(0));
                return value.negate();
            }
        }
        throw new AssertionError("not a literal: " + node);
    }

    public static boolean isNullLiteral(RexNode node) {
        return (node instanceof RexLiteral) && (((RexLiteral) node).getValue() == null);
    }

    /**
     * @return whether value is appropriate for its type (we have rules about
     * these things)
     */
    public static boolean valueMatchesType(Comparable value, SqlTypeName typeName, boolean strict) {
        if (value == null) {
            return true;
        }
        switch (typeName) {
        case BOOLEAN:
            // Unlike SqlLiteral, we do not allow boolean null.
            return value instanceof Boolean;
        case NULL:
            return false; // value should have been null
        case INTEGER: // not allowed -- use Decimal
        case TINYINT:
        case SMALLINT:
            if (strict) {
                throw Util.unexpected(typeName);
            }
            // fall through
        case DECIMAL:
        case DOUBLE:
        case FLOAT:
        case REAL:
        case BIGINT:
            return value instanceof BigDecimal;
        case DATE:
            return value instanceof DateString;
        case TIME:
            return value instanceof TimeString;
        case TIME_WITH_LOCAL_TIME_ZONE:
            return value instanceof TimeString;
        case TIMESTAMP:
            return value instanceof TimestampString;
        case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
            return value instanceof TimestampString;
        case INTERVAL_YEAR:
        case INTERVAL_YEAR_MONTH:
        case INTERVAL_MONTH:
        case INTERVAL_DAY:
        case INTERVAL_DAY_HOUR:
        case INTERVAL_DAY_MINUTE:
        case INTERVAL_DAY_SECOND:
        case INTERVAL_HOUR:
        case INTERVAL_HOUR_MINUTE:
        case INTERVAL_HOUR_SECOND:
        case INTERVAL_MINUTE:
        case INTERVAL_MINUTE_SECOND:
        case INTERVAL_SECOND:
            // The value of a DAY-TIME interval (whatever the start and end units,
            // even say HOUR TO MINUTE) is in milliseconds (perhaps fractional
            // milliseconds). The value of a YEAR-MONTH interval is in months.
            return value instanceof BigDecimal;
        case VARBINARY: // not allowed -- use Binary
            if (strict) {
                throw Util.unexpected(typeName);
            }
            // fall through
        case BINARY:
            return value instanceof ByteString;
        case VARCHAR: // not allowed -- use Char
            if (strict) {
                throw Util.unexpected(typeName);
            }
            // fall through
        case CHAR:
            // A SqlLiteral's charset and collation are optional; not so a
            // RexLiteral.
            return (value instanceof NlsString) && (((NlsString) value).getCharset() != null)
                    && (((NlsString) value).getCollation() != null);
        case SYMBOL:
            return value instanceof Enum;
        case ROW:
        case MULTISET:
            return value instanceof List;
        case ANY:
            // Literal of type ANY is not legal. "CAST(2 AS ANY)" remains
            // an integer literal surrounded by a cast function.
            return false;
        default:
            throw Util.unexpected(typeName);
        }
    }

    public static boolean booleanValue(RexNode node) {
        return (Boolean) ((RexLiteral) node).getValue();
    }

}

// End RexLiteral.java
