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

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.drill.common.config.DrillConfig;
import org.apache.drill.exec.server.Drillbit;
import org.apache.drill.exec.server.RemoteServiceSet;

public class BatsEngine {

    public static SqlNode parse(String sql) throws SqlParseException {
        SqlParser.Config config = SqlParser.configBuilder().setUnquotedCasing(org.apache.calcite.util.Casing.TO_LOWER)
                .build();
        return parse(sql, config);
    }

    public static SqlNode parse(String sql, SqlParser.Config config) throws SqlParseException {
        SqlParser sqlParser = SqlParser.create(sql, config);
        return sqlParser.parseQuery();
    }

    public static void start() throws Exception {
        startH2();
        startDrillbit();
    }

    private static void startDrillbit() throws Exception {
        // 能查看org.apache.calcite.rel.metadata.JaninoRelMetadataProvider生成的代码
        // System.setProperty("calcite.debug", "true");

        DrillConfig drillConfig = DrillConfig.create();
        RemoteServiceSet serviceSet = RemoteServiceSet.getLocalServiceSet();
        Drillbit drillbit = new Drillbit(drillConfig, serviceSet);
        drillbit.run();
    }

    private static void startH2() throws SQLException {
        // System.setProperty("DATABASE_TO_UPPER", "false");
        // System.setProperty("h2.lobInDatabase", "false");
        // System.setProperty("h2.lobClientMaxSizeMemory", "1024");
        System.setProperty("java.io.tmpdir", "./target/mytest/tmp");
        System.setProperty("h2.baseDir", "./target/mytest");
        // System.setProperty("h2.check2", "true");
        ArrayList<String> list = new ArrayList<String>();
        // list.add("-tcp");
        // //list.add("-tool");
        // org.h2.tools.Server.main(list.toArray(new String[list.size()]));
        //
        // list.add("-tcp");
        // list.add("-tcpPort");
        // list.add("9092");

        // list.add("-pg");
        list.add("-tcp");
        // list.add("-web");
        // list.add("-ifExists");
        org.h2.tools.Server.main(list.toArray(new String[list.size()]));
    }
}
