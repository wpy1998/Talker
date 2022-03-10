package Client.Yang.NetworkTopology;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
            System.out.println(line);
            result += line;
        }
        JSONArray array = JSON.parseObject(result).getJSONObject("lldp").getJSONArray("interface");
        for(int i = 0; i < array.size(); i++){
            JSONObject object = array.getJSONObject(i);
            Iterator<String> iterator = object.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                JSONObject networkCard = object.getJSONObject(key);
                String via = networkCard.getString("via");
                if (!via.equals("LLDP")) continue;
                JSONObject other = getNetworkCardNeighbor(key);
                System.out.println(networkCard.getJSONObject("chassis").getJSONObject(host_name).toString());
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
            System.out.println(line);
            result += line;
        }
        JSONObject object = JSON.parseObject(result).getJSONObject("lldp").getJSONObject("interface");
        Iterator<String> iterator = object.keySet().iterator();
        String key = iterator.next();
        JSONObject message = object.getJSONObject(key);
        System.out.println(message.toString());
        return message;
    }

    private class LLDPLink{
        @Setter@Getter
        String source_node, source_tp, dest_node, dest_tp;
    }
}
