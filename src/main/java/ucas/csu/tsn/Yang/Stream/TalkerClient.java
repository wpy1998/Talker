package ucas.csu.tsn.Yang.Stream;

import ucas.csu.tsn.NettyAPI.RpcDecoder;
import ucas.csu.tsn.NettyAPI.RpcEncoder;
import ucas.csu.tsn.NettyAPI.RpcRequest;
import ucas.csu.tsn.NettyAPI.RpcResponse;
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
import ucas.csu.tsn.RestfulAPI.RestfulDeleteInfo;
import ucas.csu.tsn.RestfulAPI.RestfulPutInfo;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:18 PM
 */
public class TalkerClient {
    private final String host, unit = "Byte";
    private final int port;
    @Getter
    private Channel channel;
    @Getter
    private int resultCode;
    private Header header;
    private String url, body;
    private long size;

    @Builder
    public TalkerClient(String host, @NonNull int port, @NonNull Header header,
                        @NonNull String url, @NonNull String body) throws Exception {
        this.port = port;
        this.host = host == null ? "localhost" : host;
        this.header = header;
        this.url = url;
        this.body = body;
        this.size = this.body.getBytes("gbk").length;

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
                        pipeline.addLast(new TalkerClientHandler(body));
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
//        System.out.println(body);
    }

    private int join_talker(){
        String url = this.url + header.getKey();
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
        System.out.println("<TSN Client> register talker to controller <TSN Client>");
        return restfulPutInfo.putInfo(device.toString());
    }

    public int leave_talker(){
        String url = this.url + header.getKey();
//        System.out.println(url);
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder()
                .url(url)
                .build();
        System.out.println("<TSN Client> remove talker from controller <TSN Client>");
        return restfulDeleteInfo.deleteInfo();
    }

    public String getKey(){
        return this.header.getKey();
    }
}
