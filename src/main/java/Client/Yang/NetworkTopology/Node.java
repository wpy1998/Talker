package Client.Yang.NetworkTopology;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import static Client.Hardware.Computer.*;

public class Node {
    //node-attributes
    String node_id;//h,s need
    List<String> supporting_nodes;//Unloaded

    //termination-point
    List<String> termination_points;//h, s need

    //l3-unicast-igp-topology:igp-node-attributes

    public Node(){
        this.node_id = host_name + ":" + device_mac;
        supporting_nodes = new ArrayList<>();
        termination_points = new ArrayList<>();

        supporting_nodes.add(this.node_id);

        termination_points.add("not known");
    }

    public JSONObject getJSONObject(){
        JSONObject node = new JSONObject();
        node.put("node-id", node_id);

        node.put("host", device_ip);
        node.put("port", 17830);
//        node.put("username", "admin");
//        node.put("password", "admin");

        JSONArray supportingNodes = new JSONArray();
        for(String str: supporting_nodes){
            JSONObject object = new JSONObject();
            object.put("node-ref", str);
//            object.put("topology-ref", "NULL");
            supportingNodes.add(object);
        }
        node.put("supporting-node", supportingNodes);
//
        JSONArray terminationPoints = new JSONArray();
        for (String str: termination_points){
            JSONObject object = new JSONObject();
            object.put("tp-id", str);
            terminationPoints.add(object);
        }
        node.put("termination-point", terminationPoints);
        return node;
    }

//    private class TerminationPoint{
//        public String tp_id;
//        //l3-unicast-igp-topology:igp-termination-point-attributes
//
//        public TerminationPoint(){
//        }
//
//        public JSONObject getJSONObject(){
//            JSONObject termination_point = new JSONObject();
//            termination_point.put("tp-id", tp_id);
//            return termination_point;
//        }
//    }
}
