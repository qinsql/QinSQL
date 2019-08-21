@REM
@REM  Licensed to the Apache Software Foundation (ASF) under one or more
@REM  contributor license agreements.  See the NOTICE file distributed with
@REM  this work for additional information regarding copyright ownership.
@REM  The ASF licenses this file to You under the Apache License, Version 2.0
@REM  (the "License"); you may not use this file except in compliance with
@REM  the License.  You may obtain a copy of the License at
@REM
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.

@echo off
if "%OS%" == "Windows_NT" setlocal

if NOT DEFINED JAVA_HOME goto :err

pushd %~dp0..
if NOT DEFINED BATS_HOME set BATS_HOME=%CD%
popd

if NOT DEFINED BATS_MAIN set BATS_MAIN=sqlline.SqlLine -ac org.lealone.bats.engine.BatsSqlLineApplication

set JAVA_OPTS=-Xms10M^
 -Dlogback.configurationFile=logback.xml^
 -Dbats.logdir="%BATS_HOME%\logs"

REM ***** CLASSPATH library setting *****

REM Ensure that any user defined CLASSPATH variables are not used on startup
set CLASSPATH="%BATS_HOME%\conf;%BATS_HOME%\lib\*"
goto okClasspath

:okClasspath
set BATS_PARAMS=%1 %2 %3 %4
goto runShell

set BATS_PARAMS=

:param
set str=%1
if "%str%"=="" (
    goto end
)
set BATS_PARAMS=%BATS_PARAMS% %str%
shift /0
goto param

:end
if "%BATS_PARAMS%"=="" (
    goto runShell
)

:runShell
"%JAVA_HOME%\bin\java" %JAVA_OPTS% -cp %CLASSPATH% %BATS_MAIN% %BATS_PARAMS%
goto finally

:err
echo JAVA_HOME environment variable must be set!
pause

:finally

ENDLOCAL
