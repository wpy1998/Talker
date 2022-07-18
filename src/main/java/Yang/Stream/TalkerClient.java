package Yang.Stream;

import NettyAPI.RpcDecoder;
import NettyAPI.RpcEncoder;
import NettyAPI.RpcRequest;
import NettyAPI.RpcResponse;
import RestfulAPI.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

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
    @Getter
    private int resultCode;
    private Header header;
    private String url, unit;
    private long size;

    @Builder
    public TalkerClient(String host, @NonNull int port, @NonNull Header header,
                        @NonNull String url, String unit, Long size){
        this.port = port;
        this.host = host == null ? "localhost" : host;
        this.header = header;
        this.url = url;
        this.unit = unit == null ? "Byte" : unit;
        this.size = size == null ? 0 : size;

        join_talker();
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
                        pipeline.addLast(new RpcDecoder(RpcResponse.class));
                        pipeline.addLast(new TalkerClientHandler());
                    }
                });
        final ChannelFuture future = b.connect(host, port).sync();

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    System.out.println("Connected Successful");
                    resultCode = 200;
                }else {
                    System.out.println("Connected Failure");
                    resultCode = 400;
                }
                future.channel().close();
                group.shutdownGracefully();
                leave_talker();
            }
        });

        this.channel = future.channel();
    }

    private int join_talker(){
        String url = this.url + header.getKey();
//        System.out.println(url);
        RestfulPutInfo restfulPutInfo = RestfulPutInfo.builder()
                .url(url)
//                .isDebug(true)
                .build();

        JSONObject joinStream = header.getJSONObject(true, true,
                true, true, true,
                true, true);
        joinStream.put("packet-size", this.size);
        joinStream.put("packet-unit", this.unit);
        joinStream.put("create-time", System.currentTimeMillis());
        JSONArray streams = new JSONArray();
        streams.add(joinStream);
        JSONObject device = new JSONObject();
        device.put("stream-list", streams);
        System.out.println("--register listener to controller--");
        return restfulPutInfo.putInfo(device.toString());
    }

    public int leave_talker(){
        String url = this.url + header.getKey();
//        System.out.println(url);
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder()
                .url(url)
                .build();
        System.out.println("--remove listener to controller--");
        return restfulDeleteInfo.deleteInfo();
    }

    public String getKey(){
        return this.header.getKey();
    }
}
