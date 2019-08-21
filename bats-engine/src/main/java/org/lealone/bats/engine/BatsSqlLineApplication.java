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

import org.apache.drill.exec.client.DrillSqlLineApplication;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;

import sqlline.ConnectionMetadata;
import sqlline.PromptHandler;
import sqlline.SqlLine;

public class BatsSqlLineApplication extends DrillSqlLineApplication {

    public BatsSqlLineApplication() {
        super("bats-sqlline.conf", "bats-sqlline-override.conf");
    }

    @Override
    public PromptHandler getPromptHandler(SqlLine sqlLine) {
        // if (config.hasPath(PROMPT_WITH_SCHEMA) && config.getBoolean(PROMPT_WITH_SCHEMA)) {
        return new PromptHandler(sqlLine) {
            @Override
            protected AttributedString getDefaultPrompt(int connectionIndex, String url, String defaultPrompt) {
                AttributedStringBuilder builder = new AttributedStringBuilder();
                builder.style(resolveStyle("f:y"));
                builder.append("bats");

                ConnectionMetadata meta = sqlLine.getConnectionMetadata();

                String currentSchema = meta.getCurrentSchema();
                if (currentSchema != null) {
                    builder.append(" (").append(currentSchema).append(")");
                }
                return builder.style(resolveStyle("default")).append("> ").toAttributedString();
            }
        };
        // }
        // return super.getPromptHandler(sqlLine);
    }
}
