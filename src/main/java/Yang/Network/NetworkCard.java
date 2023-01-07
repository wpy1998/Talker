package Yang.Network;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import static Hardware.Computer.host_merge;

public class NetworkCard {
    @Getter
    private String ip, ipv6, mac, netmask, node_id;
    @Getter
    private int speed;

    public NetworkCard(){
        ip = "127.0.0.1";
        ipv6 = "::1";
        mac = null;
    }

    public void loadWindowsObject(JSONObject origin){
    }

    public void loadLinuxObject(JSONObject origin){
        System.out.println(origin);
        mac = origin.getString("ether").replace(":", "-");
        ip = origin.getString("inet");
        ipv6 = origin.getString("inet6");
        netmask = origin.getString("netmask");
        JSONObject ethtool = origin.getJSONObject("ethtool");
        String midString = ethtool.getString("Speed");
        char midChar[] = midString.toCharArray();
        midString = "";
        for (int i = 0; i < midChar.length; i++){
            if (midChar[i] >= '0' && midChar[i] <= '9'){
                midString = midString + midChar[i];
            }else {
                break;
            }
        }
        speed = Integer.parseInt(midString);
        node_id = host_merge;
    }

    public JSONObject getJSONObject(){
        JSONObject node = new JSONObject();
        node.put("node-id", node_id);
        node.put("node-type", "device");
        return node;
    }
}
