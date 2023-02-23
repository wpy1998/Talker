package ucas.csu.tsn.Yang.Stream;

import com.alibaba.fastjson.JSONObject;
import ucas.csu.tsn.NettyAPI.RpcRequest;
import ucas.csu.tsn.NettyAPI.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ListenerServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println("<TSN Client listenerServer> Client: " + ctx.channel().remoteAddress());
        RpcRequest rpcRequest = (RpcRequest) msg;
        JSONObject object = JSONObject.parseObject((String) rpcRequest.getData());
        long timeTap = object.getLong("timeTap"), current = System.currentTimeMillis();
        System.out.println("<TSN Client listenerServer> Client Message: " +
                object.getString("body") + ", costTime = " + (current - timeTap));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("<TSN Client listenerServer> Server Get Message finished...");
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setData("<TSN Client listenerServer> Hello，Client....^_^");
        ctx.channel().writeAndFlush(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
