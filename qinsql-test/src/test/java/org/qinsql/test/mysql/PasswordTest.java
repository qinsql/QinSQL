/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
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
