package Client;

import CSCoder.RpcRequest;
import io.netty.channel.Channel;

import java.util.UUID;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:52 PM
 */
public class ClientLauncher {
    public static void main(String[] args) throws Exception {
        TalkerClient client = new TalkerClient("localhost", 17835);
        client.start();

        Channel channel = client.getChannel();
        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setData("client message");
        channel.writeAndFlush(request);
    }
}
