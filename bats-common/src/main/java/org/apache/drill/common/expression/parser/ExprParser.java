// Generated from org\apache\drill\common\expression\parser\ExprParser.g4 by ANTLR 4.7.1
package org.apache.drill.common.expression.parser;

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
  
//Explicit import...
import java.util.*;
import org.apache.drill.common.expression.*;
import org.apache.drill.common.expression.PathSegment.NameSegment;
import org.apache.drill.common.expression.PathSegment.ArraySegment;
import org.apache.drill.common.types.*;
import org.apache.drill.common.types.TypeProtos.*;
import org.apache.drill.common.types.TypeProtos.DataMode;
import org.apache.drill.common.types.TypeProtos.MajorType;
import org.apache.drill.common.exceptions.ExpressionParsingException;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExprParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		If=1, Else=2, Return=3, Then=4, End=5, In=6, Case=7, When=8, Cast=9, Convert=10, 
		AnyValue=11, Nullable=12, Repeat=13, As=14, BIT=15, INT=16, BIGINT=17, 
		FLOAT4=18, FLOAT8=19, VARCHAR=20, VARBINARY=21, DATE=22, TIMESTAMP=23, 
		TIME=24, TIMESTAMPTZ=25, INTERVAL=26, INTERVALYEAR=27, INTERVALDAY=28, 
		Period=29, DECIMAL9=30, DECIMAL18=31, DECIMAL28DENSE=32, DECIMAL28SPARSE=33, 
		DECIMAL38DENSE=34, DECIMAL38SPARSE=35, VARDECIMAL=36, Or=37, And=38, Equals=39, 
		NEquals=40, GTEquals=41, LTEquals=42, Caret=43, Excl=44, GT=45, LT=46, 
		Plus=47, Minus=48, Asterisk=49, ForwardSlash=50, Percent=51, OBrace=52, 
		CBrace=53, OBracket=54, CBracket=55, OParen=56, CParen=57, SColon=58, 
		Comma=59, QMark=60, Colon=61, SingleQuote=62, Bool=63, Number=64, Identifier=65, 
		QuotedIdentifier=66, String=67, LineComment=68, BlockComment=69, Space=70;
	public static final int
		RULE_parse = 0, RULE_functionCall = 1, RULE_convertCall = 2, RULE_anyValueCall = 3, 
		RULE_castCall = 4, RULE_repeat = 5, RULE_dataType = 6, RULE_booleanType = 7, 
		RULE_numType = 8, RULE_charType = 9, RULE_precision = 10, RULE_scale = 11, 
		RULE_dateType = 12, RULE_typeLen = 13, RULE_ifStatement = 14, RULE_ifStat = 15, 
		RULE_elseIfStat = 16, RULE_caseStatement = 17, RULE_caseWhenStat = 18, 
		RULE_caseElseStat = 19, RULE_exprList = 20, RULE_expression = 21, RULE_condExpr = 22, 
		RULE_orExpr = 23, RULE_andExpr = 24, RULE_equExpr = 25, RULE_relExpr = 26, 
		RULE_addExpr = 27, RULE_mulExpr = 28, RULE_xorExpr = 29, RULE_unaryExpr = 30, 
		RULE_atom = 31, RULE_pathSegment = 32, RULE_nameSegment = 33, RULE_arraySegment = 34, 
		RULE_lookup = 35;
	public static final String[] ruleNames = {
		"parse", "functionCall", "convertCall", "anyValueCall", "castCall", "repeat", 
		"dataType", "booleanType", "numType", "charType", "precision", "scale", 
		"dateType", "typeLen", "ifStatement", "ifStat", "elseIfStat", "caseStatement", 
		"caseWhenStat", "caseElseStat", "exprList", "expression", "condExpr", 
		"orExpr", "andExpr", "equExpr", "relExpr", "addExpr", "mulExpr", "xorExpr", 
		"unaryExpr", "atom", "pathSegment", "nameSegment", "arraySegment", "lookup"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'if'", "'else'", "'return'", "'then'", "'end'", "'in'", "'case'", 
		"'when'", "'cast'", null, null, "'nullable'", "'repeat'", "'as'", null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "'.'", null, null, null, null, null, null, null, null, null, null, 
		null, "'>='", "'<='", "'^'", "'!'", "'>'", "'<'", "'+'", "'-'", "'*'", 
		"'/'", "'%'", "'{'", "'}'", "'['", "']'", "'('", "')'", "';'", "','", 
		"'?'", "':'", "'''"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "If", "Else", "Return", "Then", "End", "In", "Case", "When", "Cast", 
		"Convert", "AnyValue", "Nullable", "Repeat", "As", "BIT", "INT", "BIGINT", 
		"FLOAT4", "FLOAT8", "VARCHAR", "VARBINARY", "DATE", "TIMESTAMP", "TIME", 
		"TIMESTAMPTZ", "INTERVAL", "INTERVALYEAR", "INTERVALDAY", "Period", "DECIMAL9", 
		"DECIMAL18", "DECIMAL28DENSE", "DECIMAL28SPARSE", "DECIMAL38DENSE", "DECIMAL38SPARSE", 
		"VARDECIMAL", "Or", "And", "Equals", "NEquals", "GTEquals", "LTEquals", 
		"Caret", "Excl", "GT", "LT", "Plus", "Minus", "Asterisk", "ForwardSlash", 
		"Percent", "OBrace", "CBrace", "OBracket", "CBracket", "OParen", "CParen", 
		"SColon", "Comma", "QMark", "Colon", "SingleQuote", "Bool", "Number", 
		"Identifier", "QuotedIdentifier", "String", "LineComment", "BlockComment", 
		"Space"
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
	public String getGrammarFileName() { return "ExprParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	  private String fullExpression;
	  private int tokenPos;

	  public static void p(String s){
	    System.out.println(s);
	  }
	  
	  public ExpressionPosition pos(Token token){
	    return new ExpressionPosition(fullExpression, token.getTokenIndex());
	  }

	public ExprParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ParseContext extends ParserRuleContext {
		public LogicalExpression e;
		public ExpressionContext expression;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ExprParser.EOF, 0); }
		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitParse(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			((ParseContext)_localctx).expression = expression();
			setState(73);
			match(EOF);

			    ((ParseContext)_localctx).e =  ((ParseContext)_localctx).expression.e;
			    if (fullExpression == null) fullExpression = (((ParseContext)_localctx).expression!=null?_input.getText(((ParseContext)_localctx).expression.start,((ParseContext)_localctx).expression.stop):null);
			    tokenPos = (((ParseContext)_localctx).expression!=null?(((ParseContext)_localctx).expression.start):null).getTokenIndex();
			  
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

	public static class FunctionCallContext extends ParserRuleContext {
		public LogicalExpression e;
		public Token Identifier;
		public ExprListContext exprList;
		public TerminalNode Identifier() { return getToken(ExprParser.Identifier, 0); }
		public TerminalNode OParen() { return getToken(ExprParser.OParen, 0); }
		public TerminalNode CParen() { return getToken(ExprParser.CParen, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitFunctionCall(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			((FunctionCallContext)_localctx).Identifier = match(Identifier);
			setState(77);
			match(OParen);
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << If) | (1L << Case) | (1L << Cast) | (1L << Convert) | (1L << AnyValue) | (1L << Excl) | (1L << Plus) | (1L << Minus) | (1L << OParen) | (1L << SingleQuote) | (1L << Bool))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Number - 64)) | (1L << (Identifier - 64)) | (1L << (QuotedIdentifier - 64)) | (1L << (String - 64)))) != 0)) {
				{
				setState(78);
				((FunctionCallContext)_localctx).exprList = exprList();
				}
			}

			setState(81);
			match(CParen);
			((FunctionCallContext)_localctx).e = 
			      FunctionCallFactory.createExpression((((FunctionCallContext)_localctx).Identifier!=null?((FunctionCallContext)_localctx).Identifier.getText():null), pos(((FunctionCallContext)_localctx).Identifier),
			        (((FunctionCallContext)_localctx).exprList == null ? new ArrayList<>() : ((FunctionCallContext)_localctx).exprList.listE)); 
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

	public static class ConvertCallContext extends ParserRuleContext {
		public LogicalExpression e;
		public Token Convert;
		public ExpressionContext expression;
		public Token String;
		public TerminalNode Convert() { return getToken(ExprParser.Convert, 0); }
		public TerminalNode OParen() { return getToken(ExprParser.OParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Comma() { return getToken(ExprParser.Comma, 0); }
		public TerminalNode String() { return getToken(ExprParser.String, 0); }
		public TerminalNode CParen() { return getToken(ExprParser.CParen, 0); }
		public ConvertCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_convertCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterConvertCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitConvertCall(this);
		}
	}

	public final ConvertCallContext convertCall() throws RecognitionException {
		ConvertCallContext _localctx = new ConvertCallContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_convertCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			((ConvertCallContext)_localctx).Convert = match(Convert);
			setState(85);
			match(OParen);
			setState(86);
			((ConvertCallContext)_localctx).expression = expression();
			setState(87);
			match(Comma);
			setState(88);
			((ConvertCallContext)_localctx).String = match(String);
			setState(89);
			match(CParen);
			 ((ConvertCallContext)_localctx).e =  FunctionCallFactory.createConvert((((ConvertCallContext)_localctx).Convert!=null?((ConvertCallContext)_localctx).Convert.getText():null), (((ConvertCallContext)_localctx).String!=null?((ConvertCallContext)_localctx).String.getText():null), ((ConvertCallContext)_localctx).expression.e, pos(((ConvertCallContext)_localctx).Convert));
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

	public static class AnyValueCallContext extends ParserRuleContext {
		public LogicalExpression e;
		public Token AnyValue;
		public ExprListContext exprList;
		public TerminalNode AnyValue() { return getToken(ExprParser.AnyValue, 0); }
		public TerminalNode OParen() { return getToken(ExprParser.OParen, 0); }
		public TerminalNode CParen() { return getToken(ExprParser.CParen, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public AnyValueCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyValueCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterAnyValueCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitAnyValueCall(this);
		}
	}

	public final AnyValueCallContext anyValueCall() throws RecognitionException {
		AnyValueCallContext _localctx = new AnyValueCallContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_anyValueCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			((AnyValueCallContext)_localctx).AnyValue = match(AnyValue);
			setState(93);
			match(OParen);
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << If) | (1L << Case) | (1L << Cast) | (1L << Convert) | (1L << AnyValue) | (1L << Excl) | (1L << Plus) | (1L << Minus) | (1L << OParen) | (1L << SingleQuote) | (1L << Bool))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Number - 64)) | (1L << (Identifier - 64)) | (1L << (QuotedIdentifier - 64)) | (1L << (String - 64)))) != 0)) {
				{
				setState(94);
				((AnyValueCallContext)_localctx).exprList = exprList();
				}
			}

			setState(97);
			match(CParen);
			((AnyValueCallContext)_localctx).e = 
			      FunctionCallFactory.createExpression((((AnyValueCallContext)_localctx).AnyValue!=null?((AnyValueCallContext)_localctx).AnyValue.getText():null), pos(((AnyValueCallContext)_localctx).AnyValue),
			       (((AnyValueCallContext)_localctx).exprList == null ? new ArrayList<>() : ((AnyValueCallContext)_localctx).exprList.listE)); 
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

	public static class CastCallContext extends ParserRuleContext {
		public LogicalExpression e;
		public Token Cast;
		public ExpressionContext expression;
		public DataTypeContext dataType;
		public RepeatContext repeat;
		public TerminalNode Cast() { return getToken(ExprParser.Cast, 0); }
		public TerminalNode OParen() { return getToken(ExprParser.OParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode As() { return getToken(ExprParser.As, 0); }
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public TerminalNode CParen() { return getToken(ExprParser.CParen, 0); }
		public RepeatContext repeat() {
			return getRuleContext(RepeatContext.class,0);
		}
		public CastCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterCastCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitCastCall(this);
		}
	}

	public final CastCallContext castCall() throws RecognitionException {
		CastCallContext _localctx = new CastCallContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_castCall);

			  List<LogicalExpression> exprs = new ArrayList<>();
			  ExpressionPosition p = null;
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			((CastCallContext)_localctx).Cast = match(Cast);
			setState(101);
			match(OParen);
			setState(102);
			((CastCallContext)_localctx).expression = expression();
			setState(103);
			match(As);
			setState(104);
			((CastCallContext)_localctx).dataType = dataType();
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Repeat) {
				{
				setState(105);
				((CastCallContext)_localctx).repeat = repeat();
				}
			}

			setState(108);
			match(CParen);
			  if (((CastCallContext)_localctx).repeat != null && ((CastCallContext)_localctx).repeat.isRep.compareTo(Boolean.TRUE)==0)
			           ((CastCallContext)_localctx).e =  FunctionCallFactory.createCast(TypeProtos.MajorType.newBuilder().mergeFrom(((CastCallContext)_localctx).dataType.type).setMode(DataMode.REPEATED).build(), pos(((CastCallContext)_localctx).Cast), ((CastCallContext)_localctx).expression.e);
			         else
			           ((CastCallContext)_localctx).e =  FunctionCallFactory.createCast(((CastCallContext)_localctx).dataType.type, pos(((CastCallContext)_localctx).Cast), ((CastCallContext)_localctx).expression.e);
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

	public static class RepeatContext extends ParserRuleContext {
		public Boolean isRep;
		public TerminalNode Repeat() { return getToken(ExprParser.Repeat, 0); }
		public RepeatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_repeat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterRepeat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitRepeat(this);
		}
	}

	public final RepeatContext repeat() throws RecognitionException {
		RepeatContext _localctx = new RepeatContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_repeat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			match(Repeat);
			 ((RepeatContext)_localctx).isRep =  Boolean.TRUE;
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

	public static class DataTypeContext extends ParserRuleContext {
		public MajorType type;
		public NumTypeContext numType;
		public CharTypeContext charType;
		public DateTypeContext dateType;
		public BooleanTypeContext booleanType;
		public NumTypeContext numType() {
			return getRuleContext(NumTypeContext.class,0);
		}
		public CharTypeContext charType() {
			return getRuleContext(CharTypeContext.class,0);
		}
		public DateTypeContext dateType() {
			return getRuleContext(DateTypeContext.class,0);
		}
		public BooleanTypeContext booleanType() {
			return getRuleContext(BooleanTypeContext.class,0);
		}
		public DataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitDataType(this);
		}
	}

	public final DataTypeContext dataType() throws RecognitionException {
		DataTypeContext _localctx = new DataTypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_dataType);
		try {
			setState(126);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
			case BIGINT:
			case FLOAT4:
			case FLOAT8:
			case DECIMAL9:
			case DECIMAL18:
			case DECIMAL28DENSE:
			case DECIMAL28SPARSE:
			case DECIMAL38DENSE:
			case DECIMAL38SPARSE:
			case VARDECIMAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(114);
				((DataTypeContext)_localctx).numType = numType();
				((DataTypeContext)_localctx).type = ((DataTypeContext)_localctx).numType.type;
				}
				break;
			case VARCHAR:
			case VARBINARY:
				enterOuterAlt(_localctx, 2);
				{
				setState(117);
				((DataTypeContext)_localctx).charType = charType();
				((DataTypeContext)_localctx).type = ((DataTypeContext)_localctx).charType.type;
				}
				break;
			case DATE:
			case TIMESTAMP:
			case TIME:
			case TIMESTAMPTZ:
			case INTERVAL:
			case INTERVALYEAR:
			case INTERVALDAY:
				enterOuterAlt(_localctx, 3);
				{
				setState(120);
				((DataTypeContext)_localctx).dateType = dateType();
				((DataTypeContext)_localctx).type = ((DataTypeContext)_localctx).dateType.type;
				}
				break;
			case BIT:
				enterOuterAlt(_localctx, 4);
				{
				setState(123);
				((DataTypeContext)_localctx).booleanType = booleanType();
				((DataTypeContext)_localctx).type = ((DataTypeContext)_localctx).booleanType.type;
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

	public static class BooleanTypeContext extends ParserRuleContext {
		public MajorType type;
		public TerminalNode BIT() { return getToken(ExprParser.BIT, 0); }
		public BooleanTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterBooleanType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitBooleanType(this);
		}
	}

	public final BooleanTypeContext booleanType() throws RecognitionException {
		BooleanTypeContext _localctx = new BooleanTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_booleanType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			match(BIT);
			 ((BooleanTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.BIT); 
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

	public static class NumTypeContext extends ParserRuleContext {
		public MajorType type;
		public PrecisionContext precision;
		public ScaleContext scale;
		public TerminalNode INT() { return getToken(ExprParser.INT, 0); }
		public TerminalNode BIGINT() { return getToken(ExprParser.BIGINT, 0); }
		public TerminalNode FLOAT4() { return getToken(ExprParser.FLOAT4, 0); }
		public TerminalNode FLOAT8() { return getToken(ExprParser.FLOAT8, 0); }
		public TerminalNode DECIMAL9() { return getToken(ExprParser.DECIMAL9, 0); }
		public TerminalNode OParen() { return getToken(ExprParser.OParen, 0); }
		public PrecisionContext precision() {
			return getRuleContext(PrecisionContext.class,0);
		}
		public TerminalNode Comma() { return getToken(ExprParser.Comma, 0); }
		public ScaleContext scale() {
			return getRuleContext(ScaleContext.class,0);
		}
		public TerminalNode CParen() { return getToken(ExprParser.CParen, 0); }
		public TerminalNode DECIMAL18() { return getToken(ExprParser.DECIMAL18, 0); }
		public TerminalNode DECIMAL28DENSE() { return getToken(ExprParser.DECIMAL28DENSE, 0); }
		public TerminalNode DECIMAL28SPARSE() { return getToken(ExprParser.DECIMAL28SPARSE, 0); }
		public TerminalNode DECIMAL38DENSE() { return getToken(ExprParser.DECIMAL38DENSE, 0); }
		public TerminalNode DECIMAL38SPARSE() { return getToken(ExprParser.DECIMAL38SPARSE, 0); }
		public TerminalNode VARDECIMAL() { return getToken(ExprParser.VARDECIMAL, 0); }
		public NumTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterNumType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitNumType(this);
		}
	}

	public final NumTypeContext numType() throws RecognitionException {
		NumTypeContext _localctx = new NumTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_numType);
		try {
			setState(195);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(131);
				match(INT);
				 ((NumTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.INT); 
				}
				break;
			case BIGINT:
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				match(BIGINT);
				 ((NumTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.BIGINT); 
				}
				break;
			case FLOAT4:
				enterOuterAlt(_localctx, 3);
				{
				setState(135);
				match(FLOAT4);
				 ((NumTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.FLOAT4); 
				}
				break;
			case FLOAT8:
				enterOuterAlt(_localctx, 4);
				{
				setState(137);
				match(FLOAT8);
				 ((NumTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.FLOAT8); 
				}
				break;
			case DECIMAL9:
				enterOuterAlt(_localctx, 5);
				{
				setState(139);
				match(DECIMAL9);
				setState(140);
				match(OParen);
				setState(141);
				((NumTypeContext)_localctx).precision = precision();
				setState(142);
				match(Comma);
				setState(143);
				((NumTypeContext)_localctx).scale = scale();
				setState(144);
				match(CParen);
				 ((NumTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.DECIMAL9).setMode(DataMode.REQUIRED).setPrecision(((NumTypeContext)_localctx).precision.value.intValue()).setScale(((NumTypeContext)_localctx).scale.value.intValue()).build(); 
				}
				break;
			case DECIMAL18:
				enterOuterAlt(_localctx, 6);
				{
				setState(147);
				match(DECIMAL18);
				setState(148);
				match(OParen);
				setState(149);
				((NumTypeContext)_localctx).precision = precision();
				setState(150);
				match(Comma);
				setState(151);
				((NumTypeContext)_localctx).scale = scale();
				setState(152);
				match(CParen);
				 ((NumTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.DECIMAL18).setMode(DataMode.REQUIRED).setPrecision(((NumTypeContext)_localctx).precision.value.intValue()).setScale(((NumTypeContext)_localctx).scale.value.intValue()).build(); 
				}
				break;
			case DECIMAL28DENSE:
				enterOuterAlt(_localctx, 7);
				{
				setState(155);
				match(DECIMAL28DENSE);
				setState(156);
				match(OParen);
				setState(157);
				((NumTypeContext)_localctx).precision = precision();
				setState(158);
				match(Comma);
				setState(159);
				((NumTypeContext)_localctx).scale = scale();
				setState(160);
				match(CParen);
				 ((NumTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.DECIMAL28DENSE).setMode(DataMode.REQUIRED).setPrecision(((NumTypeContext)_localctx).precision.value.intValue()).setScale(((NumTypeContext)_localctx).scale.value.intValue()).build(); 
				}
				break;
			case DECIMAL28SPARSE:
				enterOuterAlt(_localctx, 8);
				{
				setState(163);
				match(DECIMAL28SPARSE);
				setState(164);
				match(OParen);
				setState(165);
				((NumTypeContext)_localctx).precision = precision();
				setState(166);
				match(Comma);
				setState(167);
				((NumTypeContext)_localctx).scale = scale();
				setState(168);
				match(CParen);
				 ((NumTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.DECIMAL28SPARSE).setMode(DataMode.REQUIRED).setPrecision(((NumTypeContext)_localctx).precision.value.intValue()).setScale(((NumTypeContext)_localctx).scale.value.intValue()).build(); 
				}
				break;
			case DECIMAL38DENSE:
				enterOuterAlt(_localctx, 9);
				{
				setState(171);
				match(DECIMAL38DENSE);
				setState(172);
				match(OParen);
				setState(173);
				((NumTypeContext)_localctx).precision = precision();
				setState(174);
				match(Comma);
				setState(175);
				((NumTypeContext)_localctx).scale = scale();
				setState(176);
				match(CParen);
				 ((NumTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.DECIMAL38DENSE).setMode(DataMode.REQUIRED).setPrecision(((NumTypeContext)_localctx).precision.value.intValue()).setScale(((NumTypeContext)_localctx).scale.value.intValue()).build(); 
				}
				break;
			case DECIMAL38SPARSE:
				enterOuterAlt(_localctx, 10);
				{
				setState(179);
				match(DECIMAL38SPARSE);
				setState(180);
				match(OParen);
				setState(181);
				((NumTypeContext)_localctx).precision = precision();
				setState(182);
				match(Comma);
				setState(183);
				((NumTypeContext)_localctx).scale = scale();
				setState(184);
				match(CParen);
				 ((NumTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.DECIMAL38SPARSE).setMode(DataMode.REQUIRED).setPrecision(((NumTypeContext)_localctx).precision.value.intValue()).setScale(((NumTypeContext)_localctx).scale.value.intValue()).build(); 
				}
				break;
			case VARDECIMAL:
				enterOuterAlt(_localctx, 11);
				{
				setState(187);
				match(VARDECIMAL);
				setState(188);
				match(OParen);
				setState(189);
				((NumTypeContext)_localctx).precision = precision();
				setState(190);
				match(Comma);
				setState(191);
				((NumTypeContext)_localctx).scale = scale();
				setState(192);
				match(CParen);
				 ((NumTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.VARDECIMAL).setMode(DataMode.REQUIRED).setPrecision(((NumTypeContext)_localctx).precision.value.intValue()).setScale(((NumTypeContext)_localctx).scale.value.intValue()).build(); 
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

	public static class CharTypeContext extends ParserRuleContext {
		public MajorType type;
		public TypeLenContext typeLen;
		public TerminalNode VARCHAR() { return getToken(ExprParser.VARCHAR, 0); }
		public TypeLenContext typeLen() {
			return getRuleContext(TypeLenContext.class,0);
		}
		public TerminalNode VARBINARY() { return getToken(ExprParser.VARBINARY, 0); }
		public CharTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterCharType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitCharType(this);
		}
	}

	public final CharTypeContext charType() throws RecognitionException {
		CharTypeContext _localctx = new CharTypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_charType);
		try {
			setState(205);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VARCHAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(197);
				match(VARCHAR);
				setState(198);
				((CharTypeContext)_localctx).typeLen = typeLen();
				((CharTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.VARCHAR).setMode(DataMode.REQUIRED).setPrecision(((CharTypeContext)_localctx).typeLen.length.intValue()).build(); 
				}
				break;
			case VARBINARY:
				enterOuterAlt(_localctx, 2);
				{
				setState(201);
				match(VARBINARY);
				setState(202);
				((CharTypeContext)_localctx).typeLen = typeLen();
				((CharTypeContext)_localctx).type =  TypeProtos.MajorType.newBuilder().setMinorType(TypeProtos.MinorType.VARBINARY).setMode(DataMode.REQUIRED).setPrecision(((CharTypeContext)_localctx).typeLen.length.intValue()).build();
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

	public static class PrecisionContext extends ParserRuleContext {
		public Integer value;
		public Token Number;
		public TerminalNode Number() { return getToken(ExprParser.Number, 0); }
		public PrecisionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_precision; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterPrecision(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitPrecision(this);
		}
	}

	public final PrecisionContext precision() throws RecognitionException {
		PrecisionContext _localctx = new PrecisionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_precision);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			((PrecisionContext)_localctx).Number = match(Number);
			((PrecisionContext)_localctx).value =  Integer.parseInt((((PrecisionContext)_localctx).Number!=null?((PrecisionContext)_localctx).Number.getText():null)); 
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

	public static class ScaleContext extends ParserRuleContext {
		public Integer value;
		public Token Number;
		public TerminalNode Number() { return getToken(ExprParser.Number, 0); }
		public ScaleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scale; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterScale(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitScale(this);
		}
	}

	public final ScaleContext scale() throws RecognitionException {
		ScaleContext _localctx = new ScaleContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_scale);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			((ScaleContext)_localctx).Number = match(Number);
			((ScaleContext)_localctx).value =  Integer.parseInt((((ScaleContext)_localctx).Number!=null?((ScaleContext)_localctx).Number.getText():null)); 
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

	public static class DateTypeContext extends ParserRuleContext {
		public MajorType type;
		public TerminalNode DATE() { return getToken(ExprParser.DATE, 0); }
		public TerminalNode TIMESTAMP() { return getToken(ExprParser.TIMESTAMP, 0); }
		public TerminalNode TIME() { return getToken(ExprParser.TIME, 0); }
		public TerminalNode TIMESTAMPTZ() { return getToken(ExprParser.TIMESTAMPTZ, 0); }
		public TerminalNode INTERVAL() { return getToken(ExprParser.INTERVAL, 0); }
		public TerminalNode INTERVALYEAR() { return getToken(ExprParser.INTERVALYEAR, 0); }
		public TerminalNode INTERVALDAY() { return getToken(ExprParser.INTERVALDAY, 0); }
		public DateTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterDateType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitDateType(this);
		}
	}

	public final DateTypeContext dateType() throws RecognitionException {
		DateTypeContext _localctx = new DateTypeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_dateType);
		try {
			setState(227);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DATE:
				enterOuterAlt(_localctx, 1);
				{
				setState(213);
				match(DATE);
				 ((DateTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.DATE); 
				}
				break;
			case TIMESTAMP:
				enterOuterAlt(_localctx, 2);
				{
				setState(215);
				match(TIMESTAMP);
				 ((DateTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.TIMESTAMP); 
				}
				break;
			case TIME:
				enterOuterAlt(_localctx, 3);
				{
				setState(217);
				match(TIME);
				 ((DateTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.TIME); 
				}
				break;
			case TIMESTAMPTZ:
				enterOuterAlt(_localctx, 4);
				{
				setState(219);
				match(TIMESTAMPTZ);
				 ((DateTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.TIMESTAMPTZ); 
				}
				break;
			case INTERVAL:
				enterOuterAlt(_localctx, 5);
				{
				setState(221);
				match(INTERVAL);
				 ((DateTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.INTERVAL); 
				}
				break;
			case INTERVALYEAR:
				enterOuterAlt(_localctx, 6);
				{
				setState(223);
				match(INTERVALYEAR);
				 ((DateTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.INTERVALYEAR); 
				}
				break;
			case INTERVALDAY:
				enterOuterAlt(_localctx, 7);
				{
				setState(225);
				match(INTERVALDAY);
				 ((DateTypeContext)_localctx).type =  Types.required(TypeProtos.MinorType.INTERVALDAY); 
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

	public static class TypeLenContext extends ParserRuleContext {
		public Integer length;
		public Token Number;
		public TerminalNode OParen() { return getToken(ExprParser.OParen, 0); }
		public TerminalNode Number() { return getToken(ExprParser.Number, 0); }
		public TerminalNode CParen() { return getToken(ExprParser.CParen, 0); }
		public TypeLenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeLen; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterTypeLen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitTypeLen(this);
		}
	}

	public final TypeLenContext typeLen() throws RecognitionException {
		TypeLenContext _localctx = new TypeLenContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_typeLen);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(OParen);
			setState(230);
			((TypeLenContext)_localctx).Number = match(Number);
			setState(231);
			match(CParen);
			((TypeLenContext)_localctx).length =  Integer.parseInt((((TypeLenContext)_localctx).Number!=null?((TypeLenContext)_localctx).Number.getText():null));
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

	public static class IfStatementContext extends ParserRuleContext {
		public LogicalExpression e;
		public IfStatContext i1;
		public ElseIfStatContext elseIfStat;
		public ExpressionContext expression;
		public TerminalNode Else() { return getToken(ExprParser.Else, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode End() { return getToken(ExprParser.End, 0); }
		public IfStatContext ifStat() {
			return getRuleContext(IfStatContext.class,0);
		}
		public List<ElseIfStatContext> elseIfStat() {
			return getRuleContexts(ElseIfStatContext.class);
		}
		public ElseIfStatContext elseIfStat(int i) {
			return getRuleContext(ElseIfStatContext.class,i);
		}
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitIfStatement(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_ifStatement);

			  IfExpression.Builder s = IfExpression.newBuilder();
			
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			((IfStatementContext)_localctx).i1 = ifStat();
			s.setIfCondition(((IfStatementContext)_localctx).i1.i); s.setPosition(pos((((IfStatementContext)_localctx).i1!=null?(((IfStatementContext)_localctx).i1.start):null))); 
			setState(241);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(236);
					((IfStatementContext)_localctx).elseIfStat = elseIfStat();
					 s.setIfCondition(((IfStatementContext)_localctx).elseIfStat.i); 
					}
					} 
				}
				setState(243);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			setState(244);
			match(Else);
			setState(245);
			((IfStatementContext)_localctx).expression = expression();
			 s.setElse(((IfStatementContext)_localctx).expression.e); 
			setState(247);
			match(End);
			}
			_ctx.stop = _input.LT(-1);

				  ((IfStatementContext)_localctx).e =  s.build();
				
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

	public static class IfStatContext extends ParserRuleContext {
		public IfExpression.IfCondition i;
		public ExpressionContext e1;
		public ExpressionContext e2;
		public TerminalNode If() { return getToken(ExprParser.If, 0); }
		public TerminalNode Then() { return getToken(ExprParser.Then, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public IfStatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterIfStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitIfStat(this);
		}
	}

	public final IfStatContext ifStat() throws RecognitionException {
		IfStatContext _localctx = new IfStatContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_ifStat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(If);
			setState(250);
			((IfStatContext)_localctx).e1 = expression();
			setState(251);
			match(Then);
			setState(252);
			((IfStatContext)_localctx).e2 = expression();
			 ((IfStatContext)_localctx).i =  new IfExpression.IfCondition(((IfStatContext)_localctx).e1.e, ((IfStatContext)_localctx).e2.e); 
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

	public static class ElseIfStatContext extends ParserRuleContext {
		public IfExpression.IfCondition i;
		public ExpressionContext e1;
		public ExpressionContext e2;
		public TerminalNode Else() { return getToken(ExprParser.Else, 0); }
		public TerminalNode If() { return getToken(ExprParser.If, 0); }
		public TerminalNode Then() { return getToken(ExprParser.Then, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ElseIfStatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterElseIfStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitElseIfStat(this);
		}
	}

	public final ElseIfStatContext elseIfStat() throws RecognitionException {
		ElseIfStatContext _localctx = new ElseIfStatContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_elseIfStat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			match(Else);
			setState(256);
			match(If);
			setState(257);
			((ElseIfStatContext)_localctx).e1 = expression();
			setState(258);
			match(Then);
			setState(259);
			((ElseIfStatContext)_localctx).e2 = expression();
			 ((ElseIfStatContext)_localctx).i =  new IfExpression.IfCondition(((ElseIfStatContext)_localctx).e1.e, ((ElseIfStatContext)_localctx).e2.e); 
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

	public static class CaseStatementContext extends ParserRuleContext {
		public LogicalExpression e;
		public CaseWhenStatContext caseWhenStat;
		public CaseElseStatContext caseElseStat;
		public TerminalNode Case() { return getToken(ExprParser.Case, 0); }
		public CaseElseStatContext caseElseStat() {
			return getRuleContext(CaseElseStatContext.class,0);
		}
		public TerminalNode End() { return getToken(ExprParser.End, 0); }
		public List<CaseWhenStatContext> caseWhenStat() {
			return getRuleContexts(CaseWhenStatContext.class);
		}
		public CaseWhenStatContext caseWhenStat(int i) {
			return getRuleContext(CaseWhenStatContext.class,i);
		}
		public CaseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterCaseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitCaseStatement(this);
		}
	}

	public final CaseStatementContext caseStatement() throws RecognitionException {
		CaseStatementContext _localctx = new CaseStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_caseStatement);

			  IfExpression.Builder s = IfExpression.newBuilder();
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(Case);
			setState(266); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(263);
				((CaseStatementContext)_localctx).caseWhenStat = caseWhenStat();
				s.setIfCondition(((CaseStatementContext)_localctx).caseWhenStat.e); 
				}
				}
				setState(268); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==When );
			setState(270);
			((CaseStatementContext)_localctx).caseElseStat = caseElseStat();
			 s.setElse(((CaseStatementContext)_localctx).caseElseStat.e); 
			setState(272);
			match(End);
			}
			_ctx.stop = _input.LT(-1);

				  ((CaseStatementContext)_localctx).e =  s.build();
				
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

	public static class CaseWhenStatContext extends ParserRuleContext {
		public IfExpression.IfCondition e;
		public ExpressionContext e1;
		public ExpressionContext e2;
		public TerminalNode When() { return getToken(ExprParser.When, 0); }
		public TerminalNode Then() { return getToken(ExprParser.Then, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CaseWhenStatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseWhenStat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterCaseWhenStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitCaseWhenStat(this);
		}
	}

	public final CaseWhenStatContext caseWhenStat() throws RecognitionException {
		CaseWhenStatContext _localctx = new CaseWhenStatContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_caseWhenStat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			match(When);
			setState(275);
			((CaseWhenStatContext)_localctx).e1 = expression();
			setState(276);
			match(Then);
			setState(277);
			((CaseWhenStatContext)_localctx).e2 = expression();
			((CaseWhenStatContext)_localctx).e =  new IfExpression.IfCondition(((CaseWhenStatContext)_localctx).e1.e, ((CaseWhenStatContext)_localctx).e2.e); 
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

	public static class CaseElseStatContext extends ParserRuleContext {
		public LogicalExpression e;
		public ExpressionContext expression;
		public TerminalNode Else() { return getToken(ExprParser.Else, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public CaseElseStatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseElseStat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterCaseElseStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitCaseElseStat(this);
		}
	}

	public final CaseElseStatContext caseElseStat() throws RecognitionException {
		CaseElseStatContext _localctx = new CaseElseStatContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_caseElseStat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280);
			match(Else);
			setState(281);
			((CaseElseStatContext)_localctx).expression = expression();
			((CaseElseStatContext)_localctx).e =  ((CaseElseStatContext)_localctx).expression.e; 
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

	public static class ExprListContext extends ParserRuleContext {
		public List<LogicalExpression> listE;
		public ExpressionContext e1;
		public ExpressionContext e2;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(ExprParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(ExprParser.Comma, i);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitExprList(this);
		}
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_exprList);

			  ((ExprListContext)_localctx).listE =  new ArrayList<>();
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			((ExprListContext)_localctx).e1 = expression();
			_localctx.listE.add(((ExprListContext)_localctx).e1.e); 
			setState(292);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(286);
				match(Comma);
				setState(287);
				((ExprListContext)_localctx).e2 = expression();
				_localctx.listE.add(((ExprListContext)_localctx).e2.e); 
				}
				}
				setState(294);
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

	public static class ExpressionContext extends ParserRuleContext {
		public LogicalExpression e;
		public IfStatementContext ifStatement;
		public CaseStatementContext caseStatement;
		public CondExprContext condExpr;
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public CaseStatementContext caseStatement() {
			return getRuleContext(CaseStatementContext.class,0);
		}
		public CondExprContext condExpr() {
			return getRuleContext(CondExprContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_expression);
		try {
			setState(304);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case If:
				enterOuterAlt(_localctx, 1);
				{
				setState(295);
				((ExpressionContext)_localctx).ifStatement = ifStatement();
				((ExpressionContext)_localctx).e =  ((ExpressionContext)_localctx).ifStatement.e; 
				}
				break;
			case Case:
				enterOuterAlt(_localctx, 2);
				{
				setState(298);
				((ExpressionContext)_localctx).caseStatement = caseStatement();
				((ExpressionContext)_localctx).e =  ((ExpressionContext)_localctx).caseStatement.e; 
				}
				break;
			case Cast:
			case Convert:
			case AnyValue:
			case Excl:
			case Plus:
			case Minus:
			case OParen:
			case SingleQuote:
			case Bool:
			case Number:
			case Identifier:
			case QuotedIdentifier:
			case String:
				enterOuterAlt(_localctx, 3);
				{
				setState(301);
				((ExpressionContext)_localctx).condExpr = condExpr();
				((ExpressionContext)_localctx).e =  ((ExpressionContext)_localctx).condExpr.e; 
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

	public static class CondExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public OrExprContext orExpr;
		public OrExprContext orExpr() {
			return getRuleContext(OrExprContext.class,0);
		}
		public CondExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterCondExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitCondExpr(this);
		}
	}

	public final CondExprContext condExpr() throws RecognitionException {
		CondExprContext _localctx = new CondExprContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_condExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			((CondExprContext)_localctx).orExpr = orExpr();
			((CondExprContext)_localctx).e =  ((CondExprContext)_localctx).orExpr.e; 
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

	public static class OrExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public AndExprContext a1;
		public AndExprContext a2;
		public List<AndExprContext> andExpr() {
			return getRuleContexts(AndExprContext.class);
		}
		public AndExprContext andExpr(int i) {
			return getRuleContext(AndExprContext.class,i);
		}
		public List<TerminalNode> Or() { return getTokens(ExprParser.Or); }
		public TerminalNode Or(int i) {
			return getToken(ExprParser.Or, i);
		}
		public OrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitOrExpr(this);
		}
	}

	public final OrExprContext orExpr() throws RecognitionException {
		OrExprContext _localctx = new OrExprContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_orExpr);

			  List<LogicalExpression> exprs = new ArrayList<>();
			  ExpressionPosition p = null;
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			((OrExprContext)_localctx).a1 = andExpr();
			 exprs.add(((OrExprContext)_localctx).a1.e); p = pos( (((OrExprContext)_localctx).a1!=null?(((OrExprContext)_localctx).a1.start):null) );
			setState(317);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Or) {
				{
				{
				setState(311);
				match(Or);
				setState(312);
				((OrExprContext)_localctx).a2 = andExpr();
				 exprs.add(((OrExprContext)_localctx).a2.e); 
				}
				}
				setState(319);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			_ctx.stop = _input.LT(-1);

				  if(exprs.size() == 1){
				    ((OrExprContext)_localctx).e =  exprs.get(0);
				  }else{
				    ((OrExprContext)_localctx).e =  FunctionCallFactory.createBooleanOperator("or", p, exprs);
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

	public static class AndExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public EquExprContext e1;
		public EquExprContext e2;
		public List<EquExprContext> equExpr() {
			return getRuleContexts(EquExprContext.class);
		}
		public EquExprContext equExpr(int i) {
			return getRuleContext(EquExprContext.class,i);
		}
		public List<TerminalNode> And() { return getTokens(ExprParser.And); }
		public TerminalNode And(int i) {
			return getToken(ExprParser.And, i);
		}
		public AndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitAndExpr(this);
		}
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_andExpr);

			  List<LogicalExpression> exprs = new ArrayList<>();
			  ExpressionPosition p = null;
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			((AndExprContext)_localctx).e1 = equExpr();
			 exprs.add(((AndExprContext)_localctx).e1.e); p = pos( (((AndExprContext)_localctx).e1!=null?(((AndExprContext)_localctx).e1.start):null) );  
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==And) {
				{
				{
				setState(322);
				match(And);
				setState(323);
				((AndExprContext)_localctx).e2 = equExpr();
				 exprs.add(((AndExprContext)_localctx).e2.e);  
				}
				}
				setState(330);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			_ctx.stop = _input.LT(-1);

				  if(exprs.size() == 1){
				    ((AndExprContext)_localctx).e =  exprs.get(0);
				  }else{
				    ((AndExprContext)_localctx).e =  FunctionCallFactory.createBooleanOperator("and", p, exprs);
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

	public static class EquExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public RelExprContext r1;
		public Token cmpr;
		public RelExprContext r2;
		public List<RelExprContext> relExpr() {
			return getRuleContexts(RelExprContext.class);
		}
		public RelExprContext relExpr(int i) {
			return getRuleContext(RelExprContext.class,i);
		}
		public List<TerminalNode> Equals() { return getTokens(ExprParser.Equals); }
		public TerminalNode Equals(int i) {
			return getToken(ExprParser.Equals, i);
		}
		public List<TerminalNode> NEquals() { return getTokens(ExprParser.NEquals); }
		public TerminalNode NEquals(int i) {
			return getToken(ExprParser.NEquals, i);
		}
		public EquExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterEquExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitEquExpr(this);
		}
	}

	public final EquExprContext equExpr() throws RecognitionException {
		EquExprContext _localctx = new EquExprContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_equExpr);

			  List<LogicalExpression> exprs = new ArrayList<>();
			  List<String> cmps = new ArrayList<>();
			  ExpressionPosition p = null;
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(331);
			((EquExprContext)_localctx).r1 = relExpr();
			 exprs.add(((EquExprContext)_localctx).r1.e); p = pos( (((EquExprContext)_localctx).r1!=null?(((EquExprContext)_localctx).r1.start):null) );
			    
			setState(339);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Equals || _la==NEquals) {
				{
				{
				setState(333);
				((EquExprContext)_localctx).cmpr = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==Equals || _la==NEquals) ) {
					((EquExprContext)_localctx).cmpr = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(334);
				((EquExprContext)_localctx).r2 = relExpr();
				exprs.add(((EquExprContext)_localctx).r2.e); cmps.add((((EquExprContext)_localctx).cmpr!=null?((EquExprContext)_localctx).cmpr.getText():null)); 
				}
				}
				setState(341);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			_ctx.stop = _input.LT(-1);

				  ((EquExprContext)_localctx).e =  FunctionCallFactory.createByOp(exprs, p, cmps);
				
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

	public static class RelExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public AddExprContext left;
		public Token cmpr;
		public AddExprContext right;
		public List<AddExprContext> addExpr() {
			return getRuleContexts(AddExprContext.class);
		}
		public AddExprContext addExpr(int i) {
			return getRuleContext(AddExprContext.class,i);
		}
		public TerminalNode GTEquals() { return getToken(ExprParser.GTEquals, 0); }
		public TerminalNode LTEquals() { return getToken(ExprParser.LTEquals, 0); }
		public TerminalNode GT() { return getToken(ExprParser.GT, 0); }
		public TerminalNode LT() { return getToken(ExprParser.LT, 0); }
		public RelExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterRelExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitRelExpr(this);
		}
	}

	public final RelExprContext relExpr() throws RecognitionException {
		RelExprContext _localctx = new RelExprContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_relExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342);
			((RelExprContext)_localctx).left = addExpr();
			((RelExprContext)_localctx).e =  ((RelExprContext)_localctx).left.e; 
			setState(348);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GTEquals) | (1L << LTEquals) | (1L << GT) | (1L << LT))) != 0)) {
				{
				setState(344);
				((RelExprContext)_localctx).cmpr = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GTEquals) | (1L << LTEquals) | (1L << GT) | (1L << LT))) != 0)) ) {
					((RelExprContext)_localctx).cmpr = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(345);
				((RelExprContext)_localctx).right = addExpr();
				((RelExprContext)_localctx).e =  FunctionCallFactory.createExpression((((RelExprContext)_localctx).cmpr!=null?((RelExprContext)_localctx).cmpr.getText():null), pos((((RelExprContext)_localctx).left!=null?(((RelExprContext)_localctx).left.start):null)), ((RelExprContext)_localctx).left.e, ((RelExprContext)_localctx).right.e); 
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

	public static class AddExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public MulExprContext m1;
		public Token op;
		public MulExprContext m2;
		public List<MulExprContext> mulExpr() {
			return getRuleContexts(MulExprContext.class);
		}
		public MulExprContext mulExpr(int i) {
			return getRuleContext(MulExprContext.class,i);
		}
		public List<TerminalNode> Plus() { return getTokens(ExprParser.Plus); }
		public TerminalNode Plus(int i) {
			return getToken(ExprParser.Plus, i);
		}
		public List<TerminalNode> Minus() { return getTokens(ExprParser.Minus); }
		public TerminalNode Minus(int i) {
			return getToken(ExprParser.Minus, i);
		}
		public AddExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterAddExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitAddExpr(this);
		}
	}

	public final AddExprContext addExpr() throws RecognitionException {
		AddExprContext _localctx = new AddExprContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_addExpr);

			  List<LogicalExpression> exprs = new ArrayList<>();
			  List<String> ops = new ArrayList<>();
			  ExpressionPosition p = null;
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			((AddExprContext)_localctx).m1 = mulExpr();
			exprs.add(((AddExprContext)_localctx).m1.e); p = pos((((AddExprContext)_localctx).m1!=null?(((AddExprContext)_localctx).m1.start):null)); 
			setState(358);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Plus || _la==Minus) {
				{
				{
				setState(352);
				((AddExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==Plus || _la==Minus) ) {
					((AddExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(353);
				((AddExprContext)_localctx).m2 = mulExpr();
				exprs.add(((AddExprContext)_localctx).m2.e); ops.add((((AddExprContext)_localctx).op!=null?((AddExprContext)_localctx).op.getText():null)); 
				}
				}
				setState(360);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			_ctx.stop = _input.LT(-1);

				  ((AddExprContext)_localctx).e =  FunctionCallFactory.createByOp(exprs, p, ops);
				
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

	public static class MulExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public XorExprContext p1;
		public Token op;
		public XorExprContext p2;
		public List<XorExprContext> xorExpr() {
			return getRuleContexts(XorExprContext.class);
		}
		public XorExprContext xorExpr(int i) {
			return getRuleContext(XorExprContext.class,i);
		}
		public List<TerminalNode> Asterisk() { return getTokens(ExprParser.Asterisk); }
		public TerminalNode Asterisk(int i) {
			return getToken(ExprParser.Asterisk, i);
		}
		public List<TerminalNode> ForwardSlash() { return getTokens(ExprParser.ForwardSlash); }
		public TerminalNode ForwardSlash(int i) {
			return getToken(ExprParser.ForwardSlash, i);
		}
		public List<TerminalNode> Percent() { return getTokens(ExprParser.Percent); }
		public TerminalNode Percent(int i) {
			return getToken(ExprParser.Percent, i);
		}
		public MulExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mulExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterMulExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitMulExpr(this);
		}
	}

	public final MulExprContext mulExpr() throws RecognitionException {
		MulExprContext _localctx = new MulExprContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_mulExpr);

			  List<LogicalExpression> exprs = new ArrayList<>();
			  List<String> ops = new ArrayList<>();
			  ExpressionPosition p = null;
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(361);
			((MulExprContext)_localctx).p1 = xorExpr();
			exprs.add(((MulExprContext)_localctx).p1.e); p = pos((((MulExprContext)_localctx).p1!=null?(((MulExprContext)_localctx).p1.start):null));
			setState(369);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Asterisk) | (1L << ForwardSlash) | (1L << Percent))) != 0)) {
				{
				{
				setState(363);
				((MulExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Asterisk) | (1L << ForwardSlash) | (1L << Percent))) != 0)) ) {
					((MulExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(364);
				((MulExprContext)_localctx).p2 = xorExpr();
				exprs.add(((MulExprContext)_localctx).p2.e); ops.add((((MulExprContext)_localctx).op!=null?((MulExprContext)_localctx).op.getText():null)); 
				}
				}
				setState(371);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			_ctx.stop = _input.LT(-1);

				  ((MulExprContext)_localctx).e =  FunctionCallFactory.createByOp(exprs, p, ops);
				
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

	public static class XorExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public UnaryExprContext u1;
		public Token Caret;
		public UnaryExprContext u2;
		public List<UnaryExprContext> unaryExpr() {
			return getRuleContexts(UnaryExprContext.class);
		}
		public UnaryExprContext unaryExpr(int i) {
			return getRuleContext(UnaryExprContext.class,i);
		}
		public List<TerminalNode> Caret() { return getTokens(ExprParser.Caret); }
		public TerminalNode Caret(int i) {
			return getToken(ExprParser.Caret, i);
		}
		public XorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterXorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitXorExpr(this);
		}
	}

	public final XorExprContext xorExpr() throws RecognitionException {
		XorExprContext _localctx = new XorExprContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_xorExpr);

		    List<LogicalExpression> exprs = new ArrayList<>();
		    List<String> ops = new ArrayList<>();
			  ExpressionPosition p = null;
			
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			((XorExprContext)_localctx).u1 = unaryExpr();
			exprs.add(((XorExprContext)_localctx).u1.e); p = pos((((XorExprContext)_localctx).u1!=null?(((XorExprContext)_localctx).u1.start):null));
			setState(380);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Caret) {
				{
				{
				setState(374);
				((XorExprContext)_localctx).Caret = match(Caret);
				setState(375);
				((XorExprContext)_localctx).u2 = unaryExpr();
				exprs.add(((XorExprContext)_localctx).u2.e); ops.add((((XorExprContext)_localctx).Caret!=null?((XorExprContext)_localctx).Caret.getText():null));
				}
				}
				setState(382);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			_ctx.stop = _input.LT(-1);

				  ((XorExprContext)_localctx).e =  FunctionCallFactory.createByOp(exprs, p, ops);
				
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

	public static class UnaryExprContext extends ParserRuleContext {
		public LogicalExpression e;
		public Token sign;
		public Token Number;
		public Token Minus;
		public AtomContext atom;
		public Token Excl;
		public TerminalNode Number() { return getToken(ExprParser.Number, 0); }
		public TerminalNode Plus() { return getToken(ExprParser.Plus, 0); }
		public TerminalNode Minus() { return getToken(ExprParser.Minus, 0); }
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public TerminalNode Excl() { return getToken(ExprParser.Excl, 0); }
		public UnaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterUnaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitUnaryExpr(this);
		}
	}

	public final UnaryExprContext unaryExpr() throws RecognitionException {
		UnaryExprContext _localctx = new UnaryExprContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_unaryExpr);
		int _la;
		try {
			setState(399);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(384);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Plus || _la==Minus) {
					{
					setState(383);
					((UnaryExprContext)_localctx).sign = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==Plus || _la==Minus) ) {
						((UnaryExprContext)_localctx).sign = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(386);
				((UnaryExprContext)_localctx).Number = match(Number);
				((UnaryExprContext)_localctx).e =  ValueExpressions.getNumericExpression((((UnaryExprContext)_localctx).sign!=null?((UnaryExprContext)_localctx).sign.getText():null), (((UnaryExprContext)_localctx).Number!=null?((UnaryExprContext)_localctx).Number.getText():null), pos((((UnaryExprContext)_localctx).sign != null) ? ((UnaryExprContext)_localctx).sign : ((UnaryExprContext)_localctx).Number)); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(388);
				((UnaryExprContext)_localctx).Minus = match(Minus);
				setState(389);
				((UnaryExprContext)_localctx).atom = atom();
				((UnaryExprContext)_localctx).e =  FunctionCallFactory.createExpression("u-", pos(((UnaryExprContext)_localctx).Minus), ((UnaryExprContext)_localctx).atom.e); 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(392);
				((UnaryExprContext)_localctx).Excl = match(Excl);
				setState(393);
				((UnaryExprContext)_localctx).atom = atom();
				((UnaryExprContext)_localctx).e =  FunctionCallFactory.createExpression("!", pos(((UnaryExprContext)_localctx).Excl), ((UnaryExprContext)_localctx).atom.e); 
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(396);
				((UnaryExprContext)_localctx).atom = atom();
				((UnaryExprContext)_localctx).e =  ((UnaryExprContext)_localctx).atom.e; 
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

	public static class AtomContext extends ParserRuleContext {
		public LogicalExpression e;
		public Token Bool;
		public LookupContext lookup;
		public TerminalNode Bool() { return getToken(ExprParser.Bool, 0); }
		public LookupContext lookup() {
			return getRuleContext(LookupContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitAtom(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_atom);
		try {
			setState(406);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
				enterOuterAlt(_localctx, 1);
				{
				setState(401);
				((AtomContext)_localctx).Bool = match(Bool);
				((AtomContext)_localctx).e =  new ValueExpressions.BooleanExpression((((AtomContext)_localctx).Bool!=null?((AtomContext)_localctx).Bool.getText():null), pos(((AtomContext)_localctx).Bool)); 
				}
				break;
			case Cast:
			case Convert:
			case AnyValue:
			case OParen:
			case SingleQuote:
			case Identifier:
			case QuotedIdentifier:
			case String:
				enterOuterAlt(_localctx, 2);
				{
				setState(403);
				((AtomContext)_localctx).lookup = lookup();
				((AtomContext)_localctx).e =  ((AtomContext)_localctx).lookup.e; 
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

	public static class PathSegmentContext extends ParserRuleContext {
		public NameSegment seg;
		public NameSegmentContext s1;
		public NameSegmentContext nameSegment() {
			return getRuleContext(NameSegmentContext.class,0);
		}
		public PathSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterPathSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitPathSegment(this);
		}
	}

	public final PathSegmentContext pathSegment() throws RecognitionException {
		PathSegmentContext _localctx = new PathSegmentContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_pathSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			((PathSegmentContext)_localctx).s1 = nameSegment();
			((PathSegmentContext)_localctx).seg =  ((PathSegmentContext)_localctx).s1.seg;
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

	public static class NameSegmentContext extends ParserRuleContext {
		public NameSegment seg;
		public Token QuotedIdentifier;
		public PathSegmentContext s1;
		public ArraySegmentContext s2;
		public Token Identifier;
		public TerminalNode QuotedIdentifier() { return getToken(ExprParser.QuotedIdentifier, 0); }
		public ArraySegmentContext arraySegment() {
			return getRuleContext(ArraySegmentContext.class,0);
		}
		public TerminalNode Period() { return getToken(ExprParser.Period, 0); }
		public PathSegmentContext pathSegment() {
			return getRuleContext(PathSegmentContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(ExprParser.Identifier, 0); }
		public NameSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nameSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterNameSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitNameSegment(this);
		}
	}

	public final NameSegmentContext nameSegment() throws RecognitionException {
		NameSegmentContext _localctx = new NameSegmentContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_nameSegment);
		try {
			setState(425);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QuotedIdentifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(411);
				((NameSegmentContext)_localctx).QuotedIdentifier = match(QuotedIdentifier);
				setState(415);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Period:
					{
					{
					setState(412);
					match(Period);
					setState(413);
					((NameSegmentContext)_localctx).s1 = pathSegment();
					}
					}
					break;
				case OBracket:
					{
					setState(414);
					((NameSegmentContext)_localctx).s2 = arraySegment();
					}
					break;
				case EOF:
				case Else:
				case Then:
				case End:
				case When:
				case As:
				case Or:
				case And:
				case Equals:
				case NEquals:
				case GTEquals:
				case LTEquals:
				case Caret:
				case GT:
				case LT:
				case Plus:
				case Minus:
				case Asterisk:
				case ForwardSlash:
				case Percent:
				case CParen:
				case Comma:
					break;
				default:
					break;
				}

				    if (((NameSegmentContext)_localctx).s1 == null && ((NameSegmentContext)_localctx).s2 == null) {
				      ((NameSegmentContext)_localctx).seg =  new NameSegment((((NameSegmentContext)_localctx).QuotedIdentifier!=null?((NameSegmentContext)_localctx).QuotedIdentifier.getText():null));
				    } else {
				      ((NameSegmentContext)_localctx).seg =  new NameSegment((((NameSegmentContext)_localctx).QuotedIdentifier!=null?((NameSegmentContext)_localctx).QuotedIdentifier.getText():null), (((NameSegmentContext)_localctx).s1 == null ? ((NameSegmentContext)_localctx).s2.seg : ((NameSegmentContext)_localctx).s1.seg));
				    }
				  
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(418);
				((NameSegmentContext)_localctx).Identifier = match(Identifier);
				setState(422);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Period:
					{
					{
					setState(419);
					match(Period);
					setState(420);
					((NameSegmentContext)_localctx).s1 = pathSegment();
					}
					}
					break;
				case OBracket:
					{
					setState(421);
					((NameSegmentContext)_localctx).s2 = arraySegment();
					}
					break;
				case EOF:
				case Else:
				case Then:
				case End:
				case When:
				case As:
				case Or:
				case And:
				case Equals:
				case NEquals:
				case GTEquals:
				case LTEquals:
				case Caret:
				case GT:
				case LT:
				case Plus:
				case Minus:
				case Asterisk:
				case ForwardSlash:
				case Percent:
				case CParen:
				case Comma:
					break;
				default:
					break;
				}

				    if (((NameSegmentContext)_localctx).s1 == null && ((NameSegmentContext)_localctx).s2 == null) {
				      ((NameSegmentContext)_localctx).seg =  new NameSegment((((NameSegmentContext)_localctx).Identifier!=null?((NameSegmentContext)_localctx).Identifier.getText():null));
				    } else {
				      ((NameSegmentContext)_localctx).seg =  new NameSegment((((NameSegmentContext)_localctx).Identifier!=null?((NameSegmentContext)_localctx).Identifier.getText():null), (((NameSegmentContext)_localctx).s1 == null ? ((NameSegmentContext)_localctx).s2.seg : ((NameSegmentContext)_localctx).s1.seg));
				    }
				   
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

	public static class ArraySegmentContext extends ParserRuleContext {
		public PathSegment seg;
		public Token Number;
		public PathSegmentContext s1;
		public ArraySegmentContext s2;
		public TerminalNode OBracket() { return getToken(ExprParser.OBracket, 0); }
		public TerminalNode Number() { return getToken(ExprParser.Number, 0); }
		public TerminalNode CBracket() { return getToken(ExprParser.CBracket, 0); }
		public ArraySegmentContext arraySegment() {
			return getRuleContext(ArraySegmentContext.class,0);
		}
		public TerminalNode Period() { return getToken(ExprParser.Period, 0); }
		public PathSegmentContext pathSegment() {
			return getRuleContext(PathSegmentContext.class,0);
		}
		public ArraySegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arraySegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterArraySegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitArraySegment(this);
		}
	}

	public final ArraySegmentContext arraySegment() throws RecognitionException {
		ArraySegmentContext _localctx = new ArraySegmentContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_arraySegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
			match(OBracket);
			setState(428);
			((ArraySegmentContext)_localctx).Number = match(Number);
			setState(429);
			match(CBracket);
			setState(433);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Period:
				{
				{
				setState(430);
				match(Period);
				setState(431);
				((ArraySegmentContext)_localctx).s1 = pathSegment();
				}
				}
				break;
			case OBracket:
				{
				setState(432);
				((ArraySegmentContext)_localctx).s2 = arraySegment();
				}
				break;
			case EOF:
			case Else:
			case Then:
			case End:
			case When:
			case As:
			case Or:
			case And:
			case Equals:
			case NEquals:
			case GTEquals:
			case LTEquals:
			case Caret:
			case GT:
			case LT:
			case Plus:
			case Minus:
			case Asterisk:
			case ForwardSlash:
			case Percent:
			case CParen:
			case Comma:
				break;
			default:
				break;
			}

			    if (((ArraySegmentContext)_localctx).s1 == null && ((ArraySegmentContext)_localctx).s2 == null) {
			      ((ArraySegmentContext)_localctx).seg =  new ArraySegment((((ArraySegmentContext)_localctx).Number!=null?((ArraySegmentContext)_localctx).Number.getText():null));
			    } else {
			      ((ArraySegmentContext)_localctx).seg =  new ArraySegment((((ArraySegmentContext)_localctx).Number!=null?((ArraySegmentContext)_localctx).Number.getText():null), (((ArraySegmentContext)_localctx).s1 == null ? ((ArraySegmentContext)_localctx).s2.seg : ((ArraySegmentContext)_localctx).s1.seg));
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

	public static class LookupContext extends ParserRuleContext {
		public LogicalExpression e;
		public FunctionCallContext functionCall;
		public ConvertCallContext convertCall;
		public AnyValueCallContext anyValueCall;
		public CastCallContext castCall;
		public PathSegmentContext pathSegment;
		public Token String;
		public ExpressionContext expression;
		public Token Identifier;
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public ConvertCallContext convertCall() {
			return getRuleContext(ConvertCallContext.class,0);
		}
		public AnyValueCallContext anyValueCall() {
			return getRuleContext(AnyValueCallContext.class,0);
		}
		public CastCallContext castCall() {
			return getRuleContext(CastCallContext.class,0);
		}
		public PathSegmentContext pathSegment() {
			return getRuleContext(PathSegmentContext.class,0);
		}
		public TerminalNode String() { return getToken(ExprParser.String, 0); }
		public TerminalNode OParen() { return getToken(ExprParser.OParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode CParen() { return getToken(ExprParser.CParen, 0); }
		public List<TerminalNode> SingleQuote() { return getTokens(ExprParser.SingleQuote); }
		public TerminalNode SingleQuote(int i) {
			return getToken(ExprParser.SingleQuote, i);
		}
		public TerminalNode Identifier() { return getToken(ExprParser.Identifier, 0); }
		public LookupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lookup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).enterLookup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprParserListener ) ((ExprParserListener)listener).exitLookup(this);
		}
	}

	public final LookupContext lookup() throws RecognitionException {
		LookupContext _localctx = new LookupContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_lookup);
		try {
			setState(463);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(437);
				((LookupContext)_localctx).functionCall = functionCall();
				((LookupContext)_localctx).e =  ((LookupContext)_localctx).functionCall.e ;
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(440);
				((LookupContext)_localctx).convertCall = convertCall();
				((LookupContext)_localctx).e =  ((LookupContext)_localctx).convertCall.e; 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(443);
				((LookupContext)_localctx).anyValueCall = anyValueCall();
				((LookupContext)_localctx).e =  ((LookupContext)_localctx).anyValueCall.e; 
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(446);
				((LookupContext)_localctx).castCall = castCall();
				((LookupContext)_localctx).e =  ((LookupContext)_localctx).castCall.e; 
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(449);
				((LookupContext)_localctx).pathSegment = pathSegment();
				((LookupContext)_localctx).e =  new SchemaPath(((LookupContext)_localctx).pathSegment.seg, pos((((LookupContext)_localctx).pathSegment!=null?(((LookupContext)_localctx).pathSegment.start):null)) ); 
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(452);
				((LookupContext)_localctx).String = match(String);
				((LookupContext)_localctx).e =  new ValueExpressions.QuotedString((((LookupContext)_localctx).String!=null?((LookupContext)_localctx).String.getText():null), (((LookupContext)_localctx).String!=null?((LookupContext)_localctx).String.getText():null).length(), pos(((LookupContext)_localctx).String) ); 
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(454);
				match(OParen);
				setState(455);
				((LookupContext)_localctx).expression = expression();
				setState(456);
				match(CParen);
				((LookupContext)_localctx).e =  ((LookupContext)_localctx).expression.e; 
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(459);
				match(SingleQuote);
				setState(460);
				((LookupContext)_localctx).Identifier = match(Identifier);
				setState(461);
				match(SingleQuote);
				((LookupContext)_localctx).e =  new SchemaPath((((LookupContext)_localctx).Identifier!=null?((LookupContext)_localctx).Identifier.getText():null), pos(((LookupContext)_localctx).Identifier) ); 
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3H\u01d4\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\3\2\3\2\3\2\3\2\3\3\3\3\3\3\5\3R\n\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\5\5b\n\5\3\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\3\6\3\6\5\6m\n\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u0081\n\b\3\t\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00c6\n\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\5\13\u00d0\n\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16"+
		"\u00e6\n\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\7\20\u00f2"+
		"\n\20\f\20\16\20\u00f5\13\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\6"+
		"\23\u010d\n\23\r\23\16\23\u010e\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\7\26\u0125"+
		"\n\26\f\26\16\26\u0128\13\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3"+
		"\27\5\27\u0133\n\27\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\7\31"+
		"\u013e\n\31\f\31\16\31\u0141\13\31\3\32\3\32\3\32\3\32\3\32\3\32\7\32"+
		"\u0149\n\32\f\32\16\32\u014c\13\32\3\33\3\33\3\33\3\33\3\33\3\33\7\33"+
		"\u0154\n\33\f\33\16\33\u0157\13\33\3\34\3\34\3\34\3\34\3\34\3\34\5\34"+
		"\u015f\n\34\3\35\3\35\3\35\3\35\3\35\3\35\7\35\u0167\n\35\f\35\16\35\u016a"+
		"\13\35\3\36\3\36\3\36\3\36\3\36\3\36\7\36\u0172\n\36\f\36\16\36\u0175"+
		"\13\36\3\37\3\37\3\37\3\37\3\37\3\37\7\37\u017d\n\37\f\37\16\37\u0180"+
		"\13\37\3 \5 \u0183\n \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \5 \u0192"+
		"\n \3!\3!\3!\3!\3!\5!\u0199\n!\3\"\3\"\3\"\3#\3#\3#\3#\5#\u01a2\n#\3#"+
		"\3#\3#\3#\3#\5#\u01a9\n#\3#\5#\u01ac\n#\3$\3$\3$\3$\3$\3$\5$\u01b4\n$"+
		"\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%"+
		"\3%\3%\3%\3%\3%\5%\u01d2\n%\3%\2\2&\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,.\60\62\64\668:<>@BDFH\2\6\3\2)*\4\2+,/\60\3\2\61\62\3\2\63"+
		"\65\2\u01e5\2J\3\2\2\2\4N\3\2\2\2\6V\3\2\2\2\b^\3\2\2\2\nf\3\2\2\2\fq"+
		"\3\2\2\2\16\u0080\3\2\2\2\20\u0082\3\2\2\2\22\u00c5\3\2\2\2\24\u00cf\3"+
		"\2\2\2\26\u00d1\3\2\2\2\30\u00d4\3\2\2\2\32\u00e5\3\2\2\2\34\u00e7\3\2"+
		"\2\2\36\u00ec\3\2\2\2 \u00fb\3\2\2\2\"\u0101\3\2\2\2$\u0108\3\2\2\2&\u0114"+
		"\3\2\2\2(\u011a\3\2\2\2*\u011e\3\2\2\2,\u0132\3\2\2\2.\u0134\3\2\2\2\60"+
		"\u0137\3\2\2\2\62\u0142\3\2\2\2\64\u014d\3\2\2\2\66\u0158\3\2\2\28\u0160"+
		"\3\2\2\2:\u016b\3\2\2\2<\u0176\3\2\2\2>\u0191\3\2\2\2@\u0198\3\2\2\2B"+
		"\u019a\3\2\2\2D\u01ab\3\2\2\2F\u01ad\3\2\2\2H\u01d1\3\2\2\2JK\5,\27\2"+
		"KL\7\2\2\3LM\b\2\1\2M\3\3\2\2\2NO\7C\2\2OQ\7:\2\2PR\5*\26\2QP\3\2\2\2"+
		"QR\3\2\2\2RS\3\2\2\2ST\7;\2\2TU\b\3\1\2U\5\3\2\2\2VW\7\f\2\2WX\7:\2\2"+
		"XY\5,\27\2YZ\7=\2\2Z[\7E\2\2[\\\7;\2\2\\]\b\4\1\2]\7\3\2\2\2^_\7\r\2\2"+
		"_a\7:\2\2`b\5*\26\2a`\3\2\2\2ab\3\2\2\2bc\3\2\2\2cd\7;\2\2de\b\5\1\2e"+
		"\t\3\2\2\2fg\7\13\2\2gh\7:\2\2hi\5,\27\2ij\7\20\2\2jl\5\16\b\2km\5\f\7"+
		"\2lk\3\2\2\2lm\3\2\2\2mn\3\2\2\2no\7;\2\2op\b\6\1\2p\13\3\2\2\2qr\7\17"+
		"\2\2rs\b\7\1\2s\r\3\2\2\2tu\5\22\n\2uv\b\b\1\2v\u0081\3\2\2\2wx\5\24\13"+
		"\2xy\b\b\1\2y\u0081\3\2\2\2z{\5\32\16\2{|\b\b\1\2|\u0081\3\2\2\2}~\5\20"+
		"\t\2~\177\b\b\1\2\177\u0081\3\2\2\2\u0080t\3\2\2\2\u0080w\3\2\2\2\u0080"+
		"z\3\2\2\2\u0080}\3\2\2\2\u0081\17\3\2\2\2\u0082\u0083\7\21\2\2\u0083\u0084"+
		"\b\t\1\2\u0084\21\3\2\2\2\u0085\u0086\7\22\2\2\u0086\u00c6\b\n\1\2\u0087"+
		"\u0088\7\23\2\2\u0088\u00c6\b\n\1\2\u0089\u008a\7\24\2\2\u008a\u00c6\b"+
		"\n\1\2\u008b\u008c\7\25\2\2\u008c\u00c6\b\n\1\2\u008d\u008e\7 \2\2\u008e"+
		"\u008f\7:\2\2\u008f\u0090\5\26\f\2\u0090\u0091\7=\2\2\u0091\u0092\5\30"+
		"\r\2\u0092\u0093\7;\2\2\u0093\u0094\b\n\1\2\u0094\u00c6\3\2\2\2\u0095"+
		"\u0096\7!\2\2\u0096\u0097\7:\2\2\u0097\u0098\5\26\f\2\u0098\u0099\7=\2"+
		"\2\u0099\u009a\5\30\r\2\u009a\u009b\7;\2\2\u009b\u009c\b\n\1\2\u009c\u00c6"+
		"\3\2\2\2\u009d\u009e\7\"\2\2\u009e\u009f\7:\2\2\u009f\u00a0\5\26\f\2\u00a0"+
		"\u00a1\7=\2\2\u00a1\u00a2\5\30\r\2\u00a2\u00a3\7;\2\2\u00a3\u00a4\b\n"+
		"\1\2\u00a4\u00c6\3\2\2\2\u00a5\u00a6\7#\2\2\u00a6\u00a7\7:\2\2\u00a7\u00a8"+
		"\5\26\f\2\u00a8\u00a9\7=\2\2\u00a9\u00aa\5\30\r\2\u00aa\u00ab\7;\2\2\u00ab"+
		"\u00ac\b\n\1\2\u00ac\u00c6\3\2\2\2\u00ad\u00ae\7$\2\2\u00ae\u00af\7:\2"+
		"\2\u00af\u00b0\5\26\f\2\u00b0\u00b1\7=\2\2\u00b1\u00b2\5\30\r\2\u00b2"+
		"\u00b3\7;\2\2\u00b3\u00b4\b\n\1\2\u00b4\u00c6\3\2\2\2\u00b5\u00b6\7%\2"+
		"\2\u00b6\u00b7\7:\2\2\u00b7\u00b8\5\26\f\2\u00b8\u00b9\7=\2\2\u00b9\u00ba"+
		"\5\30\r\2\u00ba\u00bb\7;\2\2\u00bb\u00bc\b\n\1\2\u00bc\u00c6\3\2\2\2\u00bd"+
		"\u00be\7&\2\2\u00be\u00bf\7:\2\2\u00bf\u00c0\5\26\f\2\u00c0\u00c1\7=\2"+
		"\2\u00c1\u00c2\5\30\r\2\u00c2\u00c3\7;\2\2\u00c3\u00c4\b\n\1\2\u00c4\u00c6"+
		"\3\2\2\2\u00c5\u0085\3\2\2\2\u00c5\u0087\3\2\2\2\u00c5\u0089\3\2\2\2\u00c5"+
		"\u008b\3\2\2\2\u00c5\u008d\3\2\2\2\u00c5\u0095\3\2\2\2\u00c5\u009d\3\2"+
		"\2\2\u00c5\u00a5\3\2\2\2\u00c5\u00ad\3\2\2\2\u00c5\u00b5\3\2\2\2\u00c5"+
		"\u00bd\3\2\2\2\u00c6\23\3\2\2\2\u00c7\u00c8\7\26\2\2\u00c8\u00c9\5\34"+
		"\17\2\u00c9\u00ca\b\13\1\2\u00ca\u00d0\3\2\2\2\u00cb\u00cc\7\27\2\2\u00cc"+
		"\u00cd\5\34\17\2\u00cd\u00ce\b\13\1\2\u00ce\u00d0\3\2\2\2\u00cf\u00c7"+
		"\3\2\2\2\u00cf\u00cb\3\2\2\2\u00d0\25\3\2\2\2\u00d1\u00d2\7B\2\2\u00d2"+
		"\u00d3\b\f\1\2\u00d3\27\3\2\2\2\u00d4\u00d5\7B\2\2\u00d5\u00d6\b\r\1\2"+
		"\u00d6\31\3\2\2\2\u00d7\u00d8\7\30\2\2\u00d8\u00e6\b\16\1\2\u00d9\u00da"+
		"\7\31\2\2\u00da\u00e6\b\16\1\2\u00db\u00dc\7\32\2\2\u00dc\u00e6\b\16\1"+
		"\2\u00dd\u00de\7\33\2\2\u00de\u00e6\b\16\1\2\u00df\u00e0\7\34\2\2\u00e0"+
		"\u00e6\b\16\1\2\u00e1\u00e2\7\35\2\2\u00e2\u00e6\b\16\1\2\u00e3\u00e4"+
		"\7\36\2\2\u00e4\u00e6\b\16\1\2\u00e5\u00d7\3\2\2\2\u00e5\u00d9\3\2\2\2"+
		"\u00e5\u00db\3\2\2\2\u00e5\u00dd\3\2\2\2\u00e5\u00df\3\2\2\2\u00e5\u00e1"+
		"\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\33\3\2\2\2\u00e7\u00e8\7:\2\2\u00e8"+
		"\u00e9\7B\2\2\u00e9\u00ea\7;\2\2\u00ea\u00eb\b\17\1\2\u00eb\35\3\2\2\2"+
		"\u00ec\u00ed\5 \21\2\u00ed\u00f3\b\20\1\2\u00ee\u00ef\5\"\22\2\u00ef\u00f0"+
		"\b\20\1\2\u00f0\u00f2\3\2\2\2\u00f1\u00ee\3\2\2\2\u00f2\u00f5\3\2\2\2"+
		"\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00f3"+
		"\3\2\2\2\u00f6\u00f7\7\4\2\2\u00f7\u00f8\5,\27\2\u00f8\u00f9\b\20\1\2"+
		"\u00f9\u00fa\7\7\2\2\u00fa\37\3\2\2\2\u00fb\u00fc\7\3\2\2\u00fc\u00fd"+
		"\5,\27\2\u00fd\u00fe\7\6\2\2\u00fe\u00ff\5,\27\2\u00ff\u0100\b\21\1\2"+
		"\u0100!\3\2\2\2\u0101\u0102\7\4\2\2\u0102\u0103\7\3\2\2\u0103\u0104\5"+
		",\27\2\u0104\u0105\7\6\2\2\u0105\u0106\5,\27\2\u0106\u0107\b\22\1\2\u0107"+
		"#\3\2\2\2\u0108\u010c\7\t\2\2\u0109\u010a\5&\24\2\u010a\u010b\b\23\1\2"+
		"\u010b\u010d\3\2\2\2\u010c\u0109\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u010c"+
		"\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0110\3\2\2\2\u0110\u0111\5(\25\2\u0111"+
		"\u0112\b\23\1\2\u0112\u0113\7\7\2\2\u0113%\3\2\2\2\u0114\u0115\7\n\2\2"+
		"\u0115\u0116\5,\27\2\u0116\u0117\7\6\2\2\u0117\u0118\5,\27\2\u0118\u0119"+
		"\b\24\1\2\u0119\'\3\2\2\2\u011a\u011b\7\4\2\2\u011b\u011c\5,\27\2\u011c"+
		"\u011d\b\25\1\2\u011d)\3\2\2\2\u011e\u011f\5,\27\2\u011f\u0126\b\26\1"+
		"\2\u0120\u0121\7=\2\2\u0121\u0122\5,\27\2\u0122\u0123\b\26\1\2\u0123\u0125"+
		"\3\2\2\2\u0124\u0120\3\2\2\2\u0125\u0128\3\2\2\2\u0126\u0124\3\2\2\2\u0126"+
		"\u0127\3\2\2\2\u0127+\3\2\2\2\u0128\u0126\3\2\2\2\u0129\u012a\5\36\20"+
		"\2\u012a\u012b\b\27\1\2\u012b\u0133\3\2\2\2\u012c\u012d\5$\23\2\u012d"+
		"\u012e\b\27\1\2\u012e\u0133\3\2\2\2\u012f\u0130\5.\30\2\u0130\u0131\b"+
		"\27\1\2\u0131\u0133\3\2\2\2\u0132\u0129\3\2\2\2\u0132\u012c\3\2\2\2\u0132"+
		"\u012f\3\2\2\2\u0133-\3\2\2\2\u0134\u0135\5\60\31\2\u0135\u0136\b\30\1"+
		"\2\u0136/\3\2\2\2\u0137\u0138\5\62\32\2\u0138\u013f\b\31\1\2\u0139\u013a"+
		"\7\'\2\2\u013a\u013b\5\62\32\2\u013b\u013c\b\31\1\2\u013c\u013e\3\2\2"+
		"\2\u013d\u0139\3\2\2\2\u013e\u0141\3\2\2\2\u013f\u013d\3\2\2\2\u013f\u0140"+
		"\3\2\2\2\u0140\61\3\2\2\2\u0141\u013f\3\2\2\2\u0142\u0143\5\64\33\2\u0143"+
		"\u014a\b\32\1\2\u0144\u0145\7(\2\2\u0145\u0146\5\64\33\2\u0146\u0147\b"+
		"\32\1\2\u0147\u0149\3\2\2\2\u0148\u0144\3\2\2\2\u0149\u014c\3\2\2\2\u014a"+
		"\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b\63\3\2\2\2\u014c\u014a\3\2\2"+
		"\2\u014d\u014e\5\66\34\2\u014e\u0155\b\33\1\2\u014f\u0150\t\2\2\2\u0150"+
		"\u0151\5\66\34\2\u0151\u0152\b\33\1\2\u0152\u0154\3\2\2\2\u0153\u014f"+
		"\3\2\2\2\u0154\u0157\3\2\2\2\u0155\u0153\3\2\2\2\u0155\u0156\3\2\2\2\u0156"+
		"\65\3\2\2\2\u0157\u0155\3\2\2\2\u0158\u0159\58\35\2\u0159\u015e\b\34\1"+
		"\2\u015a\u015b\t\3\2\2\u015b\u015c\58\35\2\u015c\u015d\b\34\1\2\u015d"+
		"\u015f\3\2\2\2\u015e\u015a\3\2\2\2\u015e\u015f\3\2\2\2\u015f\67\3\2\2"+
		"\2\u0160\u0161\5:\36\2\u0161\u0168\b\35\1\2\u0162\u0163\t\4\2\2\u0163"+
		"\u0164\5:\36\2\u0164\u0165\b\35\1\2\u0165\u0167\3\2\2\2\u0166\u0162\3"+
		"\2\2\2\u0167\u016a\3\2\2\2\u0168\u0166\3\2\2\2\u0168\u0169\3\2\2\2\u0169"+
		"9\3\2\2\2\u016a\u0168\3\2\2\2\u016b\u016c\5<\37\2\u016c\u0173\b\36\1\2"+
		"\u016d\u016e\t\5\2\2\u016e\u016f\5<\37\2\u016f\u0170\b\36\1\2\u0170\u0172"+
		"\3\2\2\2\u0171\u016d\3\2\2\2\u0172\u0175\3\2\2\2\u0173\u0171\3\2\2\2\u0173"+
		"\u0174\3\2\2\2\u0174;\3\2\2\2\u0175\u0173\3\2\2\2\u0176\u0177\5> \2\u0177"+
		"\u017e\b\37\1\2\u0178\u0179\7-\2\2\u0179\u017a\5> \2\u017a\u017b\b\37"+
		"\1\2\u017b\u017d\3\2\2\2\u017c\u0178\3\2\2\2\u017d\u0180\3\2\2\2\u017e"+
		"\u017c\3\2\2\2\u017e\u017f\3\2\2\2\u017f=\3\2\2\2\u0180\u017e\3\2\2\2"+
		"\u0181\u0183\t\4\2\2\u0182\u0181\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0184"+
		"\3\2\2\2\u0184\u0185\7B\2\2\u0185\u0192\b \1\2\u0186\u0187\7\62\2\2\u0187"+
		"\u0188\5@!\2\u0188\u0189\b \1\2\u0189\u0192\3\2\2\2\u018a\u018b\7.\2\2"+
		"\u018b\u018c\5@!\2\u018c\u018d\b \1\2\u018d\u0192\3\2\2\2\u018e\u018f"+
		"\5@!\2\u018f\u0190\b \1\2\u0190\u0192\3\2\2\2\u0191\u0182\3\2\2\2\u0191"+
		"\u0186\3\2\2\2\u0191\u018a\3\2\2\2\u0191\u018e\3\2\2\2\u0192?\3\2\2\2"+
		"\u0193\u0194\7A\2\2\u0194\u0199\b!\1\2\u0195\u0196\5H%\2\u0196\u0197\b"+
		"!\1\2\u0197\u0199\3\2\2\2\u0198\u0193\3\2\2\2\u0198\u0195\3\2\2\2\u0199"+
		"A\3\2\2\2\u019a\u019b\5D#\2\u019b\u019c\b\"\1\2\u019cC\3\2\2\2\u019d\u01a1"+
		"\7D\2\2\u019e\u019f\7\37\2\2\u019f\u01a2\5B\"\2\u01a0\u01a2\5F$\2\u01a1"+
		"\u019e\3\2\2\2\u01a1\u01a0\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a3\3\2"+
		"\2\2\u01a3\u01ac\b#\1\2\u01a4\u01a8\7C\2\2\u01a5\u01a6\7\37\2\2\u01a6"+
		"\u01a9\5B\"\2\u01a7\u01a9\5F$\2\u01a8\u01a5\3\2\2\2\u01a8\u01a7\3\2\2"+
		"\2\u01a8\u01a9\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ac\b#\1\2\u01ab\u019d"+
		"\3\2\2\2\u01ab\u01a4\3\2\2\2\u01acE\3\2\2\2\u01ad\u01ae\78\2\2\u01ae\u01af"+
		"\7B\2\2\u01af\u01b3\79\2\2\u01b0\u01b1\7\37\2\2\u01b1\u01b4\5B\"\2\u01b2"+
		"\u01b4\5F$\2\u01b3\u01b0\3\2\2\2\u01b3\u01b2\3\2\2\2\u01b3\u01b4\3\2\2"+
		"\2\u01b4\u01b5\3\2\2\2\u01b5\u01b6\b$\1\2\u01b6G\3\2\2\2\u01b7\u01b8\5"+
		"\4\3\2\u01b8\u01b9\b%\1\2\u01b9\u01d2\3\2\2\2\u01ba\u01bb\5\6\4\2\u01bb"+
		"\u01bc\b%\1\2\u01bc\u01d2\3\2\2\2\u01bd\u01be\5\b\5\2\u01be\u01bf\b%\1"+
		"\2\u01bf\u01d2\3\2\2\2\u01c0\u01c1\5\n\6\2\u01c1\u01c2\b%\1\2\u01c2\u01d2"+
		"\3\2\2\2\u01c3\u01c4\5B\"\2\u01c4\u01c5\b%\1\2\u01c5\u01d2\3\2\2\2\u01c6"+
		"\u01c7\7E\2\2\u01c7\u01d2\b%\1\2\u01c8\u01c9\7:\2\2\u01c9\u01ca\5,\27"+
		"\2\u01ca\u01cb\7;\2\2\u01cb\u01cc\b%\1\2\u01cc\u01d2\3\2\2\2\u01cd\u01ce"+
		"\7@\2\2\u01ce\u01cf\7C\2\2\u01cf\u01d0\7@\2\2\u01d0\u01d2\b%\1\2\u01d1"+
		"\u01b7\3\2\2\2\u01d1\u01ba\3\2\2\2\u01d1\u01bd\3\2\2\2\u01d1\u01c0\3\2"+
		"\2\2\u01d1\u01c3\3\2\2\2\u01d1\u01c6\3\2\2\2\u01d1\u01c8\3\2\2\2\u01d1"+
		"\u01cd\3\2\2\2\u01d2I\3\2\2\2\34Qal\u0080\u00c5\u00cf\u00e5\u00f3\u010e"+
		"\u0126\u0132\u013f\u014a\u0155\u015e\u0168\u0173\u017e\u0182\u0191\u0198"+
		"\u01a1\u01a8\u01ab\u01b3\u01d1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}