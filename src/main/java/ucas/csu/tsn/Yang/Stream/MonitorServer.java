package ucas.csu.tsn.Yang.Stream;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import ucas.csu.tsn.MonitorDecoder;
import ucas.csu.tsn.MonitorEncoder;
import ucas.csu.tsn.MonitorRequest;
import ucas.csu.tsn.MonitorResponse;

import java.util.ArrayList;
import java.util.List;

public class MonitorServer {
    protected int port = 9802;
    protected Thread serverThread;
    @Getter
    private List<TalkerClient> talkerClients;

    public MonitorServer(){
        talkerClients = new ArrayList<>();
    }

    private void initServer(){
        serverThread = new Thread(new Runnable() {
            EventLoopGroup bossGroup, workerGroup;
            ChannelFuture future;
            @Override
            public void run() {
                try {
                    bossGroup = new NioEventLoopGroup();
                    workerGroup = new NioEventLoopGroup();
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel){
                                    socketChannel.pipeline()
                                            .addLast(new MonitorDecoder(MonitorRequest.class))
                                            .addLast(new MonitorEncoder(MonitorResponse.class))
                                            .addLast(new MonitorServerHandler());
                                }
                            });
                    future = b.bind(port).sync();
                    if (future.isSuccess()){
                        System.out.println("<TSN Client MonitorServer> MonitorServer Start Successful.");
                    }else {
                        System.out.println("<TSN Client MonitorServer> MonitorServer Start Failure.");
                        stopServer();
                    }
                    future.channel().closeFuture().sync();
                }catch (InterruptedException e){
                    stopServer();
                    System.out.println("<TSN Client MonitorServer> Server Stop.");
                }
            }

            public void stopServer(){
                if (bossGroup != null)
                    bossGroup.shutdownGracefully();
                if (workerGroup != null)
                    workerGroup.shutdownGracefully();
            }
        });
    }

    public void start(){
        if (serverThread == null){
            initServer();
        }
        System.out.println("<TSN Client MonitorServer> Server has started.");
        serverThread.start();
    }

    public void stop(){
        if (serverThread != null){
            serverThread.interrupt();
        }
        talkerClients.clear();
        System.out.println("<TSN Client MonitorServer> Server has stopped.");
    }

    synchronized public void insertTalkerClient(TalkerClient client){
        System.out.println("<TSN Client MonitorServer> Server insert TalkerClient " +
                client.getKey() + ".");
        talkerClients.add(client);
    }

    synchronized public void removeTalkerClient(String key){
        for (TalkerClient client: talkerClients){
            if (key.equals(client.getKey())){
                System.out.println("<TSN Client MonitorServer> Server remove TalkerClient " +
                        client.getKey() + ".");
                talkerClients.remove(client);
                return;
            }
        }
    }
}
