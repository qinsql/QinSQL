// Generated from org\apache\drill\common\expression\parser\ExprLexer.g4 by ANTLR 4.7.1
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

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExprLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"If", "Else", "Return", "Then", "End", "In", "Case", "When", "Cast", "Convert", 
		"AnyValue", "Nullable", "Repeat", "As", "BIT", "INT", "BIGINT", "FLOAT4", 
		"FLOAT8", "VARCHAR", "VARBINARY", "DATE", "TIMESTAMP", "TIME", "TIMESTAMPTZ", 
		"INTERVAL", "INTERVALYEAR", "INTERVALDAY", "Period", "DECIMAL9", "DECIMAL18", 
		"DECIMAL28DENSE", "DECIMAL28SPARSE", "DECIMAL38DENSE", "DECIMAL38SPARSE", 
		"VARDECIMAL", "Or", "And", "Equals", "NEquals", "GTEquals", "LTEquals", 
		"Caret", "Excl", "GT", "LT", "Plus", "Minus", "Asterisk", "ForwardSlash", 
		"Percent", "OBrace", "CBrace", "OBracket", "CBracket", "OParen", "CParen", 
		"SColon", "Comma", "QMark", "Colon", "SingleQuote", "Bool", "Number", 
		"Identifier", "QuotedIdentifier", "String", "LineComment", "BlockComment", 
		"Space", "Int", "Digit"
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


	public ExprLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ExprLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 65:
			QuotedIdentifier_action((RuleContext)_localctx, actionIndex);
			break;
		case 66:
			String_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void QuotedIdentifier_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			    setText(getText().substring(1, getText().length()-1).replaceAll("\\\\(.)", "$1"));
			  
			break;
		}
	}
	private void String_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:

			    setText(getText().substring(1, getText().length()-1).replaceAll("\\\\(.)", "$1"));
			  
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2H\u0347\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t"+
		"\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00cd\n\13\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00e1\n\f"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00fc\n\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\5\21\u0104\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\5\22\u0112\n\22\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u0120\n\23\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u012e\n\24\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u013e\n\25\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\5\26\u0152\n\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\5\27\u015c\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u0170\n\30\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\5\31\u017a\n\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\5\32\u0192\n\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u01a4\n\33\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\5\34\u01be\n\34\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\5\35\u01d6\n\35\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u01ea\n\37\3 \3 \3"+
		" \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \5 \u01fe\n \3!\3!\3!\3"+
		"!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3"+
		"!\3!\5!\u021c\n!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3"+
		"\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\5\""+
		"\u023c\n\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3"+
		"#\3#\3#\3#\3#\3#\3#\3#\3#\5#\u025a\n#\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3"+
		"$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\5$\u027a\n"+
		"$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u0290"+
		"\n%\3&\3&\3&\3&\3&\3&\5&\u0298\n&\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u02a0\n"+
		"\'\3(\3(\3(\5(\u02a5\n(\3)\3)\3)\3)\5)\u02ab\n)\3*\3*\3*\3+\3+\3+\3,\3"+
		",\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64"+
		"\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>"+
		"\3>\3?\3?\3@\3@\3@\3@\3@\3@\3@\3@\3@\5@\u02e4\n@\3A\3A\3A\7A\u02e9\nA"+
		"\fA\16A\u02ec\13A\5A\u02ee\nA\3A\3A\5A\u02f2\nA\3A\7A\u02f5\nA\fA\16A"+
		"\u02f8\13A\5A\u02fa\nA\3B\3B\3B\7B\u02ff\nB\fB\16B\u0302\13B\3C\3C\3C"+
		"\3C\7C\u0308\nC\fC\16C\u030b\13C\3C\3C\3C\3D\3D\3D\3D\7D\u0314\nD\fD\16"+
		"D\u0317\13D\3D\3D\3D\3E\3E\3E\3E\7E\u0320\nE\fE\16E\u0323\13E\3E\3E\3"+
		"F\3F\3F\3F\7F\u032b\nF\fF\16F\u032e\13F\3F\3F\3F\3F\3F\3G\6G\u0336\nG"+
		"\rG\16G\u0337\3G\3G\3H\3H\7H\u033e\nH\fH\16H\u0341\13H\3H\5H\u0344\nH"+
		"\3I\3I\3\u032c\2J\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64"+
		"g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087E\u0089"+
		"F\u008bG\u008dH\u008f\2\u0091\2\3\2\t\4\2GGgg\4\2--//\6\2&&C\\aac|\4\2"+
		"^^bb\4\2))^^\4\2\f\f\17\17\5\2\13\f\16\17\"\"\2\u0371\2\3\3\2\2\2\2\5"+
		"\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2"+
		"\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33"+
		"\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2"+
		"\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2"+
		"\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2"+
		"\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K"+
		"\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2"+
		"\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2"+
		"\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q"+
		"\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2"+
		"\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087"+
		"\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\3\u0093\3\2\2"+
		"\2\5\u0096\3\2\2\2\7\u009b\3\2\2\2\t\u00a2\3\2\2\2\13\u00a7\3\2\2\2\r"+
		"\u00ab\3\2\2\2\17\u00ae\3\2\2\2\21\u00b3\3\2\2\2\23\u00b8\3\2\2\2\25\u00bd"+
		"\3\2\2\2\27\u00e0\3\2\2\2\31\u00e2\3\2\2\2\33\u00eb\3\2\2\2\35\u00f2\3"+
		"\2\2\2\37\u00fb\3\2\2\2!\u0103\3\2\2\2#\u0111\3\2\2\2%\u011f\3\2\2\2\'"+
		"\u012d\3\2\2\2)\u013d\3\2\2\2+\u0151\3\2\2\2-\u015b\3\2\2\2/\u016f\3\2"+
		"\2\2\61\u0179\3\2\2\2\63\u0191\3\2\2\2\65\u01a3\3\2\2\2\67\u01bd\3\2\2"+
		"\29\u01d5\3\2\2\2;\u01d7\3\2\2\2=\u01e9\3\2\2\2?\u01fd\3\2\2\2A\u021b"+
		"\3\2\2\2C\u023b\3\2\2\2E\u0259\3\2\2\2G\u0279\3\2\2\2I\u028f\3\2\2\2K"+
		"\u0297\3\2\2\2M\u029f\3\2\2\2O\u02a4\3\2\2\2Q\u02aa\3\2\2\2S\u02ac\3\2"+
		"\2\2U\u02af\3\2\2\2W\u02b2\3\2\2\2Y\u02b4\3\2\2\2[\u02b6\3\2\2\2]\u02b8"+
		"\3\2\2\2_\u02ba\3\2\2\2a\u02bc\3\2\2\2c\u02be\3\2\2\2e\u02c0\3\2\2\2g"+
		"\u02c2\3\2\2\2i\u02c4\3\2\2\2k\u02c6\3\2\2\2m\u02c8\3\2\2\2o\u02ca\3\2"+
		"\2\2q\u02cc\3\2\2\2s\u02ce\3\2\2\2u\u02d0\3\2\2\2w\u02d2\3\2\2\2y\u02d4"+
		"\3\2\2\2{\u02d6\3\2\2\2}\u02d8\3\2\2\2\177\u02e3\3\2\2\2\u0081\u02e5\3"+
		"\2\2\2\u0083\u02fb\3\2\2\2\u0085\u0303\3\2\2\2\u0087\u030f\3\2\2\2\u0089"+
		"\u031b\3\2\2\2\u008b\u0326\3\2\2\2\u008d\u0335\3\2\2\2\u008f\u0343\3\2"+
		"\2\2\u0091\u0345\3\2\2\2\u0093\u0094\7k\2\2\u0094\u0095\7h\2\2\u0095\4"+
		"\3\2\2\2\u0096\u0097\7g\2\2\u0097\u0098\7n\2\2\u0098\u0099\7u\2\2\u0099"+
		"\u009a\7g\2\2\u009a\6\3\2\2\2\u009b\u009c\7t\2\2\u009c\u009d\7g\2\2\u009d"+
		"\u009e\7v\2\2\u009e\u009f\7w\2\2\u009f\u00a0\7t\2\2\u00a0\u00a1\7p\2\2"+
		"\u00a1\b\3\2\2\2\u00a2\u00a3\7v\2\2\u00a3\u00a4\7j\2\2\u00a4\u00a5\7g"+
		"\2\2\u00a5\u00a6\7p\2\2\u00a6\n\3\2\2\2\u00a7\u00a8\7g\2\2\u00a8\u00a9"+
		"\7p\2\2\u00a9\u00aa\7f\2\2\u00aa\f\3\2\2\2\u00ab\u00ac\7k\2\2\u00ac\u00ad"+
		"\7p\2\2\u00ad\16\3\2\2\2\u00ae\u00af\7e\2\2\u00af\u00b0\7c\2\2\u00b0\u00b1"+
		"\7u\2\2\u00b1\u00b2\7g\2\2\u00b2\20\3\2\2\2\u00b3\u00b4\7y\2\2\u00b4\u00b5"+
		"\7j\2\2\u00b5\u00b6\7g\2\2\u00b6\u00b7\7p\2\2\u00b7\22\3\2\2\2\u00b8\u00b9"+
		"\7e\2\2\u00b9\u00ba\7c\2\2\u00ba\u00bb\7u\2\2\u00bb\u00bc\7v\2\2\u00bc"+
		"\24\3\2\2\2\u00bd\u00be\7e\2\2\u00be\u00bf\7q\2\2\u00bf\u00c0\7p\2\2\u00c0"+
		"\u00c1\7x\2\2\u00c1\u00c2\7g\2\2\u00c2\u00c3\7t\2\2\u00c3\u00c4\7v\2\2"+
		"\u00c4\u00c5\7a\2\2\u00c5\u00cc\3\2\2\2\u00c6\u00c7\7h\2\2\u00c7\u00c8"+
		"\7t\2\2\u00c8\u00c9\7q\2\2\u00c9\u00cd\7o\2\2\u00ca\u00cb\7v\2\2\u00cb"+
		"\u00cd\7q\2\2\u00cc\u00c6\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cd\26\3\2\2\2"+
		"\u00ce\u00cf\7c\2\2\u00cf\u00d0\7p\2\2\u00d0\u00d1\7{\2\2\u00d1\u00d2"+
		"\7a\2\2\u00d2\u00d3\7x\2\2\u00d3\u00d4\7c\2\2\u00d4\u00d5\7n\2\2\u00d5"+
		"\u00d6\7w\2\2\u00d6\u00e1\7g\2\2\u00d7\u00d8\7C\2\2\u00d8\u00d9\7P\2\2"+
		"\u00d9\u00da\7[\2\2\u00da\u00db\7a\2\2\u00db\u00dc\7X\2\2\u00dc\u00dd"+
		"\7C\2\2\u00dd\u00de\7N\2\2\u00de\u00df\7W\2\2\u00df\u00e1\7G\2\2\u00e0"+
		"\u00ce\3\2\2\2\u00e0\u00d7\3\2\2\2\u00e1\30\3\2\2\2\u00e2\u00e3\7p\2\2"+
		"\u00e3\u00e4\7w\2\2\u00e4\u00e5\7n\2\2\u00e5\u00e6\7n\2\2\u00e6\u00e7"+
		"\7c\2\2\u00e7\u00e8\7d\2\2\u00e8\u00e9\7n\2\2\u00e9\u00ea\7g\2\2\u00ea"+
		"\32\3\2\2\2\u00eb\u00ec\7t\2\2\u00ec\u00ed\7g\2\2\u00ed\u00ee\7r\2\2\u00ee"+
		"\u00ef\7g\2\2\u00ef\u00f0\7c\2\2\u00f0\u00f1\7v\2\2\u00f1\34\3\2\2\2\u00f2"+
		"\u00f3\7c\2\2\u00f3\u00f4\7u\2\2\u00f4\36\3\2\2\2\u00f5\u00f6\7d\2\2\u00f6"+
		"\u00f7\7k\2\2\u00f7\u00fc\7v\2\2\u00f8\u00f9\7D\2\2\u00f9\u00fa\7K\2\2"+
		"\u00fa\u00fc\7V\2\2\u00fb\u00f5\3\2\2\2\u00fb\u00f8\3\2\2\2\u00fc \3\2"+
		"\2\2\u00fd\u00fe\7k\2\2\u00fe\u00ff\7p\2\2\u00ff\u0104\7v\2\2\u0100\u0101"+
		"\7K\2\2\u0101\u0102\7P\2\2\u0102\u0104\7V\2\2\u0103\u00fd\3\2\2\2\u0103"+
		"\u0100\3\2\2\2\u0104\"\3\2\2\2\u0105\u0106\7d\2\2\u0106\u0107\7k\2\2\u0107"+
		"\u0108\7i\2\2\u0108\u0109\7k\2\2\u0109\u010a\7p\2\2\u010a\u0112\7v\2\2"+
		"\u010b\u010c\7D\2\2\u010c\u010d\7K\2\2\u010d\u010e\7I\2\2\u010e\u010f"+
		"\7K\2\2\u010f\u0110\7P\2\2\u0110\u0112\7V\2\2\u0111\u0105\3\2\2\2\u0111"+
		"\u010b\3\2\2\2\u0112$\3\2\2\2\u0113\u0114\7h\2\2\u0114\u0115\7n\2\2\u0115"+
		"\u0116\7q\2\2\u0116\u0117\7c\2\2\u0117\u0118\7v\2\2\u0118\u0120\7\66\2"+
		"\2\u0119\u011a\7H\2\2\u011a\u011b\7N\2\2\u011b\u011c\7Q\2\2\u011c\u011d"+
		"\7C\2\2\u011d\u011e\7V\2\2\u011e\u0120\7\66\2\2\u011f\u0113\3\2\2\2\u011f"+
		"\u0119\3\2\2\2\u0120&\3\2\2\2\u0121\u0122\7h\2\2\u0122\u0123\7n\2\2\u0123"+
		"\u0124\7q\2\2\u0124\u0125\7c\2\2\u0125\u0126\7v\2\2\u0126\u012e\7:\2\2"+
		"\u0127\u0128\7H\2\2\u0128\u0129\7N\2\2\u0129\u012a\7Q\2\2\u012a\u012b"+
		"\7C\2\2\u012b\u012c\7V\2\2\u012c\u012e\7:\2\2\u012d\u0121\3\2\2\2\u012d"+
		"\u0127\3\2\2\2\u012e(\3\2\2\2\u012f\u0130\7x\2\2\u0130\u0131\7c\2\2\u0131"+
		"\u0132\7t\2\2\u0132\u0133\7e\2\2\u0133\u0134\7j\2\2\u0134\u0135\7c\2\2"+
		"\u0135\u013e\7t\2\2\u0136\u0137\7X\2\2\u0137\u0138\7C\2\2\u0138\u0139"+
		"\7T\2\2\u0139\u013a\7E\2\2\u013a\u013b\7J\2\2\u013b\u013c\7C\2\2\u013c"+
		"\u013e\7T\2\2\u013d\u012f\3\2\2\2\u013d\u0136\3\2\2\2\u013e*\3\2\2\2\u013f"+
		"\u0140\7x\2\2\u0140\u0141\7c\2\2\u0141\u0142\7t\2\2\u0142\u0143\7d\2\2"+
		"\u0143\u0144\7k\2\2\u0144\u0145\7p\2\2\u0145\u0146\7c\2\2\u0146\u0147"+
		"\7t\2\2\u0147\u0152\7{\2\2\u0148\u0149\7X\2\2\u0149\u014a\7C\2\2\u014a"+
		"\u014b\7T\2\2\u014b\u014c\7D\2\2\u014c\u014d\7K\2\2\u014d\u014e\7P\2\2"+
		"\u014e\u014f\7C\2\2\u014f\u0150\7T\2\2\u0150\u0152\7[\2\2\u0151\u013f"+
		"\3\2\2\2\u0151\u0148\3\2\2\2\u0152,\3\2\2\2\u0153\u0154\7f\2\2\u0154\u0155"+
		"\7c\2\2\u0155\u0156\7v\2\2\u0156\u015c\7g\2\2\u0157\u0158\7F\2\2\u0158"+
		"\u0159\7C\2\2\u0159\u015a\7V\2\2\u015a\u015c\7G\2\2\u015b\u0153\3\2\2"+
		"\2\u015b\u0157\3\2\2\2\u015c.\3\2\2\2\u015d\u015e\7v\2\2\u015e\u015f\7"+
		"k\2\2\u015f\u0160\7o\2\2\u0160\u0161\7g\2\2\u0161\u0162\7u\2\2\u0162\u0163"+
		"\7v\2\2\u0163\u0164\7c\2\2\u0164\u0165\7o\2\2\u0165\u0170\7r\2\2\u0166"+
		"\u0167\7V\2\2\u0167\u0168\7K\2\2\u0168\u0169\7O\2\2\u0169\u016a\7G\2\2"+
		"\u016a\u016b\7U\2\2\u016b\u016c\7V\2\2\u016c\u016d\7C\2\2\u016d\u016e"+
		"\7O\2\2\u016e\u0170\7R\2\2\u016f\u015d\3\2\2\2\u016f\u0166\3\2\2\2\u0170"+
		"\60\3\2\2\2\u0171\u0172\7v\2\2\u0172\u0173\7k\2\2\u0173\u0174\7o\2\2\u0174"+
		"\u017a\7g\2\2\u0175\u0176\7V\2\2\u0176\u0177\7K\2\2\u0177\u0178\7O\2\2"+
		"\u0178\u017a\7G\2\2\u0179\u0171\3\2\2\2\u0179\u0175\3\2\2\2\u017a\62\3"+
		"\2\2\2\u017b\u017c\7v\2\2\u017c\u017d\7k\2\2\u017d\u017e\7o\2\2\u017e"+
		"\u017f\7g\2\2\u017f\u0180\7u\2\2\u0180\u0181\7v\2\2\u0181\u0182\7c\2\2"+
		"\u0182\u0183\7o\2\2\u0183\u0184\7r\2\2\u0184\u0185\7v\2\2\u0185\u0192"+
		"\7|\2\2\u0186\u0187\7V\2\2\u0187\u0188\7K\2\2\u0188\u0189\7O\2\2\u0189"+
		"\u018a\7G\2\2\u018a\u018b\7U\2\2\u018b\u018c\7V\2\2\u018c\u018d\7C\2\2"+
		"\u018d\u018e\7O\2\2\u018e\u018f\7R\2\2\u018f\u0190\7V\2\2\u0190\u0192"+
		"\7\\\2\2\u0191\u017b\3\2\2\2\u0191\u0186\3\2\2\2\u0192\64\3\2\2\2\u0193"+
		"\u0194\7k\2\2\u0194\u0195\7p\2\2\u0195\u0196\7v\2\2\u0196\u0197\7g\2\2"+
		"\u0197\u0198\7t\2\2\u0198\u0199\7x\2\2\u0199\u019a\7c\2\2\u019a\u01a4"+
		"\7n\2\2\u019b\u019c\7K\2\2\u019c\u019d\7P\2\2\u019d\u019e\7V\2\2\u019e"+
		"\u019f\7G\2\2\u019f\u01a0\7T\2\2\u01a0\u01a1\7X\2\2\u01a1\u01a2\7C\2\2"+
		"\u01a2\u01a4\7N\2\2\u01a3\u0193\3\2\2\2\u01a3\u019b\3\2\2\2\u01a4\66\3"+
		"\2\2\2\u01a5\u01a6\7k\2\2\u01a6\u01a7\7p\2\2\u01a7\u01a8\7v\2\2\u01a8"+
		"\u01a9\7g\2\2\u01a9\u01aa\7t\2\2\u01aa\u01ab\7x\2\2\u01ab\u01ac\7c\2\2"+
		"\u01ac\u01ad\7n\2\2\u01ad\u01ae\7{\2\2\u01ae\u01af\7g\2\2\u01af\u01b0"+
		"\7c\2\2\u01b0\u01be\7t\2\2\u01b1\u01b2\7K\2\2\u01b2\u01b3\7P\2\2\u01b3"+
		"\u01b4\7V\2\2\u01b4\u01b5\7G\2\2\u01b5\u01b6\7T\2\2\u01b6\u01b7\7X\2\2"+
		"\u01b7\u01b8\7C\2\2\u01b8\u01b9\7N\2\2\u01b9\u01ba\7[\2\2\u01ba\u01bb"+
		"\7G\2\2\u01bb\u01bc\7C\2\2\u01bc\u01be\7T\2\2\u01bd\u01a5\3\2\2\2\u01bd"+
		"\u01b1\3\2\2\2\u01be8\3\2\2\2\u01bf\u01c0\7k\2\2\u01c0\u01c1\7p\2\2\u01c1"+
		"\u01c2\7v\2\2\u01c2\u01c3\7g\2\2\u01c3\u01c4\7t\2\2\u01c4\u01c5\7x\2\2"+
		"\u01c5\u01c6\7c\2\2\u01c6\u01c7\7n\2\2\u01c7\u01c8\7f\2\2\u01c8\u01c9"+
		"\7c\2\2\u01c9\u01d6\7{\2\2\u01ca\u01cb\7K\2\2\u01cb\u01cc\7P\2\2\u01cc"+
		"\u01cd\7V\2\2\u01cd\u01ce\7G\2\2\u01ce\u01cf\7T\2\2\u01cf\u01d0\7X\2\2"+
		"\u01d0\u01d1\7C\2\2\u01d1\u01d2\7N\2\2\u01d2\u01d3\7F\2\2\u01d3\u01d4"+
		"\7C\2\2\u01d4\u01d6\7[\2\2\u01d5\u01bf\3\2\2\2\u01d5\u01ca\3\2\2\2\u01d6"+
		":\3\2\2\2\u01d7\u01d8\7\60\2\2\u01d8<\3\2\2\2\u01d9\u01da\7f\2\2\u01da"+
		"\u01db\7g\2\2\u01db\u01dc\7e\2\2\u01dc\u01dd\7k\2\2\u01dd\u01de\7o\2\2"+
		"\u01de\u01df\7c\2\2\u01df\u01e0\7n\2\2\u01e0\u01ea\7;\2\2\u01e1\u01e2"+
		"\7F\2\2\u01e2\u01e3\7G\2\2\u01e3\u01e4\7E\2\2\u01e4\u01e5\7K\2\2\u01e5"+
		"\u01e6\7O\2\2\u01e6\u01e7\7C\2\2\u01e7\u01e8\7N\2\2\u01e8\u01ea\7;\2\2"+
		"\u01e9\u01d9\3\2\2\2\u01e9\u01e1\3\2\2\2\u01ea>\3\2\2\2\u01eb\u01ec\7"+
		"f\2\2\u01ec\u01ed\7g\2\2\u01ed\u01ee\7e\2\2\u01ee\u01ef\7k\2\2\u01ef\u01f0"+
		"\7o\2\2\u01f0\u01f1\7c\2\2\u01f1\u01f2\7n\2\2\u01f2\u01f3\7\63\2\2\u01f3"+
		"\u01fe\7:\2\2\u01f4\u01f5\7F\2\2\u01f5\u01f6\7G\2\2\u01f6\u01f7\7E\2\2"+
		"\u01f7\u01f8\7K\2\2\u01f8\u01f9\7O\2\2\u01f9\u01fa\7C\2\2\u01fa\u01fb"+
		"\7N\2\2\u01fb\u01fc\7\63\2\2\u01fc\u01fe\7:\2\2\u01fd\u01eb\3\2\2\2\u01fd"+
		"\u01f4\3\2\2\2\u01fe@\3\2\2\2\u01ff\u0200\7f\2\2\u0200\u0201\7g\2\2\u0201"+
		"\u0202\7e\2\2\u0202\u0203\7k\2\2\u0203\u0204\7o\2\2\u0204\u0205\7c\2\2"+
		"\u0205\u0206\7n\2\2\u0206\u0207\7\64\2\2\u0207\u0208\7:\2\2\u0208\u0209"+
		"\7f\2\2\u0209\u020a\7g\2\2\u020a\u020b\7p\2\2\u020b\u020c\7u\2\2\u020c"+
		"\u021c\7g\2\2\u020d\u020e\7F\2\2\u020e\u020f\7G\2\2\u020f\u0210\7E\2\2"+
		"\u0210\u0211\7K\2\2\u0211\u0212\7O\2\2\u0212\u0213\7C\2\2\u0213\u0214"+
		"\7N\2\2\u0214\u0215\7\64\2\2\u0215\u0216\7:\2\2\u0216\u0217\7F\2\2\u0217"+
		"\u0218\7G\2\2\u0218\u0219\7P\2\2\u0219\u021a\7U\2\2\u021a\u021c\7G\2\2"+
		"\u021b\u01ff\3\2\2\2\u021b\u020d\3\2\2\2\u021cB\3\2\2\2\u021d\u021e\7"+
		"f\2\2\u021e\u021f\7g\2\2\u021f\u0220\7e\2\2\u0220\u0221\7k\2\2\u0221\u0222"+
		"\7o\2\2\u0222\u0223\7c\2\2\u0223\u0224\7n\2\2\u0224\u0225\7\64\2\2\u0225"+
		"\u0226\7:\2\2\u0226\u0227\7u\2\2\u0227\u0228\7r\2\2\u0228\u0229\7c\2\2"+
		"\u0229\u022a\7t\2\2\u022a\u022b\7u\2\2\u022b\u023c\7g\2\2\u022c\u022d"+
		"\7F\2\2\u022d\u022e\7G\2\2\u022e\u022f\7E\2\2\u022f\u0230\7K\2\2\u0230"+
		"\u0231\7O\2\2\u0231\u0232\7C\2\2\u0232\u0233\7N\2\2\u0233\u0234\7\64\2"+
		"\2\u0234\u0235\7:\2\2\u0235\u0236\7U\2\2\u0236\u0237\7R\2\2\u0237\u0238"+
		"\7C\2\2\u0238\u0239\7T\2\2\u0239\u023a\7U\2\2\u023a\u023c\7G\2\2\u023b"+
		"\u021d\3\2\2\2\u023b\u022c\3\2\2\2\u023cD\3\2\2\2\u023d\u023e\7f\2\2\u023e"+
		"\u023f\7g\2\2\u023f\u0240\7e\2\2\u0240\u0241\7k\2\2\u0241\u0242\7o\2\2"+
		"\u0242\u0243\7c\2\2\u0243\u0244\7n\2\2\u0244\u0245\7\65\2\2\u0245\u0246"+
		"\7:\2\2\u0246\u0247\7f\2\2\u0247\u0248\7g\2\2\u0248\u0249\7p\2\2\u0249"+
		"\u024a\7u\2\2\u024a\u025a\7g\2\2\u024b\u024c\7F\2\2\u024c\u024d\7G\2\2"+
		"\u024d\u024e\7E\2\2\u024e\u024f\7K\2\2\u024f\u0250\7O\2\2\u0250\u0251"+
		"\7C\2\2\u0251\u0252\7N\2\2\u0252\u0253\7\65\2\2\u0253\u0254\7:\2\2\u0254"+
		"\u0255\7F\2\2\u0255\u0256\7G\2\2\u0256\u0257\7P\2\2\u0257\u0258\7U\2\2"+
		"\u0258\u025a\7G\2\2\u0259\u023d\3\2\2\2\u0259\u024b\3\2\2\2\u025aF\3\2"+
		"\2\2\u025b\u025c\7f\2\2\u025c\u025d\7g\2\2\u025d\u025e\7e\2\2\u025e\u025f"+
		"\7k\2\2\u025f\u0260\7o\2\2\u0260\u0261\7c\2\2\u0261\u0262\7n\2\2\u0262"+
		"\u0263\7\65\2\2\u0263\u0264\7:\2\2\u0264\u0265\7u\2\2\u0265\u0266\7r\2"+
		"\2\u0266\u0267\7c\2\2\u0267\u0268\7t\2\2\u0268\u0269\7u\2\2\u0269\u027a"+
		"\7g\2\2\u026a\u026b\7F\2\2\u026b\u026c\7G\2\2\u026c\u026d\7E\2\2\u026d"+
		"\u026e\7K\2\2\u026e\u026f\7O\2\2\u026f\u0270\7C\2\2\u0270\u0271\7N\2\2"+
		"\u0271\u0272\7\65\2\2\u0272\u0273\7:\2\2\u0273\u0274\7U\2\2\u0274\u0275"+
		"\7R\2\2\u0275\u0276\7C\2\2\u0276\u0277\7T\2\2\u0277\u0278\7U\2\2\u0278"+
		"\u027a\7G\2\2\u0279\u025b\3\2\2\2\u0279\u026a\3\2\2\2\u027aH\3\2\2\2\u027b"+
		"\u027c\7x\2\2\u027c\u027d\7c\2\2\u027d\u027e\7t\2\2\u027e\u027f\7f\2\2"+
		"\u027f\u0280\7g\2\2\u0280\u0281\7e\2\2\u0281\u0282\7k\2\2\u0282\u0283"+
		"\7o\2\2\u0283\u0284\7c\2\2\u0284\u0290\7n\2\2\u0285\u0286\7X\2\2\u0286"+
		"\u0287\7C\2\2\u0287\u0288\7T\2\2\u0288\u0289\7F\2\2\u0289\u028a\7G\2\2"+
		"\u028a\u028b\7E\2\2\u028b\u028c\7K\2\2\u028c\u028d\7O\2\2\u028d\u028e"+
		"\7C\2\2\u028e\u0290\7N\2\2\u028f\u027b\3\2\2\2\u028f\u0285\3\2\2\2\u0290"+
		"J\3\2\2\2\u0291\u0292\7q\2\2\u0292\u0298\7t\2\2\u0293\u0294\7Q\2\2\u0294"+
		"\u0298\7T\2\2\u0295\u0296\7Q\2\2\u0296\u0298\7t\2\2\u0297\u0291\3\2\2"+
		"\2\u0297\u0293\3\2\2\2\u0297\u0295\3\2\2\2\u0298L\3\2\2\2\u0299\u029a"+
		"\7c\2\2\u029a\u029b\7p\2\2\u029b\u02a0\7f\2\2\u029c\u029d\7C\2\2\u029d"+
		"\u029e\7P\2\2\u029e\u02a0\7F\2\2\u029f\u0299\3\2\2\2\u029f\u029c\3\2\2"+
		"\2\u02a0N\3\2\2\2\u02a1\u02a2\7?\2\2\u02a2\u02a5\7?\2\2\u02a3\u02a5\7"+
		"?\2\2\u02a4\u02a1\3\2\2\2\u02a4\u02a3\3\2\2\2\u02a5P\3\2\2\2\u02a6\u02a7"+
		"\7>\2\2\u02a7\u02ab\7@\2\2\u02a8\u02a9\7#\2\2\u02a9\u02ab\7?\2\2\u02aa"+
		"\u02a6\3\2\2\2\u02aa\u02a8\3\2\2\2\u02abR\3\2\2\2\u02ac\u02ad\7@\2\2\u02ad"+
		"\u02ae\7?\2\2\u02aeT\3\2\2\2\u02af\u02b0\7>\2\2\u02b0\u02b1\7?\2\2\u02b1"+
		"V\3\2\2\2\u02b2\u02b3\7`\2\2\u02b3X\3\2\2\2\u02b4\u02b5\7#\2\2\u02b5Z"+
		"\3\2\2\2\u02b6\u02b7\7@\2\2\u02b7\\\3\2\2\2\u02b8\u02b9\7>\2\2\u02b9^"+
		"\3\2\2\2\u02ba\u02bb\7-\2\2\u02bb`\3\2\2\2\u02bc\u02bd\7/\2\2\u02bdb\3"+
		"\2\2\2\u02be\u02bf\7,\2\2\u02bfd\3\2\2\2\u02c0\u02c1\7\61\2\2\u02c1f\3"+
		"\2\2\2\u02c2\u02c3\7\'\2\2\u02c3h\3\2\2\2\u02c4\u02c5\7}\2\2\u02c5j\3"+
		"\2\2\2\u02c6\u02c7\7\177\2\2\u02c7l\3\2\2\2\u02c8\u02c9\7]\2\2\u02c9n"+
		"\3\2\2\2\u02ca\u02cb\7_\2\2\u02cbp\3\2\2\2\u02cc\u02cd\7*\2\2\u02cdr\3"+
		"\2\2\2\u02ce\u02cf\7+\2\2\u02cft\3\2\2\2\u02d0\u02d1\7=\2\2\u02d1v\3\2"+
		"\2\2\u02d2\u02d3\7.\2\2\u02d3x\3\2\2\2\u02d4\u02d5\7A\2\2\u02d5z\3\2\2"+
		"\2\u02d6\u02d7\7<\2\2\u02d7|\3\2\2\2\u02d8\u02d9\7)\2\2\u02d9~\3\2\2\2"+
		"\u02da\u02db\7v\2\2\u02db\u02dc\7t\2\2\u02dc\u02dd\7w\2\2\u02dd\u02e4"+
		"\7g\2\2\u02de\u02df\7h\2\2\u02df\u02e0\7c\2\2\u02e0\u02e1\7n\2\2\u02e1"+
		"\u02e2\7u\2\2\u02e2\u02e4\7g\2\2\u02e3\u02da\3\2\2\2\u02e3\u02de\3\2\2"+
		"\2\u02e4\u0080\3\2\2\2\u02e5\u02ed\5\u008fH\2\u02e6\u02ea\7\60\2\2\u02e7"+
		"\u02e9\5\u0091I\2\u02e8\u02e7\3\2\2\2\u02e9\u02ec\3\2\2\2\u02ea\u02e8"+
		"\3\2\2\2\u02ea\u02eb\3\2\2\2\u02eb\u02ee\3\2\2\2\u02ec\u02ea\3\2\2\2\u02ed"+
		"\u02e6\3\2\2\2\u02ed\u02ee\3\2\2\2\u02ee\u02f9\3\2\2\2\u02ef\u02f1\t\2"+
		"\2\2\u02f0\u02f2\t\3\2\2\u02f1\u02f0\3\2\2\2\u02f1\u02f2\3\2\2\2\u02f2"+
		"\u02f6\3\2\2\2\u02f3\u02f5\5\u0091I\2\u02f4\u02f3\3\2\2\2\u02f5\u02f8"+
		"\3\2\2\2\u02f6\u02f4\3\2\2\2\u02f6\u02f7\3\2\2\2\u02f7\u02fa\3\2\2\2\u02f8"+
		"\u02f6\3\2\2\2\u02f9\u02ef\3\2\2\2\u02f9\u02fa\3\2\2\2\u02fa\u0082\3\2"+
		"\2\2\u02fb\u0300\t\4\2\2\u02fc\u02ff\t\4\2\2\u02fd\u02ff\5\u0091I\2\u02fe"+
		"\u02fc\3\2\2\2\u02fe\u02fd\3\2\2\2\u02ff\u0302\3\2\2\2\u0300\u02fe\3\2"+
		"\2\2\u0300\u0301\3\2\2\2\u0301\u0084\3\2\2\2\u0302\u0300\3\2\2\2\u0303"+
		"\u0309\7b\2\2\u0304\u0308\n\5\2\2\u0305\u0306\7^\2\2\u0306\u0308\t\5\2"+
		"\2\u0307\u0304\3\2\2\2\u0307\u0305\3\2\2\2\u0308\u030b\3\2\2\2\u0309\u0307"+
		"\3\2\2\2\u0309\u030a\3\2\2\2\u030a\u030c\3\2\2\2\u030b\u0309\3\2\2\2\u030c"+
		"\u030d\7b\2\2\u030d\u030e\bC\2\2\u030e\u0086\3\2\2\2\u030f\u0315\7)\2"+
		"\2\u0310\u0314\n\6\2\2\u0311\u0312\7^\2\2\u0312\u0314\t\6\2\2\u0313\u0310"+
		"\3\2\2\2\u0313\u0311\3\2\2\2\u0314\u0317\3\2\2\2\u0315\u0313\3\2\2\2\u0315"+
		"\u0316\3\2\2\2\u0316\u0318\3\2\2\2\u0317\u0315\3\2\2\2\u0318\u0319\7)"+
		"\2\2\u0319\u031a\bD\3\2\u031a\u0088\3\2\2\2\u031b\u031c\7\61\2\2\u031c"+
		"\u031d\7\61\2\2\u031d\u0321\3\2\2\2\u031e\u0320\n\7\2\2\u031f\u031e\3"+
		"\2\2\2\u0320\u0323\3\2\2\2\u0321\u031f\3\2\2\2\u0321\u0322\3\2\2\2\u0322"+
		"\u0324\3\2\2\2\u0323\u0321\3\2\2\2\u0324\u0325\bE\4\2\u0325\u008a\3\2"+
		"\2\2\u0326\u0327\7\61\2\2\u0327\u0328\7,\2\2\u0328\u032c\3\2\2\2\u0329"+
		"\u032b\13\2\2\2\u032a\u0329\3\2\2\2\u032b\u032e\3\2\2\2\u032c\u032d\3"+
		"\2\2\2\u032c\u032a\3\2\2\2\u032d\u032f\3\2\2\2\u032e\u032c\3\2\2\2\u032f"+
		"\u0330\7,\2\2\u0330\u0331\7\61\2\2\u0331\u0332\3\2\2\2\u0332\u0333\bF"+
		"\4\2\u0333\u008c\3\2\2\2\u0334\u0336\t\b\2\2\u0335\u0334\3\2\2\2\u0336"+
		"\u0337\3\2\2\2\u0337\u0335\3\2\2\2\u0337\u0338\3\2\2\2\u0338\u0339\3\2"+
		"\2\2\u0339\u033a\bG\4\2\u033a\u008e\3\2\2\2\u033b\u033f\4\63;\2\u033c"+
		"\u033e\5\u0091I\2\u033d\u033c\3\2\2\2\u033e\u0341\3\2\2\2\u033f\u033d"+
		"\3\2\2\2\u033f\u0340\3\2\2\2\u0340\u0344\3\2\2\2\u0341\u033f\3\2\2\2\u0342"+
		"\u0344\7\62\2\2\u0343\u033b\3\2\2\2\u0343\u0342\3\2\2\2\u0344\u0090\3"+
		"\2\2\2\u0345\u0346\4\62;\2\u0346\u0092\3\2\2\2/\2\u00cc\u00e0\u00fb\u0103"+
		"\u0111\u011f\u012d\u013d\u0151\u015b\u016f\u0179\u0191\u01a3\u01bd\u01d5"+
		"\u01e9\u01fd\u021b\u023b\u0259\u0279\u028f\u0297\u029f\u02a4\u02aa\u02e3"+
		"\u02ea\u02ed\u02f1\u02f6\u02f9\u02fe\u0300\u0307\u0309\u0313\u0315\u0321"+
		"\u032c\u0337\u033f\u0343\5\3C\2\3D\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}