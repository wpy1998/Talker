package Client.Yang;

import Client.HttpInfo.PostInfo;
import Client.Yang.Talker.Header;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static Client.ClientApp.cuc_ip;

public class CUCConnect {
    public CUCConnect(){
        talkerUrl = new HashMap<>();
        talkerUrl.put("join", "http://" + cuc_ip + ":8181/restconf/operations/talker:join");
        talkerUrl.put("leave", "http://" + cuc_ip + ":8181/restconf/operations/talker:leave");
    }
    //talker: header, body
    private static int unique_id = 0;

    synchronized private int getUniqueId(){
        int current = unique_id;
        unique_id++;
        return current;
    }

    private String convertUniqueID(int uniqueId){
        int front, next;
        next = unique_id % 100;
        front = unique_id % 10000 - next;
        String s1, s2;
        s1 = String.valueOf(front);
        s2 = String.valueOf(next);
        if (s1.length() == 1){
            s1 = "0" + s1;
        }
        if (s2.length() == 1){
            s2 = "0" + s2;
        }
        return s1 + "-" + s2;
    }

    Map<String, String> talkerUrl;
    public void registerAndSendStream(String body){
        int uniqueId = getUniqueId();
        Header header = Header.builder().uniqueId(convertUniqueID(uniqueId))
                .rank((short) 0)
                .build();
        join(header);
    }

    private int join(Header header){
        String url = talkerUrl.get("join");
        System.out.println(url);
        PostInfo postInfo = PostInfo.builder().url(url).build();

        JSONObject joinHeader = header.getJSONObject(true, true, true,
                true, true, true,
                true);
        JSONObject stream = new JSONObject();
        JSONObject input = new JSONObject();
        stream.put("header", joinHeader);
        stream.put("body", "join stream");
        input.put("input", stream);
        return postInfo.postInfo(input.toString());
    }
}
