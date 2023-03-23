package ucas.csu.tsn.Yang.Stream;

import lombok.Getter;
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
    @Setter@Getter
    private long sendTime, acceptTime;
    @Setter
    private TalkerClient father;

    public TalkerClientHandler(){
        this.body = "Talker Client";
    }

    public TalkerClientHandler(String body){
        this.body = body;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse){
        acceptTime = System.currentTimeMillis();
        System.out.println("<TSN Client talkerClient> costTime = " + (acceptTime - sendTime) +
                ", Get Server response data: " + rpcResponse.getData());
        ctx.channel().close();
        father.stop();
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
