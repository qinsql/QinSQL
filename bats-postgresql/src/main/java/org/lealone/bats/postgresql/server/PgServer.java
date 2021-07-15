/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.lealone.bats.postgresql.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.lealone.db.Constants;
import org.lealone.net.AsyncConnection;
import org.lealone.net.AsyncConnectionManager;
import org.lealone.net.NetFactory;
import org.lealone.net.NetFactoryManager;
import org.lealone.net.NetNode;
import org.lealone.net.NetServer;
import org.lealone.net.WritableChannel;
import org.lealone.server.DelegatedProtocolServer;

/**
 * This class implements a subset of the PostgreSQL protocol as described here:
 * http://developer.postgresql.org/pgdocs/postgres/protocol.html
 * The PostgreSQL catalog is described here:
 * http://www.postgresql.org/docs/7.4/static/catalogs.html
 *
 * @author Thomas Mueller
 * @author Sergi Vladykin 2009-07-03 (convertType)
 * @author zhh
 */
public class PgServer extends DelegatedProtocolServer implements AsyncConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(PgServer.class);

    public static String PG_CATALOG_FILE = "/org/lealone/bats/postgresql/resources/pg_catalog.sql";

    /**
     * The default port to use for the PG server.
     * This value is also in the documentation and in the Server javadoc.
     */
    public static final int DEFAULT_PORT = 9510;

    /**
     * The VARCHAR type.
     */
    public static final int PG_TYPE_VARCHAR = 1043;

    /**
     * The integer array type (for the column pg_index.indkey).
     */
    public static final int PG_TYPE_INT2VECTOR = 22;

    private static final int PG_TYPE_BOOL = 16;
    private static final int PG_TYPE_BYTEA = 17;
    private static final int PG_TYPE_BPCHAR = 1042;
    private static final int PG_TYPE_INT8 = 20;
    private static final int PG_TYPE_INT2 = 21;
    private static final int PG_TYPE_INT4 = 23;
    private static final int PG_TYPE_TEXT = 25;
    private static final int PG_TYPE_OID = 26;
    private static final int PG_TYPE_FLOAT4 = 700;
    private static final int PG_TYPE_FLOAT8 = 701;
    private static final int PG_TYPE_UNKNOWN = 705;
    private static final int PG_TYPE_TEXTARRAY = 1009;
    private static final int PG_TYPE_DATE = 1082;
    private static final int PG_TYPE_TIME = 1083;
    private static final int PG_TYPE_TIMESTAMP_NO_TMZONE = 1114;
    private static final int PG_TYPE_NUMERIC = 1700;

    private final HashSet<Integer> typeSet = new HashSet<>();
    private final Set<PgServerConnection> connections = Collections.synchronizedSet(new HashSet<PgServerConnection>());
    private boolean trace;

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getType() {
        return PgServerEngine.NAME;
    }

    void trace(String msg) {
        if (trace)
            logger.info(msg);
    }

    void traceError(Throwable e) {
        logger.info(e);
    }

    boolean getTrace() {
        return trace;
    }

    @Override
    public void init(Map<String, String> config) {
        if (!config.containsKey("port"))
            config.put("port", String.valueOf(DEFAULT_PORT));

        trace = Boolean.parseBoolean(config.get("trace"));

        NetFactory factory = NetFactoryManager.getFactory(config);
        NetServer netServer = factory.createNetServer();
        netServer.setConnectionManager(this);
        setProtocolServer(netServer);
        netServer.init(config);

        NetNode.setLocalTcpNode(getHost(), getPort());
    }

    @Override
    public boolean runInMainThread() {
        return protocolServer.runInMainThread();
    }

    @Override
    public synchronized void start() {
        if (isStarted())
            return;
        super.start();
    }

    @Override
    public synchronized void stop() {
        if (isStopped())
            return;
        super.stop();

        for (PgServerConnection c : new ArrayList<>(connections)) {
            c.close();
        }
    }

    /**
     * The Java implementation of the PostgreSQL function pg_get_indexdef. The
     * method is used to get CREATE INDEX command for an index, or the column
     * definition of one column in the index.
     *
     * @param conn the connection
     * @param indexId the index id
     * @param ordinalPosition the ordinal position (null if the SQL statement
     *            should be returned)
     * @param pretty this flag is ignored
     * @return the SQL statement or the column name
     */
    public static String getIndexColumn(Connection conn, int indexId, Integer ordinalPosition, Boolean pretty)
            throws SQLException {
        if (ordinalPosition == null || ordinalPosition.intValue() == 0) {
            PreparedStatement prep = conn.prepareStatement("select sql from information_schema.indexes where id=?");
            prep.setInt(1, indexId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return "";
        }
        PreparedStatement prep = conn.prepareStatement(
                "select column_name from information_schema.indexes where id=? and ordinal_position=?");
        prep.setInt(1, indexId);
        prep.setInt(2, ordinalPosition.intValue());
        ResultSet rs = prep.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        return "";
    }

    /**
     * Get the name of the current schema.
     * This method is called by the database.
     *
     * @param conn the connection
     * @return the schema name
     */
    public static String getCurrentSchema(Connection conn) throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("call schema()");
        rs.next();
        return rs.getString(1);
    }

    /**
     * Get the OID of an object.
     * This method is called by the database.
     *
     * @param conn the connection
     * @param tableName the table name
     * @return the oid
     */
    public static int getOid(Connection conn, String tableName) throws SQLException {
        if (tableName.startsWith("\"") && tableName.endsWith("\"")) {
            tableName = tableName.substring(1, tableName.length() - 1);
        }
        PreparedStatement prep = conn.prepareStatement("select oid from pg_class where relName = ?");
        prep.setString(1, tableName);
        ResultSet rs = prep.executeQuery();
        if (!rs.next()) {
            return 0;
        }
        return rs.getInt(1);
    }

    /**
     * Get the name of this encoding code.
     * This method is called by the database.
     *
     * @param code the encoding code
     * @return the encoding name
     */
    public static String getEncodingName(int code) {
        switch (code) {
        case 0:
            return "SQL_ASCII";
        case 6:
            return "UTF8";
        case 8:
            return "LATIN1";
        default:
            return code < 40 ? "UTF8" : "";
        }
    }

    /**
     * Get the version. This method must return PostgreSQL to keep some clients
     * happy. This method is called by the database.
     *
     * @return the server name and version
     */
    public static String getVersion() {
        return "PostgreSQL 8.1.4  server protocol using H2 " + Constants.getFullVersion();
    }

    /**
     * Get the current system time.
     * This method is called by the database.
     *
     * @return the current system time
     */
    public static Timestamp getStartTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Get the user name for this id.
     * This method is called by the database.
     *
     * @param conn the connection
     * @param id the user id
     * @return the user name
     */
    public static String getUserById(Connection conn, int id) throws SQLException {
        PreparedStatement prep = conn.prepareStatement("SELECT NAME FROM INFORMATION_SCHEMA.USERS WHERE ID=?");
        prep.setInt(1, id);
        ResultSet rs = prep.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    /**
     * Check if the this session has the given database privilege.
     * This method is called by the database.
     *
     * @param id the session id
     * @param privilege the privilege to check
     * @return true
     */
    public static boolean hasDatabasePrivilege(int id, String privilege) {
        return true;
    }

    /**
     * Check if the current session has access to this table.
     * This method is called by the database.
     *
     * @param table the table name
     * @param privilege the privilege to check
     * @return true
     */
    public static boolean hasTablePrivilege(String table, String privilege) {
        return true;
    }

    /**
     * Get the current transaction id.
     * This method is called by the database.
     *
     * @param table the table name
     * @param id the id
     * @return 1
     */
    public static int getCurrentTid(String table, String id) {
        return 1;
    }

    /**
     * Convert the SQL type to a PostgreSQL type
     *
     * @param type the SQL type
     * @return the PostgreSQL type
     */
    public static int convertType(final int type) {
        switch (type) {
        case Types.BOOLEAN:
            return PG_TYPE_BOOL;
        case Types.VARCHAR:
            return PG_TYPE_VARCHAR;
        case Types.CLOB:
            return PG_TYPE_TEXT;
        case Types.CHAR:
            return PG_TYPE_BPCHAR;
        case Types.SMALLINT:
            return PG_TYPE_INT2;
        case Types.INTEGER:
            return PG_TYPE_INT4;
        case Types.BIGINT:
            return PG_TYPE_INT8;
        case Types.DECIMAL:
            return PG_TYPE_NUMERIC;
        case Types.REAL:
            return PG_TYPE_FLOAT4;
        case Types.DOUBLE:
            return PG_TYPE_FLOAT8;
        case Types.TIME:
            return PG_TYPE_TIME;
        case Types.DATE:
            return PG_TYPE_DATE;
        case Types.TIMESTAMP:
            return PG_TYPE_TIMESTAMP_NO_TMZONE;
        case Types.VARBINARY:
            return PG_TYPE_BYTEA;
        case Types.BLOB:
            return PG_TYPE_OID;
        case Types.ARRAY:
            return PG_TYPE_TEXTARRAY;
        default:
            return PG_TYPE_UNKNOWN;
        }
    }

    /**
     * Get the type hash set.
     *
     * @return the type set
     */
    HashSet<Integer> getTypeSet() {
        return typeSet;
    }

    /**
     * Check whether a data type is supported.
     * A warning is logged if not.
     *
     * @param type the type
     */
    void checkType(int type) {
        if (!typeSet.contains(type)) {
            logger.info("Unsupported type: " + type);
        }
    }

    @Override
    public synchronized AsyncConnection createConnection(WritableChannel writableChannel, boolean isServer) {
        PgServerConnection conn = new PgServerConnection(this, writableChannel, isServer);
        connections.add(conn);
        conn.setProcessId(connections.size());
        return conn;

    }

    @Override
    public synchronized void removeConnection(AsyncConnection conn) {
        connections.remove(conn);
    }

}
