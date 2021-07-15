/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.jdbc;

import java.util.Collection;
import java.util.Collections;

import org.apache.shardingsphere.proxy.backend.communication.jdbc.recognizer.spi.JDBCDriverURLRecognizer;
import org.lealone.client.jdbc.JdbcDriver;

public abstract class LealoneJDBCDriverURLRecognizer implements JDBCDriverURLRecognizer {

    @Override
    public Collection<String> getURLPrefixes() {
        return Collections.singletonList("jdbc:lealone:");
    }

    @Override
    public String getDriverClassName() {
        JdbcDriver.load();
        return JdbcDriver.class.getName();
    }
}
