package com.lifeng.network;

import com.lifeng.task.TaskDispatcher;

public class NettyConnectionManager {
    private final NettyServer nettyServer;
    private final NettyClient nettyClient;
    private final NettyConfig nettyConfig;
    private final TaskDispatcher taskDispatcher;

    public NettyConnectionManager(NettyConfig config, TaskDispatcher taskDispatcher) {
        nettyClient = new NettyClient(config);
        nettyServer = new NettyServer(config);
        this.nettyConfig = config;
        this.taskDispatcher = taskDispatcher;
    }

    public void start() throws Exception {
        StreamProcessProtocol processProtocol = new StreamProcessProtocol(nettyConfig, taskDispatcher);
        nettyServer.init(processProtocol);
        nettyClient.init(processProtocol);
    }

    public void shutdown() {
        nettyClient.shutdown();
        nettyServer.shutdown();
    }
}
