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
package org.apache.calcite.rex.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.calcite.config.CalciteSystemProperty;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexBiVisitor;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexDigestIncludeType;
import org.apache.calcite.rex.RexInterpreter;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexVisitor;
import org.apache.calcite.sql.SqlCollation;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.parser.SqlParserUtil;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.ByteString;
import org.apache.calcite.util.CompositeList;
import org.apache.calcite.util.ConversionUtil;
import org.apache.calcite.util.DateString;
import org.apache.calcite.util.DateTimeUtils;
import org.apache.calcite.util.Litmus;
import org.apache.calcite.util.NlsString;
import org.apache.calcite.util.TimeString;
import org.apache.calcite.util.TimeUnit;
import org.apache.calcite.util.TimestampString;
import org.apache.calcite.util.Util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

@SuppressWarnings("rawtypes")
public class RexLiteralImpl extends RexNodeBase implements RexLiteral {
    // ~ Instance fields --------------------------------------------------------

    /**
     * The value of this literal. Must be consistent with its type, as per
     * {@link #valueMatchesType}. For example, you can't store an
     * {@link Integer} value here just because you feel like it -- all numbers are
     * represented by a {@link BigDecimal}. But since this field is private, it
     * doesn't really matter how the values are stored.
     */
    private final Comparable value;

    // /**
    // * The real type of this literal, as reported by {@link #getType}.
    // */
    // private final RelDataType type;

    // TODO jvs 26-May-2006: Use SqlTypeFamily instead; it exists
    // for exactly this purpose (to avoid the confusion which results
    // from overloading SqlTypeName).
    /**
     * An indication of the broad type of this literal -- even if its type isn't
     * a SQL type. Sometimes this will be different than the SQL type; for
     * example, all exact numbers, including integers have typeName
     * {@link SqlTypeName#DECIMAL}. See {@link #valueMatchesType} for the
     * definitive story.
     */
    private final SqlTypeName typeName;

    private static final ImmutableList<TimeUnit> TIME_UNITS = ImmutableList.copyOf(TimeUnit.values());

    // ~ Constructors -----------------------------------------------------------

    /**
     * Creates a <code>RexLiteral</code>.
     */
    RexLiteralImpl(Comparable value, RelDataType type, SqlTypeName typeName) {
        super(type);
        this.value = value;
        this.typeName = Objects.requireNonNull(typeName);
        Preconditions.checkArgument(RexLiteral.valueMatchesType(value, typeName, true));
        Preconditions.checkArgument((value == null) == type.isNullable());
        Preconditions.checkArgument(typeName != SqlTypeName.ANY);
        this.digest = computeDigest(RexDigestIncludeType.OPTIONAL);
    }

    // ~ Methods ----------------------------------------------------------------

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
    @Override
    public final String computeDigest(RexDigestIncludeType includeType) {
        if (includeType == RexDigestIncludeType.OPTIONAL) {
            if (digest != null) {
                // digest is initialized with OPTIONAL, so cached value matches for
                // includeType=OPTIONAL as well
                return digest;
            }
            // Compute we should include the type or not
            includeType = digestIncludesType();
        } else if (digest != null && includeType == digestIncludesType()) {
            // The digest is always computed with includeType=OPTIONAL
            // If it happened to omit the type, we want to optimize computeDigest(NO_TYPE) as well
            // If the digest includes the type, we want to optimize computeDigest(ALWAYS)
            return digest;
        }

        return toJavaString(value, typeName, type, includeType);
    }

    /**
     * Returns true if {@link RexDigestIncludeType#OPTIONAL} digest would include data type.
     *
     * @see RexCall#computeDigest(boolean)
     * @return true if {@link RexDigestIncludeType#OPTIONAL} digest would include data type
     */
    @Override
    public RexDigestIncludeType digestIncludesType() {
        return shouldIncludeType(value, type);
    }

