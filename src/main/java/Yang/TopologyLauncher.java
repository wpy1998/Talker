package Yang;

import Hardware.Computer;
import RestfulAPI.RestfulDeleteInfo;
import RestfulAPI.RestfulPutInfo;
import Yang.Topology.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopologyLauncher {
    private static Thread timerThread = null;
    private String topologyId, urlFront, hostName;
    private Computer computer;

    public TopologyLauncher(Computer computer){
        this.urlFront = computer.urls.get("tsn-topology");
        this.hostName = computer.host_name;
        this.computer = computer;
        this.topologyId = this.computer.topology_id;
    }

    public void startTimerThread(){
        if (timerThread != null){
            System.out.println("<TSN Client> timer has started <TSN Client>");
            return;
        }

        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int timeInterval = 10 * 60 * 1000;
                    while (true){
                        computer.refresh();
                        registerDevice();
                        Thread.sleep(timeInterval);
                    }
                }catch (InterruptedException e){
                    System.out.println("<TSN Client> Thread: TimerThread interrupted <TSN Client>");
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

    public void registerDevice(){
        NetworkCard networkCard = computer.getCurrentNetworkCard();
        String url = this.urlFront + "topology/" + topologyId + "/node/" + networkCard.getNode_id();
        JSONObject node = networkCard.getJSONObject();
        System.out.println("<TSN Client> register node to controller <TSN Client>");
        RestfulPutInfo restfulPutInfo = RestfulPutInfo.builder().url(url).build();
//        JSONArray array = new JSONArray();
//        array.add(node);
        JSONObject object = new JSONObject();
        object.put("node", node);
        restfulPutInfo.putInfo(object.toString());
    }

    public void removeDevice(){
        NetworkCard networkCard = computer.getCurrentNetworkCard();
        String url = this.urlFront + "topology/" + topologyId + "/node/" + networkCard.getNode_id();
        System.out.println("<TSN Client> remove node from controller <TSN Client>");
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder().url(url).build();
        restfulDeleteInfo.deleteInfo();
    }
}
