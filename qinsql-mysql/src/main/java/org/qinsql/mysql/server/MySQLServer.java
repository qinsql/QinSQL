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
package org.qinsql.mysql.server;

import java.util.Map;

import org.lealone.db.LealoneDatabase;
import org.lealone.net.WritableChannel;
import org.lealone.server.AsyncServer;
import org.lealone.server.Scheduler;

public class MySQLServer extends AsyncServer<MySQLServerConnection> {

    public static final String DATABASE_NAME = "mysql";
    public static final int DEFAULT_PORT = 9310;

    @Override
    public String getType() {
        return MySQLServerEngine.NAME;
    }

    @Override
    public void init(Map<String, String> config) {
        super.init(config);

        // 创建默认的 mysql 数据库
        String sql = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME //
                + " PARAMETERS(DEFAULT_SQL_ENGINE='" + MySQLServerEngine.NAME + "')";
        LealoneDatabase.getInstance().getSystemSession().prepareStatementLocal(sql).executeUpdate();
    }

    @Override
    protected int getDefaultPort() {
        return DEFAULT_PORT;
    }

    @Override
    protected MySQLServerConnection createConnection(WritableChannel writableChannel,
            Scheduler scheduler) {
        return new MySQLServerConnection(this, writableChannel, scheduler);
    }

    @Override
    protected void afterRegister(MySQLServerConnection conn, Scheduler scheduler) {
        int threadId = scheduler.getHandlerId();
        // 连接创建成功后先握手
        conn.handshake(threadId);
    }
}
