import Hardware.Computer;
import Yang.StreamLauncher;
import Yang.NetworkTopologyLauncher;

import java.io.IOException;
import java.util.Scanner;

import static Hardware.Computer.*;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/24/22 1:58 AM
 */
public class TalkerApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Computer computer = new Computer();
        NetworkTopologyLauncher launcher = NetworkTopologyLauncher.builder()
                .topologyId(computer.urls.get("tsn-topology"))
                .hostName(host_name + device_mac.get(0))
                .topologyId(topology_id)
                .build();
        launcher.startTimerThread();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_name + device_mac.get(0))
                .build();
        streamLauncher.startPollingThread();
        streamLauncher.registerTalkerStream("aaa");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.next();
            if (str.equals("quit") || str.equals("exit") || str.equals("stop")){
                streamLauncher.stopPollingThread();
                launcher.stopTimerThread();
                break;
            }
        }

        //test register and remove Device
//        cucConnect.registerDevice(lldp);
//        cucConnect.removeDevice(lldp);

        //test register and remove stream
//        cucConnect.registerTalkerStream("message1");
//        cucConnect.registerTalkerStream("message2");
    }
}
