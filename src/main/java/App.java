import Entity.NetworkTopology;
import Hardware.Computer;
import HttpInfo.PutInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.juniper.netconf.Device;
import net.juniper.netconf.XML;
import org.xml.sax.SAXException;

import java.io.IOException;

import static Hardware.Computer.host_name;

public class App {
    public static void main(String[] args) throws IOException, SAXException {
        Computer computer = new Computer();
        computer.refresh();
        System.out.println(computer.device_ip + ", " + computer.device_mac);
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
        PutInfo putInfo = PutInfo.builder()
                .url("http://" + destionation_ip + ":8181/restconf/config/network-topology:network-topology").build();
        putInfo.putInfo(network.toString());

        Device device = Device.builder()
                .hostName(host_name)
                .userName("admin")
                .password("admin")
                .hostKeysFileName("hostKeysFileName")
                .build();
        device.connect();

        //Send RPC and receive RPC Reply as XML
        XML rpc_reply = device.executeRPC("get-interface-information");
        /* OR
         * device.executeRPC("<get-interface-information/>");
         * OR
         * device.executeRPC("<rpc><get-interface-information/></rpc>");
         */

        //Print the RPC-Reply and close the device.
        System.out.println(rpc_reply);
        device.close();
    }
}
