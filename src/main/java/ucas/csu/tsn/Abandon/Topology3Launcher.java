package ucas.csu.tsn.Abandon;

import ucas.csu.tsn.Hardware.Computer;
import ucas.csu.tsn.RestfulAPI.RestfulDeleteInfo;
import ucas.csu.tsn.RestfulAPI.RestfulPutInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/29/22 6:16 PM
 */
public class Topology3Launcher {
    private static Thread timerThread = null;
    @Getter
    private LLDP3 lldp3;
    private String topologyId, urlFront, hostName;

    @Builder
    public Topology3Launcher(@NonNull String topologyId, @NonNull String urlFront,
                             @NonNull String hostName, @NonNull Computer computer) throws IOException {
        this.topologyId = topologyId;
        this.urlFront = urlFront;
        this.hostName = hostName;

        this.lldp3 = new LLDP3(computer);
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
                        registerDevice(lldp3);
                        Thread.sleep(timeInterval);
                    }
                }catch (InterruptedException e){
                    System.out.println("--Thread: TimerThread interrupted--");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        timerThread.start();
    }

    public void stopTimerThread(){
        if (timerThread != null && timerThread.isAlive()){
            timerThread.interrupt();
        }
        removeDevice();
    }

    /**
     * create by: wpy
     * description: topology connect 以下参数,函数仅在操作network topology config库时使用
     * create time: 3/10/22 6:09 PM
     *
     * @Param: null
     * @return
     * @param lldp3
     */
    public void registerDevice(LLDP3 lldp3){
        Topology3 topology3 = Topology3.builder().topology_id(topologyId).lldp3(lldp3).build();
        String url = this.urlFront + "topology/" + topologyId;

        JSONObject net = topology3.getJSONObject();
        JSONArray nodes = net.getJSONArray("node");
        for (int i = 0; i < nodes.size(); i++){
            JSONObject node = nodes.getJSONObject(i);
            String url1 = url + "/node/" + node.getString("node-id");
            System.out.println("--register node to controller--");
            RestfulPutInfo restfulPutInfo = RestfulPutInfo.builder().url(url1).build();
            JSONArray array = new JSONArray();
            array.add(node);
            JSONObject object = new JSONObject();
            object.put("node", node);
            restfulPutInfo.putInfo(object.toString());
        }
        JSONArray links = net.getJSONArray("link");
        for (int i = 0; i < links.size(); i++){
            JSONObject link = links.getJSONObject(i);
            String url1 = url + "/link/" + link.getString("link-id");
            System.out.println("--register link to controller--");
            RestfulPutInfo restfulPutInfo = RestfulPutInfo.builder().url(url1).build();
            JSONArray array = new JSONArray();
            array.add(link);
            JSONObject object = new JSONObject();
            object.put("link", link);
            restfulPutInfo.putInfo(object.toString());
        }
    }

    public void removeDevice(){
        String url = this.urlFront + "topology/" + topologyId + "/node/" + hostName;
        System.out.println("--remove node from controller--");
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder().url(url).build();
        restfulDeleteInfo.deleteInfo();
        for (int i = 0; i < lldp3.linkList.size(); i++){
            Link link = lldp3.linkList.get(i);
            removeLink(link);
        }
    }

    public void removeLink(Link link){
        String url = this.urlFront + "topology/" + topologyId + "/link/"
                + link.getLink_id();
        System.out.println("--remove link from controller--");
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder()
                .url(url).build();
        restfulDeleteInfo.deleteInfo();
    }
}
