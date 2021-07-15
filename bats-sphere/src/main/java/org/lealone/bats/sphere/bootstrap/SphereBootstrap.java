/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.bootstrap;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.shardingsphere.proxy.arguments.BootstrapArguments;
import org.apache.shardingsphere.proxy.config.ProxyConfigurationLoader;
import org.apache.shardingsphere.proxy.config.YamlProxyConfiguration;
import org.apache.shardingsphere.proxy.initializer.BootstrapInitializer;
import org.lealone.common.exceptions.DbException;

public class SphereBootstrap {

    // private static boolean started;

    public static synchronized void start(int port) {
        // if (!started) {
        Thread t = new Thread(() -> {
            try {
                BootstrapArguments bootstrapArgs = new BootstrapArguments(new String[0]);
                YamlProxyConfiguration yamlConfig = ProxyConfigurationLoader.load(bootstrapArgs.getConfigurationPath());
                createBootstrapInitializer(yamlConfig).init(yamlConfig, port);
            } catch (Exception e) {
                throw DbException.convert(e);
            }
        });
        t.setDaemon(false);
        t.start();
        // started = true;
        // }
    }

    public static void main(final String[] args) throws IOException, SQLException {
        BootstrapArguments bootstrapArgs = new BootstrapArguments(args);
        YamlProxyConfiguration yamlConfig = ProxyConfigurationLoader.load(bootstrapArgs.getConfigurationPath());
        createBootstrapInitializer(yamlConfig).init(yamlConfig, bootstrapArgs.getPort());
    }

    private static BootstrapInitializer createBootstrapInitializer(final YamlProxyConfiguration yamlConfig) {
        return new SphereBootstrapInitializer();
    }
}
