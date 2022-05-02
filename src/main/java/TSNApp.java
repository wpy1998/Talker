import Hardware.Command;
import Hardware.Computer;
import Yang.NetworkLauncher;
import Yang.StreamLauncher;

import java.io.IOException;

import static Hardware.Computer.*;

public class TSNApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Computer computer = new Computer();
        NetworkLauncher networkLauncher = NetworkLauncher.builder()
                .urlFront(computer.urls.get("tsn-topology"))
                .hostName(host_merge)
                .topologyId(topology_id)
                .build();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_merge)
                .build();

        Command command = Command.builder().topologyLauncher(networkLauncher)
                .streamLauncher(streamLauncher).build();
        command.start();
    }
}
