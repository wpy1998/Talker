package Client.Yang;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static Client.Hardware.Computer.*;

public class NetworkTopologyYang {
    public NetworkTopologyYang(){
    }

    public Topology createTopology(String topology_id){
        Topology topology = new Topology();
        topology.topology_id = topology_id;
        return topology;
    }

    public class Topology{
        public String topology_id;
        public boolean server_provided;
        //container topology-types needed to be defined

        public List<String> underlay_topologies;
        public List<Node> nodes;
        public List<Link> links;

        private Topology(){
            underlay_topologies = new ArrayList<>();
            nodes = new ArrayList<>();
            links = new ArrayList<>();
        }

        public Node createNode(String hostName, String ip, String mac, int node_type){
            Node node = new Node(hostName, ip, mac, node_type);
            node.node_id = hostName + ":" +  mac;
            nodes.add(node);
            return node;
        }
    }

    public class Node{
        int node_type;//0-host, 1-switch

        //node-attributes
        String node_id;//h,s need
        String ip, mac;
        List<SupportingNode> supporting_nodes;//Unloaded

        //termination-point
        List<TerminationPoint> termination_points;//h, s need

        private Node(String hostName, String ip, String mac, int node_type){
            this.node_type = node_type;
            this.ip = ip;
            this.mac = mac;
            this.node_id = hostName + "," + mac;
            supporting_nodes = new ArrayList<>();
            termination_points = new ArrayList<>();
            if (this.node_type == 0){
                TerminationPoint terminationPoint = new TerminationPoint();
                terminationPoint.tp_id = this.node_id;
                termination_points.add(terminationPoint);
            }
        }

        public JSONObject getJSONObject(){
            JSONObject node = new JSONObject();
            node.put("node-id", node_id);
            if(supporting_nodes.size() > 0){
                JSONArray supportingNodes = new JSONArray();
                for (SupportingNode supportingNode: supporting_nodes){
                    JSONObject supporting_node = supportingNode.getJSONObject();
                    supportingNodes.add(supporting_node);
                }
                node.put("supporting-node", supportingNodes);
            }

            JSONArray terminationPoints = new JSONArray();
            for (TerminationPoint terminationPoint: termination_points){
                JSONObject terminationPointJSONObject = terminationPoint.getJSONObject();
                terminationPoints.add(terminationPointJSONObject);
            }
            node.put("termination-point", terminationPoints);
            return node;
        }

        private class SupportingNode{
            String topology_ref, node_ref;

            public JSONObject getJSONObject(){
                JSONObject supportingNode = new JSONObject();
                supportingNode.put("topology-ref", topology_ref);
                supportingNode.put("node-ref", node_ref);
                return supportingNode;
            }
        }

        private class TerminationPoint{
            public String tp_id;
            public List<String> tp_refs;

            public TerminationPoint(){
                tp_refs = new ArrayList<>();
            }

            public JSONObject getJSONObject(){
                JSONObject termination_point = new JSONObject();
                termination_point.put("tp-id", tp_id);
                JSONArray tpRefs = new JSONArray();
                for (String str: tp_refs){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("tp-ref", str);
                    tpRefs.add(jsonObject);
                }
                return termination_point;
            }
        }
    }

    public class Link{
        String link_id;
        //source
        String source_tp, source_node;
        //destination
        String dest_node, dest_tp;
        //supporting-link
        List<String> supporting_links;//Unloaded

        private Link(){
            supporting_links = new ArrayList<>();
        }

        JSONObject getJSONObject(){
            JSONObject link = new JSONObject();
            link.put("link-id", link_id);
            JSONObject source = new JSONObject();
            source.put("source-tp", source_tp);
            source.put("source-node", source_node);
            link.put("source", source);
            JSONObject destination = new JSONObject();
            destination.put("dest-node", dest_node);
            destination.put("dest-tp", dest_tp);
            link.put("destination", destination);
            return link;
        }
    }

    public JSONObject buildTestNode(){
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
        node.put("host", device_ip);
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