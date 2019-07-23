#!/bin/sh

#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
if [ "x$BATS_HOME" = "x" ]; then
    BATS_HOME="`dirname "$0"`/.."
fi

if [ "x$JAVA_HOME" = "x" ]; then
    echo JAVA_HOME environment variable must be set!
    exit 1;
fi

BATS_MAIN=org.lealone.bats.engine.BatsEngine

# JAVA_OPTS=-ea
# JAVA_OPTS="$JAVA_OPTS -Xms10M"
# JAVA_OPTS="$JAVA_OPTS -Xmx1G"
# JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
# JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
# JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
# JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
# JAVA_OPTS="$JAVA_OPTS -XX:SurvivorRatio=8"
# JAVA_OPTS="$JAVA_OPTS -XX:MaxTenuringThreshold=1"
# JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
# JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"

JAVA_OPTS=-Xms10M
JAVA_OPTS="$JAVA_OPTS -Dlogback.configurationFile=logback.xml"
JAVA_OPTS="$JAVA_OPTS -Dbats.logdir=$BATS_HOME/logs"
# JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y"

CLASSPATH=$BATS_HOME/conf:%BATS_HOME%/lib/*

"$JAVA_HOME/bin/java" $JAVA_OPTS -cp $CLASSPATH $BATS_MAIN
