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
package org.qinsql.test.start;

import java.util.ArrayList;

import org.lealone.common.exceptions.ConfigException;
import org.lealone.db.Constants;
import org.lealone.p2p.config.Config;
import org.lealone.p2p.config.Config.PluggableEngineDef;
import org.lealone.p2p.server.P2pServerEngine;
import org.qinsql.engine.server.QinServerEngine;

//加上-Xbootclasspath/p:../qinsql-function/target/generated-sources;../qinsql-function/src/main/java
public class QinEngineStart extends NodeBase {

    // YamlConfigLoader的子类必须有一个无参数的构造函数
    public QinEngineStart() {
        nodeBaseDirPrefix = "qinsql";
    }

    @Override
    public void applyConfig(Config config) throws ConfigException {
        // enableQinServer(config);
        for (PluggableEngineDef e : config.protocol_server_engines) {
            if (P2pServerEngine.NAME.equalsIgnoreCase(e.name)) {
                e.enabled = false;
            }
        }
        super.applyConfig(config);
    }

    public static void main(String[] args) throws Exception {
        NodeBase.run(QinEngineStart.class, null);
    }

    public static void enableQinServer(Config config) {
        enableProtocolServer(config, QinServerEngine.NAME, Constants.DEFAULT_TCP_PORT);
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
