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
package org.lealone.bats.test.executor;

import org.apache.drill.common.config.DrillConfig;
import org.apache.drill.exec.server.Drillbit;
import org.apache.drill.exec.server.RemoteServiceSet;

// 加-Xbootclasspath/p:../bats-executor/src/main/java;../bats-test/target/test-data
public class ExecutorTest {

    public static void main(String[] args) throws Throwable {
        // 能查看org.apache.calcite.rel.metadata.JaninoRelMetadataProvider生成的代码
        System.setProperty("calcite.debug", "true");

        DrillConfig drillConfig = DrillConfig.create();
        RemoteServiceSet serviceSet = RemoteServiceSet.getLocalServiceSet();
        Drillbit drillbit = new Drillbit(drillConfig, serviceSet);
        drillbit.run();
    }

}
