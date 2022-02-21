import Hardware.HardwareMessage;
import HttpInfo.GetInfo;
import HttpInfo.PostInfo;
import StreamHeader.StreamHeader;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException{
        HardwareMessage hdMessage = new HardwareMessage();
        hdMessage.refresh();
        System.out.println(hdMessage.device_ip + ", " + hdMessage.device_mac);
        String url = "http://10.2.25.85:8181/restconf/operations/talker:";

        PostInfo postInfo = new PostInfo(url + "join");
        StreamHeader header = new StreamHeader();
        JSONObject stream_header = header.getJSONObject(false, true,
                false, false, false,
                false, false);
        JSONObject stream = new JSONObject();
        JSONObject input = new JSONObject();
        stream.put("header", stream_header);
        stream.put("body", "test join");
        input.put("input", stream);
        postInfo.postInfo(input.toString());

        GetInfo getInfo = new GetInfo(url + "test");
        getInfo.getInfo();

        PostInfo postInfo1 = new PostInfo(url + "leave");
        postInfo1.postInfo(input.toString());
    }
}
