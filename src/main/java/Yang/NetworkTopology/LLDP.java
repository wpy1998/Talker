package Yang.NetworkTopology;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Hardware.Computer.host_name;

public class LLDP {
    public List<Link> linkList;
    public Node current;

    public LLDP() throws IOException {
        linkList = new ArrayList<>();
        current = new Node();
        refresh();
    }

    /**
     * create by: wpy
     * description: refresh the LLDP message
     * create time: 3/10/22 6:11 AM
     *
      * @Param: null
     * @return
     */
    public void refresh() throws IOException{
        getLocalInterface();
    }

    /**
     * create by: wpy
     * description: getLocalInterface->getNeighbor->buildLink->buildNode
     * in order to get current LLDP message
     * create time: 3/10/22 6:09 AM
     *
      * @Param: null
     * @return
     */
    private void getLocalInterface() throws IOException {
        this.linkList.clear();
        Process process = Runtime.getRuntime().exec("lldpcli show interfaces -f json");
        InputStreamReader ir=new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader (ir);
        String line, result = "";
        while ((line = input.readLine ()) != null){
            result += line;
        }
        JSONObject lldp = JSON.parseObject(result).getJSONObject("lldp");
        int length;
        try {
            length = lldp.getJSONArray("interface").size();
        }catch (Exception e){
            length = 1;
        }
        if (length > 1){
            JSONArray array = lldp.getJSONArray("interface");
            for(int i = 0; i < array.size(); i++){
                JSONObject object = array.getJSONObject(i);
                Iterator<String> iterator = object.keySet().iterator();
                while (iterator.hasNext()){
                    String networkCardName = iterator.next();
                    JSONObject networkCard = object.getJSONObject(networkCardName);
                    String via = networkCard.getString("via");
                    if (!via.equals("LLDP")) continue;
                    JSONObject neighbor = getNetworkCardNeighbor(networkCardName);
                    buildLink(networkCardName, neighbor);
                    buildNode(networkCardName, networkCard, neighbor);
                }
            }
            return;
        }

        JSONObject object = lldp.getJSONObject("interface");
        Iterator<String> iterator = object.keySet().iterator();
        while (iterator.hasNext()){
            String networkCardName = iterator.next();
            JSONObject networkCard = object.getJSONObject(networkCardName);
            String via = networkCard.getString("via");
            if (!via.equals("LLDP")) continue;
            JSONObject neighbor = getNetworkCardNeighbor(networkCardName);
            buildLink(networkCardName, neighbor);
            buildNode(networkCardName, networkCard, neighbor);
        }
    }

    private JSONObject getNetworkCardNeighbor(String networkCardName) throws IOException {
        Process process = Runtime.getRuntime().exec("lldpcli show neighbors ports " +
                networkCardName + " summary -f json");
        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader (ir);
        String line, result = "";
        while ((line = input.readLine ()) != null){
            result += line;
        }
        JSONObject neighbor = null;
        JSONObject object;
        try {
            object = JSON.parseObject(result).getJSONObject("lldp").getJSONObject("interface");
        }catch (Exception e){
            object = JSON.parseObject(result).getJSONObject("lldp").getJSONArray("interface").getJSONObject(0);
        }
        Iterator<String> iterator = object.keySet().iterator();
        String key = iterator.next();
        neighbor = object.getJSONObject(key);
        return neighbor;
    }

    private void buildLink(String networkCardName, JSONObject neighbor){
        String dest_node, dest_tp;
        Iterator<String> iterator = neighbor.getJSONObject("chassis").keySet().iterator();
        dest_node = iterator.next();
        dest_tp = neighbor.getJSONObject("port").getString("descr");
        if (dest_node == null || dest_tp == null){
            buildEmptyLink(networkCardName, neighbor);
            return;
        }
        Link link = Link.builder().source_node(host_name)
                .source_tp(networkCardName)
                .dest_node(dest_node)
                .dest_tp(dest_tp).build();
        linkList.add(link);
    }

    private void buildEmptyLink(String networkCardName, JSONObject neighbor){
        String mac = neighbor.getJSONObject("chassis")
                .getJSONObject("id").getString("value");
        Link link = Link.builder().source_node(host_name).source_tp(networkCardName)
                .dest_node(mac).dest_tp(mac).build();
        linkList.add(link);
    }

    private void buildNode(String networkCardName, JSONObject local, JSONObject neighbor){
        current.node_id = host_name;
        current.setTermination_points(networkCardName);
        Iterator<String> iterator = neighbor.getJSONObject("chassis").keySet().iterator();
        String dest_tp = iterator.next();
        current.setAttachmentPoint(networkCardName, dest_tp);
        String ip, mac;
        ip = local.getJSONObject("chassis").getJSONObject(host_name)
                .getJSONArray("mgmt-ip").getString(0);
        mac = local.getJSONObject("chassis").getJSONObject(host_name)
                .getJSONObject("id").getString("value");
        current.setAddress(ip, mac);
    }
}
