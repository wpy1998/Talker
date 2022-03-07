package Client;

import Client.Hardware.Computer;
import Client.HttpInfo.GetInfo;
import Client.Yang.CUCConnect;
import com.alibaba.fastjson.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;

import static Client.Hardware.Computer.*;

public class ClientApp {
    public static final String cuc_ip = "10.2.25.85";
    public static void main(String[] args) throws IOException, SAXException {
        Computer computer = new Computer();
        computer.refresh();
        System.out.println(computer.device_ip + ", " + computer.device_mac);

//        PutInfo putInfo = PutInfo.builder()
//                .url("http://" + cuc_ip + ":8181/restconf/config/network-topology:network-topology" +
//                        "/topology/topology-netconf/node/" + host_name + "/").build();
//        JSONObject node = buildTestNode();
//        JSONObject input1 = new JSONObject();
//        input1.put("urn:TBD:params:xml:ns:yang:network-topology:node", node);
//        System.out.println(input1.toString());
//        putInfo.putInfo(input1.toString());

//        Topology topology = new Topology("tsn-network");
//        Node currentNode = new Node();
//        topology.addNode(currentNode);
//
//        JSONObject object = topology.getJSONObject();
//        JSONArray array = new JSONArray();
//        array.add(topology.getJSONObject());
//
//        JSONObject topologies = new JSONObject();
//        JSONObject network_topology = new JSONObject();
//        topologies.put("topology", array);
//        network_topology.put("urn:TBD:params:xml:ns:yang:network-topology:network-topology", topologies);
//        PutInfo putInfo = PutInfo.builder()
//                .url("http://" + cuc_ip + ":8181/restconf/config/network-topology:network-topology").build();
//        System.out.println(network_topology.toString());
//        putInfo.putInfo(network_topology.toString());

        GetInfo getInfo = GetInfo.builder().url("http://" + cuc_ip +
                ":8181/restconf/operations/tsn-talker-type:test").build();
        getInfo.getInfo();

        CUCConnect cucConnect = new CUCConnect();
        cucConnect.registerAndSendStream("test");
        cucConnect.registerAndSendStream("1111");
    }

    public static JSONObject buildTestNode(){
        JSONObject node = new JSONObject();

        node.put("node-id", host_name);
        node.put("host", device_ip);
        node.put("port", 17830);
        node.put("username", "admin");
        node.put("password", "admin");
        node.put("tcp-only", false);
        node.put("reconnect-on-changed-schema", false);
        node.put("connection-timeout-millis", 20000);
        node.put("max-connection-attempts", 0);
        node.put("between-attempts-timeout-millis", 2000);
        node.put("sleep-factor", 1.5);
        node.put("keepalive-delay", 120);

        return node;
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