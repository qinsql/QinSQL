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

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExprParser}.
 */
public interface ExprParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExprParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(ExprParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(ExprParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(ExprParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(ExprParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#convertCall}.
	 * @param ctx the parse tree
	 */
	void enterConvertCall(ExprParser.ConvertCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#convertCall}.
	 * @param ctx the parse tree
	 */
	void exitConvertCall(ExprParser.ConvertCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#anyValueCall}.
	 * @param ctx the parse tree
	 */
	void enterAnyValueCall(ExprParser.AnyValueCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#anyValueCall}.
	 * @param ctx the parse tree
	 */
	void exitAnyValueCall(ExprParser.AnyValueCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#castCall}.
	 * @param ctx the parse tree
	 */
	void enterCastCall(ExprParser.CastCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#castCall}.
	 * @param ctx the parse tree
	 */
	void exitCastCall(ExprParser.CastCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#repeat}.
	 * @param ctx the parse tree
	 */
	void enterRepeat(ExprParser.RepeatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#repeat}.
	 * @param ctx the parse tree
	 */
	void exitRepeat(ExprParser.RepeatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(ExprParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(ExprParser.DataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#booleanType}.
	 * @param ctx the parse tree
	 */
	void enterBooleanType(ExprParser.BooleanTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#booleanType}.
	 * @param ctx the parse tree
	 */
	void exitBooleanType(ExprParser.BooleanTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#numType}.
	 * @param ctx the parse tree
	 */
	void enterNumType(ExprParser.NumTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#numType}.
	 * @param ctx the parse tree
	 */
	void exitNumType(ExprParser.NumTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#charType}.
	 * @param ctx the parse tree
	 */
	void enterCharType(ExprParser.CharTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#charType}.
	 * @param ctx the parse tree
	 */
	void exitCharType(ExprParser.CharTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#precision}.
	 * @param ctx the parse tree
	 */
	void enterPrecision(ExprParser.PrecisionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#precision}.
	 * @param ctx the parse tree
	 */
	void exitPrecision(ExprParser.PrecisionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#scale}.
	 * @param ctx the parse tree
	 */
	void enterScale(ExprParser.ScaleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#scale}.
	 * @param ctx the parse tree
	 */
	void exitScale(ExprParser.ScaleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#dateType}.
	 * @param ctx the parse tree
	 */
	void enterDateType(ExprParser.DateTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#dateType}.
	 * @param ctx the parse tree
	 */
	void exitDateType(ExprParser.DateTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#typeLen}.
	 * @param ctx the parse tree
	 */
	void enterTypeLen(ExprParser.TypeLenContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#typeLen}.
	 * @param ctx the parse tree
	 */
	void exitTypeLen(ExprParser.TypeLenContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(ExprParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(ExprParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#ifStat}.
	 * @param ctx the parse tree
	 */
	void enterIfStat(ExprParser.IfStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#ifStat}.
	 * @param ctx the parse tree
	 */
	void exitIfStat(ExprParser.IfStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#elseIfStat}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStat(ExprParser.ElseIfStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#elseIfStat}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStat(ExprParser.ElseIfStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(ExprParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(ExprParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#caseWhenStat}.
	 * @param ctx the parse tree
	 */
	void enterCaseWhenStat(ExprParser.CaseWhenStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#caseWhenStat}.
	 * @param ctx the parse tree
	 */
	void exitCaseWhenStat(ExprParser.CaseWhenStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#caseElseStat}.
	 * @param ctx the parse tree
	 */
	void enterCaseElseStat(ExprParser.CaseElseStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#caseElseStat}.
	 * @param ctx the parse tree
	 */
	void exitCaseElseStat(ExprParser.CaseElseStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#exprList}.
	 * @param ctx the parse tree
	 */
	void enterExprList(ExprParser.ExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#exprList}.
	 * @param ctx the parse tree
	 */
	void exitExprList(ExprParser.ExprListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ExprParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ExprParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#condExpr}.
	 * @param ctx the parse tree
	 */
	void enterCondExpr(ExprParser.CondExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#condExpr}.
	 * @param ctx the parse tree
	 */
	void exitCondExpr(ExprParser.CondExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(ExprParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(ExprParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(ExprParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(ExprParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#equExpr}.
	 * @param ctx the parse tree
	 */
	void enterEquExpr(ExprParser.EquExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#equExpr}.
	 * @param ctx the parse tree
	 */
	void exitEquExpr(ExprParser.EquExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#relExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelExpr(ExprParser.RelExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#relExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelExpr(ExprParser.RelExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(ExprParser.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(ExprParser.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void enterMulExpr(ExprParser.MulExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void exitMulExpr(ExprParser.MulExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#xorExpr}.
	 * @param ctx the parse tree
	 */
	void enterXorExpr(ExprParser.XorExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#xorExpr}.
	 * @param ctx the parse tree
	 */
	void exitXorExpr(ExprParser.XorExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(ExprParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(ExprParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(ExprParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(ExprParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#pathSegment}.
	 * @param ctx the parse tree
	 */
	void enterPathSegment(ExprParser.PathSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#pathSegment}.
	 * @param ctx the parse tree
	 */
	void exitPathSegment(ExprParser.PathSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#nameSegment}.
	 * @param ctx the parse tree
	 */
	void enterNameSegment(ExprParser.NameSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#nameSegment}.
	 * @param ctx the parse tree
	 */
	void exitNameSegment(ExprParser.NameSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#arraySegment}.
	 * @param ctx the parse tree
	 */
	void enterArraySegment(ExprParser.ArraySegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#arraySegment}.
	 * @param ctx the parse tree
	 */
	void exitArraySegment(ExprParser.ArraySegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#lookup}.
	 * @param ctx the parse tree
	 */
	void enterLookup(ExprParser.LookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#lookup}.
	 * @param ctx the parse tree
	 */
	void exitLookup(ExprParser.LookupContext ctx);
}