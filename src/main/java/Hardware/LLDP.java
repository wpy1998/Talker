package Hardware;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LLDP {

    public LLDP(){
    }

    public JSONObject getLocalInterface(){
        return null;
    }

    protected List<String> runCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        InputStream in = process.getInputStream();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "gbk"));
        String line;
        List<String> terminals = new ArrayList<>();
        while ((line = br.readLine ()) != null){
            if (line.length() != 0){
                terminals.add(line);
            }
        }
        return terminals;
    }

    protected String clearBrackets(String content){
        String result = "";
        int flag = 0;
        for (int i = 0; i < content.length(); i++){
            if (content.charAt(i) == '(' || content.charAt(i) == '<'){
                flag++;
            }else if (content.charAt(i) == ')' || content.charAt(i) == '>'){
                flag--;
            }else if (flag == 0){
                result += content.charAt(i);
            }
        }
        return result;
    }
}
