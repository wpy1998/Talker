package Client;

import HttpInfo.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:30 PM
 */
public class TalkerClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcResponse rpcResponse) throws Exception {
        System.out.println("Get Server response data: " + rpcResponse.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        ctx.close();
    }
}
