import Hardware.Command;
import Hardware.Computer;
import Yang.NetworkTopologyLauncher;
import Yang.StreamLauncher;

import java.io.IOException;

import static Hardware.Computer.*;

public class TSNApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Computer computer = new Computer();
        NetworkTopologyLauncher networkTopologyLauncher = NetworkTopologyLauncher.builder()
                .urlFront(computer.urls.get("tsn-topology"))
                .hostName(host_merge)
                .topologyId(topology_id)
                .build();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_merge)
                .build();

        Command command = Command.builder().topologyLauncher(networkTopologyLauncher)
                .streamLauncher(streamLauncher).build();
        command.start();
    }
}
