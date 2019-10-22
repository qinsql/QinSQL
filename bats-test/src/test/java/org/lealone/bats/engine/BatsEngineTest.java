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
package org.lealone.bats.engine;

import java.util.ArrayList;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.lealone.bats.engine.server.BatsServer;
import org.lealone.bats.engine.server.BatsServerEngine;
import org.lealone.common.exceptions.ConfigException;
import org.lealone.p2p.config.Config;
import org.lealone.p2p.config.Config.PluggableEngineDef;

//加上-Xbootclasspath/p:../bats-function/target/generated-sources;../bats-function/src/main/java
public class BatsEngineTest extends NodeBase {

    // YamlConfigLoader的子类必须有一个无参数的构造函数
    public BatsEngineTest() {
        nodeBaseDirPrefix = "bats";
    }

    @Override
    public void applyConfig(Config config) throws ConfigException {
        enableBatsServer(config);
        super.applyConfig(config);
    }

    public static void main(String[] args) throws Exception {
        // parse();
        NodeBase.run(BatsEngineTest.class, null);
    }

    public static void parse() throws SqlParseException {
        String sql = "select * from test where f1=1 or f2=2 order by f3 limit 2";
        SqlNode sqlNode = BatsEngine.parse(sql);
        System.out.println(sqlNode);
    }

    public static void enableBatsServer(Config config) {
        enableProtocolServer(config, BatsServerEngine.NAME, BatsServer.DEFAULT_TCP_PORT);
    }

    private static void enableProtocolServer(Config config, String protocolServerName, int port) {
        if (config.protocol_server_engines == null) {
            config.protocol_server_engines = new ArrayList<>(1);
        }

        PluggableEngineDef def = new PluggableEngineDef();
        def.enabled = true;
        def.name = protocolServerName;
        def.getParameters().put("port", port + "");
        def.getParameters().put("allow_others", "true");

        config.protocol_server_engines.add(def);
    }
}
