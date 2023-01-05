package Yang.Network;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LLDP2 {
    private String windowsCommand = "ipconfig -all", linuxCommand = "ifconfig";
    @Getter
    private String systemType;

    public LLDP2(){
        String os = System.getProperty("os.name");
        if (os.charAt(0) == 'L' || os.charAt(0) == 'l'){
            systemType = "Linux";
        }else if (os.charAt(0) == 'W' || os.charAt(0) == 'w'){
            systemType = "Windows";
        }
    }

    //chcp 437 change to english
    //chcp 936 change to chinese
    public void getLocalInterface() throws IOException {
        if (systemType == "Windows"){
            runWindowsCommand();
        }else if (systemType == "Linux"){
            runLinuxCommand();
        }
    }

    private void runWindowsCommand() throws IOException {
        Process process = Runtime.getRuntime().exec(windowsCommand);
        InputStream in = process.getInputStream();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "gbk"));
        String line;
        List<String> terminals = new ArrayList<>();
        while ((line = br.readLine ()) != null){
            if (line.length() != 0){
                terminals.add(new String(line));
            }
        }
        JSONObject origin = new JSONObject(), midObject = null;
//        for (String terminal: terminals){
//            System.out.println(terminal);
//        }
        for (int i = 0; i < terminals.size(); i++){
            String terminal = terminals.get(i);
            if (terminal.charAt(0) != ' '){
                JSONObject object = new JSONObject();
                String key = terminal.replace(":", "");
                System.out.println(key);
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
                System.out.println("key = " + key + ", value = " + value);
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
        System.out.println(origin);
    }

    private void runLinuxCommand() throws IOException {
        Process process = Runtime.getRuntime().exec(linuxCommand);
        InputStream in = process.getInputStream();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "gbk"));
        String line;
        List<String> terminals = new ArrayList<>();
        while ((line = br.readLine ()) != null){
            if (line.length() != 0){
                terminals.add(new String(line));
            }
        }
        JSONObject origin = new JSONObject(), midObject = null;
        for (String terminal: terminals){
            System.out.println(terminal);
            String midString = clearBrackets(terminal);
            List<String> elements = new ArrayList<>();
            String[] temp = midString.split(" ");
            for (int i = 0; i < temp.length; i++){
                if (temp[i].length() != 0){
                    elements.add(temp[i]);
//                    System.out.println(temp[i]);
                }
            }
            if (terminal.charAt(0) != ' '){
                JSONObject object = new JSONObject();
                String name = elements.get(0).replace(":", "");
                origin.put(name, object);
                String flag = elements.get(1);
                String midList[] = flag.split("=");
                object.put(midList[0], midList[1]);
                for (int i = 2; i < elements.size(); i = i + 2){
                    object.put(elements.get(i), elements.get(i + 1));
                }
                midObject = object;
            }else {
                if (elements.size() % 2 == 0){
                    for (int i = 0; i < elements.size(); i = i + 2){
                        midObject.put(elements.get(i), elements.get(i + 1));
                    }
                }else {
                    JSONObject xObject;
                    if (midObject.get(elements.get(0)) != null){
                        xObject = midObject.getJSONObject(elements.get(0));
                    }else {
                        xObject = new JSONObject();
                        midObject.put(elements.get(0), xObject);
                    }
                    for (int i = 1; i < elements.size(); i = i + 2){
                        xObject.put(elements.get(i), elements.get(i + 1));
                    }
                }
            }
        }
        System.out.println(origin);
    }

    private String clearBrackets(String content){
        String result = "";
        int flag = 0;
        for (int i = 0; i < content.length(); i++){
            if (content.charAt(i) == '(' || content.charAt(i) == '<'){
                flag++;
            }else if (content.charAt(i) == ')' || content.charAt(i) == '>'){
                flag--;
            }else if (flag == 0){
                result += content.charAt(i);
            }
        }
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