// Generated from org\apache\drill\exec\record\metadata\schema\parser\SchemaParser.g4 by ANTLR 4.7.1
package org.apache.drill.exec.record.metadata.schema.parser;

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

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SchemaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SchemaParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SchemaParser#schema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema(SchemaParser.SchemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumns(SchemaParser.ColumnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#column_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_def(SchemaParser.Column_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn(SchemaParser.ColumnContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#primitive_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitive_column(SchemaParser.Primitive_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#simple_array_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_array_column(SchemaParser.Simple_array_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#struct_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_column(SchemaParser.Struct_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#complex_array_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplex_array_column(SchemaParser.Complex_array_columnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id}
	 * labeled alternative in {@link SchemaParser#column_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(SchemaParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code quoted_id}
	 * labeled alternative in {@link SchemaParser#column_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuoted_id(SchemaParser.Quoted_idContext ctx);
	/**
	 * Visit a parse tree produced by the {@code int}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt(SchemaParser.IntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bigint}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBigint(SchemaParser.BigintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code float}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat(SchemaParser.FloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code double}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDouble(SchemaParser.DoubleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decimal}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimal(SchemaParser.DecimalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(SchemaParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varchar}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarchar(SchemaParser.VarcharContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binary}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary(SchemaParser.BinaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code time}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTime(SchemaParser.TimeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code date}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate(SchemaParser.DateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code timestamp}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestamp(SchemaParser.TimestampContext ctx);
	/**
	 * Visit a parse tree produced by the {@code interval_year}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_year(SchemaParser.Interval_yearContext ctx);
	/**
	 * Visit a parse tree produced by the {@code interval_day}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval_day(SchemaParser.Interval_dayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code interval}
	 * labeled alternative in {@link SchemaParser#simple_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval(SchemaParser.IntervalContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#complex_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplex_type(SchemaParser.Complex_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#simple_array_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_array_type(SchemaParser.Simple_array_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#complex_array_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplex_array_type(SchemaParser.Complex_array_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#struct_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_type(SchemaParser.Struct_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#nullability}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullability(SchemaParser.NullabilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#format_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormat_value(SchemaParser.Format_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#default_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_value(SchemaParser.Default_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#property_values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_values(SchemaParser.Property_valuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#property_pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_pair(SchemaParser.Property_pairContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#string_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_value(SchemaParser.String_valueContext ctx);
}