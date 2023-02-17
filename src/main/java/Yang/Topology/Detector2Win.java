package Yang.Topology;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;

public class Detector2Win extends Detector {
    private String firstWindowsCommand = "ipconfig -all";

    public Detector2Win(){
    }

    @Override
    public JSONObject getLocalInterface(){
        JSONObject networkCards = null;
        try {
            List<String> terminals;
            terminals = runCommand(firstWindowsCommand);
            networkCards = extractNetworkCard(terminals);
        }catch (IOException e){
            e.printStackTrace();
        }
        return networkCards;
    }

    protected JSONObject extractNetworkCard(List<String> terminals){
        JSONObject origin = new JSONObject(), midObject = null;
        for (int i = 0; i < terminals.size(); i++){
            String terminal = terminals.get(i);
            if (terminal.charAt(0) != ' '){
                JSONObject object = new JSONObject();
                String key = terminal.replace(":", "");
//                System.out.println("***" + key + "***");
                origin.put(key, object);
                midObject = object;
            }else {
                String key = "", value = "";
                int j;
                for (j = 0; j < terminal.length(); j++){
                    if (terminal.charAt(j) == ':'){
                        break;
                    }else {
                        key += terminal.charAt(j);
                    }
                }
                value = terminal.substring(j+2);
                key = key.replace(".", "");
                key = key.replace(" ", "");
                key = convertStandardWindows(key);
                value = clearBrackets(convertStandardWindows(value));
                if (value == "") continue;
//                System.out.println("key = " + key + ", value = " + value);
                if (key == "Default Gateway" || key == "DNS Servers"){
                    midObject.put(key + " IPV6", value);
                    value = terminals.get(i + 1).replace(" ", "");
                    value = clearBrackets(convertStandardWindows(value));
                    midObject.put(key + " IPV4", value);
                    i++;
                }else {
                    midObject.put(key, value);
                }
            }
        }
        JSONObject networkCardObject = origin.getJSONObject("以太网适配器 以太网");
//        System.out.println(networkCardObject);
//        for (String key: networkCardObject.keySet()){
//            System.out.println(key + ", " + networkCardObject.getString(key));
//        }
        JSONObject result = new JSONObject();
        result.put("Ethernet adapter Ethernet", networkCardObject);
        return result;
    }

    private String convertStandardWindows(String name){
        switch (name){
            case "是": return "Yes";
            case "否": return "No";
            case "主机名": return "Host Name";
            case "主DNS后缀": return "Primary Dns Suffix";
            case "节点类型": return "Node Type";
            case "IP路由已启用": return "IP Routing Enabled";
            case "WINS代理已启用": return "WINS Proxy Enabled";
            case "DNS后缀搜索列表": return "DNS Suffix Search List";
            case "连接特定的DNS后缀": return "Connection-specific DNS Suffix";
            case "描述": return "Description";
            case "物理地址": return "Physical Address";
            case "DHCP已启用": return "DHCP Enabled";
            case "自动配置已启用": return "Autoconfiguration Enabled";
            case "本地链接IPv6地址": return "Link-local IPv6 Address";
            case "IPv4地址": return "IPv4 Address";
            case "子网掩码": return "Subnet Mask";
            case "默认网关": return "Default Gateway";
            case "DHCPv6客户端DUID": return "DHCPv6 Client DUID";
            case "TCPIP上的NetBIOS": return "NetBIOS over Tcpip";
            case "已启用": return "Enabled";
            case "获得租约的时间": return "Lease Obtained";
            case "租约过期的时间": return "Lease Expires";
            case "DHCP服务器": return "DHCP Server";
            case "混合": return "Hybrid";
            case "媒体状态": return "Media State";
            case "媒体已断开连接": return "Media disconnected";
            case "DNS服务器": return "DNS Servers";

            case "HostName": return "Host Name";
            case "PrimaryDnsSuffix": return "Primary Dns Suffix";
            case "NodeType": return "Node Type";
            case "IPRoutingEnabled": return "IP Routing Enabled";
            case "WINSProxyEnabled": return "WINS Proxy Enabled";
            case "DNSSuffixSearchList": return "DNS Suffix Search List";
            case "Connection-specificDNSSuffix": return "Connection-specific DNS Suffix";
            case "PhysicalAddress": return "Physical Address";
            case "DHCPEnabled": return "DHCP Enabled";
            case "AutoconfigurationEnabled": return "Autoconfiguration Enabled";
            case "Link-localIPv6Address": return "Link-local IPv6 Address";
            case "IPv4Address": return "IPv4 Address";
            case "SubnetMask": return "Subnet Mask";
            case "DefaultGateway": return "Default Gateway";
            case "DHCPv6ClientDUID": return "DHCPv6 Client DUID";
            case "NetBIOSoverTcpip": return "NetBIOS over Tcpip";
            case "LeaseObtained": return "Lease Obtained";
            case "LeaseExpires": return "Lease Expires";
            case "DHCPServer": return "DHCP Server";
            case "MediaState": return "Media State";
            case "Mediadisconnected": return "Media disconnected";
            case "DNSServers": return "DNS Servers";
            default: return name;
        }
    }
}
