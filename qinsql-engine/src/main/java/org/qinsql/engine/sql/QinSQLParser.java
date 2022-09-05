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
package org.qinsql.engine.sql;

import org.lealone.common.exceptions.UnsupportedSchemaException;
import org.lealone.db.session.ServerSession;
import org.lealone.sql.LealoneSQLParser;
import org.lealone.sql.StatementBase;

public class QinSQLParser extends LealoneSQLParser {

    public QinSQLParser(ServerSession session) {
        super(session);
    }

    @Override
    public StatementBase parse(String sql) {
        try {
            return super.parse(sql);
        } catch (UnsupportedSchemaException e) {
            return new QinQuery((ServerSession) e.getSession(), sql);
        }
    }
}
