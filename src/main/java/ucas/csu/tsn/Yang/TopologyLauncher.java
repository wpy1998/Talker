package ucas.csu.tsn.Yang;

import ucas.csu.tsn.Yang.Topology.NetworkCard;
import ucas.csu.tsn.Hardware.Computer;
import ucas.csu.tsn.RestfulAPI.RestfulDeleteInfo;
import ucas.csu.tsn.RestfulAPI.RestfulPutInfo;
import com.alibaba.fastjson.JSONObject;

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
            System.out.println("<TSN Client TopologyLauncher> timer has started.");
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
                    System.out.println("<TSN Client TopologyLauncher> Thread: TimerThread interrupted.");
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
        System.out.println("<TSN Client TopologyLauncher> register node to controller.");
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
        System.out.println("<TSN Client TopologyLauncher> remove node from controller.");
        RestfulDeleteInfo restfulDeleteInfo = RestfulDeleteInfo.builder().url(url).build();
        restfulDeleteInfo.deleteInfo();
    }
}
