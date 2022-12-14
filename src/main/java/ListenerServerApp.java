import Hardware.Computer;
import Yang.StreamLauncher;

import java.io.IOException;
import java.util.Scanner;

import static Hardware.Computer.host_merge;

public class ListenerServerApp {
    public static void main(String[] args) throws IOException {
        Computer computer = new Computer();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_merge)
                .build();
        streamLauncher.startListenerServer();

        Scanner scanner = new Scanner(System.in);
        while (true){
            String str = scanner.next();

            if (str.equals("quit") || str.equals("exit") || str.equals("stop")){
                streamLauncher.stopListenerServer();
                break;
            }
        }

    }
}
