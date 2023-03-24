package ucas.csu.tsn.Yang.Stream;

import lombok.Setter;
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
    @Setter
    private boolean isRegister;
    private TalkerClientHandler handler;
    private EventLoopGroup group;

    @Builder
    public TalkerClient(String host, @NonNull int port, Header header,
                        String url, @NonNull String body) throws Exception {
        this.port = port;
        this.host = host == null ? "localhost" : host;
        this.header = header;
        this.url = url;
        this.body = body;
        this.size = this.body.getBytes("gbk").length;
        if (this.header == null || this.url == null){
            this.isRegister = false;
        }else {
            this.isRegister = true;
        }

        group = new NioEventLoopGroup();
        handler = new TalkerClientHandler();
        handler.setFather(this);
        join_talker();
    }

    public void start() throws InterruptedException {
        long current = System.currentTimeMillis();
        setSendTime(current);
        JSONObject object = new JSONObject();
        object.put("timeTap", current);
        object.put("body", body);
        handler.setBody(object.toJSONString());

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("<TSN Client talkerClient> Connecting to...");
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcRequest.class));
                        pipeline.addLast(new RpcDecoder(RpcResponse.class));
                        pipeline.addLast(handler);
                    }
                });
        final ChannelFuture future = b.connect(host, port).sync();

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    System.out.println("<TSN Client talkerClient> Connected Successful");
                    resultCode = 200;
                }else {
                    System.out.println("<TSN Client talkerClient> Connected Failure");
                    resultCode = 400;
                }
//                stopFuture();
            }

            private void stopFuture(){
                future.channel().close();
                group.shutdownGracefully();
                leave_talker();
            }
        });

        this.channel = future.channel();
//        System.out.println(body);
    }

    private int join_talker(){
        if (!isRegister){
            return 200;
        }
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
        System.out.println("<TSN Client talkerClient> register talker to controller.");
        return restfulPutInfo.putInfo(device.toString());
    }

    public void stop(){
        group.shutdownGracefully();
        leave_talker();
    }

    public int leave_talker(){
        if (!isRegister){
            return 200;
        }
        String url = this.url + header.getKey();
//        System.out.println(url);
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder()
                .url(url)
                .build();
        System.out.println("<TSN Client talkerClient> remove talker from controller.");
        return restfulDeleteInfo.deleteInfo();
    }

    public String getKey(){
        return this.header.getKey();
    }

    public long getSendTime(){
        return handler.getSendTime();
    }

    public void setSendTime(long sendTime){
        handler.setSendTime(sendTime);
    }

    public long getAcceptTime(){
        return handler.getAcceptTime();
    }

    public void setAcceptTime(long acceptTime){
        handler.setAcceptTime(acceptTime);
    }
}
