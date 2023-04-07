/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test;

import org.lealone.main.Lealone;

//加上-Xbootclasspath/p:../qinsql-function/target/generated-sources;../qinsql-function/src/main/java
public class QinEngineStart {

    public static void main(String[] args) {
        Lealone.main(args);
    }

}
