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
package org.qinsql.test.mysql;

import org.lealone.common.util.Utils;
import org.qinsql.mysql.server.util.RandomUtil;
import org.qinsql.mysql.server.util.SecurityUtil;

public class PasswordTest {

    public static void main(String[] args) {
        String password = "PasswordTest";
        byte[] seed = RandomUtil.randomBytes(20);
        byte[] hash1 = SecurityUtil.scramble411(password.getBytes(), seed);

        byte[] sha1Pass = SecurityUtil.sha1(password);
        byte[] hash2 = SecurityUtil.scramble411Sha1Pass(sha1Pass, seed);

        System.out.println(Utils.compareSecure(hash1, hash2));
    }

}
