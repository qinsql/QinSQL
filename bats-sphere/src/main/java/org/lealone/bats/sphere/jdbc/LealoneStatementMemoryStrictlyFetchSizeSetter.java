/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.jdbc;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.shardingsphere.proxy.backend.communication.jdbc.statement.fetchsize.StatementMemoryStrictlyFetchSizeSetter;
import org.lealone.db.SysProperties;

public abstract class LealoneStatementMemoryStrictlyFetchSizeSetter implements StatementMemoryStrictlyFetchSizeSetter {

    private Properties props;

    @Override
    public void setFetchSize(final Statement statement) throws SQLException {
        statement.setFetchSize(SysProperties.SERVER_RESULT_SET_FETCH_SIZE);
    }

    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void setProps(Properties props) {
        this.props = props;
    }
}
