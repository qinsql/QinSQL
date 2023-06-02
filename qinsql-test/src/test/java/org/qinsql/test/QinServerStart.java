/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test;

import org.qinsql.main.QinSQL;

//加以下参数才能正常运行jdk vector
//--add-modules jdk.incubator.vector -server
public class QinServerStart {

    public static void main(String[] args) {
        QinSQL.main(args, "qinsql-test.yaml");
    }

}
