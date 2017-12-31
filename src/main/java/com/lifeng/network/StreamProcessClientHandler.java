package com.lifeng.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StreamProcessClientHandler extends SimpleChannelInboundHandler<NettyMessage> {

    private static final Logger logger = LogManager.getLogger(StreamProcessClientHandler.class);

    public void userEventTriggered(ChannelHandlerContext ctc, Object event) throws Exception {
        logger.info("trigger user event");
        // if no read event close the ctx
        if (event instanceof IdleStateEvent) {
            IdleStateEvent tmp = (IdleStateEvent) event;
            if (tmp.state().equals(IdleStateEvent.WRITER_IDLE_STATE_EVENT)) {
                ctc.write(NettyMessage.HeartBeatMessage.getInstance());
            }
            logger.info("send heart beat message");
        }
    }

    // check the
    public void channelWritabilityChanged(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NettyMessage nettyMessage) throws Exception {

    }
}
