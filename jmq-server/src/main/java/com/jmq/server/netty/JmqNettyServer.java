package com.jmq.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by hackway on 16/11/1.
 */
public class JmqNettyServer implements NettyServer {


    public static final ChannelGroup ALL_CHANNELS = new DefaultChannelGroup("JMQ-CHANNELS", GlobalEventExecutor.INSTANCE);
    private ServerBootstrap serverBootstrap;
    private final NettyConfig nettyConfig;
    private ChannelInitializer<? extends  Channel> channelInitializer;

    public JmqNettyServer(ChannelInitializer<? extends Channel> channelInitializer) {
        this.nettyConfig = new NettyConfig();
        this.channelInitializer = channelInitializer;
    }

    public JmqNettyServer(NettyConfig nettyConfig, ChannelInitializer<? extends Channel> channelInitializer) {
        this.nettyConfig = nettyConfig;
        this.channelInitializer = channelInitializer;
    }

    @Override
    public void startServer() throws Exception {
        startServer(4096);
    }

    @Override
    public void startServer(int port) throws Exception {
        nettyConfig.setSocketAddress(new InetSocketAddress(port));
        startServer(nettyConfig.getSocketAddress());
    }

    @Override
    public void startServer(InetSocketAddress socketAddress) throws Exception {
        try {
            serverBootstrap = new ServerBootstrap();
            Map<ChannelOption<?>, Object>  channelOptions = nettyConfig.getChannelOptions();
            if (null != channelOptions) {
                for (ChannelOption k: channelOptions.keySet()) {
                    serverBootstrap.option(k, channelOptions.get(k));
                }
            }
            serverBootstrap.group(nettyConfig.getBossGroup(), nettyConfig.getWorkerGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer);
            Channel serverChannel = serverBootstrap.bind(nettyConfig.getSocketAddress()).sync().channel();
            ALL_CHANNELS.add(serverChannel);
        } catch (Exception e) {
            stopServer();
            throw(e);
        }
    }

    @Override
    public void stopServer() throws Exception {
        ChannelGroupFuture futures = ALL_CHANNELS.close();
        try {
            futures.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (null != nettyConfig.getBossGroup()) {
                nettyConfig.getBossGroup().shutdownGracefully();
            }
            if (null != nettyConfig.getWorkerGroup()) {
                nettyConfig.getWorkerGroup().shutdownGracefully();
            }
        }
    }

    @Override
    public ChannelInitializer<? extends Channel> getChannelInitializer() {
        return this.channelInitializer;
    }

    @Override
    public void setChannelInitializer(ChannelInitializer<? extends Channel> initializer) {
        this.channelInitializer = initializer;
    }

    @Override
    public NettyConfig getNettyConfig() {
        return this.nettyConfig;
    }
}
