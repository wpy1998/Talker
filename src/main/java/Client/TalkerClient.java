package Client;

import HttpInfo.RpcEncoder;
import HttpInfo.RpcRequest;
import HttpInfo.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:18 PM
 */
public class TalkerClient {
    private final String host;
    private final int port;
    @Getter
    private Channel channel;
    public TalkerClient(String host, int port){
        this.port = port;
        this.host = host;
    }

    public void start() throws Exception{
        final EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("Connecting to...");
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcRequest.class));
                        pipeline.addLast(new RpcEncoder(RpcResponse.class));
                        pipeline.addLast(new TalkerClientHandler());
                    }
                });
        final ChannelFuture future = b.connect(host, port).sync();

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    System.out.println("Connected Successful");
                }else {
                    System.out.println("Connected Failed");
                }
            }
        });

        this.channel = future.channel();
    }
}
