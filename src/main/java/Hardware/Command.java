package Hardware;

import Yang.NetworkTopologyLauncher;
import Yang.StreamLauncher;
import lombok.Builder;
import lombok.NonNull;

import java.util.Scanner;

public class Command {
    private NetworkTopologyLauncher topologyLauncher;
    private StreamLauncher streamLauncher;

    @Builder
    public Command(@NonNull NetworkTopologyLauncher topologyLauncher,
                   @NonNull StreamLauncher streamLauncher){
        this.topologyLauncher = topologyLauncher;
        this.streamLauncher = streamLauncher;
    }

    public void start() throws InterruptedException {
        int pattern  = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("************************************************");
        System.out.println("Please input the number which pattern to start: \n" +
                "1. Talker\n2. Listener");
        System.out.println("************************************************");

        String input = scanner.next();
        if (input.equals("1") || input.equals("talker") || input.equals("Talker")){
            pattern = 1;
        }else if (input.equals("2") || input.equals("listener") ||
                input.equals("Listener")){
            pattern = 2;
        }

        topologyLauncher.startTimerThread();
        if (pattern == 1){
            System.out.println("--start Talker Pattern--");
            streamLauncher.startPollingThread();
            streamLauncher.registerTalkerStream("talker client message");
        }else if (pattern == 2){
            System.out.println("--start Listener Pattern--");
            streamLauncher.startListenerServer();
        }else {
            System.out.println("--start Test Pattern--");
        }

        while (scanner.hasNext()){
            String str = scanner.next();

            if (str.equals("quit") || str.equals("exit") || str.equals("stop")){
                streamLauncher.stopStreamLauncher();
                topologyLauncher.stopTimerThread();
                break;
            }else{
                switch (pattern){
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