    private static String toJavaString(Comparable value, SqlTypeName typeName, RelDataType type,
            RexDigestIncludeType includeType) {
        assert includeType != RexDigestIncludeType.OPTIONAL : "toJavaString must not be called with includeType=OPTIONAL";
        String fullTypeString = type.getFullTypeString();
        if (value == null) {
            return includeType == RexDigestIncludeType.NO_TYPE ? "null" : "null:" + fullTypeString;
        }
        StringBuilder sb = new StringBuilder();
        appendAsJava(value, sb, typeName, false, includeType);

        if (includeType != RexDigestIncludeType.NO_TYPE) {
            sb.append(':');
            if (!fullTypeString.endsWith("NOT NULL")) {
                sb.append(fullTypeString);
            } else {
                // Trim " NOT NULL". Apparently, the literal is not null, so we just print the data type.
                sb.append(fullTypeString, 0, fullTypeString.length() - 9);
            }
        }
        return sb.toString();
    }

    /**
     * Computes if data type can be omitted from the digset.
     * <p>For instance, {@code 1:BIGINT} has to keep data type while {@code 1:INT}
     * should be represented as just {@code 1}.
     *
     * <p>Implementation assumption: this method should be fast. In fact might call
     * {@link NlsString#getValue()} which could decode the string, however we rely on the cache there.
     *
     * @see RexLiteral#computeDigest(RexDigestIncludeType)
     * @param value value of the literal
     * @param type type of the literal
     * @return NO_TYPE when type can be omitted, ALWAYS otherwise
     */
    private static RexDigestIncludeType shouldIncludeType(Comparable value, RelDataType type) {
        if (type.isNullable()) {
            // This means "null literal", so we require a type for it
            // There might be exceptions like AND(null, true) which are handled by RexCall#computeDigest
            return RexDigestIncludeType.ALWAYS;
        }
        // The variable here simplifies debugging (one can set a breakpoint at return)
        // final ensures we set the value in all the branches, and it ensures the value is set just once
        final RexDigestIncludeType includeType;
        if (type.getSqlTypeName() == SqlTypeName.BOOLEAN || type.getSqlTypeName() == SqlTypeName.INTEGER
                || type.getSqlTypeName() == SqlTypeName.SYMBOL) {
            // We don't want false:BOOLEAN NOT NULL, so we don't print type information for
            // non-nullable BOOLEAN and INTEGER
            includeType = RexDigestIncludeType.NO_TYPE;
        } else if (type.getSqlTypeName() == SqlTypeName.CHAR && value instanceof NlsString) {
            NlsString nlsString = (NlsString) value;

            // Ignore type information for 'Bar':CHAR(3)
            if (((nlsString.getCharset() != null && type.getCharset().equals(nlsString.getCharset()))
                    || (nlsString.getCharset() == null && SqlCollation.IMPLICIT.getCharset().equals(type.getCharset())))
                    && nlsString.getCollation().equals(type.getCollation())
                    && ((NlsString) value).getValue().length() == type.getPrecision()) {
                includeType = RexDigestIncludeType.NO_TYPE;
            } else {
                includeType = RexDigestIncludeType.ALWAYS;
            }
        } else if (type.getPrecision() == 0 && (type.getSqlTypeName() == SqlTypeName.TIME
                || type.getSqlTypeName() == SqlTypeName.TIMESTAMP || type.getSqlTypeName() == SqlTypeName.DATE)) {
            // Ignore type information for '12:23:20':TIME(0)
            // Note that '12:23:20':TIME WITH LOCAL TIME ZONE
            includeType = RexDigestIncludeType.NO_TYPE;
        } else {
            includeType = RexDigestIncludeType.ALWAYS;
        }
        return includeType;
    }

    /** Returns whether a value is valid as a constant value, using the same
     * criteria as {@link #valueMatchesType}. */
    public static boolean validConstant(Object o, Litmus litmus) {
        if (o == null || o instanceof BigDecimal || o instanceof NlsString || o instanceof ByteString) {
            return litmus.succeed();
        } else if (o instanceof List) {
            List list = (List) o;
            for (Object o1 : list) {
                if (!validConstant(o1, litmus)) {
                    return litmus.fail("not a constant: {}", o1);
                }
            }
            return litmus.succeed();
        } else if (o instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<Object, Object> map = (Map) o;
            for (Map.Entry entry : map.entrySet()) {
                if (!validConstant(entry.getKey(), litmus)) {
                    return litmus.fail("not a constant: {}", entry.getKey());
                }
                if (!validConstant(entry.getValue(), litmus)) {
                    return litmus.fail("not a constant: {}", entry.getValue());
                }
            }
            return litmus.succeed();
        } else {
            return litmus.fail("not a constant: {}", o);
        }
    }

