package ucas.csu.tsn;

import ucas.csu.tsn.Yang.Stream.ListenerServer;
import ucas.csu.tsn.Yang.Topology.NetworkCard;
import ucas.csu.tsn.Hardware.Computer;
import ucas.csu.tsn.Yang.StreamLauncher;

import java.io.IOException;
import java.util.Scanner;

public class ListenerServerApp {
    public static void main(String[] args) throws IOException {
        ListenerServer listenerServer = ListenerServer.builder()
                .port(17835)
                .build();
        listenerServer.start();

        Scanner scanner = new Scanner(System.in);
        while (true){
            String str = scanner.next();

            if (str.equals("quit") || str.equals("exit") || str.equals("stop")){
                listenerServer.stop();
                break;
            }
        }
    }
}
