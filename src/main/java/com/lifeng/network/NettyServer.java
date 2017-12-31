package com.lifeng.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyServer {
    private final static Logger logger = LogManager.getLogger(NettyServer.class);
    private NettyConfig nettyConfig;
    private ServerBootstrap bootstrap;
    private ChannelFuture bind;

    NettyServer(NettyConfig nettyConfig){
        this.nettyConfig = nettyConfig;
    }

    void init(final NettyProtocol protocol) throws Exception {

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(nettyConfig.getServerBossThreadNum());
        EventLoopGroup workerGroup = new NioEventLoopGroup(nettyConfig.getServerWorkerThreadNum());

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, nettyConfig.getConnectBackLog())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(protocol.getServerChannelHandlers());
                    }
                });

        // Start the server.
        bind = bootstrap.bind(nettyConfig.getServerPort()).syncUninterruptibly();
        logger.info("netty server startup");

    }
    void shutdown() {
        bind.channel().close().awaitUninterruptibly();
        bootstrap.group().shutdownGracefully();
        logger.info("netty server shutdown");
    }
}
