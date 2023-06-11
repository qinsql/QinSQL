/// *
// * Copyright Lealone Database Group.
// * Licensed under the Server Side Public License, v 1.
// * Initial Developer: zhh
// */
// package org.qinsql.bench.misc;
//
// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.TimeUnit;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.function.BiFunction;
//
// import com.datastax.oss.driver.api.core.CqlSession;
// import com.datastax.oss.driver.api.core.cql.BatchStatement;
// import com.datastax.oss.driver.api.core.cql.BatchType;
// import com.datastax.oss.driver.api.core.cql.BoundStatement;
// import com.datastax.oss.driver.api.core.cql.PreparedStatement;
// import com.datastax.oss.driver.api.core.cql.ResultSet;
// import com.datastax.oss.driver.api.core.cql.Row;
//
//// 需要运行cassandra，用 cassandra -f 运行
// public class CasssandraInsertBTest {
// private int outerLoop = 30;
// private int innerLoop = 10;
// private int sqlCountPerInnerLoop = 50;
// private int threadCount = 32;
// boolean printInnerLoopResult;
// AtomicInteger id = new AtomicInteger();
//
// public static void main(String[] args) {
// new CasssandraInsertBTest().start();
// }
//
// private void start() {
// try (CqlSession session = CqlSession.builder().build()) {
// ResultSet rs = session.execute("select release_version from system.local");
// Row row = rs.one();
// System.out.println(row.getString("release_version"));
//
// createKeyspace(session);
// createTable(session);
// try {
// run();
// } catch (Exception e) {
// e.printStackTrace();
// }
//
// // rs = session.execute("select f2 from btest.test where f1<10 ALLOW FILTERING");
// // List<Row> rows = rs.all();
// // System.out.println("row count: " + rows.size());
// //
// // rs = session.execute("select count(*) as cnt from btest.test");
// // row = rs.one();
// // System.out.println("row count: " + row.getLong("cnt"));
// }
// }
//
// static void createKeyspace(CqlSession session) {
// String sql = "CREATE KEYSPACE IF NOT EXISTS btest "
// + "WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}";
// session.execute(sql);
// }
//
// private static void createTable(CqlSession session) {
// session.execute("use btest");
// session.execute("DROP TABLE IF EXISTS test");
// session.execute("CREATE TABLE test (f1 int PRIMARY KEY, f2 int)");
// }
//
// private void run() throws Exception {
// CqlSession[] sessions = new CqlSession[threadCount];
// for (int i = 0; i < threadCount; i++) {
// CqlSession session = CqlSession.builder().build();
// session.execute("use btest");
// sessions[i] = session;
// }
// for (int j = 0; j < outerLoop; j++) {
// UpdateThread[] threads = new UpdateThread[threadCount];
// for (int i = 0; i < threadCount; i++) {
// threads[i] = new UpdateThread(i, sessions[i]);
// threads[i].warmUp();
// }
// long t1 = System.nanoTime();
// for (int i = 0; i < threadCount; i++) {
// threads[i].start();
// }
// for (int i = 0; i < threadCount; i++) {
// threads[i].join();
// }
// long t2 = System.nanoTime();
// System.out.println("CasssandraInsertBTest sql count: "
// + (threadCount * innerLoop * sqlCountPerInnerLoop) //
// + " total time: " + TimeUnit.NANOSECONDS.toMillis(t2 - t1) + " ms");
// }
// for (int i = 0; i < threadCount; i++) {
// sessions[i].closeAsync();
// }
// }
//
// class UpdateThread extends Thread {
// CqlSession session;
// PreparedStatement statement;
//
// public UpdateThread(int id, CqlSession session) {
// super("Thread-" + id);
// this.session = session;
// statement = session.prepare("insert into test(f1,f2) values(?,1)");
// }
//
// String nextSql() {
// return "insert into test(f1,f2) values(" + id.incrementAndGet() + ",1)";
// }
//
// public void warmUp() throws Exception {
// }
//
// @Override
// public void run() {
// // executeUpdateSync();
// executeUpdateAsync();
// // executeBatchUpdateAsync();
// }
//
// protected void executeUpdateSync() {
// long t1 = System.nanoTime();
// for (int j = 0; j < innerLoop; j++) {
// for (int i = 0; i < sqlCountPerInnerLoop; i++) {
// String sql = nextSql();
// session.execute(sql);
// }
// }
// printInnerLoopResult(t1);
// }
//
// @SuppressWarnings({ "unchecked", "rawtypes" })
// protected void executeUpdateAsync() {
// long t1 = System.nanoTime();
// for (int j = 0; j < innerLoop; j++) {
// CountDownLatch latch = new CountDownLatch(sqlCountPerInnerLoop);
// for (int i = 0; i < sqlCountPerInnerLoop; i++) {
// String sql = nextSql();
// session.executeAsync(sql).handle(new BiFunction() {
// @Override
// public Object apply(Object t, Object u) {
// latch.countDown();
// return null;
// }
// });
// }
// try {
// latch.await();
// } catch (InterruptedException e) {
// e.printStackTrace();
// }
// }
// printInnerLoopResult(t1);
// }
//
// protected void executeBatchUpdateAsync() {
// long t1 = System.nanoTime();
// for (int j = 0; j < innerLoop; j++) {
// BatchStatement batchStatement = BatchStatement.newInstance(BatchType.UNLOGGED);
// for (int i = 0; i < sqlCountPerInnerLoop; i++) {
// BoundStatement boundStmt = statement.bind();
// boundStmt.setInt(0, id.incrementAndGet());
// batchStatement.add(boundStmt);
// }
// session.execute(batchStatement);
// }
// printInnerLoopResult(t1);
// }
//
// private void printInnerLoopResult(long t1) {
// if (printInnerLoopResult) {
// long t2 = System.nanoTime();
// System.out
// .println("CasssandraInsertBTest sql count: " + (innerLoop * sqlCountPerInnerLoop) //
// + " total time: " + TimeUnit.NANOSECONDS.toMillis(t2 - t1) + " ms");
// }
// }
// }
// }
