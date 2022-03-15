package Yang.NetworkTopology;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Topology {
    public String topology_id;
    public boolean server_provided;
    //container topology-types needed to be defined

    public List<String> underlay_topologies;
    public List<Node> nodes;
    public List<Link> links;

    @Builder
    public Topology(@NonNull String topology_id,
                    @NonNull LLDP lldp){
        this.topology_id = topology_id;
        underlay_topologies = new ArrayList<>();
        nodes = new ArrayList<>();
        links = new ArrayList<>();
        addLink(lldp);
        this.nodes.add(lldp.current);
    }

    public void addLink(LLDP lldp){
        for(Link link: lldp.linkList){
            this.links.add(link);
        }
    }

    public JSONObject getJSONObject(){
        JSONObject topology = new JSONObject();
        topology.put("topology-id", this.topology_id);
        JSONArray array = new JSONArray();
        for (Node node: this.nodes){
            array.add(node.getJSONObject());
        }
        topology.put("node", array);
        JSONArray array1 = new JSONArray();
        for (Link link: this.links){
            array1.add(link.getJSONObject());
        }
        topology.put("link", array1);
        return topology;
    }
}
