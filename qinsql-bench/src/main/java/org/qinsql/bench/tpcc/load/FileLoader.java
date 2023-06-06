/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.load;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C) 2011 CodeFutures Corporation. All rights reserved.
 */
public class FileLoader implements RecordLoader {

    protected static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected BufferedOutputStream os;

    protected final StringBuilder b = new StringBuilder();

    public FileLoader(File file) throws IOException {
        this.os = new BufferedOutputStream(new FileOutputStream(file, true));
    }

    @Override
    public void load(Record r) throws Exception {
        b.setLength(0);
        final Object[] field = r.getField();
        for (int i = 0; i < field.length; i++) {
            if (i > 0) {
                b.append('\t');
            }
            if (field[i] == null) {
                b.append("\\N");
            } else if (field[i] instanceof Date) {
                b.append(dateTimeFormat.format((Date) field[i]));
            } else {
                b.append(field[i]);
            }
        }
        os.write(b.toString().getBytes());
        os.write("\n".getBytes());
    }

    @Override
    public void commit() throws Exception {
        // ignore
    }

    @Override
    public void close() throws Exception {
        os.close();
    }
}
