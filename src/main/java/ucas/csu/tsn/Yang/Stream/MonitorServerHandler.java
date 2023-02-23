package ucas.csu.tsn.Yang.Stream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import ucas.csu.tsn.MonitorRequest;
import ucas.csu.tsn.MonitorResponse;

import java.util.HashMap;
import java.util.Map;

public class MonitorServerHandler extends ChannelInboundHandlerAdapter {
    @Getter
    private Map<String, TalkerClient> talkerClients;

    public MonitorServerHandler(){
        talkerClients = new HashMap<>();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        System.out.println("<TSN Client MonitorServer> Client: " + ctx.channel().remoteAddress()
                + ".");
        MonitorRequest monitorRequest = (MonitorRequest) msg;
        String data = (String) monitorRequest.getData(), body;
        JSONObject object = JSON.parseObject(data);
        String type = object.getString("type");
        if (type.equals("tsn")){
            body = object.getString("key");
            navigate(body);
        }else {
            body = data;
            System.out.println("<TSN Client MonitorServer> Client Message: " + body + ".");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("<TSN Client MonitorServer> Server Get Message finished...");
        MonitorResponse monitorResponse = new MonitorResponse();
        monitorResponse.setData("<TSN Client MonitorServer> Hello，Client....^_^");
        ctx.channel().writeAndFlush(monitorResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    private void navigate(String key) throws InterruptedException {
        TalkerClient talkerClient = talkerClients.get(key);
        if (talkerClient == null){
            return;
        }
        talkerClient.start();
        talkerClients.remove(key);
    }

    synchronized public void insertTalkerClient(TalkerClient client){
        System.out.println("<TSN Client MonitorServer> Server insert TalkerClient " +
                client.getKey() + ".");
        talkerClients.put(client.getKey(), client);
    }

    synchronized public void removeTalkerClient(String key){
        if (talkerClients.get(key) != null){
            System.out.println("<TSN Client MonitorServer> Server remove TalkerClient " +
                    key + ".");
            talkerClients.remove(key);
        }
    }
}
