package Yang;

import Yang.Stream.ListenerServer;
import Yang.Stream.TalkerClient;
import HttpInfo.DeleteInfo;
import HttpInfo.PutInfo;
import Yang.NetworkTopology.LLDP;
import Yang.NetworkTopology.Link;
import Yang.NetworkTopology.Topology;
import Yang.Stream.Header;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

import static Hardware.Computer.host_name;
import static Hardware.Computer.timerThread;

public class ControllerConnect {
    public static final String cuc_ip = "10.2.25.4";
    public static final String topology_id = "tsn-network";

    Map<String, String> urls;
    private LLDP lldp;

    @Builder
    public ControllerConnect(@NonNull LLDP lldp){
        urls = new HashMap<>();
        urls.put("tsn-talker", "http://" + cuc_ip +
                ":8181/restconf/config/tsn-talker-type:stream-talker-config/devices/");
        urls.put("tsn-topology", "http://" + cuc_ip +
                ":8181/restconf/config/network-topology:network-topology/");
        urls.put("tsn-listener", "http://" + cuc_ip +
                ":8181/restconf/config/tsn-listener-type:stream-listener-config/devices/");
        this.lldp = lldp;

        startTimerThread();
    }

    private void startTimerThread(){
        if (timerThread != null){
            System.out.println("timer has started");
            return;
        }

        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int timeInterval = 15 * 60 * 1000;
                    while (true){
                        registerDevice(lldp);
                        Thread.sleep(timeInterval);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        timerThread.start();
    }

    /**
     * create by: wpy
     * description: topology connect 以下参数,函数仅在操作network topology config库时使用
     * create time: 3/10/22 6:09 PM
     *
      * @Param: null
     * @return
     */
    public void registerDevice(LLDP lldp){
        String url = this.urls.get("tsn-topology") + "topology/" + topology_id;
        Topology topology = Topology.builder().topology_id(topology_id).lldp(lldp).build();

        JSONArray array = new JSONArray();
        array.add(topology.getJSONObject());
        JSONObject topologies = new JSONObject();
        topologies.put("topology", array);

        PutInfo putInfo = PutInfo.builder().url(url).build();
        putInfo.putInfo(topologies.toString());
    }

    public void removeDevice(){
        String url = this.urls.get("tsn-topology") + "topology/" + topology_id + "/node/" + host_name;
        DeleteInfo deleteInfo = DeleteInfo.builder().url(url).build();
        deleteInfo.deleteInfo();
        for (int i = 0; i < lldp.linkList.size(); i++){
            Link link = lldp.linkList.get(i);
            removeLink(link);
        }
    }

    public void removeLink(Link link){
        String url = this.urls.get("tsn-topology") + "topology/" + topology_id + "/link/"
                + link.getLink_id();
        System.out.println(url);
        DeleteInfo deleteInfo = DeleteInfo.builder()
                .url(url).build();
        deleteInfo.deleteInfo();
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

    synchronized private int getUniqueId(){
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
        next = unique_id % 100;
        front = unique_id % 10000 - next;
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

    public void registerTalkerStream(String body) throws Exception {
        int uniqueId = getUniqueId();
        Header header = Header.builder().uniqueId(convertUniqueID(uniqueId))
                .rank((short) 0)
                .build();

        TalkerClient client = TalkerClient.builder().host("localhost").port(17835).header(header)
                .url(urls.get("tsn-talker") + host_name + "/stream-list/").build();
        client.start();
    }

    /**
     * create by: wpy
     * description: Listener
     * create time: 3/15/22 8:28 PM
     *
      * @Param: null
     * @return
     */
    //listener 以下参数,函数仅在操作listener config库时使用
    public void startListenerServer() throws InterruptedException {
        int uniqueId = getUniqueId();
        Header header = Header.builder().uniqueId(convertUniqueID(uniqueId))
                .rank((short) 0)
                .build();
        ListenerServer server = ListenerServer.builder().port(17835).header(header)
                .url(urls.get("tsn-listener") + host_name + "/stream-list/").build();
        server.start();
    }

    //status 以下参数,函数仅在操作status config库时使用
}
