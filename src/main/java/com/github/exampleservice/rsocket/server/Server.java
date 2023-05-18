package com.github.exampleservice.rsocket.server;

import com.github.exampleservice.rsocket.service.SocketAcceptorImpl;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;

public class Server {

    public static void main(String[] args) {
        final RSocketServer socketServer = RSocketServer.create(new SocketAcceptorImpl());
        final CloseableChannel closeableChannel = socketServer.bindNow(TcpServerTransport.create(6565));

        closeableChannel.onClose().block();
    }
}
