package ucas.csu.tsn.Yang.Stream;

import ucas.csu.tsn.NettyAPI.RpcRequest;
import ucas.csu.tsn.NettyAPI.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ListenerServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
//        RpcRequest request = (RpcRequest) msg;
//        System.out.println("Server GET: " + request.toString());
//        RpcResponse response = new RpcResponse();
//        response.setId(UUID.randomUUID().toString());
//        response.setData("Server message");
//        response.setStatus(1);
//        ctx.writeAndFlush(response);

        System.out.println("Client: " + ctx.channel().remoteAddress());
        RpcRequest rpcRequest = (RpcRequest) msg;
        System.out.println("Client Message: " + rpcRequest.getData());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("Server Get Message finished..");
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setData("Hello，Client....^_^");
        ctx.channel().writeAndFlush(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
