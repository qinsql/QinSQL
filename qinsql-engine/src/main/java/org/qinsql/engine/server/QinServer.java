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
package org.qinsql.engine.server;

import java.util.Map;

import org.apache.drill.common.config.DrillConfig;
import org.apache.drill.exec.server.Drillbit;
import org.apache.drill.exec.server.RemoteServiceSet;
import org.apache.drill.exec.store.StoragePlugin;
import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.lealone.db.LealoneDatabase;
import org.lealone.net.AsyncConnectionManager;
import org.lealone.p2p.config.ConfigDescriptor;
import org.lealone.p2p.server.P2pClusterCoordinator;
import org.lealone.server.TcpServer;

public class QinServer extends TcpServer implements AsyncConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(QinServer.class);

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
        return QinServerEngine.NAME;
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
        drillbit.shutdown();
    }

    @SuppressWarnings("resource")
    private void startDrillbit() throws Exception {
        // 能查看org.apache.calcite.rel.metadata.JaninoRelMetadataProvider生成的代码
        // System.setProperty("calcite.debug", "true");

        DrillConfig drillConfig = DrillConfig.create();
        RemoteServiceSet serviceSet;
        if (ConfigDescriptor.getLocalNode() != null) {
            serviceSet = new RemoteServiceSet(new P2pClusterCoordinator());
        } else {
            serviceSet = RemoteServiceSet.getLocalServiceSet();
        }
        drillbit = new Drillbit(drillConfig, serviceSet);
        drillbit.setHostName(getHost());
        drillbit.run();

        for (Map.Entry<String, StoragePlugin> e : drillbit.getStoragePluginRegistry()) {
            LealoneDatabase.addUnsupportedSchema(e.getKey());
        }
    }
}
