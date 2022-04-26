import Hardware.Computer;
import Yang.StreamLauncher;

import static Hardware.Computer.host_name;
import static Hardware.Computer.macs;

/**
 * @author : wpy
 * @description: TODO
 * @date : 4/26/22 1:12 AM
 */
public class TestApp {
    public static void main(String[] args) {
        Computer computer = new Computer();
        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_name + macs.get(0))
                .build();
        streamLauncher.startPollingThread();
        streamLauncher.registerTalkerStream("talker client message");
    }
}
