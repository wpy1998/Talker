import Hardware.Computer;
import Yang.StreamLauncher;
import Yang.NetworkTopologyLauncher;

import java.io.IOException;

import static Hardware.Computer.*;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/24/22 1:58 AM
 */
public class TalkerApp {
    public static void main(String[] args) throws IOException, InterruptedException {
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
        command.start(Command.StartType.Talker);
    }
}
