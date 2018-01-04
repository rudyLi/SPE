package com.lifeng.network;

import com.lifeng.tuple.Tuple;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StreamProcessResultQueue {
    private final BlockingQueue<Tuple> result = new LinkedBlockingQueue<>();

    private ChannelHandlerContext ctx;
    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    private void notifyQueueNonEmpty() {
        ctx.executor().execute(new Runnable() {
            @Override
            public void run() {
                ctx.pipeline().fireUserEventTriggered(DataConsumeTask.getInstance());
            }
        });
    }

    public void addNewTask(Tuple tuple) throws InterruptedException {
        boolean isEmpty = isEmpty();
        result.put(tuple);
        if (isEmpty) {
            notifyQueueNonEmpty();
        }
    }
    public boolean isEmpty() {
        return result.isEmpty();
    }

    public Tuple next() throws InterruptedException {
        return result.poll(1l, TimeUnit.SECONDS);
    }
    public static class DataConsumeTask{
        private final static DataConsumeTask task = new DataConsumeTask();
        private DataConsumeTask() {
        }

        public static DataConsumeTask getInstance() {
            return task;
        }

    }

}
