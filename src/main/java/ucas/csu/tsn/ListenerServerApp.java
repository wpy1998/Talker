package ucas.csu.tsn;

import ucas.csu.tsn.Yang.Topology.NetworkCard;
import ucas.csu.tsn.Hardware.Computer;
import ucas.csu.tsn.Yang.StreamLauncher;

import java.io.IOException;
import java.util.Scanner;

public class ListenerServerApp {
    public static void main(String[] args) throws IOException {
        Computer computer = new Computer();
        NetworkCard networkCard = computer.getCurrentNetworkCard();

        StreamLauncher streamLauncher = new StreamLauncher(computer);
        streamLauncher.startListenerServer(networkCard);

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
