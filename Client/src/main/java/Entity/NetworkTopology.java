package Entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static Hardware.Computer.*;

public class NetworkTopology {
    public JSONArray buildTopologies(){
        JSONArray topologies = new JSONArray();
        topologies.add(buildTopology());
        return topologies;
    }

    public JSONObject buildTopology(){
        JSONObject topology = new JSONObject();
        topology.put("topology-id", "topology_1");
        JSONArray nodes = new JSONArray();
        nodes.add(buildNode_host());
        topology.put("node", nodes);
        return topology;
    }

    public JSONObject buildNode_host(){
        JSONObject node = new JSONObject();
//        node.put("node-id", "host:" + device_mac);
//
//        JSONArray termination_points = new JSONArray();
//        JSONObject termination_point = new JSONObject();
//        termination_point.put("termination-point", "host:" + device_mac);
//        termination_points.add(termination_point);
//        node.put("termination-point", termination_points);
//
//        node.put("host-tracker-service:id", device_mac);
//        JSONArray host_tracker_service_addresses = new JSONArray();
//        JSONObject host_tracker_service_address = new JSONObject();
//        host_tracker_service_address.put("id", 0);
//        host_tracker_service_address.put("mac", device_mac);
//        host_tracker_service_address.put("first-seen", 0);//时间戳？
//        host_tracker_service_address.put("last-seen", 0);
//        host_tracker_service_address.put("ip", device_ip);
//        host_tracker_service_addresses.add(host_tracker_service_address);
//        node.put("host-tracker-service:addresses", host_tracker_service_addresses);
//
//        JSONArray host_tracker_service_attachment_points = new JSONArray();
//        JSONObject host_tracker_service_attachment_point = new JSONObject();
//        host_tracker_service_attachment_point.put("tp-id", "NONE");
//        host_tracker_service_attachment_point.put("corresponding-tp", "host:" + device_mac);
//        host_tracker_service_attachment_point.put("active", true);
//        host_tracker_service_attachment_points.add(host_tracker_service_attachment_point);

        node.put("node-id", host_name);
        node.put("host", "10.2.25.198");
        node.put("port", 17830);
        node.put("username", "admin");
        node.put("password", "admin");
//        node.put("tcp-only", false);
//        node.put("reconnect-on-changed-schema", false);
//        node.put("connection-timeout-millis", 20000);
//        node.put("max-connection-attempts", 0);
//        node.put("between-attempts-timeout-millis", 2000);
//        node.put("sleep-factor", 1.5);
//        node.put("keepalive-delay", 120);

        return node;
    }
}
/*
*
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

* */