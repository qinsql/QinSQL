/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.jdbc;

import org.apache.shardingsphere.proxy.backend.communication.jdbc.connection.BackendConnection;
import org.apache.shardingsphere.proxy.frontend.context.FrontendContext;
import org.apache.shardingsphere.proxy.frontend.spi.DatabaseProtocolFrontendEngine;

public abstract class LealoneFrontendEngine implements DatabaseProtocolFrontendEngine {

    private final FrontendContext frontendContext = new FrontendContext(false, true);

    @Override
    public void release(final BackendConnection backendConnection) {
    }

    @Override
    public FrontendContext getFrontendContext() {
        return frontendContext;
    }
}
