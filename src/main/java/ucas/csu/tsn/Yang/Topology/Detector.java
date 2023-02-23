package ucas.csu.tsn.Yang.Topology;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Detector {

    public Detector(){
    }

    public JSONObject getLocalInterface(){
        return null;
    }

    public List<String> runCommand(String command) throws IOException {
        return runCommand(command, false);
    }

    public List<String> runCommand(String command, boolean isDebug) throws IOException {
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
            if (isDebug){
                System.out.println(line);
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
