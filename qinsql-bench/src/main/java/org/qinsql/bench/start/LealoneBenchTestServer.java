/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.start;

import org.lealone.main.Lealone;

public class LealoneBenchTestServer {

    public static void main(String[] args) {
        System.setProperty("lealone.config", "lealone-bench.yaml");
        Lealone.main(args);
    }
}
