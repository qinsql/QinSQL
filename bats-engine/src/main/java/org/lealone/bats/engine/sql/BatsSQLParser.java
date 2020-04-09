/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.bats.engine.sql;

import org.lealone.db.session.ServerSession;
import org.lealone.db.table.Column;
import org.lealone.db.table.Table;
import org.lealone.sql.Parser;
import org.lealone.sql.SQLParser;
import org.lealone.sql.StatementBase;
import org.lealone.sql.expression.Expression;

public class BatsSQLParser implements SQLParser {

    private final Parser parser;

    @Override
    public void setRightsChecked(boolean rightsChecked) {
        parser.setRightsChecked(rightsChecked);
    }

    @Override
    public Expression parseExpression(String sql) {
        return parser.parseExpression(sql);
    }

    public Table parseTableName(String sql) {
        return parser.parseTableName(sql);
    }

    @Override
    public StatementBase parse(String sql) {
        return parser.parse(sql);
    }

    @Override
    public Column parseColumnForTable(String columnSql) {
        return parser.parseColumnForTable(columnSql);
    }

    public BatsSQLParser(ServerSession session) {
        parser = new Parser(session);
    }
}
