package Hardware;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Computer {//获取硬件信息
    public static String device_ip = null;
    public static List<String> device_mac = null;
    public static String host_name = null;

    public void refresh() throws UnknownHostException, SocketException {//获取ip和第一个mac地址
        InetAddress ia = InetAddress.getLocalHost();
        String ip = ia.getHostAddress();
        device_ip = ip;
        device_mac = getALLMac();
        host_name = ia.getHostName();
    }

    public List<String> getALLMac() throws SocketException {//获取mac地址
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
                    sb.append(String.format("%02x%s", mac[i], (i < mac.length - 1) ? ":" : ""));
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
