import Entity.NetworkTopology;
import Hardware.HardwareMessage;
import HttpInfo.GetInfo;
import HttpInfo.PostInfo;
import Entity.StreamHeader;
import HttpInfo.PutInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException{
        HardwareMessage hdMessage = new HardwareMessage();
        hdMessage.refresh();
        System.out.println(hdMessage.device_ip + ", " + hdMessage.device_mac);
        String destionation_ip = "192.168.1.16";
//        String url = "http://" + destionation_ip + ":8181/restconf/operations/talker:";
//
//        PostInfo postInfo = new PostInfo(url + "join");
//        StreamHeader header = new StreamHeader();
//        JSONObject stream_header = header.getJSONObject(false, true,
//                false, false, false,
//                false, false);
//        JSONObject stream = new JSONObject();
//        JSONObject input = new JSONObject();
//        stream.put("header", stream_header);
//        stream.put("body", "test join");
//        input.put("input", stream);
//        postInfo.postInfo(input.toString());
//
//        GetInfo getInfo = new GetInfo(url + "test");
//        getInfo.getInfo();
//
//        PostInfo postInfo1 = new PostInfo(url + "leave");
//        postInfo1.postInfo(input.toString());

        NetworkTopology networkTopology = new NetworkTopology();
        JSONArray topologies = networkTopology.buildTopologies();
        JSONObject network = new JSONObject();
        network.put("topology", topologies);
        System.out.println(network.toString());
        PutInfo putInfo = new PutInfo("http://" + destionation_ip + ":8181/restconf/config/network-topology:network-topology");
        putInfo.putInfo(network.toString());
    }
}
