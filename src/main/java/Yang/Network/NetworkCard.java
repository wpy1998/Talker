package Yang.Network;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import static Hardware.Computer.firstSeen;

public class NetworkCard {
    @Getter
    private String ip, ipv6, mac, netmask, node_id, name, hostName;
    @Getter
    private int speed;

    public NetworkCard(String name, String hostName){
        this.hostName = hostName;
        this.name = name;
        ip = "127.0.0.1";
        ipv6 = "::1";
        mac = null;
    }

    public void loadWindowsObject(JSONObject origin){
    }

    public void loadLinuxObject(JSONObject origin){
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
        node_id = this.hostName + this.mac;
    }

    public JSONObject getJSONObject(){
        JSONObject node = new JSONObject();
        node.put("node-id", this.node_id);
        node.put("node-type", "device");
        node.put("id", mac.replace(":", "-"));

        JSONArray addresses = new JSONArray();
        JSONObject address = new JSONObject();
        addresses.add(address);
        address.put("id", 0);
        address.put("first-seen", firstSeen);
        address.put("mac", this.mac.replace("-", ":"));
        address.put("last-seen", System.currentTimeMillis());
        address.put("ip", this.ip);
        node.put("addresses", addresses);

        JSONArray terminationPoints = new JSONArray();
        JSONObject terminationPoint = new JSONObject();
        terminationPoints.add(terminationPoint);
        terminationPoint.put("tp-id", this.name);
        node.put("termination-point", terminationPoints);
        return node;
    }
}
