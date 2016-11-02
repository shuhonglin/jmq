package com.jmq.server.netty;

import com.jmq.server.Server;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * Created by hackway on 16/11/1.
 */
public interface NettyServer extends Server {

    public ChannelInitializer<? extends Channel> getChannelInitializer();

    public void setChannelInitializer(ChannelInitializer<? extends Channel> initializer);

    public NettyConfig getNettyConfig();

}
