import Hardware.Command;
import Hardware.Computer;
import Yang.StreamLauncher;
import Yang.TopologyLauncher;

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
