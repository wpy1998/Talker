package Client.Yang.NetworkTopology;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Iterator;

import static Client.Hardware.Computer.host_name;

public class LLDPImpl {
    public LLDPImpl(){
    }

    public void getLLDPMessage() throws IOException{
        getInterface();
    }

    private void getInterface() throws IOException {
        Process process = Runtime.getRuntime().exec("lldpcli show interfaces -f json");
        InputStreamReader ir=new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader (ir);
        String line, result = "";
        while ((line = input.readLine ()) != null){
            result += line;
        }
        try {
            JSONArray array = JSON.parseObject(result).getJSONObject("lldp").getJSONArray("interface");
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
                }
            }
        }catch (Exception e){
            JSONObject object = JSON.parseObject(result).getJSONObject("lldp").getJSONObject("interface");
            Iterator<String> iterator = object.keySet().iterator();
            while (iterator.hasNext()){
                String networkCardName = iterator.next();
                JSONObject networkCard = object.getJSONObject(networkCardName);
                String via = networkCard.getString("via");
                if (!via.equals("LLDP")) continue;
                JSONObject neighbor = getNetworkCardNeighbor(networkCardName);
                buildLink(networkCardName, neighbor);
            }
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
        }Iterator<String> iterator = object.keySet().iterator();
        String key = iterator.next();
        neighbor = object.getJSONObject(key);
        return neighbor;
    }

    private Link buildLink(String networkCardName, JSONObject neighbor){
        String dest_node, dest_tp;
        Iterator<String> iterator = neighbor.getJSONObject("chassis").keySet().iterator();
        dest_node = iterator.next();
        dest_tp = neighbor.getJSONObject("port").getString("descr");
        return Link.builder().source_node(host_name)
                .source_tp(networkCardName)
                .dest_node(dest_node)
                .dest_tp(dest_tp).build();
    }
}
