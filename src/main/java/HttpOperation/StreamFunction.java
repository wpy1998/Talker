package HttpOperation;

import StreamHeader.StreamHeader;
import com.alibaba.fastjson.JSONObject;

public class StreamFunction {//join, stream, leave
    public void join(String url, String body){
        StreamHeader header = new StreamHeader();
        JSONObject stream_header = header.getJSONObject(false, true,
                false, false, false,
                false, false);
        JSONObject stream = new JSONObject();
        JSONObject input = new JSONObject();
        stream.put("header", stream_header);
        stream.put("body", body);
        input.put("input", stream);
        HttpClient httpClient = new HttpClient();
        httpClient.sendPost(url + "join", input.toString());
    }

    public void stream(String url){
        HttpClient httpClient = new HttpClient();
        JSONObject input = new JSONObject();
        JSONObject name = new JSONObject();
        name.put("name", "SDN");
        input.put("input", name);
        httpClient.sendPost(url + "stream", input.toString());
    }

    public void leave(String url, String body){
        StreamHeader header = new StreamHeader();
        JSONObject stream_header = header.getJSONObject(false, false,
                false, false, false,
                false, false);
        JSONObject stream = new JSONObject();
        JSONObject input = new JSONObject();
        stream.put("header", stream_header);
        stream.put("body", body);
        input.put("input", stream);
        HttpClient httpClient = new HttpClient();
        httpClient.sendPost(url + "leave", input.toString());
    }

    public void test(String url){
        HttpClient httpClient = new HttpClient();
        httpClient.sendPost(url + "test", null);
    }
}
