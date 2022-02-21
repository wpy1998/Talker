import Hardware.HardwareMessage;
import HttpOperation.StreamFunction;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException{
        HardwareMessage hdMessage = new HardwareMessage();
        hdMessage.refresh();
        System.out.println(hdMessage.device_ip + ", " + hdMessage.device_mac);
        String url = "http://10.2.25.85:8181/restconf/operations/talker:";
        StreamFunction streamFunction = new StreamFunction();
        streamFunction.join(url, "hello join");
        streamFunction.test(url);
    }
}
