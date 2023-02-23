package ucas.csu.tsn;

import ucas.csu.tsn.Hardware.Command;
import ucas.csu.tsn.Hardware.Computer;
import ucas.csu.tsn.Yang.StreamLauncher;
import ucas.csu.tsn.Yang.TopologyLauncher;

public class TSNApp {
    public static void main(String[] args) throws Exception{
        Computer computer = new Computer();
        TopologyLauncher topologyLauncher = new TopologyLauncher(computer);
        StreamLauncher streamLauncher = new StreamLauncher(computer);

        Command command = Command.builder()
                .topologyLauncher(topologyLauncher)
                .streamLauncher(streamLauncher)
                .computer(computer)
                .build();
        command.start();
    }
}