    /** Returns a list of the time units covered by an interval type such
     * as HOUR TO SECOND. Adds MILLISECOND if the end is SECOND, to deal with
     * fractional seconds. */
    private static List<TimeUnit> getTimeUnits(SqlTypeName typeName) {
        final TimeUnit start = typeName.getStartUnit();
        final TimeUnit end = typeName.getEndUnit();
        final ImmutableList<TimeUnit> list = TIME_UNITS.subList(start.ordinal(), end.ordinal() + 1);
        if (end == TimeUnit.SECOND) {
            return CompositeList.of(list, ImmutableList.of(TimeUnit.MILLISECOND));
        }
        return list;
    }

    private String intervalString(BigDecimal v) {
        final List<TimeUnit> timeUnits = getTimeUnits(type.getSqlTypeName());
        final StringBuilder b = new StringBuilder();
        for (TimeUnit timeUnit : timeUnits) {
            final BigDecimal[] result = v.divideAndRemainder(timeUnit.multiplier);
            if (b.length() > 0) {
                b.append(timeUnit.separator);
            }
            final int width = b.length() == 0 ? -1 : width(timeUnit); // don't pad 1st
            pad(b, result[0].toString(), width);
            v = result[1];
        }
        if (Util.last(timeUnits) == TimeUnit.MILLISECOND) {
            while (b.toString().matches(".*\\.[0-9]*0")) {
                if (b.toString().endsWith(".0")) {
                    b.setLength(b.length() - 2); // remove ".0"
                } else {
                    b.setLength(b.length() - 1); // remove "0"
                }
            }
        }
        return b.toString();
    }

    private static void pad(StringBuilder b, String s, int width) {
        if (width >= 0) {
            for (int i = s.length(); i < width; i++) {
                b.append('0');
            }
        }
        b.append(s);
    }

    private static int width(TimeUnit timeUnit) {
        switch (timeUnit) {
        case MILLISECOND:
            return 3;
        case HOUR:
        case MINUTE:
        case SECOND:
            return 2;
        default:
            return -1;
        }
    }

    /**
     * Prints the value this literal as a Java string constant.
     */
    @Override
    public void printAsJava(PrintWriter pw) {
        appendAsJava(value, pw, typeName, true, RexDigestIncludeType.NO_TYPE);
    }

