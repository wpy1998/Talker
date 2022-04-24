import Hardware.Computer;
import Yang.NetworkTopology.LLDP;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

/**
 * @author : wpy
 * @description: TODO
 * @date : 4/24/22 5:38 AM
 */
public class TestApp {
    public static void main(String[] args) throws IOException {
        Computer computer = new Computer();
        LLDP lldp = new LLDP();
        JSONObject speed = lldp.getSpeed("10.2.25.225");
        System.out.println(speed.toString());
    }
}
