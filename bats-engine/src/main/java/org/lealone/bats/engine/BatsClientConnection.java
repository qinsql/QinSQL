/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.bats.engine;

import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.drill.exec.physical.impl.materialize.QueryWritableBatch;
import org.apache.drill.exec.proto.GeneralRPCProtos.Ack;
import org.apache.drill.exec.proto.UserBitShared.QueryResult;
import org.apache.drill.exec.proto.UserBitShared.UserCredentials;
import org.apache.drill.exec.record.RecordBatch;
import org.apache.drill.exec.rpc.RpcOutcomeListener;
import org.apache.drill.exec.rpc.user.UserSession;
import org.apache.drill.exec.work.user.UserWorker;
import org.h2.result.ResultInterface;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class BatsClientConnection implements org.apache.drill.exec.rpc.UserClientConnection {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BatsClientConnection.class);

    private final UserSession session;
    private final SocketAddress remoteAddress;
    private final CountDownLatch resultReceived;

    private ResultInterface result;

    public BatsClientConnection(String userName, UserWorker userWorker, SocketAddress remoteAddress,
            final CountDownLatch resultReceived) {
        session = UserSession.Builder.newBuilder()
                .withCredentials(UserCredentials.newBuilder().setUserName(userName).build())
                .withOptionManager(userWorker.getSystemOptions())
                // .withUserProperties(inbound.getProperties())
                // .setSupportComplexTypes(inbound.getSupportComplexTypes())
                .build();
        this.remoteAddress = remoteAddress;
        this.resultReceived = resultReceived;
    }

    @Override
    public UserSession getSession() {
        return session;
    }

    @Override
    public void sendResult(RpcOutcomeListener<Ack> listener, QueryResult result) {
        logger.info("sendResult");
    }

    @Override
    public void sendData(RpcOutcomeListener<Ack> listener, QueryWritableBatch result) {
        throw new UnsupportedOperationException("sendData");
    }

    @Override
    public boolean needsRawData() {
        return true;
    }

    @Override
    public void sendData(RpcOutcomeListener<Ack> listener, RecordBatch data) {
        result = new BatsResult(data);
        resultReceived.countDown();
    }

    public ResultInterface getResult() {
        return result;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ChannelFuture getChannelClosureFuture() {
        return new ChannelFuture() {

            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public boolean isCancellable() {
                return false;
            }

            @Override
            public Throwable cause() {
                return null;
            }

            @Override
            public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public boolean await(long timeoutMillis) throws InterruptedException {
                return false;
            }

            @Override
            public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
                return false;
            }

            @Override
            public boolean awaitUninterruptibly(long timeoutMillis) {
                return false;
            }

            @Override
            public Void getNow() {
                return null;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Void get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Void get(long timeout, TimeUnit unit)
                    throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }

            @Override
            public Channel channel() {
                return null;
            }

            @Override
            public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
                return null;
            }

            @Override
            public ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
                return null;
            }

            @Override
            public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
                return null;
            }

            @Override
            public ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
                return null;
            }

            @Override
            public ChannelFuture sync() throws InterruptedException {
                return null;
            }

            @Override
            public ChannelFuture syncUninterruptibly() {
                return null;
            }

            @Override
            public ChannelFuture await() throws InterruptedException {
                return null;
            }

            @Override
            public ChannelFuture awaitUninterruptibly() {
                return null;
            }

        };
    }

}