    /**
     * Appends the specified value in the provided destination as a Java string. The value must be
     * consistent with the type, as per {@link #valueMatchesType}.
     *
     * <p>Typical return values:</p>
     *
     * <ul>
     * <li>true</li>
     * <li>null</li>
     * <li>"Hello, world!"</li>
     * <li>1.25</li>
     * <li>1234ABCD</li>
     * </ul>
     *
     * <p>The destination where the value is appended must not incur I/O operations. This method is
     * not meant to be used for writing the values to permanent storage.</p>
     *
     * @param value a value to be appended to the provided destination as a Java string
     * @param destination a destination where to append the specified value
     * @param typeName a type name to be used for the transformation of the value to a Java string
     * @param includeType an indicator whether to include the data type in the Java representation
     * @throws IllegalStateException if the appending to the destination <code>Appendable</code> fails
     *         due to I/O
     */
    private static void appendAsJava(Comparable value, Appendable destination, SqlTypeName typeName, boolean java,
            RexDigestIncludeType includeType) {
        try {
            switch (typeName) {
            case CHAR:
                NlsString nlsString = (NlsString) value;
                if (java) {
                    Util.printJavaString(destination, nlsString.getValue(), true);
                } else {
                    boolean includeCharset = (nlsString.getCharsetName() != null)
                            && !nlsString.getCharsetName().equals(CalciteSystemProperty.DEFAULT_CHARSET.value());
                    destination.append(nlsString.asSql(includeCharset, false));
                }
                break;
            case BOOLEAN:
                assert value instanceof Boolean;
                destination.append(value.toString());
                break;
            case DECIMAL:
                assert value instanceof BigDecimal;
                destination.append(value.toString());
                break;
            case DOUBLE:
                assert value instanceof BigDecimal;
                destination.append(Util.toScientificNotation((BigDecimal) value));
                break;
            case BIGINT:
                assert value instanceof BigDecimal;
                long narrowLong = ((BigDecimal) value).longValue();
                destination.append(String.valueOf(narrowLong));
                destination.append('L');
                break;
            case BINARY:
                assert value instanceof ByteString;
                destination.append("X'");
                destination.append(((ByteString) value).toString(16));
                destination.append("'");
                break;
            case NULL:
                assert value == null;
                destination.append("null");
                break;
            case SYMBOL:
                assert value instanceof Enum;
                destination.append("FLAG(");
                destination.append(value.toString());
                destination.append(")");
                break;
            case DATE:
                assert value instanceof DateString;
                destination.append(value.toString());
                break;
            case TIME:
                assert value instanceof TimeString;
                destination.append(value.toString());
                break;
            case TIME_WITH_LOCAL_TIME_ZONE:
                assert value instanceof TimeString;
                destination.append(value.toString());
                break;
            case TIMESTAMP:
                assert value instanceof TimestampString;
                destination.append(value.toString());
                break;
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                assert value instanceof TimestampString;
                destination.append(value.toString());
                break;
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
                assert value instanceof BigDecimal;
                destination.append(value.toString());
                break;
            case MULTISET:
            case ROW:
                @SuppressWarnings("unchecked")
                final List<RexLiteral> list = (List) value;
                destination.append((new AbstractList<String>() {
                    @Override
                    public String get(int index) {
                        return list.get(index).computeDigest(includeType);
                    }

                    @Override
                    public int size() {
                        return list.size();
                    }
                }).toString());
                break;
            default:
                assert RexLiteral.valueMatchesType(value, typeName, true);
                throw Util.needToImplement(typeName);
            }
        } catch (IOException e) {
            throw new IllegalStateException("The destination Appendable should not incur I/O.", e);
        }
    }

    /**
     * Converts a Jdbc string into a RexLiteral. This method accepts a string,
     * as returned by the Jdbc method ResultSet.getString(), and restores the
     * string into an equivalent RexLiteral. It allows one to use Jdbc strings
     * as a common format for data.
     *
     * <p>If a null literal is provided, then a null pointer will be returned.
     *
     * @param type     data type of literal to be read
     * @param typeName type family of literal
     * @param literal  the (non-SQL encoded) string representation, as returned
     *                 by the Jdbc call to return a column as a string
     * @return a typed RexLiteral, or null
     */
    public static RexLiteral fromJdbcString(RelDataType type, SqlTypeName typeName, String literal) {
        if (literal == null) {
            return null;
        }

        switch (typeName) {
        case CHAR:
            Charset charset = type.getCharset();
            SqlCollation collation = type.getCollation();
            NlsString str = new NlsString(literal, charset.name(), collation);
            return new RexLiteralImpl(str, type, typeName);
        case BOOLEAN:
            boolean b = ConversionUtil.toBoolean(literal);
            return new RexLiteralImpl(b, type, typeName);
        case DECIMAL:
        case DOUBLE:
            BigDecimal d = new BigDecimal(literal);
            return new RexLiteralImpl(d, type, typeName);
        case BINARY:
            byte[] bytes = ConversionUtil.toByteArrayFromString(literal, 16);
            return new RexLiteralImpl(new ByteString(bytes), type, typeName);
        case NULL:
            return new RexLiteralImpl(null, type, typeName);
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
            long millis = SqlParserUtil.intervalToMillis(literal, type.getIntervalQualifier());
            return new RexLiteralImpl(BigDecimal.valueOf(millis), type, typeName);
        case INTERVAL_YEAR:
        case INTERVAL_YEAR_MONTH:
        case INTERVAL_MONTH:
            long months = SqlParserUtil.intervalToMonths(literal, type.getIntervalQualifier());
            return new RexLiteralImpl(BigDecimal.valueOf(months), type, typeName);
        case DATE:
        case TIME:
        case TIMESTAMP:
            String format = getCalendarFormat(typeName);
            TimeZone tz = DateTimeUtils.UTC_ZONE;
            final Comparable v;
            switch (typeName) {
            case DATE:
                final Calendar cal = DateTimeUtils.parseDateFormat(literal, new SimpleDateFormat(format, Locale.ROOT),
                        tz);
                if (cal == null) {
                    throw new AssertionError("fromJdbcString: invalid date/time value '" + literal + "'");
                }
                v = DateString.fromCalendarFields(cal);
                break;
            default:
                // Allow fractional seconds for times and timestamps
                assert format != null;
                final DateTimeUtils.PrecisionTime ts = DateTimeUtils.parsePrecisionDateTimeLiteral(literal,
                        new SimpleDateFormat(format, Locale.ROOT), tz, -1);
                if (ts == null) {
                    throw new AssertionError("fromJdbcString: invalid date/time value '" + literal + "'");
                }
                switch (typeName) {
                case TIMESTAMP:
                    v = TimestampString.fromCalendarFields(ts.getCalendar()).withFraction(ts.getFraction());
                    break;
                case TIME:
                    v = TimeString.fromCalendarFields(ts.getCalendar()).withFraction(ts.getFraction());
                    break;
                default:
                    throw new AssertionError();
                }
            }
            return new RexLiteralImpl(v, type, typeName);

        case SYMBOL:
            // Symbols are for internal use
        default:
            throw new AssertionError("fromJdbcString: unsupported type");
        }
    }

