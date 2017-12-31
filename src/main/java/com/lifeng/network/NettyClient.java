package com.lifeng.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class NettyClient {
    private static final Logger logger = LogManager.getLogger(NettyClient.class);

    private NettyConfig nettyConfig;
    private NettyProtocol protocol;
    private Bootstrap bootstrap;

    NettyClient(NettyConfig nettyConfig){
        this.nettyConfig = nettyConfig;
    }
    void init(final NettyProtocol protocol) throws Exception {

        // Configure the client.
        this.protocol = protocol;
        bootstrap = new Bootstrap();
        if (nettyConfig.getTransportType().equals(NettyConfig.TransportType.NIO)) {
            EventLoopGroup group = new NioEventLoopGroup(nettyConfig.getClientThreadNum());
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, nettyConfig.isTCPNoDelay())
                    .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(nettyConfig.getWriteWaterHighMark(), 2 * nettyConfig.getWriteWaterHighMark()));
        }

    }

    ChannelFuture connect(InetSocketAddress inetAddress) {
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(protocol.getClientChannelHandlers());
            }
        });
        return bootstrap.connect(inetAddress);
    }

    void shutdown() {
        bootstrap.group().shutdownGracefully();
    }
}
