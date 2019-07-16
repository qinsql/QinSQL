package org.apache.calcite.rex;

import java.util.List;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.CorrelationId;
import org.apache.calcite.rel.core.Match.RexMRAggCall;
import org.apache.calcite.rel.core.Window.RexWinAggCall;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.type.SqlTypeName;

import com.google.common.collect.ImmutableList;

public interface RexFactory {

    RexCall makeCall(RelDataType type, SqlOperator op, List<? extends RexNode> exprs);

    RexInputRef makeInputRef(int index, RelDataType type);

    RexFieldAccess makeFieldAccess(RexNode expr, RelDataTypeField field);

    RexOver makeOver(RelDataType type, SqlAggFunction op, List<RexNode> operands, RexWindow window, boolean distinct);

    RexLiteral makeLiteral(Comparable value, RelDataType type, SqlTypeName typeName);

    RexRangeRef makeRangeReference(RelDataType rangeType, int offset);

    RexDynamicParam makeDynamicParam(RelDataType type, int index);

    RexCorrelVariable makeCorrel(CorrelationId id, RelDataType type);

    RexWinAggCall makeWinAggCall(SqlAggFunction aggFun, RelDataType type, List<RexNode> operands, int ordinal,
            boolean distinct);

    RexMRAggCall makeMRAggCall(SqlAggFunction aggFun, RelDataType type, List<RexNode> operands, int ordinal);

    RexSubQuery makeSubQuery(RelDataType type, SqlOperator op, ImmutableList<RexNode> operands, RelNode rel);

    RexLocalRef makeLocalRef(int index, RelDataType type);

}
