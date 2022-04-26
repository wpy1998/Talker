import Hardware.Computer;
import Yang.NetworkTopologyLauncher;
import Yang.Stream.ListenerServer;
import Yang.Stream.TalkerClient;
import Yang.StreamLauncher;

import java.io.IOException;
import java.util.Scanner;

import static Hardware.Computer.*;

public class TSNApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Computer computer = new Computer();
        NetworkTopologyLauncher launcher = NetworkTopologyLauncher.builder()
                .urlFront(computer.urls.get("tsn-topology"))
                .hostName(host_name + device_mac.get(0))
                .topologyId(topology_id)
                .build();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_name + device_mac.get(0))
                .build();

        Scanner scanner = new Scanner(System.in);
        System.out.println("************************************************");
        System.out.println("Please input the number which pattern to start: \n1. Talker\n2. Listener");
        System.out.println("************************************************");
        String input = scanner.next();

        launcher.startTimerThread();
        int pattern = 0;
        if (input.equals("1") || input.equals("talker") || input.equals("Talker")){
            pattern = 1;
            streamLauncher.startPollingThread();
            streamLauncher.registerTalkerStream("talker client message");
        }else if (input.equals("2") || input.equals("listener") || input.equals("Listener")){
            pattern = 2;
            streamLauncher.startListenerServer();
        }else {
            System.out.println("start Test Pattern");
        }
        while (scanner.hasNext()){
            String str = scanner.next();
            if (str.equals("quit") || str.equals("exit") || str.equals("stop")){
                switch (pattern){
                    case 1:
                        streamLauncher.stopPollingThread();
                        break;
                    case 2:
                        streamLauncher.stopListenerServer();
                        break;
                    default:
                        break;
                }
                launcher.stopTimerThread();
                break;
            }
        }
    }
}
