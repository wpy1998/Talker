package Server;

import HttpInfo.RpcDecoder;
import HttpInfo.RpcEncoder;
import HttpInfo.RpcRequest;
import HttpInfo.RpcResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ListenerServer {
    private final int port;

    public ListenerServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final ListenerServerHandler serverHandler = new ListenerServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(serverHandler);
                        }
                    });
            ChannelFuture future = b.bind().sync();
            if (future.isSuccess()){
                System.out.println("Server Start Successful");
            }else {
                System.out.println("Server Start Failure");
            }
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
