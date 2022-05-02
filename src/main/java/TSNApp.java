import Hardware.Command;
import Hardware.Computer;
import Yang.TopologyLauncher;
import Yang.StreamLauncher;

import java.io.IOException;

import static Hardware.Computer.*;

public class TSNApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Computer computer = new Computer();
        TopologyLauncher topologyLauncher = TopologyLauncher.builder()
                .urlFront(computer.urls.get("tsn-topology"))
                .hostName(host_merge)
                .topologyId(topology_id)
                .build();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_merge)
                .build();

        Command command = Command.builder().topologyLauncher(topologyLauncher)
                .streamLauncher(streamLauncher).build();
        command.start();
    }
}
