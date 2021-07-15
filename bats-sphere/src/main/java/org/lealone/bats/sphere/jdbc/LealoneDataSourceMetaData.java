/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.jdbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.shardingsphere.infra.database.metadata.MemorizedDataSourceMetaData;
import org.apache.shardingsphere.infra.database.metadata.UnrecognizedDatabaseURLException;

/**
 * Data source meta data for Lealone.
 */
public class LealoneDataSourceMetaData implements MemorizedDataSourceMetaData {

    private static final int DEFAULT_PORT = -1;

    public static int getDefaultPort() {
        return DEFAULT_PORT;
    }

    private final String hostName;

    private final int port;

    private final String catalog;

    private final String schema;

    private final Pattern pattern = Pattern.compile("jdbc:lealone:(embed|~)[:/]([\\w\\-]+);?\\S*",
            Pattern.CASE_INSENSITIVE);

    public LealoneDataSourceMetaData(final String url) {
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new UnrecognizedDatabaseURLException(url, pattern.pattern());
        }
        hostName = "";
        port = DEFAULT_PORT;
        catalog = matcher.group(2);
        schema = null;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getCatalog() {
        return catalog;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
