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
package org.lealone.bats.engine.operator;

import org.lealone.bats.engine.sql.BatsQuery;
import org.lealone.db.result.LocalResult;
import org.lealone.db.session.ServerSession;
import org.lealone.sql.operator.Operator;
import org.lealone.sql.query.Select;

public class OlapOperator implements Operator {

    private Select select;
    private LocalResult localResult;
    private BatsQuery query;

    public OlapOperator(Select select, LocalResult localResult) {
        this.select = select;
        this.localResult = localResult;
    }

    @Override
    public void start() {
        ServerSession session = select.getSession();
        String sql = select.getSQL();
        query = new BatsQuery(session, sql);
        query.setLocalResult(localResult);
        query.setCursor(select.getTableFilter().getCursor());
        query.createYieldableQuery(-1, false, null).run();
    }

    @Override
    public void run() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStopped() {
        return query.isStopped();
    }

    @Override
    public LocalResult getLocalResult() {
        return localResult;
    }
}
