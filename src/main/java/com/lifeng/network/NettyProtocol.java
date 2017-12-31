package com.lifeng.network;

import io.netty.channel.ChannelHandler;

public interface NettyProtocol {
    ChannelHandler[] getServerChannelHandlers();
    ChannelHandler[] getClientChannelHandlers();
}
