package com.lifeng.network;

import com.lifeng.task.TaskDispatcher;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.lifeng.network.NettyMessage.StreamMessage;

public class StreamProcessServerHandler extends SimpleChannelInboundHandler<NettyMessage> {

    private static final Logger logger = LogManager.getLogger(StreamProcessServerHandler.class);

    private TaskDispatcher taskDispatcher;
    private StreamProcessResultQueue resultQueue = new StreamProcessResultQueue();
    private final ChannelFutureListener writeListener = new WriteAndFlushNextMessageIfPossibleListener();
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
        else if (evt instanceof StreamProcessResultQueue.DataConsumeTask) {
            writeAndFlushNextMessageIfPossible(ctx.channel());
        }
    }
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        if (this.ctx == null) {
            this.ctx = ctx;
            resultQueue.setCtx(ctx);
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

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        writeAndFlushNextMessageIfPossible(ctx.channel());
    }

    private void writeAndFlushNextMessageIfPossible(final Channel channel)  {
        try {
            if (!resultQueue.isEmpty()) {
                if (channel.isWritable()) {
                    channel.writeAndFlush(resultQueue.next()).addListener(writeListener);
                }
            }
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // This listener is called after an element of the current nonEmptyReader has been
    // flushed. If successful, the listener triggers further processing of the
    // queues.
    private class WriteAndFlushNextMessageIfPossibleListener implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            try {
                if (future.isSuccess()) {
                    writeAndFlushNextMessageIfPossible(future.channel());
                }
            } catch (Throwable t) {
               t.printStackTrace();
            }
        }
    }
}
