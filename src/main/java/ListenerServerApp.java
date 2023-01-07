import Hardware.Computer;
import Yang.Network.NetworkCard;
import Yang.StreamLauncher;

import java.io.IOException;
import java.util.Scanner;

public class ListenerServerApp {
    public static void main(String[] args) throws IOException {
        Computer computer = new Computer();
        NetworkCard networkCard = computer.getNetworkCards().get(0);

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
