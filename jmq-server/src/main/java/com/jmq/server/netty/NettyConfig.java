package com.jmq.server.netty;

import com.jmq.server.concurrent.NamedThreadFactory;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by hackway on 16/11/1.
 */
public class NettyConfig {

    private Map<ChannelOption<?>, Object> channelOptions;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private int bossThreadCount = 1;
    private int workerThreadCount = Runtime.getRuntime().availableProcessors()*2;
    private InetSocketAddress socketAddress ;

    public NettyConfig() {
        this.bossGroup = new NioEventLoopGroup(bossThreadCount, new NamedThreadFactory("JMQ-BOSS-THREAD"));
        this.workerGroup = new NioEventLoopGroup(workerThreadCount, new NamedThreadFactory("JMQ-WORKER-THREAD"));
    }

    public NettyConfig(int port) {
        this();
        this.socketAddress = new InetSocketAddress(4096);
    }

    public NettyConfig(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public NettyConfig(int bossThreadCount, int workerThreadCount) {
        this();
        this.bossThreadCount = bossThreadCount;
        this.workerThreadCount = workerThreadCount;
    }

    public NettyConfig(int bossThreadCount, int workerThreadCount, int port) {
        this(bossThreadCount, workerThreadCount);
        this.socketAddress = new InetSocketAddress(port);
    }

    public NettyConfig(int bossThreadCount, int workerThreadCount, InetSocketAddress socketAddress) {
        this(bossThreadCount, workerThreadCount);
        this.socketAddress = socketAddress;
    }

    public Map<ChannelOption<?>, Object> getChannelOptions() {
        return channelOptions;
    }

    public void setChannelOptions(Map<ChannelOption<?>, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

    public synchronized NioEventLoopGroup getBossGroup() {
        if (null == this.bossGroup) {
            if (0 >= this.bossThreadCount) {
                this.bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory("JMQ-BOSS-THREAD"));
            } else {
                this.bossGroup = new NioEventLoopGroup(this.bossThreadCount, new NamedThreadFactory("JMQ-BOSS-THREAD"));
            }
        }
        return bossGroup;
    }

    public synchronized NioEventLoopGroup getWorkerGroup() {
        if (null == this.workerGroup) {
            if (0 >= this.workerThreadCount) {
                this.workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2, new NamedThreadFactory("JMQ-WORKER-THREAD"));
            } else {
                this.workerGroup = new NioEventLoopGroup(this.workerThreadCount, new NamedThreadFactory("JMQ-WORKER-THREAD"));
            }
        }
        return workerGroup;
    }

    public int getBossThreadCount() {
        return bossThreadCount;
    }

    public int getWorkerThreadCount() {
        return workerThreadCount;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setBossThreadCount(int bossThreadCount) {
        this.bossThreadCount = bossThreadCount;
    }

    public void setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }
}
