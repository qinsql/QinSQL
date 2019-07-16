package org.apache.calcite.schema;

import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.sql.validate.SqlValidatorTable;

/** Definition of a table, for the purposes of the validator and planner. */
  public interface PreparingTable
      extends RelOptTable, SqlValidatorTable {
  }