    private static String getCalendarFormat(SqlTypeName typeName) {
        switch (typeName) {
        case DATE:
            return DateTimeUtils.DATE_FORMAT_STRING;
        case TIME:
            return DateTimeUtils.TIME_FORMAT_STRING;
        case TIMESTAMP:
            return DateTimeUtils.TIMESTAMP_FORMAT_STRING;
        default:
            throw new AssertionError("getCalendarFormat: unknown type");
        }
    }

    @Override
    public SqlTypeName getTypeName() {
        return typeName;
    }

    @Override
    public RelDataType getType() {
        return type;
    }

    @Override
    public SqlKind getKind() {
        return SqlKind.LITERAL;
    }

    /**
     * Returns whether this literal's value is null.
     */
    @Override
    public boolean isNull() {
        return value == null;
    }

    /**
     * Returns the value of this literal.
     *
     * <p>For backwards compatibility, returns DATE. TIME and TIMESTAMP as a
     * {@link Calendar} value in UTC time zone.
     */
    @Override
    public Comparable getValue() {
        assert RexLiteral.valueMatchesType(value, typeName, true) : value;
        if (value == null) {
            return null;
        }
        switch (typeName) {
        case TIME:
        case DATE:
        case TIMESTAMP:
            return getValueAs(Calendar.class);
        default:
            return value;
        }
    }

    /**
     * Returns the value of this literal, in the form that the calculator
     * program builder wants it.
     */
    @Override
    public Object getValue2() {
        if (value == null) {
            return null;
        }
        switch (typeName) {
        case CHAR:
            return getValueAs(String.class);
        case DECIMAL:
        case TIMESTAMP:
        case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
            return getValueAs(Long.class);
        case DATE:
        case TIME:
        case TIME_WITH_LOCAL_TIME_ZONE:
            return getValueAs(Integer.class);
        default:
            return value;
        }
    }

    /**
     * Returns the value of this literal, in the form that the rex-to-lix
     * translator wants it.
     */
    @Override
    public Object getValue3() {
        if (value == null) {
            return null;
        }
        switch (typeName) {
        case DECIMAL:
            assert value instanceof BigDecimal;
            return value;
        default:
            return getValue2();
        }
    }

