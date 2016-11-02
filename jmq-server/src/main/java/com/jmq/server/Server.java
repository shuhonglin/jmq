package com.jmq.server;

import java.net.InetSocketAddress;

/**
 * Created by hackway on 16/11/1.
 */
public interface Server {

    void startServer() throws Exception;

    void startServer(int port) throws Exception;

    void startServer(InetSocketAddress socketAddress) throws Exception;

    void stopServer() throws Exception;

}
