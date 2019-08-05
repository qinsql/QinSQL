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

import org.lealone.bats.engine.sql.BatsSQLEngine;
import org.lealone.server.ProtocolServer;
import org.lealone.server.ProtocolServerEngineBase;

public class BatsServerEngine extends ProtocolServerEngineBase {

    public static final String NAME = BatsSQLEngine.NAME;

    private final BatsServer server = new BatsServer();

    public BatsServerEngine() {
        super(BatsSQLEngine.NAME);
    }

    @Override
    public ProtocolServer getProtocolServer() {
        return server;
    }

    @Override
    protected ProtocolServer getProtocolServer(int port) {
        return server;
    }

    @Override
    public void init(Map<String, String> config) {
        server.init(config);
    }

    @Override
    public void close() {
        server.stop();
    }

}
