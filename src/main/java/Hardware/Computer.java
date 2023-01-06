package Hardware;

import Yang.Network.LLDP2Win;
import Yang.Network.LLDP2linux;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.*;
import java.util.*;

public class Computer {//获取硬件信息, LLDP
    public static String host_name = null, host_merge, systemType = "";
    public static final String cuc_ip = "localhost";
    public static final String topology_id = "tsn-network";
    public static long firstSeen = System.currentTimeMillis();
    public Map<String, String> urls;
    public static List<String> ipv4s, ipv6s, macs;
    private LLDP lldp;

    public Computer(){
        System.out.println("<TSN Client> 本软件运行背景是在有线连接，不支持无线连接，操作系统仅支持Linux <TSN Client>");
        urls = new HashMap<>();
        urls.put("tsn-talker", "http://" + cuc_ip +
                ":8181/restconf/config/tsn-talker-type:stream-talker-config/devices/");
        urls.put("tsn-topology", "http://" + cuc_ip +
                ":8181/restconf/config/network-topology:network-topology/");
        urls.put("tsn-listener", "http://" + cuc_ip +
                ":8181/restconf/config/tsn-listener-type:stream-listener-config/devices/");
        systemType = getSystemType();
        if (systemType.equals("Windows")){
            lldp = new LLDP2Win();
        }else if (systemType.equals("Linux")){
            lldp = new LLDP2linux();
        }else {
            lldp = new LLDP();
        }
        ipv4s = new ArrayList<>();
        ipv6s = new ArrayList<>();
        macs = new ArrayList<>();
        try {
            refresh();
            System.out.println(ipv4s + ", " + macs + ", " + ipv6s + ", " + host_name);
            if (macs.size() == 0){
                System.out.println("--Did not find suitable mac--");
                host_merge = host_name;
            }else {
                host_merge = host_name + macs.get(0);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void refresh() throws IOException {//获取ip和第一个mac地址
        macs.clear();
        ipv4s.clear();
        ipv6s.clear();
        InetAddress ia = InetAddress.getLocalHost();
        host_name = ia.getHostName();

        Process process = Runtime.getRuntime().exec("lldpcli show interfaces " +
                "-f json");
        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader (ir);
        String line, result = "";
        while ((line = input.readLine ()) != null){
            result += line;
        }
        JSONObject origin = JSON.parseObject(result).getJSONObject("lldp");
        try {
            JSONObject inter = origin.getJSONObject("interface");
            addComputerMessage(inter);
        }catch (Exception e){
            JSONArray inter = origin.getJSONArray("interface");
            for (int i = 0; i < inter.size(); i++){
                addComputerMessage(inter.getJSONObject(i));
            }
        }

        JSONObject networkCards = lldp.getLocalInterface();
        System.out.println(networkCards);
    }

    public void addComputerMessage(JSONObject inter){
        Iterator<String> iterator = inter.keySet().iterator();
        String networkCardName = iterator.next();
        JSONObject networkCard = inter.getJSONObject(networkCardName);
//        if (!networkCard.getString("via").equals("LLDP")){
//            return;
//        }
        JSONObject chassis = networkCard.getJSONObject("chassis");
        iterator = chassis.keySet().iterator();
        String name = iterator.next();
        JSONObject target = chassis.getJSONObject(name);
        String mac = target.getJSONObject("id").getString("value")
                .replace(':', '-');
        insertToList(macs, mac);
        try{
            insertToList(ipv4s, target.getJSONArray("mgmt-ip").getString(0));
            insertToList(ipv6s, target.getJSONArray("mgmt-ip").getString(1));
        }catch (Exception e){
            insertToList(ipv4s, target.getString("mgmt-ip"));
            insertToList(ipv6s, "0000:0000:0000:0000:0000:0000:0000:0000");
        }
    }

    private void insertToList(List<String> list, String content){
        for (String str: list){
            if (content.equals(str)){
                return;
            }
        }
        list.add(content);
    }

    public String getSystemType() {
        String systemType = "";
        String os = System.getProperty("os.name");
        if (os.charAt(0) == 'L' || os.charAt(0) == 'l'){
            systemType = "Linux";
        }else if (os.charAt(0) == 'W' || os.charAt(0) == 'w'){
            systemType = "Windows";
        }
        return systemType;
    }
}
