package Yang.Stream;

import HttpInfo.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Builder;
import lombok.NonNull;

import static Hardware.Computer.host_name;

public class ListenerServer {
    private final int port;
    private Header header;
    private String url;

    @Builder
    public ListenerServer(@NonNull int port, @NonNull Header header, @NonNull String url) {
        this.port = port;
        this.header = header;
        this.url = url;
    }

    public void start() throws InterruptedException {
        join_listener();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new RpcDecoder(RpcRequest.class))
                                .addLast(new RpcEncoder(RpcResponse.class))
                                .addLast(new ListenerServerHandler());
                    }
                });
        ChannelFuture future = b.bind(port).sync();
        if (future.isSuccess()){
            System.out.println("Server Start Successful");
        }else {
            System.out.println("Server Start Failure");
            future.cause().printStackTrace();
            bossGroup.shutdownGracefully(); //关闭线程组
            workerGroup.shutdownGracefully();
            leave_listener();
        }
        future.channel().closeFuture().sync();
    }

    private int join_listener(){
        String url = this.url + header.getKey();
        System.out.println(url);
        PutInfo putInfo = PutInfo.builder().url(url).build();

        JSONObject joinStream = header.getJSONObject(true, false,
                true, false, false,
                true, true);
        joinStream.put("body", "join listener");
        JSONArray streams = new JSONArray();
        streams.add(joinStream);
        JSONObject device = new JSONObject();
        device.put("stream-list", streams);
        return putInfo.putInfo(device.toString());
    }

    private int leave_listener(){
        String url = this.url + header.getKey();
        System.out.println(url);
        DeleteInfo deleteInfo = DeleteInfo.builder().url(url).build();
        return deleteInfo.deleteInfo();
    }
}
