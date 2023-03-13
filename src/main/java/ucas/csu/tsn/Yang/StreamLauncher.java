package ucas.csu.tsn.Yang;

import ucas.csu.tsn.Yang.Stream.MonitorServer;
import ucas.csu.tsn.Yang.Stream.TalkerClient;
import ucas.csu.tsn.Yang.Topology.NetworkCard;
import ucas.csu.tsn.Hardware.Computer;
import ucas.csu.tsn.Yang.Stream.ListenerServer;
import ucas.csu.tsn.Yang.Stream.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StreamLauncher {
    private String talkerFront, listenerFront, hostName;
    private static ListenerServer server = null;
    private static Thread pollingThread = null;
    private MonitorServer monitorServer;

    public StreamLauncher(Computer computer){
        this.talkerFront = computer.urls.get("tsn-talker");
        this.listenerFront = computer.urls.get("tsn-listener");
        this.hostName = computer.host_name;
        monitorServer = new MonitorServer();
    }

    public void stopStreamLauncher(){
        stopPollingThread();
        stopListenerServer();
        monitorServer.stop();
    }

    public void startPollingThread(){
        if (monitorServer == null){
            monitorServer = new MonitorServer();
        }
        monitorServer.start();
        if (pollingThread != null){
            System.out.println("<TSN Client StreamLauncher> Polling Thread has started.");
            return;
        }
        pollingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int timeInterval = 15 * 1000;
                try {
                    while (true){
                        Map<String, TalkerClient> talkerClients = monitorServer.getTalkerClients();
                        if (talkerClients.size() != 0){
                            System.out.println("<TSN Client StreamLauncher> Unallocated stream " +
                                    "shown as follows: ");
                        }
                        for (String key: talkerClients.keySet()){
                            System.out.println(key);
                        }
                        Thread.sleep(timeInterval);
                    }
                }catch (InterruptedException e){
                    System.out.println("<TSN Client StreamLauncher> PollingThread interrupted.");
                }
            }
        });
        pollingThread.start();
    }

    public void stopPollingThread(){
        if (pollingThread != null && pollingThread.isAlive()){
            pollingThread.interrupt();
        }
        if (monitorServer != null){
            monitorServer.stop();
        }
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
        front = (uniqueId % 10000) / 100;
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

    public void registerTalkerStream(String body, NetworkCard networkCard,
                                     String destIp) throws Exception {
        List<String> destIps = new ArrayList<>();
        destIps.add(destIp);
        List<String> destMacs = new ArrayList<>();
        destMacs.add("00-0c-29-6b-bf-4e");
        int uniqueId = allocateUniqueId();
        Header header = Header.builder().uniqueId(convertUniqueID(uniqueId))
                .rank((short) 0)
                .networkCard(networkCard)
                .isHaveIpv4(true)
                .dest_ip(destIps)
                .dest_mac(destMacs)
                .build();

        TalkerClient client = TalkerClient.builder()
                .host(destIp)
                .port(17835)
                .header(header)
                .url(this.talkerFront + networkCard.getMac().replace(":", "-")
                        + "/stream-list/")
                .body(body)
                .build();
        monitorServer.insertTalkerClient(client);
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
    public void startListenerServer(NetworkCard networkCard){
        int uniqueId = allocateUniqueId();
        Header header = Header.builder().uniqueId(convertUniqueID(uniqueId))
                .rank((short) 0)
                .networkCard(networkCard)
                .build();
        server = ListenerServer.builder().port(17835).header(header)
                .url(this.listenerFront + networkCard.getMac().replace(":", "-")
                        + "/stream-list/").build();
        server.start();
    }

    public void stopListenerServer(){
        if (server != null){
            server.stop();
        }
    }

    //status 以下参数,函数仅在操作status config库时使用
}
