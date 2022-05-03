package NettyAPI;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:35 PM
 */
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> target;

    public RpcEncoder(Class target){
        this.target = target;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Object msg, ByteBuf out) throws Exception {
        if (target.isInstance(msg)){
            byte[] data = JSON.toJSONBytes(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
