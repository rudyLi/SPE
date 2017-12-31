package com.lifeng.network;

public class NettyConnectionManager {
    private final NettyServer nettyServer;
    private final NettyClient nettyClient;
    private final NettyConfig nettyConfig;

    public NettyConnectionManager(NettyConfig config) {
        nettyClient = new NettyClient(config);
        nettyServer = new NettyServer(config);
        this.nettyConfig = config;
    }

    public void start() throws Exception {
        StreamProcessProtocol processProtocol = new StreamProcessProtocol(nettyConfig);
        nettyServer.init(processProtocol);
        nettyClient.init(processProtocol);
    }

    public void shutdown() {
        nettyClient.shutdown();
        nettyServer.shutdown();
    }
}
