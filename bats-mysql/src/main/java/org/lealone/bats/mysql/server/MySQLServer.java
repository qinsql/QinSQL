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
package org.lealone.bats.mysql.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lealone.db.LealoneDatabase;
import org.lealone.net.AsyncConnection;
import org.lealone.net.AsyncConnectionManager;
import org.lealone.net.NetFactory;
import org.lealone.net.NetFactoryManager;
import org.lealone.net.NetNode;
import org.lealone.net.NetServer;
import org.lealone.net.WritableChannel;
import org.lealone.server.DelegatedProtocolServer;

public class MySQLServer extends DelegatedProtocolServer implements AsyncConnectionManager {

    public static final String DATABASE_NAME = "mysql";
    public static final int DEFAULT_PORT = 9310;

    private final Set<MySQLServerConnection> connections = Collections
            .synchronizedSet(new HashSet<MySQLServerConnection>());

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getType() {
        return MySQLServerEngine.NAME;
    }

    @Override
    public void init(Map<String, String> config) {
        if (!config.containsKey("port"))
            config.put("port", String.valueOf(DEFAULT_PORT));

        NetFactory factory = NetFactoryManager.getFactory(config);
        NetServer netServer = factory.createNetServer();
        netServer.setConnectionManager(this);
        setProtocolServer(netServer);
        netServer.init(config);

        NetNode.setLocalTcpNode(getHost(), getPort());
    }

    private void createDatabase() {
        String sql = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME //
                + " PARAMETERS(DEFAULT_SQL_ENGINE='" + MySQLServerEngine.NAME + "')";
        LealoneDatabase.getInstance().getSystemSession().prepareStatementLocal(sql).executeUpdate();
    }

    @Override
    public boolean runInMainThread() {
        return protocolServer.runInMainThread();
    }

    @Override
    public synchronized void start() {
        if (isStarted())
            return;
        super.start();

        createDatabase();
    }

    @Override
    public synchronized void stop() {
        if (isStopped())
            return;
        super.stop();

        for (MySQLServerConnection c : new ArrayList<>(connections)) {
            c.close();
        }
    }

    public synchronized void addConnection(MySQLServerConnection conn) {
        connections.add(conn);
    }

    @Override
    public synchronized AsyncConnection createConnection(WritableChannel writableChannel, boolean isServer) {
        MySQLServerConnection conn = new MySQLServerConnection(this, writableChannel, isServer);
        conn.handshake();
        return conn;
    }

    @Override
    public synchronized void removeConnection(AsyncConnection conn) {
        connections.remove(conn);
    }
}
