package Yang;

import RestfulAPI.RestfulDeleteInfo;
import RestfulAPI.RestfulPutInfo;
import Yang.NetworkTopology.LLDP;
import Yang.NetworkTopology.Link;
import Yang.NetworkTopology.Topology;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.NonNull;

import java.io.IOException;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/29/22 6:16 PM
 */
public class NetworkTopologyLauncher {
    private static Thread timerThread = null;
    private LLDP lldp;
    private String topologyId, topologyFront, hostName;

    @Builder
    public NetworkTopologyLauncher(@NonNull String topologyId, @NonNull String topologyFront,
                                   @NonNull String hostName) throws IOException {
        this.topologyId = topologyId;
        this.topologyFront = topologyFront;
        this.hostName = hostName;

        this.lldp = new LLDP();
    }

    public void startTimerThread(){
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
                    removeDevice();
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
     * @param lldp
     */
    public void registerDevice(LLDP lldp){
        String url = this.topologyFront + "topology/" + topologyId;
        Topology topology = Topology.builder().topology_id(topologyId).lldp(lldp).build();

        JSONArray array = new JSONArray();
        array.add(topology.getJSONObject());
        JSONObject topologies = new JSONObject();
        topologies.put("topology", array);

        RestfulPutInfo restfulPutInfo = RestfulPutInfo.builder().url(url).build();
        restfulPutInfo.putInfo(topologies.toString());
    }

    public void removeDevice(){
        String url = this.topologyFront + "topology/" + topologyId + "/node/" + hostName;
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder().url(url).build();
        restfulDeleteInfo.deleteInfo();
        for (int i = 0; i < lldp.linkList.size(); i++){
            Link link = lldp.linkList.get(i);
            removeLink(link);
        }
    }

    public void removeLink(Link link){
        String url = this.topologyFront + "topology/" + topologyId + "/link/"
                + link.getLink_id();
        System.out.println(url);
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder()
                .url(url).build();
        restfulDeleteInfo.deleteInfo();
    }
}
