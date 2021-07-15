/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.test;

import org.lealone.main.Lealone;

public class LealoneSphereStart {

    public static void main(String[] args) {
        System.setProperty("lealone.ignore.unknown.settings", "true");
        Lealone.main(args);
    }
}
