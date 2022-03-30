import Hardware.Computer;
import Yang.StreamLauncher;
import Yang.NetworkTopologyLauncher;

import static Hardware.Computer.host_name;
import static Hardware.Computer.topology_id;

public class ListenerApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        NetworkTopologyLauncher launcher = NetworkTopologyLauncher.builder()
                .topologyFront(computer.urls.get("tsn-topology"))
                .hostName(host_name)
                .topologyId(topology_id)
                .build();
        launcher.startTimerThread();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_name)
                .build();
        streamLauncher.startListenerServer();
    }
}