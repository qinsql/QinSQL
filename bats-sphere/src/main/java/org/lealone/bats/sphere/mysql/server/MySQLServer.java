/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.mysql.server;

import java.util.Map;

import org.lealone.bats.sphere.bootstrap.SphereBootstrap;
import org.lealone.net.NetFactory;
import org.lealone.net.NetFactoryManager;
import org.lealone.net.NetNode;
import org.lealone.net.NetServer;
import org.lealone.server.DelegatedProtocolServer;

public class MySQLServer extends DelegatedProtocolServer {

    public static final String DATABASE_NAME = "mysql";
    public static final int DEFAULT_PORT = 9310;

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
        setProtocolServer(netServer);
        netServer.init(config);

        NetNode.setLocalTcpNode(getHost(), getPort());
    }

    @Override
    public boolean runInMainThread() {
        return protocolServer.runInMainThread();
    }

    @Override
    public synchronized void start() {
        if (isStarted())
            return;
        SphereBootstrap.start(getPort());
    }

    @Override
    public synchronized void stop() {
        if (isStopped())
            return;
        super.stop();
    }
}
