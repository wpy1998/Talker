package ucas.csu.tsn.Yang.Stream;

import ucas.csu.tsn.NettyAPI.RpcRequest;
import ucas.csu.tsn.NettyAPI.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Setter;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:30 PM
 */
public class TalkerClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Setter
    private String body;

    public TalkerClientHandler(){
        this.body = "Talker Client";
    }

    public TalkerClientHandler(String body){
        this.body = body;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcResponse rpcResponse) throws Exception {
        System.out.println("<TSN Client talkerClient> Get Server response data: " +
                rpcResponse.getData());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("<TSN Client talkerClient> Send Message: " + body);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setData(body);
        ctx.channel().writeAndFlush(rpcRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        ctx.close();
    }
}
