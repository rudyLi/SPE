package com.lifeng.network;

import com.lifeng.task.TaskDispatcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.lifeng.network.NettyMessage.StreamMessage;

public class StreamProcessServerHandler extends SimpleChannelInboundHandler<NettyMessage> {
    private static final Logger logger = LogManager.getLogger(StreamProcessServerHandler.class);
    private TaskDispatcher taskDispatcher;
    private Queue<NettyMessage> resultQueue = new LinkedBlockingQueue<>();
    private ChannelHandlerContext ctx;

    public StreamProcessServerHandler(TaskDispatcher taskDispatcher) {
        this.taskDispatcher = taskDispatcher;
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("trigger user event");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent tmp = (IdleStateEvent) evt;
            if (tmp.state().equals(IdleStateEvent.READER_IDLE_STATE_EVENT)) {
                ctx.channel().close();
                logger.info("heart beat timeout, so close the channel");
            }
        }
    }
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        if (this.ctx == null) {
            this.ctx = ctx;
        }

        super.channelRegistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NettyMessage nettyMessage) throws Exception {
        Class clazz =  nettyMessage.getClass();
        if (clazz == StreamMessage.class) {
            StreamMessage stream = (StreamMessage)nettyMessage;
            taskDispatcher.publish(stream.tuple, resultQueue);
        }

    }
}
