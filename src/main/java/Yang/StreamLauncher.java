package Yang;

import Yang.Stream.ListenerServer;
import Yang.Stream.TalkerClient;
import Yang.Stream.Header;
import lombok.Builder;
import lombok.NonNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StreamLauncher {
    private String talkerFront, listenerFront, hostName;
    private static List<TalkerClient> clients = new ArrayList<>();
    private static ListenerServer server = null;

    private static Thread pollingThread = null;

    @Builder
    public StreamLauncher(@NonNull String talkerFront, @NonNull String listenerFront,
                          @NonNull String hostName){
        this.talkerFront = talkerFront;
        this.listenerFront = listenerFront;
        this.hostName = hostName;
    }

    public void stopStreamLauncher(){
        stopPollingThread();
        stopListenerServer();
    }

    public void startPollingThread(){
        if (pollingThread != null){
            System.out.println("Polling Thread has started");
            return;
        }
        pollingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int timeInterval = 15 * 1000;
                try {
                    while (true){
                        for (TalkerClient talkerClient: clients){
                            System.out.println(talkerClient.getKey());
                        }
                        Thread.sleep(timeInterval);
                    }
                }catch (InterruptedException e){
                    System.out.println("--Thread: PollingThread interrupted--");
                }
            }
        });
        pollingThread.start();
    }

    public void stopPollingThread(){
        if (pollingThread != null && pollingThread.isAlive()){
            pollingThread.interrupt();
        }
        for (TalkerClient talkerClient: clients){
            talkerClient.leave_talker();
        }
        clients.clear();
    }

    /**
     * create by: wpy
     * description: Common
     * create time: 3/15/22 8:28 PM
     *
     * @Param: null
     * @return
     */
    private static int unique_id = 0;

    synchronized private int allocateUniqueId(){
        int current = unique_id;
        unique_id++;
        return current;
    }

    /**
     * create by: wpy
     * description: talker: header, body 以下参数,函数仅在操作talker config库时使用
     * create time: 3/10/22 6:08 PM
     *
     * @Param: null
     * @return
     */

    private String convertUniqueID(int uniqueId){
        int front, next;
        next = uniqueId % 100;
        front = uniqueId % 10000 - next;
        String s1, s2;
        s1 = String.valueOf(front);
        s2 = String.valueOf(next);
        if (s1.length() == 1){
            s1 = "0" + s1;
        }
        if (s2.length() == 1){
            s2 = "0" + s2;
        }
        return s1 + "-" + s2;
    }

    public void registerTalkerStream(String body) throws UnsupportedEncodingException {
        int uniqueId = allocateUniqueId();
        Header header = Header.builder().uniqueId(convertUniqueID(uniqueId))
                .rank((short) 0)
                .build();
        String unit = "Byte";
        long size = body.getBytes("gbk").length;

        TalkerClient client = TalkerClient.builder()
                .host("localhost")
                .port(17835)
                .header(header)
                .url(this.talkerFront + this.hostName + "/stream-list/")
                .size(size)
                .unit(unit)
                .build();
        clients.add(client);
    }

    /**
     * create by: wpy
     * description: Listener
     * create time: 3/15/22 8:28 PM
     *
     * @Param: null
     * @return
     */
    //listener 以下参数, 函数仅在操作listener config库时使用
    public void startListenerServer(){
        int uniqueId = allocateUniqueId();
        Header header = Header.builder().uniqueId(convertUniqueID(uniqueId))
                .rank((short) 0)
                .build();
        server = ListenerServer.builder().port(17835).header(header)
                .url(this.listenerFront + hostName + "/stream-list/").build();
        server.start();
    }

    public void stopListenerServer(){
        if (server != null){
            server.stop();
        }
    }

    //status 以下参数,函数仅在操作status config库时使用
}
