import Hardware.Computer;
import Yang.StreamLauncher;
import Yang.TopologyLauncher;

import java.net.UnknownHostException;
import java.util.Scanner;

public class TestApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        TopologyLauncher topologyLauncher = new TopologyLauncher(computer);
        topologyLauncher.startTimerThread();
        StreamLauncher streamLauncher = new StreamLauncher(computer);
        streamLauncher.startPollingThread();
        streamLauncher.registerTalkerStream("aaaaa", computer.getNetworkCards().get(0));
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.next();
            if (str.equals("exit")){
                streamLauncher.stopStreamLauncher();
                topologyLauncher.stopTimerThread();
                break;
            }
        }
    }
}
