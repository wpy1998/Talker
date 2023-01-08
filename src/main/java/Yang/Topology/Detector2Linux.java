package Yang.Topology;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Detector2Linux extends Detector {
    private final String firstLinuxCommand = "ifconfig -a", secondLinuxCommand = "ethtool ",
            thirdLinuxCommandF = "tcpdump -i ", thirdLinuxCommandS = " -nev ether proto 0x88cc -c 1";

    public Detector2Linux(){
    }

    //chcp 437 change to english
    //chcp 936 change to chinese
    @Override
    public JSONObject getLocalInterface(){
        JSONObject networkCards = null;
        try {
            List<String> terminals;
            terminals = runCommand(firstLinuxCommand);
            networkCards = extractNetworkCard(terminals);
        }catch (IOException e){
            e.printStackTrace();
        }
        return networkCards;
    }

    //base on ifconfig
    private JSONObject extractNetworkCard(List<String> terminals) throws IOException {
        JSONObject origin = new JSONObject(), midObject = null;
        //get all network card
        for (String terminal: terminals){
            String midString = clearBrackets(terminal);
            List<String> elements = new ArrayList<>();
            String[] temp = midString.split(" ");
            for (int i = 0; i < temp.length; i++){
                if (temp[i].length() != 0){
                    elements.add(temp[i]);
                }
            }
            if (terminal.charAt(0) != ' '){
                JSONObject object = new JSONObject();
                String name = elements.get(0).replace(":", "");
                origin.put(name, object);
                String flag = elements.get(1);
                String midList[] = flag.split("=");
                object.put(midList[0], midList[1]);
                for (int i = 2; i < elements.size(); i = i + 2){
                    object.put(elements.get(i), elements.get(i + 1));
                }
                midObject = object;
            }else {
                if (elements.size() % 2 == 0){
                    for (int i = 0; i < elements.size(); i = i + 2){
                        midObject.put(elements.get(i), elements.get(i + 1));
                    }
                }else {
                    JSONObject xObject;
                    if (midObject.get(elements.get(0)) != null){
                        xObject = midObject.getJSONObject(elements.get(0));
                    }else {
                        xObject = new JSONObject();
                        midObject.put(elements.get(0), xObject);
                    }
                    for (int i = 1; i < elements.size(); i = i + 2){
                        xObject.put(elements.get(i), elements.get(i + 1));
                    }
                }
            }
        }

        //remove network card of no link and get sending speed of other network card
        midObject = new JSONObject();
        for (String key: origin.keySet()){
            if (!key.equals("lo") && origin.getJSONObject(key).getString("scopeid") != null){
                JSONObject object = origin.getJSONObject(key);
                List<String> secondTerminals = runCommand(secondLinuxCommand + key);
                JSONObject ethtool = extractLinuxEthTool(secondTerminals);
                if (ethtool.getString("Speed") == null) continue;
                object.put("ethtool", ethtool);
                midObject.put(key, object);
            }
        }
        origin = midObject;
        return origin;
    }

    //base on ethtool
    private JSONObject extractLinuxEthTool(List<String> terminals){
        JSONObject origin = new JSONObject();
        for (int i = 1; i < terminals.size(); i++){
            String midString = terminals.get(i).replace("\t", "");
            midString = midString.replace(" ", "");
            String temp[] = midString.split(":");
            if (temp.length % 2 != 0) continue;
            for (int j = 0; j < temp.length; j += 2){
                origin.put(temp[j], temp[j + 1]);
            }
        }
        return origin;
    }
}