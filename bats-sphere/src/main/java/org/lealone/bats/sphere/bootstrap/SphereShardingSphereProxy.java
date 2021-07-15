/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lealone.bats.sphere.bootstrap;

import org.apache.shardingsphere.db.protocol.codec.PacketCodec;
import org.apache.shardingsphere.infra.database.type.DatabaseTypeRegistry;
import org.apache.shardingsphere.infra.spi.ShardingSphereServiceLoader;
import org.apache.shardingsphere.proxy.backend.context.BackendExecutorContext;
import org.apache.shardingsphere.proxy.frontend.netty.FrontendChannelInboundHandler;
import org.apache.shardingsphere.proxy.frontend.spi.DatabaseProtocolFrontendEngine;
import org.lealone.bats.sphere.mysql.jdbc.LealoneMySQLFrontendEngine;
import org.lealone.bats.sphere.postgresql.jdbc.LealonePostgreSQLFrontendEngine;
import org.lealone.bats.sphere.postgresql.server.PgServer;
import org.lealone.common.exceptions.DbException;
import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * ShardingSphere-Proxy.
 */
public final class SphereShardingSphereProxy {

    private static final Logger log = LoggerFactory.getLogger(SphereShardingSphereProxy.class);
    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    /**
     * Start ShardingSphere-Proxy.
     *
     * @param port port
     */
    public void start(final int port) {
        try {
            createEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            initServerBootstrap(bootstrap, port);
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("ShardingSphere-Proxy start success.");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw DbException.convert(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            BackendExecutorContext.getInstance().getExecutorKernel().close();
        }
    }

    private void createEventLoopGroup() {
        bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    private void initServerBootstrap(final ServerBootstrap bootstrap, int port) {
        bootstrap.group(bossGroup, workerGroup)
                .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(8 * 1024 * 1024, 16 * 1024 * 1024))
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true).handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerHandlerInitializer(port));
    }

    public final class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {
        final int port;

        public ServerHandlerInitializer(int port) {
            this.port = port;
        }

        @Override
        protected void initChannel(final SocketChannel socketChannel) {
            DatabaseProtocolFrontendEngine databaseProtocolFrontendEngine;
            if (port == PgServer.DEFAULT_PORT) {
                // databaseProtocolFrontendEngine = getDatabaseProtocolFrontendEngineFactory("LealonePostgreSQL");
                databaseProtocolFrontendEngine = LealonePostgreSQLFrontendEngine.INSTANCE;
            } else {
                // databaseProtocolFrontendEngine = getDatabaseProtocolFrontendEngineFactory("LealoneMySQL");
                databaseProtocolFrontendEngine = LealoneMySQLFrontendEngine.INSTANCE;
            }
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new PacketCodec(databaseProtocolFrontendEngine.getCodecEngine()));
            pipeline.addLast(new FrontendChannelInboundHandler(databaseProtocolFrontendEngine));
        }

        public DatabaseProtocolFrontendEngine getDatabaseProtocolFrontendEngineFactory(final String databaseType) {
            for (DatabaseProtocolFrontendEngine each : ShardingSphereServiceLoader
                    .newServiceInstances(DatabaseProtocolFrontendEngine.class)) {
                if (DatabaseTypeRegistry.getActualDatabaseType(databaseType).getName().equals(databaseType)) {
                    return each;
                }
            }
            throw new UnsupportedOperationException(String.format("Cannot support database type '%s'", databaseType));
        }
    }
}
