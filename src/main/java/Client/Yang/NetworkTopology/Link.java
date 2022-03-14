package Client.Yang.NetworkTopology;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Link {
    @Getter
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
                String dest_tp){
        this.link_id = source_node + "(" + source_tp + ")--" + dest_node + "(" + dest_tp + ")";
        for(int i = 0; i < this.link_id.length(); i++){
            this.link_id = this.link_id.replace('/', '*');
        }
        this.source_node = source_node;
        this.source_tp = source_tp;
        this.dest_node = dest_node;
        this.dest_tp = (dest_tp == null) ? dest_tp : null;
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

    @Override
    public String toString(){
        return getJSONObject().toString();
    }
}
