package com.lifeng.network;

import com.lifeng.task.TaskDispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import static com.lifeng.network.NettyMessage.NettyMessageEncoder;
import static com.lifeng.network.NettyMessage.NettyMessageEncoder.createFrameLengthDecoder;
import static com.lifeng.network.NettyMessage.NettyMessageDecoder;

public class StreamProcessProtocol implements NettyProtocol {
    private NettyConfig nettyConfig;
    private TaskDispatcher taskDispatcher;
    private NettyMessageEncoder messageEncoder = new NettyMessageEncoder();
    private NettyMessageDecoder messageDecoder = new NettyMessageDecoder();
    StreamProcessProtocol(NettyConfig config, TaskDispatcher taskDispatcher) {
        this.nettyConfig  = config;
        this.taskDispatcher = taskDispatcher;
    }
    @Override
    public ChannelHandler[] getServerChannelHandlers() {
        return new ChannelHandler[]{
                messageEncoder,
                createFrameLengthDecoder(),
                messageDecoder,
                new StreamProcessServerHandler(taskDispatcher),
                new IdleStateHandler(2*nettyConfig.getHeartBeatSecond(),0,0, TimeUnit.SECONDS)
        };
    }

    @Override
    public ChannelHandler[] getClientChannelHandlers() {
        return new ChannelHandler[]{
                messageEncoder,
                createFrameLengthDecoder(),
                messageDecoder,
                new StreamProcessClientHandler(),
                new IdleStateHandler(0, nettyConfig.getHeartBeatSecond(),0,TimeUnit.SECONDS)
        };
    }
}
