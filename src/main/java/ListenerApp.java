import Hardware.Computer;
import Yang.StreamLauncher;
import Yang.NetworkTopologyLauncher;

import static Hardware.Computer.*;

public class ListenerApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        NetworkTopologyLauncher networkTopologyLauncher = NetworkTopologyLauncher.builder()
                .urlFront(computer.urls.get("tsn-topology"))
                .hostName(host_name + macs.get(0))
                .topologyId(topology_id)
                .build();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_name + macs.get(0))
                .build();

        Command command = Command.builder().topologyLauncher(networkTopologyLauncher)
                .streamLauncher(streamLauncher).build();
        command.start(Command.StartType.Listener);
    }
}