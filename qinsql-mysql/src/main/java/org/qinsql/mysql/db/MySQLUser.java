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
package org.qinsql.mysql.db;

import org.lealone.common.util.Utils;
import org.lealone.db.Database;
import org.lealone.db.auth.User;
import org.qinsql.mysql.server.util.SecurityUtil;

public class MySQLUser extends User {

    public MySQLUser(Database database, int id, String userName, boolean systemUser) {
        super(database, id, userName, systemUser);
    }

    @Override
    public boolean validateUserPasswordHash(byte[] userPasswordHash, byte[] salt) {
        if (userPasswordHash.length == 0 && getPasswordHash().length == 0) {
            return true;
        }
        if (userPasswordHash.length != getPasswordHash().length) {
            return false;
        }
        byte[] hash = SecurityUtil.scramble411Sha1Pass(getPasswordHash(), salt);
        return Utils.compareSecure(userPasswordHash, hash);
    }
}
