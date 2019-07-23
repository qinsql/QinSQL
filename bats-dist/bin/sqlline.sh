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

BATS_MAIN=sqlline.SqlLine -ac org.apache.drill.exec.client.DrillSqlLineApplication

JAVA_OPTS=-Xms10M
JAVA_OPTS="$JAVA_OPTS -Dlogback.configurationFile=logback.xml"
JAVA_OPTS="$JAVA_OPTS -Dbats.logdir=$BATS_HOME/logs"

CLASSPATH=$BATS_HOME/conf:%BATS_HOME%/lib/*

if [ "x$1" = "x" ]; then
    BATS_PARAMS="$1"
fi
if [ "x$2" = "x" ]; then
    BATS_PARAMS="$BATS_PARAMS $2"
fi

"$JAVA_HOME/bin/java" $JAVA_OPTS -cp $CLASSPATH $BATS_MAIN $BATS_PARAMS
