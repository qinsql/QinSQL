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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SchemaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INT=1, INTEGER=2, BIGINT=3, FLOAT=4, DOUBLE=5, DEC=6, DECIMAL=7, NUMERIC=8, 
		BOOLEAN=9, CHAR=10, CHARACTER=11, VARYING=12, VARCHAR=13, BINARY=14, VARBINARY=15, 
		TIME=16, DATE=17, TIMESTAMP=18, INTERVAL=19, YEAR=20, MONTH=21, DAY=22, 
		HOUR=23, MINUTE=24, SECOND=25, MAP=26, STRUCT=27, ARRAY=28, COMMA=29, 
		REVERSE_QUOTE=30, LEFT_PAREN=31, RIGHT_PAREN=32, LEFT_ANGLE_BRACKET=33, 
		RIGHT_ANGLE_BRACKET=34, SINGLE_QUOTE=35, DOUBLE_QUOTE=36, LEFT_BRACE=37, 
		RIGHT_BRACE=38, EQUALS_SIGN=39, NOT=40, NULL=41, AS=42, FORMAT=43, DEFAULT=44, 
		PROPERTIES=45, NUMBER=46, ID=47, QUOTED_ID=48, SINGLE_QUOTED_STRING=49, 
		DOUBLE_QUOTED_STRING=50, LINE_COMMENT=51, BLOCK_COMMENT=52, SPACE=53;
	public static final int
		RULE_schema = 0, RULE_columns = 1, RULE_column_def = 2, RULE_column = 3, 
		RULE_primitive_column = 4, RULE_simple_array_column = 5, RULE_struct_column = 6, 
		RULE_complex_array_column = 7, RULE_column_id = 8, RULE_simple_type = 9, 
		RULE_complex_type = 10, RULE_simple_array_type = 11, RULE_complex_array_type = 12, 
		RULE_struct_type = 13, RULE_nullability = 14, RULE_format_value = 15, 
		RULE_default_value = 16, RULE_property_values = 17, RULE_property_pair = 18, 
		RULE_string_value = 19;
	public static final String[] ruleNames = {
		"schema", "columns", "column_def", "column", "primitive_column", "simple_array_column", 
		"struct_column", "complex_array_column", "column_id", "simple_type", "complex_type", 
		"simple_array_type", "complex_array_type", "struct_type", "nullability", 
		"format_value", "default_value", "property_values", "property_pair", "string_value"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'INT'", "'INTEGER'", "'BIGINT'", "'FLOAT'", "'DOUBLE'", "'DEC'", 
		"'DECIMAL'", "'NUMERIC'", "'BOOLEAN'", "'CHAR'", "'CHARACTER'", "'VARYING'", 
		"'VARCHAR'", "'BINARY'", "'VARBINARY'", "'TIME'", "'DATE'", "'TIMESTAMP'", 
		"'INTERVAL'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", 
		"'MAP'", "'STRUCT'", "'ARRAY'", "','", "'`'", "'('", "')'", "'<'", "'>'", 
		"'''", "'\"'", "'{'", "'}'", "'='", "'NOT'", "'NULL'", "'AS'", "'FORMAT'", 
		"'DEFAULT'", "'PROPERTIES'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "INT", "INTEGER", "BIGINT", "FLOAT", "DOUBLE", "DEC", "DECIMAL", 
		"NUMERIC", "BOOLEAN", "CHAR", "CHARACTER", "VARYING", "VARCHAR", "BINARY", 
		"VARBINARY", "TIME", "DATE", "TIMESTAMP", "INTERVAL", "YEAR", "MONTH", 
		"DAY", "HOUR", "MINUTE", "SECOND", "MAP", "STRUCT", "ARRAY", "COMMA", 
		"REVERSE_QUOTE", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_ANGLE_BRACKET", "RIGHT_ANGLE_BRACKET", 
		"SINGLE_QUOTE", "DOUBLE_QUOTE", "LEFT_BRACE", "RIGHT_BRACE", "EQUALS_SIGN", 
		"NOT", "NULL", "AS", "FORMAT", "DEFAULT", "PROPERTIES", "NUMBER", "ID", 
		"QUOTED_ID", "SINGLE_QUOTED_STRING", "DOUBLE_QUOTED_STRING", "LINE_COMMENT", 
		"BLOCK_COMMENT", "SPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SchemaParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SchemaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class SchemaContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SchemaParser.EOF, 0); }
		public ColumnsContext columns() {
			return getRuleContext(ColumnsContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(SchemaParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(SchemaParser.RIGHT_PAREN, 0); }
		public SchemaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schema; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitSchema(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaContext schema() throws RecognitionException {
		SchemaContext _localctx = new SchemaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_schema);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
			case QUOTED_ID:
				{
				setState(40);
				columns();
				}
				break;
			case LEFT_PAREN:
				{
				setState(41);
				match(LEFT_PAREN);
				setState(42);
				columns();
				setState(43);
				match(RIGHT_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(47);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnsContext extends ParserRuleContext {
		public List<Column_defContext> column_def() {
			return getRuleContexts(Column_defContext.class);
		}
		public Column_defContext column_def(int i) {
			return getRuleContext(Column_defContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SchemaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SchemaParser.COMMA, i);
		}
		public ColumnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columns; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitColumns(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnsContext columns() throws RecognitionException {
		ColumnsContext _localctx = new ColumnsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_columns);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			column_def();
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(50);
				match(COMMA);
				setState(51);
				column_def();
				}
				}
				setState(56);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Column_defContext extends ParserRuleContext {
		public ColumnContext column() {
			return getRuleContext(ColumnContext.class,0);
		}
		public Property_valuesContext property_values() {
			return getRuleContext(Property_valuesContext.class,0);
		}
		public Column_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_def; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitColumn_def(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_defContext column_def() throws RecognitionException {
		Column_defContext _localctx = new Column_defContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_column_def);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			column();
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PROPERTIES) {
				{
				setState(58);
				property_values();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnContext extends ParserRuleContext {
		public Primitive_columnContext primitive_column() {
			return getRuleContext(Primitive_columnContext.class,0);
		}
		public Struct_columnContext struct_column() {
			return getRuleContext(Struct_columnContext.class,0);
		}
		public Simple_array_columnContext simple_array_column() {
			return getRuleContext(Simple_array_columnContext.class,0);
		}
		public Complex_array_columnContext complex_array_column() {
			return getRuleContext(Complex_array_columnContext.class,0);
		}
		public ColumnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitColumn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnContext column() throws RecognitionException {
		ColumnContext _localctx = new ColumnContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_column);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(61);
				primitive_column();
				}
				break;
			case 2:
				{
				setState(62);
				struct_column();
				}
				break;
			case 3:
				{
				setState(63);
				simple_array_column();
				}
				break;
			case 4:
				{
				setState(64);
				complex_array_column();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Primitive_columnContext extends ParserRuleContext {
		public Column_idContext column_id() {
			return getRuleContext(Column_idContext.class,0);
		}
		public Simple_typeContext simple_type() {
			return getRuleContext(Simple_typeContext.class,0);
		}
		public NullabilityContext nullability() {
			return getRuleContext(NullabilityContext.class,0);
		}
		public Format_valueContext format_value() {
			return getRuleContext(Format_valueContext.class,0);
		}
		public Default_valueContext default_value() {
			return getRuleContext(Default_valueContext.class,0);
		}
		public Primitive_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitive_column; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitive_column(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Primitive_columnContext primitive_column() throws RecognitionException {
		Primitive_columnContext _localctx = new Primitive_columnContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_primitive_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			column_id();
			setState(68);
			simple_type();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(69);
				nullability();
				}
			}

			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FORMAT) {
				{
				setState(72);
				format_value();
				}
			}

			setState(76);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT) {
				{
				setState(75);
				default_value();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_array_columnContext extends ParserRuleContext {
		public Column_idContext column_id() {
			return getRuleContext(Column_idContext.class,0);
		}
		public Simple_array_typeContext simple_array_type() {
			return getRuleContext(Simple_array_typeContext.class,0);
		}
		public NullabilityContext nullability() {
			return getRuleContext(NullabilityContext.class,0);
		}
		public Simple_array_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_array_column; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitSimple_array_column(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_array_columnContext simple_array_column() throws RecognitionException {
		Simple_array_columnContext _localctx = new Simple_array_columnContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_simple_array_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			column_id();
			setState(79);
			simple_array_type();
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(80);
				nullability();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_columnContext extends ParserRuleContext {
		public Column_idContext column_id() {
			return getRuleContext(Column_idContext.class,0);
		}
		public Struct_typeContext struct_type() {
			return getRuleContext(Struct_typeContext.class,0);
		}
		public NullabilityContext nullability() {
			return getRuleContext(NullabilityContext.class,0);
		}
		public Struct_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_column; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitStruct_column(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Struct_columnContext struct_column() throws RecognitionException {
		Struct_columnContext _localctx = new Struct_columnContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_struct_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			column_id();
			setState(84);
			struct_type();
			setState(86);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(85);
				nullability();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Complex_array_columnContext extends ParserRuleContext {
		public Column_idContext column_id() {
			return getRuleContext(Column_idContext.class,0);
		}
		public Complex_array_typeContext complex_array_type() {
			return getRuleContext(Complex_array_typeContext.class,0);
		}
		public NullabilityContext nullability() {
			return getRuleContext(NullabilityContext.class,0);
		}
		public Complex_array_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complex_array_column; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitComplex_array_column(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Complex_array_columnContext complex_array_column() throws RecognitionException {
		Complex_array_columnContext _localctx = new Complex_array_columnContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_complex_array_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			column_id();
			setState(89);
			complex_array_type();
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(90);
				nullability();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Column_idContext extends ParserRuleContext {
		public Column_idContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_id; }
	 
		public Column_idContext() { }
		public void copyFrom(Column_idContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Quoted_idContext extends Column_idContext {
		public TerminalNode QUOTED_ID() { return getToken(SchemaParser.QUOTED_ID, 0); }
		public Quoted_idContext(Column_idContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitQuoted_id(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdContext extends Column_idContext {
		public TerminalNode ID() { return getToken(SchemaParser.ID, 0); }
		public IdContext(Column_idContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_idContext column_id() throws RecognitionException {
		Column_idContext _localctx = new Column_idContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_column_id);
		try {
			setState(95);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new IdContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(93);
				match(ID);
				}
				break;
			case QUOTED_ID:
				_localctx = new Quoted_idContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(94);
				match(QUOTED_ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_typeContext extends ParserRuleContext {
		public Simple_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_type; }
	 
		public Simple_typeContext() { }
		public void copyFrom(Simple_typeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DateContext extends Simple_typeContext {
		public TerminalNode DATE() { return getToken(SchemaParser.DATE, 0); }
		public DateContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitDate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DoubleContext extends Simple_typeContext {
		public TerminalNode DOUBLE() { return getToken(SchemaParser.DOUBLE, 0); }
		public DoubleContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitDouble(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarcharContext extends Simple_typeContext {
		public TerminalNode CHAR() { return getToken(SchemaParser.CHAR, 0); }
		public TerminalNode VARCHAR() { return getToken(SchemaParser.VARCHAR, 0); }
		public TerminalNode CHARACTER() { return getToken(SchemaParser.CHARACTER, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(SchemaParser.LEFT_PAREN, 0); }
		public TerminalNode NUMBER() { return getToken(SchemaParser.NUMBER, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(SchemaParser.RIGHT_PAREN, 0); }
		public TerminalNode VARYING() { return getToken(SchemaParser.VARYING, 0); }
		public VarcharContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitVarchar(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FloatContext extends Simple_typeContext {
		public TerminalNode FLOAT() { return getToken(SchemaParser.FLOAT, 0); }
		public FloatContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitFloat(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntContext extends Simple_typeContext {
		public TerminalNode INT() { return getToken(SchemaParser.INT, 0); }
		public TerminalNode INTEGER() { return getToken(SchemaParser.INTEGER, 0); }
		public IntContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitInt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Interval_dayContext extends Simple_typeContext {
		public TerminalNode INTERVAL() { return getToken(SchemaParser.INTERVAL, 0); }
		public TerminalNode DAY() { return getToken(SchemaParser.DAY, 0); }
		public TerminalNode HOUR() { return getToken(SchemaParser.HOUR, 0); }
		public TerminalNode MINUTE() { return getToken(SchemaParser.MINUTE, 0); }
		public TerminalNode SECOND() { return getToken(SchemaParser.SECOND, 0); }
		public Interval_dayContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitInterval_day(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanContext extends Simple_typeContext {
		public TerminalNode BOOLEAN() { return getToken(SchemaParser.BOOLEAN, 0); }
		public BooleanContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryContext extends Simple_typeContext {
		public TerminalNode BINARY() { return getToken(SchemaParser.BINARY, 0); }
		public TerminalNode VARBINARY() { return getToken(SchemaParser.VARBINARY, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(SchemaParser.LEFT_PAREN, 0); }
		public TerminalNode NUMBER() { return getToken(SchemaParser.NUMBER, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(SchemaParser.RIGHT_PAREN, 0); }
		public BinaryContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitBinary(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntervalContext extends Simple_typeContext {
		public TerminalNode INTERVAL() { return getToken(SchemaParser.INTERVAL, 0); }
		public IntervalContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitInterval(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TimeContext extends Simple_typeContext {
		public TerminalNode TIME() { return getToken(SchemaParser.TIME, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(SchemaParser.LEFT_PAREN, 0); }
		public TerminalNode NUMBER() { return getToken(SchemaParser.NUMBER, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(SchemaParser.RIGHT_PAREN, 0); }
		public TimeContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitTime(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Interval_yearContext extends Simple_typeContext {
		public TerminalNode INTERVAL() { return getToken(SchemaParser.INTERVAL, 0); }
		public TerminalNode YEAR() { return getToken(SchemaParser.YEAR, 0); }
		public TerminalNode MONTH() { return getToken(SchemaParser.MONTH, 0); }
		public Interval_yearContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitInterval_year(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DecimalContext extends Simple_typeContext {
		public TerminalNode DEC() { return getToken(SchemaParser.DEC, 0); }
		public TerminalNode DECIMAL() { return getToken(SchemaParser.DECIMAL, 0); }
		public TerminalNode NUMERIC() { return getToken(SchemaParser.NUMERIC, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(SchemaParser.LEFT_PAREN, 0); }
		public List<TerminalNode> NUMBER() { return getTokens(SchemaParser.NUMBER); }
		public TerminalNode NUMBER(int i) {
			return getToken(SchemaParser.NUMBER, i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(SchemaParser.RIGHT_PAREN, 0); }
		public TerminalNode COMMA() { return getToken(SchemaParser.COMMA, 0); }
		public DecimalContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitDecimal(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BigintContext extends Simple_typeContext {
		public TerminalNode BIGINT() { return getToken(SchemaParser.BIGINT, 0); }
		public BigintContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitBigint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TimestampContext extends Simple_typeContext {
		public TerminalNode TIMESTAMP() { return getToken(SchemaParser.TIMESTAMP, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(SchemaParser.LEFT_PAREN, 0); }
		public TerminalNode NUMBER() { return getToken(SchemaParser.NUMBER, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(SchemaParser.RIGHT_PAREN, 0); }
		public TimestampContext(Simple_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitTimestamp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_typeContext simple_type() throws RecognitionException {
		Simple_typeContext _localctx = new Simple_typeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_simple_type);
		int _la;
		try {
			setState(149);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				_localctx = new IntContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(97);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==INTEGER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				_localctx = new BigintContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(98);
				match(BIGINT);
				}
				break;
			case 3:
				_localctx = new FloatContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(99);
				match(FLOAT);
				}
				break;
			case 4:
				_localctx = new DoubleContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(100);
				match(DOUBLE);
				}
				break;
			case 5:
				_localctx = new DecimalContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(101);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DEC) | (1L << DECIMAL) | (1L << NUMERIC))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LEFT_PAREN) {
					{
					setState(102);
					match(LEFT_PAREN);
					setState(103);
					match(NUMBER);
					setState(106);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(104);
						match(COMMA);
						setState(105);
						match(NUMBER);
						}
					}

					setState(108);
					match(RIGHT_PAREN);
					}
				}

				}
				break;
			case 6:
				_localctx = new BooleanContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(111);
				match(BOOLEAN);
				}
				break;
			case 7:
				_localctx = new VarcharContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(118);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CHAR:
					{
					setState(112);
					match(CHAR);
					}
					break;
				case VARCHAR:
					{
					setState(113);
					match(VARCHAR);
					}
					break;
				case CHARACTER:
					{
					setState(114);
					match(CHARACTER);
					setState(116);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==VARYING) {
						{
						setState(115);
						match(VARYING);
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LEFT_PAREN) {
					{
					setState(120);
					match(LEFT_PAREN);
					setState(121);
					match(NUMBER);
					setState(122);
					match(RIGHT_PAREN);
					}
				}

				}
				break;
			case 8:
				_localctx = new BinaryContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(125);
				_la = _input.LA(1);
				if ( !(_la==BINARY || _la==VARBINARY) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(129);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LEFT_PAREN) {
					{
					setState(126);
					match(LEFT_PAREN);
					setState(127);
					match(NUMBER);
					setState(128);
					match(RIGHT_PAREN);
					}
				}

				}
				break;
			case 9:
				_localctx = new TimeContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(131);
				match(TIME);
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LEFT_PAREN) {
					{
					setState(132);
					match(LEFT_PAREN);
					setState(133);
					match(NUMBER);
					setState(134);
					match(RIGHT_PAREN);
					}
				}

				}
				break;
			case 10:
				_localctx = new DateContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(137);
				match(DATE);
				}
				break;
			case 11:
				_localctx = new TimestampContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(138);
				match(TIMESTAMP);
				setState(142);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LEFT_PAREN) {
					{
					setState(139);
					match(LEFT_PAREN);
					setState(140);
					match(NUMBER);
					setState(141);
					match(RIGHT_PAREN);
					}
				}

				}
				break;
			case 12:
				_localctx = new Interval_yearContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(144);
				match(INTERVAL);
				setState(145);
				_la = _input.LA(1);
				if ( !(_la==YEAR || _la==MONTH) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 13:
				_localctx = new Interval_dayContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(146);
				match(INTERVAL);
				setState(147);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DAY) | (1L << HOUR) | (1L << MINUTE) | (1L << SECOND))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 14:
				_localctx = new IntervalContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(148);
				match(INTERVAL);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Complex_typeContext extends ParserRuleContext {
		public Simple_array_typeContext simple_array_type() {
			return getRuleContext(Simple_array_typeContext.class,0);
		}
		public Complex_array_typeContext complex_array_type() {
			return getRuleContext(Complex_array_typeContext.class,0);
		}
		public Complex_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complex_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitComplex_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Complex_typeContext complex_type() throws RecognitionException {
		Complex_typeContext _localctx = new Complex_typeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_complex_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(151);
				simple_array_type();
				}
				break;
			case 2:
				{
				setState(152);
				complex_array_type();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_array_typeContext extends ParserRuleContext {
		public TerminalNode ARRAY() { return getToken(SchemaParser.ARRAY, 0); }
		public TerminalNode LEFT_ANGLE_BRACKET() { return getToken(SchemaParser.LEFT_ANGLE_BRACKET, 0); }
		public TerminalNode RIGHT_ANGLE_BRACKET() { return getToken(SchemaParser.RIGHT_ANGLE_BRACKET, 0); }
		public Simple_typeContext simple_type() {
			return getRuleContext(Simple_typeContext.class,0);
		}
		public Struct_typeContext struct_type() {
			return getRuleContext(Struct_typeContext.class,0);
		}
		public Simple_array_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_array_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitSimple_array_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_array_typeContext simple_array_type() throws RecognitionException {
		Simple_array_typeContext _localctx = new Simple_array_typeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_simple_array_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(ARRAY);
			setState(156);
			match(LEFT_ANGLE_BRACKET);
			setState(159);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
			case INTEGER:
			case BIGINT:
			case FLOAT:
			case DOUBLE:
			case DEC:
			case DECIMAL:
			case NUMERIC:
			case BOOLEAN:
			case CHAR:
			case CHARACTER:
			case VARCHAR:
			case BINARY:
			case VARBINARY:
			case TIME:
			case DATE:
			case TIMESTAMP:
			case INTERVAL:
				{
				setState(157);
				simple_type();
				}
				break;
			case STRUCT:
				{
				setState(158);
				struct_type();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(161);
			match(RIGHT_ANGLE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Complex_array_typeContext extends ParserRuleContext {
		public TerminalNode ARRAY() { return getToken(SchemaParser.ARRAY, 0); }
		public TerminalNode LEFT_ANGLE_BRACKET() { return getToken(SchemaParser.LEFT_ANGLE_BRACKET, 0); }
		public Complex_typeContext complex_type() {
			return getRuleContext(Complex_typeContext.class,0);
		}
		public TerminalNode RIGHT_ANGLE_BRACKET() { return getToken(SchemaParser.RIGHT_ANGLE_BRACKET, 0); }
		public Complex_array_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complex_array_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitComplex_array_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Complex_array_typeContext complex_array_type() throws RecognitionException {
		Complex_array_typeContext _localctx = new Complex_array_typeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_complex_array_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(ARRAY);
			setState(164);
			match(LEFT_ANGLE_BRACKET);
			setState(165);
			complex_type();
			setState(166);
			match(RIGHT_ANGLE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_typeContext extends ParserRuleContext {
		public TerminalNode STRUCT() { return getToken(SchemaParser.STRUCT, 0); }
		public TerminalNode LEFT_ANGLE_BRACKET() { return getToken(SchemaParser.LEFT_ANGLE_BRACKET, 0); }
		public ColumnsContext columns() {
			return getRuleContext(ColumnsContext.class,0);
		}
		public TerminalNode RIGHT_ANGLE_BRACKET() { return getToken(SchemaParser.RIGHT_ANGLE_BRACKET, 0); }
		public Struct_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitStruct_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Struct_typeContext struct_type() throws RecognitionException {
		Struct_typeContext _localctx = new Struct_typeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_struct_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(STRUCT);
			setState(169);
			match(LEFT_ANGLE_BRACKET);
			setState(170);
			columns();
			setState(171);
			match(RIGHT_ANGLE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NullabilityContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(SchemaParser.NOT, 0); }
		public TerminalNode NULL() { return getToken(SchemaParser.NULL, 0); }
		public NullabilityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nullability; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitNullability(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NullabilityContext nullability() throws RecognitionException {
		NullabilityContext _localctx = new NullabilityContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_nullability);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(NOT);
			setState(174);
			match(NULL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Format_valueContext extends ParserRuleContext {
		public TerminalNode FORMAT() { return getToken(SchemaParser.FORMAT, 0); }
		public String_valueContext string_value() {
			return getRuleContext(String_valueContext.class,0);
		}
		public Format_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_format_value; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitFormat_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Format_valueContext format_value() throws RecognitionException {
		Format_valueContext _localctx = new Format_valueContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_format_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			match(FORMAT);
			setState(177);
			string_value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Default_valueContext extends ParserRuleContext {
		public TerminalNode DEFAULT() { return getToken(SchemaParser.DEFAULT, 0); }
		public String_valueContext string_value() {
			return getRuleContext(String_valueContext.class,0);
		}
		public Default_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_default_value; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitDefault_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Default_valueContext default_value() throws RecognitionException {
		Default_valueContext _localctx = new Default_valueContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_default_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(DEFAULT);
			setState(180);
			string_value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Property_valuesContext extends ParserRuleContext {
		public TerminalNode PROPERTIES() { return getToken(SchemaParser.PROPERTIES, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(SchemaParser.LEFT_BRACE, 0); }
		public List<Property_pairContext> property_pair() {
			return getRuleContexts(Property_pairContext.class);
		}
		public Property_pairContext property_pair(int i) {
			return getRuleContext(Property_pairContext.class,i);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(SchemaParser.RIGHT_BRACE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SchemaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SchemaParser.COMMA, i);
		}
		public Property_valuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property_values; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitProperty_values(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Property_valuesContext property_values() throws RecognitionException {
		Property_valuesContext _localctx = new Property_valuesContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_property_values);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			match(PROPERTIES);
			setState(183);
			match(LEFT_BRACE);
			setState(184);
			property_pair();
			setState(189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(185);
				match(COMMA);
				setState(186);
				property_pair();
				}
				}
				setState(191);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(192);
			match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Property_pairContext extends ParserRuleContext {
		public List<String_valueContext> string_value() {
			return getRuleContexts(String_valueContext.class);
		}
		public String_valueContext string_value(int i) {
			return getRuleContext(String_valueContext.class,i);
		}
		public TerminalNode EQUALS_SIGN() { return getToken(SchemaParser.EQUALS_SIGN, 0); }
		public Property_pairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property_pair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitProperty_pair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Property_pairContext property_pair() throws RecognitionException {
		Property_pairContext _localctx = new Property_pairContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_property_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			string_value();
			setState(195);
			match(EQUALS_SIGN);
			setState(196);
			string_value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class String_valueContext extends ParserRuleContext {
		public TerminalNode QUOTED_ID() { return getToken(SchemaParser.QUOTED_ID, 0); }
		public TerminalNode SINGLE_QUOTED_STRING() { return getToken(SchemaParser.SINGLE_QUOTED_STRING, 0); }
		public TerminalNode DOUBLE_QUOTED_STRING() { return getToken(SchemaParser.DOUBLE_QUOTED_STRING, 0); }
		public String_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_value; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitString_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final String_valueContext string_value() throws RecognitionException {
		String_valueContext _localctx = new String_valueContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_string_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << QUOTED_ID) | (1L << SINGLE_QUOTED_STRING) | (1L << DOUBLE_QUOTED_STRING))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\67\u00cb\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\2\3\2\5\2\60\n\2\3\2\3\2"+
		"\3\3\3\3\3\3\7\3\67\n\3\f\3\16\3:\13\3\3\4\3\4\5\4>\n\4\3\5\3\5\3\5\3"+
		"\5\5\5D\n\5\3\6\3\6\3\6\5\6I\n\6\3\6\5\6L\n\6\3\6\5\6O\n\6\3\7\3\7\3\7"+
		"\5\7T\n\7\3\b\3\b\3\b\5\bY\n\b\3\t\3\t\3\t\5\t^\n\t\3\n\3\n\5\nb\n\n\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13m\n\13\3\13\5\13p\n\13"+
		"\3\13\3\13\3\13\3\13\3\13\5\13w\n\13\5\13y\n\13\3\13\3\13\3\13\5\13~\n"+
		"\13\3\13\3\13\3\13\3\13\5\13\u0084\n\13\3\13\3\13\3\13\3\13\5\13\u008a"+
		"\n\13\3\13\3\13\3\13\3\13\3\13\5\13\u0091\n\13\3\13\3\13\3\13\3\13\3\13"+
		"\5\13\u0098\n\13\3\f\3\f\5\f\u009c\n\f\3\r\3\r\3\r\3\r\5\r\u00a2\n\r\3"+
		"\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\7\23\u00be\n\23"+
		"\f\23\16\23\u00c1\13\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\2"+
		"\2\26\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(\2\b\3\2\3\4\3\2\b\n"+
		"\3\2\20\21\3\2\26\27\3\2\30\33\3\2\62\64\2\u00dc\2/\3\2\2\2\4\63\3\2\2"+
		"\2\6;\3\2\2\2\bC\3\2\2\2\nE\3\2\2\2\fP\3\2\2\2\16U\3\2\2\2\20Z\3\2\2\2"+
		"\22a\3\2\2\2\24\u0097\3\2\2\2\26\u009b\3\2\2\2\30\u009d\3\2\2\2\32\u00a5"+
		"\3\2\2\2\34\u00aa\3\2\2\2\36\u00af\3\2\2\2 \u00b2\3\2\2\2\"\u00b5\3\2"+
		"\2\2$\u00b8\3\2\2\2&\u00c4\3\2\2\2(\u00c8\3\2\2\2*\60\5\4\3\2+,\7!\2\2"+
		",-\5\4\3\2-.\7\"\2\2.\60\3\2\2\2/*\3\2\2\2/+\3\2\2\2\60\61\3\2\2\2\61"+
		"\62\7\2\2\3\62\3\3\2\2\2\638\5\6\4\2\64\65\7\37\2\2\65\67\5\6\4\2\66\64"+
		"\3\2\2\2\67:\3\2\2\28\66\3\2\2\289\3\2\2\29\5\3\2\2\2:8\3\2\2\2;=\5\b"+
		"\5\2<>\5$\23\2=<\3\2\2\2=>\3\2\2\2>\7\3\2\2\2?D\5\n\6\2@D\5\16\b\2AD\5"+
		"\f\7\2BD\5\20\t\2C?\3\2\2\2C@\3\2\2\2CA\3\2\2\2CB\3\2\2\2D\t\3\2\2\2E"+
		"F\5\22\n\2FH\5\24\13\2GI\5\36\20\2HG\3\2\2\2HI\3\2\2\2IK\3\2\2\2JL\5 "+
		"\21\2KJ\3\2\2\2KL\3\2\2\2LN\3\2\2\2MO\5\"\22\2NM\3\2\2\2NO\3\2\2\2O\13"+
		"\3\2\2\2PQ\5\22\n\2QS\5\30\r\2RT\5\36\20\2SR\3\2\2\2ST\3\2\2\2T\r\3\2"+
		"\2\2UV\5\22\n\2VX\5\34\17\2WY\5\36\20\2XW\3\2\2\2XY\3\2\2\2Y\17\3\2\2"+
		"\2Z[\5\22\n\2[]\5\32\16\2\\^\5\36\20\2]\\\3\2\2\2]^\3\2\2\2^\21\3\2\2"+
		"\2_b\7\61\2\2`b\7\62\2\2a_\3\2\2\2a`\3\2\2\2b\23\3\2\2\2c\u0098\t\2\2"+
		"\2d\u0098\7\5\2\2e\u0098\7\6\2\2f\u0098\7\7\2\2go\t\3\2\2hi\7!\2\2il\7"+
		"\60\2\2jk\7\37\2\2km\7\60\2\2lj\3\2\2\2lm\3\2\2\2mn\3\2\2\2np\7\"\2\2"+
		"oh\3\2\2\2op\3\2\2\2p\u0098\3\2\2\2q\u0098\7\13\2\2ry\7\f\2\2sy\7\17\2"+
		"\2tv\7\r\2\2uw\7\16\2\2vu\3\2\2\2vw\3\2\2\2wy\3\2\2\2xr\3\2\2\2xs\3\2"+
		"\2\2xt\3\2\2\2y}\3\2\2\2z{\7!\2\2{|\7\60\2\2|~\7\"\2\2}z\3\2\2\2}~\3\2"+
		"\2\2~\u0098\3\2\2\2\177\u0083\t\4\2\2\u0080\u0081\7!\2\2\u0081\u0082\7"+
		"\60\2\2\u0082\u0084\7\"\2\2\u0083\u0080\3\2\2\2\u0083\u0084\3\2\2\2\u0084"+
		"\u0098\3\2\2\2\u0085\u0089\7\22\2\2\u0086\u0087\7!\2\2\u0087\u0088\7\60"+
		"\2\2\u0088\u008a\7\"\2\2\u0089\u0086\3\2\2\2\u0089\u008a\3\2\2\2\u008a"+
		"\u0098\3\2\2\2\u008b\u0098\7\23\2\2\u008c\u0090\7\24\2\2\u008d\u008e\7"+
		"!\2\2\u008e\u008f\7\60\2\2\u008f\u0091\7\"\2\2\u0090\u008d\3\2\2\2\u0090"+
		"\u0091\3\2\2\2\u0091\u0098\3\2\2\2\u0092\u0093\7\25\2\2\u0093\u0098\t"+
		"\5\2\2\u0094\u0095\7\25\2\2\u0095\u0098\t\6\2\2\u0096\u0098\7\25\2\2\u0097"+
		"c\3\2\2\2\u0097d\3\2\2\2\u0097e\3\2\2\2\u0097f\3\2\2\2\u0097g\3\2\2\2"+
		"\u0097q\3\2\2\2\u0097x\3\2\2\2\u0097\177\3\2\2\2\u0097\u0085\3\2\2\2\u0097"+
		"\u008b\3\2\2\2\u0097\u008c\3\2\2\2\u0097\u0092\3\2\2\2\u0097\u0094\3\2"+
		"\2\2\u0097\u0096\3\2\2\2\u0098\25\3\2\2\2\u0099\u009c\5\30\r\2\u009a\u009c"+
		"\5\32\16\2\u009b\u0099\3\2\2\2\u009b\u009a\3\2\2\2\u009c\27\3\2\2\2\u009d"+
		"\u009e\7\36\2\2\u009e\u00a1\7#\2\2\u009f\u00a2\5\24\13\2\u00a0\u00a2\5"+
		"\34\17\2\u00a1\u009f\3\2\2\2\u00a1\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3"+
		"\u00a4\7$\2\2\u00a4\31\3\2\2\2\u00a5\u00a6\7\36\2\2\u00a6\u00a7\7#\2\2"+
		"\u00a7\u00a8\5\26\f\2\u00a8\u00a9\7$\2\2\u00a9\33\3\2\2\2\u00aa\u00ab"+
		"\7\35\2\2\u00ab\u00ac\7#\2\2\u00ac\u00ad\5\4\3\2\u00ad\u00ae\7$\2\2\u00ae"+
		"\35\3\2\2\2\u00af\u00b0\7*\2\2\u00b0\u00b1\7+\2\2\u00b1\37\3\2\2\2\u00b2"+
		"\u00b3\7-\2\2\u00b3\u00b4\5(\25\2\u00b4!\3\2\2\2\u00b5\u00b6\7.\2\2\u00b6"+
		"\u00b7\5(\25\2\u00b7#\3\2\2\2\u00b8\u00b9\7/\2\2\u00b9\u00ba\7\'\2\2\u00ba"+
		"\u00bf\5&\24\2\u00bb\u00bc\7\37\2\2\u00bc\u00be\5&\24\2\u00bd\u00bb\3"+
		"\2\2\2\u00be\u00c1\3\2\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0"+
		"\u00c2\3\2\2\2\u00c1\u00bf\3\2\2\2\u00c2\u00c3\7(\2\2\u00c3%\3\2\2\2\u00c4"+
		"\u00c5\5(\25\2\u00c5\u00c6\7)\2\2\u00c6\u00c7\5(\25\2\u00c7\'\3\2\2\2"+
		"\u00c8\u00c9\t\7\2\2\u00c9)\3\2\2\2\31/8=CHKNSX]alovx}\u0083\u0089\u0090"+
		"\u0097\u009b\u00a1\u00bf";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}