package Client.Yang;

import Client.HttpInfo.DeleteInfo;
import Client.HttpInfo.PutInfo;
import Client.Yang.Stream.Header;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static Client.ClientApp.cuc_ip;

public class CUCConnect {
    Map<String, String> urls;
    public CUCConnect(){
        urls = new HashMap<>();
        urls.put("join", "http://" + cuc_ip + ":8181/restconf/config/talker:streams");
        urls.put("leave", "http://" + cuc_ip + ":8181/restconf/config/talker:streams/stream/");
    }

    //topology connect 以下参数,函数仅在操作network topology config库时使用

    //talker: header, body 以下参数,函数仅在操作talker config库时使用
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

    public int registerAndSendStream(String body){
        int uniqueId = getUniqueId();
        Header header = Header.builder().uniqueId(convertUniqueID(uniqueId))
                .rank((short) 0)
                .build();
        int resultCode;

        resultCode = join(header);
        if(resultCode < 200 || resultCode > 300){
            throw new RuntimeException("ResultCode Error in join stream action: " + resultCode);
        }
        resultCode = stream(body);
        if(resultCode < 200 || resultCode > 300){
            throw new RuntimeException("ResultCode Error in post stream to destination: " + resultCode);
        }
        resultCode = leave(header);
        if(resultCode < 200 || resultCode > 300){
            throw new RuntimeException("ResultCode Error in leave stream action: " + resultCode);
        }
        return resultCode;
    }

    private int join(Header header){
        String url = urls.get("join");
        System.out.println(url);
        PutInfo putInfo = PutInfo.builder().url(url).build();

        JSONObject joinStream = header.getJSONObject(true, true, true,
                true, true, true,
                true);
        joinStream.put("body", "join stream");
        JSONArray streams = new JSONArray();
        streams.add(joinStream);
        JSONObject middle = new JSONObject();
        middle.put("stream", streams);
        JSONObject result = new JSONObject();
        result.put("streams", middle);
        return putInfo.putInfo(result.toString());
    }

    private int stream(String body){
        return 200;
    }

    private int leave(Header header){
        String url = urls.get("leave");
        System.out.println(url + header.getKey());
        DeleteInfo deleteInfo = DeleteInfo.builder().url(url + header.getKey()).build();

        JSONObject leaveHeader = header.getJSONObject(true, true, true,
                true, true, true,
                true);
        leaveHeader.put("body", "leave stream");
        JSONObject stream = new JSONObject();
        stream.put("stream", leaveHeader);
        return deleteInfo.deleteInfo();
    }

    //listener 以下参数,函数仅在操作listener config库时使用

    //status 以下参数,函数仅在操作status config库时使用
}
