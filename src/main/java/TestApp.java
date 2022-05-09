import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class TestApp {
    public static void main(String[] args) throws IOException {
        Process process = Runtime.getRuntime().exec("lldpcli show neighbors ports " +
                "ens33 -f json");
        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader (ir);
        String line, result = "";
        while ((line = input.readLine ()) != null){
            result += line;
        }
        JSONObject object = JSON.parseObject(result).getJSONObject("lldp");
        System.out.println(object.toString());
    }
}
