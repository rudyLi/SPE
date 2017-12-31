package com.lifeng.network;

import com.google.common.base.Preconditions;
import com.lifeng.common.ConfigOption;
import com.lifeng.common.Configuration;

import java.net.InetAddress;

public class NettyConfig {

    public static final ConfigOption<Boolean> TCP_NO_DELAY = new ConfigOption("netty.tcp.nodelay", true);

    public static final ConfigOption<Integer> NUM_THREADS_SERVER_WORKER = new ConfigOption<Integer>("netty.server.worker.num.threads", Runtime.getRuntime().availableProcessors());

    public static final ConfigOption<Integer> NUM_THREADS_SERVER_BOSS = new ConfigOption<Integer>("netty.server.boss.num.threads", 1);

    public static final ConfigOption<Integer> NUM_THREADS_CLIENT = new ConfigOption<Integer>("netty.client.num.threads", 2*Runtime.getRuntime().availableProcessors());

    public static final ConfigOption<Integer> CONNECT_TIMEOUT_SECONDS = new ConfigOption<Integer>("netty.connect.timeout.sec", 120);

    public static final ConfigOption<Integer> CONNECT_BACKLOG = new ConfigOption<Integer>("netty.server.backlog", 0);

    public static final ConfigOption<Integer> WRITE_BUFFER_WATER_MARK = new ConfigOption<Integer>("netty.write.buffer.watermark", 100);

    public static final ConfigOption<Integer> HEART_BEAT_SECONDS = new ConfigOption<Integer>("netty.heartbeat.seconds", 10);

    private InetAddress address;
    private int serverPort;
    private Configuration config;

    public NettyConfig(InetAddress address, int serverPort, Configuration config) {
        Preconditions.checkArgument(serverPort >= 0 && serverPort <= 65536, "invalid port");
        this.address = address;
        this.serverPort= serverPort;
        this.config = config;
    }

    enum TransportType {
        NIO
    }

    public TransportType getTransportType() {
        return TransportType.NIO;
    }

    public int getServerPort() {
        return serverPort;
    }

    public InetAddress getAddress() {
        return address;
    }

    public boolean isTCPNoDelay() {
        return config.getBoolean(TCP_NO_DELAY);
    }

    public int getServerWorkerThreadNum() {
        return config.getInt(NUM_THREADS_SERVER_WORKER);
    }

    public int getServerBossThreadNum() {
        return config.getInt(NUM_THREADS_SERVER_BOSS);
    }

    public int getClientThreadNum() {
        return config.getInt(NUM_THREADS_CLIENT);
    }

    public int getConnectTimeOutInSec() {
        return config.getInt(CONNECT_TIMEOUT_SECONDS);
    }

    public int getConnectBackLog() {
        return config.getInt(CONNECT_BACKLOG);
    }

    public int getWriteWaterHighMark() {
        return config.getInt(WRITE_BUFFER_WATER_MARK);
    }

    public int getHeartBeatSecond() {
        return config.getInt(HEART_BEAT_SECONDS);
    }
}
