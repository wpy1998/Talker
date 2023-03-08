package ucas.csu.tsn.Hardware;

import ucas.csu.tsn.Yang.Topology.Detector;
import ucas.csu.tsn.Yang.Topology.Detector2Win;
import ucas.csu.tsn.Yang.Topology.Detector2Linux;
import ucas.csu.tsn.Yang.Topology.NetworkCard;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.net.*;
import java.util.*;

public class Computer {//获取硬件信息, LLDP
    public String host_name = null, systemType = "";
    public final String topology_id = "tsn-network", win = "Windows", linux = "Linux";
    public String cuc_ip = "localhost";
    public static long firstSeen = System.currentTimeMillis();
    public Map<String, String> urls;
    @Getter
    private NetworkCard currentNetworkCard;
    private Detector detector;

    public Computer() throws UnknownHostException {
        System.out.println("<TSN Client> 本软件运行背景是在有线连接，不支持无线连接，操作系统支持Windows和Linux.");
        urls = new HashMap<>();
        urls.put("tsn-talker", "http://" + cuc_ip +
                ":8181/restconf/config/tsn-talker-type:stream-talker-config/devices/");
        urls.put("tsn-topology", "http://" + cuc_ip +
                ":8181/restconf/config/network-topology:network-topology/");
        urls.put("tsn-listener", "http://" + cuc_ip +
                ":8181/restconf/config/tsn-listener-type:stream-listener-config/devices/");
        systemType = getSystemType();

        if (getSystemType().equals(win)){
            detector = new Detector2Win();
        }else if (getSystemType().equals(linux)){
            detector = new Detector2Linux();
        }else {
            detector = new Detector();
        }

        refresh();
    }

    public void refresh() throws UnknownHostException {
        JSONObject networkCardsJSONObject = detector.getLocalInterface();
        if (getSystemType().equals(linux)){
            for (String key: networkCardsJSONObject.keySet()){
                currentNetworkCard = new NetworkCard(key, host_name);
                currentNetworkCard.loadLinuxObject(networkCardsJSONObject.getJSONObject(key));
            }
        }else if (getSystemType().equals(win)){
            for (String key: networkCardsJSONObject.keySet()){
                currentNetworkCard = new NetworkCard(key, host_name);
                currentNetworkCard.loadWindowsObject(networkCardsJSONObject.getJSONObject(key));
            }
        }
    }

    public String getSystemType() throws UnknownHostException {
        String systemType = "";
        String os = System.getProperty("os.name");
        host_name = InetAddress.getLocalHost().getHostName();
        if (os.charAt(0) == 'L' || os.charAt(0) == 'l'){
            systemType = "Linux";
        }else if (os.charAt(0) == 'W' || os.charAt(0) == 'w'){
            systemType = "Windows";
        }
        return systemType;
    }
}
