package Client.Yang.NetworkTopology;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Link {
    String link_id;
    //source
    String source_tp, source_node;
    //destination
    String dest_node, dest_tp;
    //supporting-link
    List<String> supporting_links;//Unloaded
    //l3-unicast-igp-topology:igp-link-attributes

    @Builder
    public Link(@NonNull String source_node,
                @NonNull String source_tp,
                @NonNull String dest_node,
                @NonNull String dest_tp){
        this.link_id = source_tp + "/" + dest_tp;
        this.source_node = source_node;
        this.source_tp = source_tp;
        this.dest_node = dest_node;
        this.dest_tp = dest_tp;
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
