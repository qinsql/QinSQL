/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.load;

/**
 * Copyright (C) 2011 CodeFutures Corporation. All rights reserved.
 */
public interface RecordLoader {

    void load(Record r) throws Exception;

    void close() throws Exception;

}
