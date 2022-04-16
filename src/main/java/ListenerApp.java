import Hardware.Computer;
import Yang.StreamLauncher;
import Yang.NetworkTopologyLauncher;

import static Hardware.Computer.*;

public class ListenerApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        NetworkTopologyLauncher launcher = NetworkTopologyLauncher.builder()
                .topologyFront(computer.urls.get("tsn-topology"))
                .hostName(host_name + device_mac.get(0))
                .topologyId(topology_id)
                .build();
        launcher.startTimerThread();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_name + device_mac.get(0))
                .build();
        streamLauncher.startListenerServer();
    }
}