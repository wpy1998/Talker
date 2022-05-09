package Yang.Network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Hardware.Computer.host_merge;
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
        Process process = Runtime.getRuntime()
                .exec("lldpcli show interfaces -f json");
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
                    buildTargetLink(networkCardName, neighbor);
                    buildNode(networkCardName, networkCard, neighbor);
                }
            }
            return;
        }

        JSONObject object = lldp.getJSONObject("interface");
        Iterator<String> iterator = object.keySet().iterator();
        while (iterator.hasNext()){//networkCard size > 1
            String networkCardName = iterator.next();
            JSONObject networkCard = object.getJSONObject(networkCardName);
            String via = networkCard.getString("via");
            if (!via.equals("LLDP")) continue;
            JSONObject neighbor = getNetworkCardNeighbor(networkCardName);
            if(neighbor == null) {
                System.out.println("NetworkCard: " + networkCardName + " has no neighbor" +
                        " through LLDP");
                continue;
            }//this network has no neighbor through LLDP
            buildTargetLink(networkCardName, neighbor);
            buildNode(networkCardName, networkCard, neighbor);
        }
    }

    private JSONObject getNetworkCardNeighbor(String networkCardName) throws IOException {
        Process process = Runtime.getRuntime().exec("lldpcli show neighbors ports "
                + networkCardName + " -f json");
        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader (ir);
        String line, result = "";
        while ((line = input.readLine ()) != null){
            result += line;
        }
        JSONObject neighbor = null, object = null;
        JSONObject lldp = JSON.parseObject(result).getJSONObject("lldp");
        int length;
        try {
            JSONArray array =  lldp.getJSONArray("interface");
            if (array != null){
                length = array.size();
            }else {
                length = 0;
            }
        }catch (Exception e){
            if(lldp.getJSONObject("interface") != null){
                length = 1;
            }else {
                length = 0;
            }
        }

        if(length > 1){
            JSONArray interfaces = JSON.parseObject(result).getJSONObject("lldp")
                    .getJSONArray("interface");
            for (int i = 0; i < interfaces.size(); i++){
                object = interfaces.getJSONObject(i);
                int ttl = object.getJSONObject(networkCardName).getJSONObject("port")
                        .getInteger("ttl");

                JSONObject chassis = object.getJSONObject(networkCardName)
                        .getJSONObject("chassis");
                Iterator<String> iterator = chassis.keySet().iterator();
                String key = iterator.next();
                if (chassis.getJSONObject("id") == null &&
                        chassis.getJSONObject(key).getJSONObject("id") != null){
                    break;
                }
            }
        }else if (length == 1){
            object = JSON.parseObject(result).getJSONObject("lldp")
                    .getJSONObject("interface");
            int ttl = object.getJSONObject(networkCardName).getJSONObject("port")
                    .getInteger("ttl");
            if (ttl > 10000) object = null;
        }else {
            return null;
        }
        if (object == null) return neighbor;
        neighbor = object.getJSONObject(networkCardName);
        return neighbor;
    }

    private void buildTargetLink(String networkCardName, JSONObject neighbor) throws IOException {
        if (neighbor == null){
            return;
        }
        String dest_node, dest_tp;
        Iterator<String> iterator = neighbor.getJSONObject("chassis").keySet().iterator();
        dest_node = iterator.next();
        dest_tp = neighbor.getJSONObject("port").getString("descr");
        if (dest_node == null || dest_tp == null){
            buildEmptyLink(networkCardName, neighbor);
            return;
        }
        Link link = Link.builder().source_node(host_merge)
                .source_tp(networkCardName)
                .dest_node(dest_node)
                .dest_tp(dest_tp)
                .build();
        JSONObject object = neighbor.getJSONObject("chassis").getJSONObject(dest_node);
        String dest_ip;
        try {
            dest_ip = object.getJSONArray("mgmt-ip").getString(0);
        }catch (Exception e){
            dest_ip = object.getString("mgmt-ip");
        }
        JSONObject speed = getSpeed(dest_ip);
        link.setSpeed(speed);
        linkList.add(link);
    }

    private void buildEmptyLink(String networkCardName, JSONObject neighbor){
        String mac = neighbor.getJSONObject("chassis")
                .getJSONObject("id").getString("value");
        Link link = Link.builder().source_node(host_merge).source_tp(networkCardName)
                .dest_node(mac).dest_tp(mac).build();
        linkList.add(link);
    }

    private void buildNode(String networkCardName, JSONObject local, JSONObject neighbor){
        if (neighbor == null){
            return;
        }
        current.node_id = host_merge;
        current.setTermination_points(networkCardName);
        Iterator<String> iterator = neighbor.getJSONObject("chassis").keySet().iterator();
        String dest_tp = iterator.next();
        current.setAttachmentPoint(networkCardName, dest_tp);
        String ip, mac;
        try{
            ip = local.getJSONObject("chassis").getJSONObject(host_name)
                    .getJSONArray("mgmt-ip").getString(0);
        }catch (Exception e){
            ip = local.getJSONObject("chassis").getJSONObject(host_name)
                    .getString("mgmt-ip");
        }
        mac = local.getJSONObject("chassis").getJSONObject(host_name)
                .getJSONObject("id").getString("value");
        current.setAddress(ip, mac);
    }

    public JSONObject getSpeed(String destinationIP) throws IOException {
        Process process = Runtime.getRuntime().exec("mtr -r " +
                destinationIP + " -j");
        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader (ir);
        String line, result = "";
        while ((line = input.readLine ()) != null){
            result += line;
        }
        if (result.length() == 0) return null;
        JSONObject report = JSON.parseObject(result).getJSONObject("report");
        JSONObject mtr = report.getJSONObject("mtr");
        JSONArray hubs = report.getJSONArray("hubs");
        JSONObject object = hubs.getJSONObject(0);
        JSONObject speed = new JSONObject();
        speed.put("packet-size", mtr.getInteger("psize"));
        speed.put("loss", object.getFloat("Loss%"));
        speed.put("best", object.getFloat("Best"));
        speed.put("worst", object.getFloat("Wrst"));
        speed.put("avg", object.getFloat("Avg"));
        return speed;
    }
}
