package Hardware;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Computer {//获取硬件信息, LLDP
    public static String device_ip = null;
    public static List<String> device_mac = null;
    public static String host_name = null;
    public static final String cuc_ip = "10.2.25.4";
    public static final String topology_id = "tsn-network";
    public Map<String, String> urls;

    public Computer(){
        urls = new HashMap<>();
        urls.put("tsn-talker", "http://" + cuc_ip +
                ":8181/restconf/config/tsn-talker-type:stream-talker-config/devices/");
        urls.put("tsn-topology", "http://" + cuc_ip +
                ":8181/restconf/config/network-topology:network-topology/");
        urls.put("tsn-listener", "http://" + cuc_ip +
                ":8181/restconf/config/tsn-listener-type:stream-listener-config/devices/");
        try {
            refresh();
            System.out.println(device_ip + ", " + device_mac + ", " + host_name);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void refresh() throws UnknownHostException, SocketException {//获取ip和第一个mac地址
        InetAddress ia = InetAddress.getLocalHost();
        String ip = ia.getHostAddress();
        device_ip = ip;
        device_mac = getDeviceMac();
        host_name = ia.getHostName();
    }

    public List<String> getDeviceMac() throws SocketException {//获取mac地址
        java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        StringBuilder sb = new StringBuilder();
        ArrayList<String> tmpMacList = new ArrayList<>();
        while (en.hasMoreElements()){
            NetworkInterface iface = en.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr: addrs){
                InetAddress ip = addr.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null){
                    continue;
                }
                byte[] mac = network.getHardwareAddress();
                if (mac == null){
                    continue;
                }
                sb.delete(0, sb.length());
                for (int i = 0; i < mac.length; i++){
                    sb.append(String.format("%02x%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                tmpMacList.add(sb.toString());
            }
        }
        if (tmpMacList.size() <= 0){
            return tmpMacList;
        }
        List<String> unique = tmpMacList.stream().distinct().collect(Collectors.toList());
        return unique;
    }
}
