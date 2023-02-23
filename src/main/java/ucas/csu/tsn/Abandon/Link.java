package ucas.csu.tsn.Abandon;

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
    String dest_node, dest_tp, dest_mac;
    //supporting-link
    List<String> supporting_links;//Unloaded
    //l3-unicast-igp-topology:igp-link-attributes

    private JSONObject speed;
    @Builder
    public Link(@NonNull String source_node,
                @NonNull String source_tp,
                @NonNull String dest_node,
                @NonNull String dest_tp,
                @NonNull String dest_mac){
        this.link_id = source_node + "(" + source_tp + ")--" + dest_node + "(" + dest_tp + ")";
        for(int i = 0; i < this.link_id.length(); i++){
            this.link_id = this.link_id.replace('/', '*');
        }
        this.source_node = source_node;
        this.source_tp = source_tp;
        this.dest_node = dest_node;
        this.dest_tp = dest_tp;
        this.dest_mac = dest_mac;
        supporting_links = new ArrayList<>();
        speed = null;
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
        if (speed != null){
            link.put("speed", speed);
        }
        return link;
    }

    @Override
    public String toString(){
        return getJSONObject().toString();
    }

    public void setSpeed(JSONObject speed) {
        this.speed = speed;
    }
}
