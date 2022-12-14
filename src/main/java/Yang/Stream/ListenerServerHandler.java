package Yang.Stream;

import NettyAPI.RpcRequest;
import NettyAPI.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.UUID;

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
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("Client Message: " + buf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("Server Get Message finished..");
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，Client....^_^", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
