package Entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static Hardware.Computer.device_ip;
import static Hardware.Computer.device_mac;

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
        node.put("node-id", "host:" + device_mac);

        JSONArray termination_points = new JSONArray();
        JSONObject termination_point = new JSONObject();
        termination_point.put("termination-point", "host:" + device_mac);
        termination_points.add(termination_point);
        node.put("termination-point", termination_points);

        node.put("host-tracker-service:id", device_mac);
        JSONArray host_tracker_service_addresses = new JSONArray();
        JSONObject host_tracker_service_address = new JSONObject();
        host_tracker_service_address.put("id", 0);
        host_tracker_service_address.put("mac", device_mac);
        host_tracker_service_address.put("first-seen", 0);//时间戳？
        host_tracker_service_address.put("last-seen", 0);
        host_tracker_service_address.put("ip", device_ip);
        host_tracker_service_addresses.add(host_tracker_service_address);
        node.put("host-tracker-service:addresses", host_tracker_service_addresses);

        JSONArray host_tracker_service_attachment_points = new JSONArray();
        JSONObject host_tracker_service_attachment_point = new JSONObject();
        host_tracker_service_attachment_point.put("tp-id", "NONE");
        host_tracker_service_attachment_point.put("corresponding-tp", "host:" + device_mac);
        host_tracker_service_attachment_point.put("active", true);
        host_tracker_service_attachment_points.add(host_tracker_service_attachment_point);

        return node;
    }
}