    /**
     * Returns the value of this literal, in the form that {@link RexInterpreter}
     * wants it.
     */
    @Override
    public Comparable getValue4() {
        if (value == null) {
            return null;
        }
        switch (typeName) {
        case TIMESTAMP:
        case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
            return getValueAs(Long.class);
        case DATE:
        case TIME:
        case TIME_WITH_LOCAL_TIME_ZONE:
            return getValueAs(Integer.class);
        default:
            return value;
        }
    }

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
    @Override
    public <T> T getValueAs(Class<T> clazz) {
        if (value == null || clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        switch (typeName) {
        case BINARY:
            if (clazz == byte[].class) {
                return clazz.cast(((ByteString) value).getBytes());
            }
            break;
        case CHAR:
            if (clazz == String.class) {
                return clazz.cast(((NlsString) value).getValue());
            } else if (clazz == Character.class) {
                return clazz.cast(((NlsString) value).getValue().charAt(0));
            }
            break;
        case VARCHAR:
            if (clazz == String.class) {
                return clazz.cast(((NlsString) value).getValue());
            }
            break;
        case DECIMAL:
            if (clazz == Long.class) {
                return clazz.cast(((BigDecimal) value).unscaledValue().longValue());
            }
            // fall through
        case BIGINT:
        case INTEGER:
        case SMALLINT:
        case TINYINT:
        case DOUBLE:
        case REAL:
        case FLOAT:
            if (clazz == Long.class) {
                return clazz.cast(((BigDecimal) value).longValue());
            } else if (clazz == Integer.class) {
                return clazz.cast(((BigDecimal) value).intValue());
            } else if (clazz == Short.class) {
                return clazz.cast(((BigDecimal) value).shortValue());
            } else if (clazz == Byte.class) {
                return clazz.cast(((BigDecimal) value).byteValue());
            } else if (clazz == Double.class) {
                return clazz.cast(((BigDecimal) value).doubleValue());
            } else if (clazz == Float.class) {
                return clazz.cast(((BigDecimal) value).floatValue());
            }
            break;
        case DATE:
            if (clazz == Integer.class) {
                return clazz.cast(((DateString) value).getDaysSinceEpoch());
            } else if (clazz == Calendar.class) {
                return clazz.cast(((DateString) value).toCalendar());
            }
            break;
        case TIME:
            if (clazz == Integer.class) {
                return clazz.cast(((TimeString) value).getMillisOfDay());
            } else if (clazz == Calendar.class) {
                // Note: Nanos are ignored
                return clazz.cast(((TimeString) value).toCalendar());
            }
            break;
        case TIME_WITH_LOCAL_TIME_ZONE:
            if (clazz == Integer.class) {
                // Milliseconds since 1970-01-01 00:00:00
                return clazz.cast(((TimeString) value).getMillisOfDay());
            }
            break;
        case TIMESTAMP:
            if (clazz == Long.class) {
                // Milliseconds since 1970-01-01 00:00:00
                return clazz.cast(((TimestampString) value).getMillisSinceEpoch());
            } else if (clazz == Calendar.class) {
                // Note: Nanos are ignored
                return clazz.cast(((TimestampString) value).toCalendar());
            }
            break;
        case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
            if (clazz == Long.class) {
                // Milliseconds since 1970-01-01 00:00:00
                return clazz.cast(((TimestampString) value).getMillisSinceEpoch());
            } else if (clazz == Calendar.class) {
                // Note: Nanos are ignored
                return clazz.cast(((TimestampString) value).toCalendar());
            }
            break;
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
            if (clazz == Integer.class) {
                return clazz.cast(((BigDecimal) value).intValue());
            } else if (clazz == Long.class) {
                return clazz.cast(((BigDecimal) value).longValue());
            } else if (clazz == String.class) {
                return clazz.cast(intervalString(getValueAs(BigDecimal.class).abs()));
            } else if (clazz == Boolean.class) {
                // return whether negative
                return clazz.cast(getValueAs(BigDecimal.class).signum() < 0);
            }
            break;
        }
        throw new AssertionError("cannot convert " + typeName + " literal to " + clazz);
    }

    @Override
    public boolean isAlwaysTrue() {
        if (typeName != SqlTypeName.BOOLEAN) {
            return false;
        }
        return RexLiteral.booleanValue(this);
    }

    @Override
    public boolean isAlwaysFalse() {
        if (typeName != SqlTypeName.BOOLEAN) {
            return false;
        }
        return !RexLiteral.booleanValue(this);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RexLiteralImpl) && equals(((RexLiteralImpl) obj).value, value)
                && equals(((RexLiteralImpl) obj).type, type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    private static boolean equals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    @Override
    public <R> R accept(RexVisitor<R> visitor) {
        return visitor.visitLiteral(this);
    }

    @Override
    public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
        return visitor.visitLiteral(this, arg);
    }
}
