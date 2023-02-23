package ucas.csu.tsn.Yang.Stream;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ucas.csu.tsn.MonitorRequest;
import ucas.csu.tsn.MonitorResponse;

public class MonitorServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println("<TSN Client MonitorServer> Client: " + ctx.channel().remoteAddress()
                + ".");
        MonitorRequest monitorRequest = (MonitorRequest) msg;
        System.out.println("<TSN Client MonitorServer> Client Message: " + monitorRequest.getData()
                + ".");
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
}
