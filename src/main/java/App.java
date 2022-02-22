import Entity.NetworkTopology;
import Hardware.Computer;
import HttpInfo.PutInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.juniper.netconf.Device;
import net.juniper.netconf.XML;
import org.xml.sax.SAXException;

import java.io.IOException;

import static Hardware.Computer.device_ip;
import static Hardware.Computer.host_name;

public class App {
    public static void main(String[] args) throws IOException, SAXException {
        Computer computer = new Computer();
        computer.refresh();
        System.out.println(computer.device_ip + ", " + computer.device_mac);
        String destionation_ip = "10.2.25.85";
//        String url = "http://" + destionation_ip + ":8181/restconf/operations/talker:";
//
//        PostInfo postInfo = new PostInfo(url + "join");
//        StreamHeader header = new StreamHeader();
//        JSONObject stream_header = header.getJSONObject(false, true,
//                false, false, false,
//                false, false);
//        JSONObject stream = new JSONObject();
//        JSONObject input = new JSONObject();
//        stream.put("header", stream_header);
//        stream.put("body", "test join");
//        input.put("input", stream);
//        postInfo.postInfo(input.toString());
//
//        GetInfo getInfo = new GetInfo(url + "test");
//        getInfo.getInfo();
//
//        PostInfo postInfo1 = new PostInfo(url + "leave");
//        postInfo1.postInfo(input.toString());

//        PutInfo putInfo = PutInfo.builder()
//                .url("http://" + destionation_ip + ":8181/restconf/config/network-topology:network-topology" +
//                        "/topology/topology-netconf/node/" + host_name + "/").build();
//        JSONObject node = new NetworkTopology().buildNode_host();
//        System.out.println(node.toString());
//        putInfo.putInfo(node.toString());

        Device device = Device.builder()
                .hostName(device_ip)
                .userName("admin")
                .password("admin")
                .port(17830)
                .timeout(50000)
                .hostKeysFileName("hostKeysFileName")
                .build();
        device.connect();

        XML rpc_reply = device.executeRPC("get-config");

        System.out.println(rpc_reply);
        device.close();
    }
}

/*
<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>ubuntu</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">127.0.0.1</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">17830</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>
{
"urn:TBD:params:xml:ns:yang:network-topology:node":{
"node-id":"ubuntu",
"host":"127.0.0.1",
"username":"admin",
"password":"admin",
"port":17830
}
}
* */