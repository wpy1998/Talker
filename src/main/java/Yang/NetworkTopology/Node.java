package Yang.NetworkTopology;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static Hardware.Computer.*;

public class Node {
    //node-attributes
    String node_id;//h,s need
    List<String> supporting_nodes;//Unloaded

    //termination-point
    List<String> termination_points;//h, s need

    //l3-unicast-igp-topology:igp-node-attributes

    List<AttachmentPoint> attachmentPoints;
    List<Address> addresses;

    public Node(){
        this.node_id = host_merge;
        supporting_nodes = new ArrayList<>();
        termination_points = new ArrayList<>();
        attachmentPoints = new ArrayList<>();
        addresses = new ArrayList<>();
    }

    public void refresh(){
        this.node_id = host_name + macs.get(0);
        supporting_nodes.clear();
        termination_points.clear();
        attachmentPoints.clear();
        addresses.clear();
    }

    private static int id = 0;

    synchronized private int getId(){
        int allocate = id;
        id++;
        return allocate;
    }

    public void setTermination_points(String tp){
        this.termination_points.add(tp);
    }

    public void setAddress(String ip, String mac){
        Address address = new Address(getId(), mac, ip);
        this.addresses.add(address);
    }

    public void setAttachmentPoint(String tpId, String correspondingTp){
        AttachmentPoint attachmentPoint = new AttachmentPoint(tpId, correspondingTp, true);
        this.attachmentPoints.add(attachmentPoint);
    }

    public JSONObject getJSONObject(){
        JSONObject node = new JSONObject();
        node.put("node-id", node_id);
        node.put("node-type", "device");

        JSONArray terminationPoints = new JSONArray();
        for (String str: termination_points){
            JSONObject object = new JSONObject();
            object.put("tp-id", str);
            terminationPoints.add(object);
        }
        node.put("termination-point", terminationPoints);

        JSONArray attachmentPoints = new JSONArray();
        for(AttachmentPoint attachmentPoint: this.attachmentPoints){
            attachmentPoints.add(attachmentPoint.getJSONObject());
        }
        node.put("attachment-points", attachmentPoints);

        JSONArray addresses = new JSONArray();
        for(Address address: this.addresses){
            addresses.add(address.getJSONObject());
        }
        node.put("addresses", addresses);

        node.put("id", macs.get(0));
        return node;
    }

    private class AttachmentPoint{
        String tpId, correspondingTp;
        boolean active;

        public AttachmentPoint(String tpId, String correspondingTp, boolean active){
            this.tpId = tpId;
            this.correspondingTp = correspondingTp;
            this.active = active;
        }

        public JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("tp-id", this.tpId);
            object.put("corresponding-tp", this.correspondingTp);
            object.put("active", this.active);
            return object;
        }
    }

    private class Address{
        int id;
        String mac, ip;

        public Address(int id, String mac, String ip){
            this.id = id;
            this.ip = ip;
            this.mac = mac;
        }

        public JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("id", this.id);
            object.put("mac", this.mac);
            object.put("ip", this.ip);
            object.put("first-seen", firstSeen);
            object.put("last-seen", System.currentTimeMillis());
            return object;
        }
    }
}
