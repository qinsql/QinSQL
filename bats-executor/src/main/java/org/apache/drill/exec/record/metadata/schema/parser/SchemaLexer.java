// Generated from org\apache\drill\exec\record\metadata\schema\parser\SchemaLexer.g4 by ANTLR 4.7.1
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

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SchemaLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"INT", "INTEGER", "BIGINT", "FLOAT", "DOUBLE", "DEC", "DECIMAL", "NUMERIC", 
		"BOOLEAN", "CHAR", "CHARACTER", "VARYING", "VARCHAR", "BINARY", "VARBINARY", 
		"TIME", "DATE", "TIMESTAMP", "INTERVAL", "YEAR", "MONTH", "DAY", "HOUR", 
		"MINUTE", "SECOND", "MAP", "STRUCT", "ARRAY", "COMMA", "REVERSE_QUOTE", 
		"LEFT_PAREN", "RIGHT_PAREN", "LEFT_ANGLE_BRACKET", "RIGHT_ANGLE_BRACKET", 
		"SINGLE_QUOTE", "DOUBLE_QUOTE", "LEFT_BRACE", "RIGHT_BRACE", "EQUALS_SIGN", 
		"NOT", "NULL", "AS", "FORMAT", "DEFAULT", "PROPERTIES", "NUMBER", "DIGIT", 
		"ID", "QUOTED_ID", "SINGLE_QUOTED_STRING", "DOUBLE_QUOTED_STRING", "LINE_COMMENT", 
		"BLOCK_COMMENT", "SPACE"
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


	public SchemaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SchemaLexer.g4"; }

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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\67\u01bc\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3"+
		"\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3"+
		"\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3"+
		"\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3"+
		"\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3"+
		"\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)"+
		"\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-"+
		"\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3/\3/\7/\u016a\n/\f/\16/\u016d"+
		"\13/\3/\5/\u0170\n/\3\60\3\60\3\61\3\61\3\61\7\61\u0177\n\61\f\61\16\61"+
		"\u017a\13\61\3\62\3\62\3\62\3\62\7\62\u0180\n\62\f\62\16\62\u0183\13\62"+
		"\3\62\3\62\3\63\3\63\3\63\3\63\7\63\u018b\n\63\f\63\16\63\u018e\13\63"+
		"\3\63\3\63\3\64\3\64\3\64\3\64\7\64\u0196\n\64\f\64\16\64\u0199\13\64"+
		"\3\64\3\64\3\65\3\65\3\65\3\65\7\65\u01a1\n\65\f\65\16\65\u01a4\13\65"+
		"\3\65\3\65\3\66\3\66\3\66\3\66\7\66\u01ac\n\66\f\66\16\66\u01af\13\66"+
		"\3\66\3\66\3\66\3\66\3\66\3\67\6\67\u01b7\n\67\r\67\16\67\u01b8\3\67\3"+
		"\67\3\u01ad\28\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\2a\61c\62e\63g\64"+
		"i\65k\66m\67\3\2\n\3\2\63;\3\2\62;\5\2&&C\\aa\4\2^^bb\4\2))^^\4\2$$^^"+
		"\4\2\f\f\17\17\5\2\13\f\16\17\"\"\2\u01c7\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2"+
		"\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2"+
		"\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2"+
		"\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2"+
		"\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M"+
		"\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2"+
		"\2\2\2[\3\2\2\2\2]\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2"+
		"\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\3o\3\2\2\2\5s\3\2\2\2\7{\3\2\2\2\t\u0082"+
		"\3\2\2\2\13\u0088\3\2\2\2\r\u008f\3\2\2\2\17\u0093\3\2\2\2\21\u009b\3"+
		"\2\2\2\23\u00a3\3\2\2\2\25\u00ab\3\2\2\2\27\u00b0\3\2\2\2\31\u00ba\3\2"+
		"\2\2\33\u00c2\3\2\2\2\35\u00ca\3\2\2\2\37\u00d1\3\2\2\2!\u00db\3\2\2\2"+
		"#\u00e0\3\2\2\2%\u00e5\3\2\2\2\'\u00ef\3\2\2\2)\u00f8\3\2\2\2+\u00fd\3"+
		"\2\2\2-\u0103\3\2\2\2/\u0107\3\2\2\2\61\u010c\3\2\2\2\63\u0113\3\2\2\2"+
		"\65\u011a\3\2\2\2\67\u011e\3\2\2\29\u0125\3\2\2\2;\u012b\3\2\2\2=\u012d"+
		"\3\2\2\2?\u012f\3\2\2\2A\u0131\3\2\2\2C\u0133\3\2\2\2E\u0135\3\2\2\2G"+
		"\u0137\3\2\2\2I\u0139\3\2\2\2K\u013b\3\2\2\2M\u013d\3\2\2\2O\u013f\3\2"+
		"\2\2Q\u0141\3\2\2\2S\u0145\3\2\2\2U\u014a\3\2\2\2W\u014d\3\2\2\2Y\u0154"+
		"\3\2\2\2[\u015c\3\2\2\2]\u016f\3\2\2\2_\u0171\3\2\2\2a\u0173\3\2\2\2c"+
		"\u017b\3\2\2\2e\u0186\3\2\2\2g\u0191\3\2\2\2i\u019c\3\2\2\2k\u01a7\3\2"+
		"\2\2m\u01b6\3\2\2\2op\7K\2\2pq\7P\2\2qr\7V\2\2r\4\3\2\2\2st\7K\2\2tu\7"+
		"P\2\2uv\7V\2\2vw\7G\2\2wx\7I\2\2xy\7G\2\2yz\7T\2\2z\6\3\2\2\2{|\7D\2\2"+
		"|}\7K\2\2}~\7I\2\2~\177\7K\2\2\177\u0080\7P\2\2\u0080\u0081\7V\2\2\u0081"+
		"\b\3\2\2\2\u0082\u0083\7H\2\2\u0083\u0084\7N\2\2\u0084\u0085\7Q\2\2\u0085"+
		"\u0086\7C\2\2\u0086\u0087\7V\2\2\u0087\n\3\2\2\2\u0088\u0089\7F\2\2\u0089"+
		"\u008a\7Q\2\2\u008a\u008b\7W\2\2\u008b\u008c\7D\2\2\u008c\u008d\7N\2\2"+
		"\u008d\u008e\7G\2\2\u008e\f\3\2\2\2\u008f\u0090\7F\2\2\u0090\u0091\7G"+
		"\2\2\u0091\u0092\7E\2\2\u0092\16\3\2\2\2\u0093\u0094\7F\2\2\u0094\u0095"+
		"\7G\2\2\u0095\u0096\7E\2\2\u0096\u0097\7K\2\2\u0097\u0098\7O\2\2\u0098"+
		"\u0099\7C\2\2\u0099\u009a\7N\2\2\u009a\20\3\2\2\2\u009b\u009c\7P\2\2\u009c"+
		"\u009d\7W\2\2\u009d\u009e\7O\2\2\u009e\u009f\7G\2\2\u009f\u00a0\7T\2\2"+
		"\u00a0\u00a1\7K\2\2\u00a1\u00a2\7E\2\2\u00a2\22\3\2\2\2\u00a3\u00a4\7"+
		"D\2\2\u00a4\u00a5\7Q\2\2\u00a5\u00a6\7Q\2\2\u00a6\u00a7\7N\2\2\u00a7\u00a8"+
		"\7G\2\2\u00a8\u00a9\7C\2\2\u00a9\u00aa\7P\2\2\u00aa\24\3\2\2\2\u00ab\u00ac"+
		"\7E\2\2\u00ac\u00ad\7J\2\2\u00ad\u00ae\7C\2\2\u00ae\u00af\7T\2\2\u00af"+
		"\26\3\2\2\2\u00b0\u00b1\7E\2\2\u00b1\u00b2\7J\2\2\u00b2\u00b3\7C\2\2\u00b3"+
		"\u00b4\7T\2\2\u00b4\u00b5\7C\2\2\u00b5\u00b6\7E\2\2\u00b6\u00b7\7V\2\2"+
		"\u00b7\u00b8\7G\2\2\u00b8\u00b9\7T\2\2\u00b9\30\3\2\2\2\u00ba\u00bb\7"+
		"X\2\2\u00bb\u00bc\7C\2\2\u00bc\u00bd\7T\2\2\u00bd\u00be\7[\2\2\u00be\u00bf"+
		"\7K\2\2\u00bf\u00c0\7P\2\2\u00c0\u00c1\7I\2\2\u00c1\32\3\2\2\2\u00c2\u00c3"+
		"\7X\2\2\u00c3\u00c4\7C\2\2\u00c4\u00c5\7T\2\2\u00c5\u00c6\7E\2\2\u00c6"+
		"\u00c7\7J\2\2\u00c7\u00c8\7C\2\2\u00c8\u00c9\7T\2\2\u00c9\34\3\2\2\2\u00ca"+
		"\u00cb\7D\2\2\u00cb\u00cc\7K\2\2\u00cc\u00cd\7P\2\2\u00cd\u00ce\7C\2\2"+
		"\u00ce\u00cf\7T\2\2\u00cf\u00d0\7[\2\2\u00d0\36\3\2\2\2\u00d1\u00d2\7"+
		"X\2\2\u00d2\u00d3\7C\2\2\u00d3\u00d4\7T\2\2\u00d4\u00d5\7D\2\2\u00d5\u00d6"+
		"\7K\2\2\u00d6\u00d7\7P\2\2\u00d7\u00d8\7C\2\2\u00d8\u00d9\7T\2\2\u00d9"+
		"\u00da\7[\2\2\u00da \3\2\2\2\u00db\u00dc\7V\2\2\u00dc\u00dd\7K\2\2\u00dd"+
		"\u00de\7O\2\2\u00de\u00df\7G\2\2\u00df\"\3\2\2\2\u00e0\u00e1\7F\2\2\u00e1"+
		"\u00e2\7C\2\2\u00e2\u00e3\7V\2\2\u00e3\u00e4\7G\2\2\u00e4$\3\2\2\2\u00e5"+
		"\u00e6\7V\2\2\u00e6\u00e7\7K\2\2\u00e7\u00e8\7O\2\2\u00e8\u00e9\7G\2\2"+
		"\u00e9\u00ea\7U\2\2\u00ea\u00eb\7V\2\2\u00eb\u00ec\7C\2\2\u00ec\u00ed"+
		"\7O\2\2\u00ed\u00ee\7R\2\2\u00ee&\3\2\2\2\u00ef\u00f0\7K\2\2\u00f0\u00f1"+
		"\7P\2\2\u00f1\u00f2\7V\2\2\u00f2\u00f3\7G\2\2\u00f3\u00f4\7T\2\2\u00f4"+
		"\u00f5\7X\2\2\u00f5\u00f6\7C\2\2\u00f6\u00f7\7N\2\2\u00f7(\3\2\2\2\u00f8"+
		"\u00f9\7[\2\2\u00f9\u00fa\7G\2\2\u00fa\u00fb\7C\2\2\u00fb\u00fc\7T\2\2"+
		"\u00fc*\3\2\2\2\u00fd\u00fe\7O\2\2\u00fe\u00ff\7Q\2\2\u00ff\u0100\7P\2"+
		"\2\u0100\u0101\7V\2\2\u0101\u0102\7J\2\2\u0102,\3\2\2\2\u0103\u0104\7"+
		"F\2\2\u0104\u0105\7C\2\2\u0105\u0106\7[\2\2\u0106.\3\2\2\2\u0107\u0108"+
		"\7J\2\2\u0108\u0109\7Q\2\2\u0109\u010a\7W\2\2\u010a\u010b\7T\2\2\u010b"+
		"\60\3\2\2\2\u010c\u010d\7O\2\2\u010d\u010e\7K\2\2\u010e\u010f\7P\2\2\u010f"+
		"\u0110\7W\2\2\u0110\u0111\7V\2\2\u0111\u0112\7G\2\2\u0112\62\3\2\2\2\u0113"+
		"\u0114\7U\2\2\u0114\u0115\7G\2\2\u0115\u0116\7E\2\2\u0116\u0117\7Q\2\2"+
		"\u0117\u0118\7P\2\2\u0118\u0119\7F\2\2\u0119\64\3\2\2\2\u011a\u011b\7"+
		"O\2\2\u011b\u011c\7C\2\2\u011c\u011d\7R\2\2\u011d\66\3\2\2\2\u011e\u011f"+
		"\7U\2\2\u011f\u0120\7V\2\2\u0120\u0121\7T\2\2\u0121\u0122\7W\2\2\u0122"+
		"\u0123\7E\2\2\u0123\u0124\7V\2\2\u01248\3\2\2\2\u0125\u0126\7C\2\2\u0126"+
		"\u0127\7T\2\2\u0127\u0128\7T\2\2\u0128\u0129\7C\2\2\u0129\u012a\7[\2\2"+
		"\u012a:\3\2\2\2\u012b\u012c\7.\2\2\u012c<\3\2\2\2\u012d\u012e\7b\2\2\u012e"+
		">\3\2\2\2\u012f\u0130\7*\2\2\u0130@\3\2\2\2\u0131\u0132\7+\2\2\u0132B"+
		"\3\2\2\2\u0133\u0134\7>\2\2\u0134D\3\2\2\2\u0135\u0136\7@\2\2\u0136F\3"+
		"\2\2\2\u0137\u0138\7)\2\2\u0138H\3\2\2\2\u0139\u013a\7$\2\2\u013aJ\3\2"+
		"\2\2\u013b\u013c\7}\2\2\u013cL\3\2\2\2\u013d\u013e\7\177\2\2\u013eN\3"+
		"\2\2\2\u013f\u0140\7?\2\2\u0140P\3\2\2\2\u0141\u0142\7P\2\2\u0142\u0143"+
		"\7Q\2\2\u0143\u0144\7V\2\2\u0144R\3\2\2\2\u0145\u0146\7P\2\2\u0146\u0147"+
		"\7W\2\2\u0147\u0148\7N\2\2\u0148\u0149\7N\2\2\u0149T\3\2\2\2\u014a\u014b"+
		"\7C\2\2\u014b\u014c\7U\2\2\u014cV\3\2\2\2\u014d\u014e\7H\2\2\u014e\u014f"+
		"\7Q\2\2\u014f\u0150\7T\2\2\u0150\u0151\7O\2\2\u0151\u0152\7C\2\2\u0152"+
		"\u0153\7V\2\2\u0153X\3\2\2\2\u0154\u0155\7F\2\2\u0155\u0156\7G\2\2\u0156"+
		"\u0157\7H\2\2\u0157\u0158\7C\2\2\u0158\u0159\7W\2\2\u0159\u015a\7N\2\2"+
		"\u015a\u015b\7V\2\2\u015bZ\3\2\2\2\u015c\u015d\7R\2\2\u015d\u015e\7T\2"+
		"\2\u015e\u015f\7Q\2\2\u015f\u0160\7R\2\2\u0160\u0161\7G\2\2\u0161\u0162"+
		"\7T\2\2\u0162\u0163\7V\2\2\u0163\u0164\7K\2\2\u0164\u0165\7G\2\2\u0165"+
		"\u0166\7U\2\2\u0166\\\3\2\2\2\u0167\u016b\t\2\2\2\u0168\u016a\5_\60\2"+
		"\u0169\u0168\3\2\2\2\u016a\u016d\3\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c"+
		"\3\2\2\2\u016c\u0170\3\2\2\2\u016d\u016b\3\2\2\2\u016e\u0170\7\62\2\2"+
		"\u016f\u0167\3\2\2\2\u016f\u016e\3\2\2\2\u0170^\3\2\2\2\u0171\u0172\t"+
		"\3\2\2\u0172`\3\2\2\2\u0173\u0178\t\4\2\2\u0174\u0177\t\4\2\2\u0175\u0177"+
		"\5_\60\2\u0176\u0174\3\2\2\2\u0176\u0175\3\2\2\2\u0177\u017a\3\2\2\2\u0178"+
		"\u0176\3\2\2\2\u0178\u0179\3\2\2\2\u0179b\3\2\2\2\u017a\u0178\3\2\2\2"+
		"\u017b\u0181\5=\37\2\u017c\u0180\n\5\2\2\u017d\u017e\7^\2\2\u017e\u0180"+
		"\t\5\2\2\u017f\u017c\3\2\2\2\u017f\u017d\3\2\2\2\u0180\u0183\3\2\2\2\u0181"+
		"\u017f\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0184\3\2\2\2\u0183\u0181\3\2"+
		"\2\2\u0184\u0185\5=\37\2\u0185d\3\2\2\2\u0186\u018c\5G$\2\u0187\u018b"+
		"\n\6\2\2\u0188\u0189\7^\2\2\u0189\u018b\t\6\2\2\u018a\u0187\3\2\2\2\u018a"+
		"\u0188\3\2\2\2\u018b\u018e\3\2\2\2\u018c\u018a\3\2\2\2\u018c\u018d\3\2"+
		"\2\2\u018d\u018f\3\2\2\2\u018e\u018c\3\2\2\2\u018f\u0190\5G$\2\u0190f"+
		"\3\2\2\2\u0191\u0197\5I%\2\u0192\u0196\n\7\2\2\u0193\u0194\7^\2\2\u0194"+
		"\u0196\t\7\2\2\u0195\u0192\3\2\2\2\u0195\u0193\3\2\2\2\u0196\u0199\3\2"+
		"\2\2\u0197\u0195\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u019a\3\2\2\2\u0199"+
		"\u0197\3\2\2\2\u019a\u019b\5I%\2\u019bh\3\2\2\2\u019c\u019d\7\61\2\2\u019d"+
		"\u019e\7\61\2\2\u019e\u01a2\3\2\2\2\u019f\u01a1\n\b\2\2\u01a0\u019f\3"+
		"\2\2\2\u01a1\u01a4\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3"+
		"\u01a5\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a5\u01a6\b\65\2\2\u01a6j\3\2\2\2"+
		"\u01a7\u01a8\7\61\2\2\u01a8\u01a9\7,\2\2\u01a9\u01ad\3\2\2\2\u01aa\u01ac"+
		"\13\2\2\2\u01ab\u01aa\3\2\2\2\u01ac\u01af\3\2\2\2\u01ad\u01ae\3\2\2\2"+
		"\u01ad\u01ab\3\2\2\2\u01ae\u01b0\3\2\2\2\u01af\u01ad\3\2\2\2\u01b0\u01b1"+
		"\7,\2\2\u01b1\u01b2\7\61\2\2\u01b2\u01b3\3\2\2\2\u01b3\u01b4\b\66\2\2"+
		"\u01b4l\3\2\2\2\u01b5\u01b7\t\t\2\2\u01b6\u01b5\3\2\2\2\u01b7\u01b8\3"+
		"\2\2\2\u01b8\u01b6\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba"+
		"\u01bb\b\67\2\2\u01bbn\3\2\2\2\20\2\u016b\u016f\u0176\u0178\u017f\u0181"+
		"\u018a\u018c\u0195\u0197\u01a2\u01ad\u01b8\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}