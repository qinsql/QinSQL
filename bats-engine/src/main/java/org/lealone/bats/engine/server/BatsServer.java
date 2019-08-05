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
package org.lealone.bats.engine.server;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.drill.common.config.DrillConfig;
import org.apache.drill.exec.server.Drillbit;
import org.apache.drill.exec.server.RemoteServiceSet;
import org.lealone.common.exceptions.DbException;
import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.lealone.db.api.ErrorCode;
import org.lealone.net.AsyncConnection;
import org.lealone.net.AsyncConnectionManager;
import org.lealone.net.NetFactory;
import org.lealone.net.NetFactoryManager;
import org.lealone.net.NetServer;
import org.lealone.net.WritableChannel;
import org.lealone.server.DelegatedProtocolServer;

public class BatsServer extends DelegatedProtocolServer implements AsyncConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(BatsServer.class);
    public static final int DEFAULT_TCP_PORT = 7216;

    private final CopyOnWriteArrayList<AsyncConnection> connections = new CopyOnWriteArrayList<>();
    private Drillbit drillbit;

    public Drillbit getDrillbit() {
        return drillbit;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getType() {
        return BatsServerEngine.NAME;
    }

    @Override
    public void init(Map<String, String> config) {
        if (!config.containsKey("port"))
            config.put("port", String.valueOf(DEFAULT_TCP_PORT));

        NetFactory factory = NetFactoryManager.getFactory(config);
        NetServer netServer = factory.createNetServer();
        netServer.setConnectionManager(this);
        setProtocolServer(netServer);
        netServer.init(config);

        // NetEndpoint.setLocalTcpEndpoint(getHost(), getPort());
    }

    @Override
    public synchronized void start() {
        if (isStarted())
            return;
        super.start();

        try {
            startDrillbit();
        } catch (Exception e) {
            logger.error("Failed to start drillbit", e);
        }
    }

    @Override
    public synchronized void stop() {
        if (isStopped())
            return;
        super.stop();
    }

    @Override
    public AsyncConnection createConnection(WritableChannel writableChannel, boolean isServer) {
        if (getAllowOthers() || allow(writableChannel.getHost())) {
            BatsServerConnection conn = new BatsServerConnection(this, writableChannel, isServer);
            connections.add(conn);
            return conn;
        } else {
            writableChannel.close();
            throw DbException.get(ErrorCode.REMOTE_CONNECTION_NOT_ALLOWED);
        }
    }

    @Override
    public void removeConnection(AsyncConnection conn) {
        connections.remove(conn);
        conn.close();
    }

    private void startDrillbit() throws Exception {
        // 能查看org.apache.calcite.rel.metadata.JaninoRelMetadataProvider生成的代码
        // System.setProperty("calcite.debug", "true");

        DrillConfig drillConfig = DrillConfig.create();
        RemoteServiceSet serviceSet = RemoteServiceSet.getLocalServiceSet();
        drillbit = new Drillbit(drillConfig, serviceSet);
        drillbit.run();
    }
}
