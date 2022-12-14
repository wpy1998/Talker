package Hardware;

import Yang.TopologyLauncher;
import Yang.StreamLauncher;
import lombok.Builder;
import lombok.NonNull;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Command {
    private TopologyLauncher topologyLauncher;
    private StreamLauncher streamLauncher;

    @Builder
    public Command(@NonNull TopologyLauncher topologyLauncher,
                   @NonNull StreamLauncher streamLauncher){
        this.topologyLauncher = topologyLauncher;
        this.streamLauncher = streamLauncher;
    }

    public void start() throws Exception {
        int pattern  = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("*****************************************************************");
        System.out.println("Please input the number which pattern to start: \n" +
                "1. Talker\n2. Listener\n3. Both");
        System.out.println("*****************************************************************");

        String input = scanner.next();
        if (input.equals("1") || input.equals("talker") || input.equals("Talker")){
            pattern = 1;
        }else if (input.equals("2") || input.equals("listener") ||
                input.equals("Listener")){
            pattern = 2;
        } else if (input.equals("3") || input.equals("both") || input.equals("Both")) {
            pattern = 3;
        }

        topologyLauncher.startTimerThread();
        if (pattern == 1){
            System.out.println("--Start Talker Client--");
            streamLauncher.startPollingThread();
            streamLauncher.registerTalkerStream("talker client message");
        }else if (pattern == 2){
            System.out.println("--Start Listener Server--");
            streamLauncher.startListenerServer();
        }else if(pattern == 3){
            System.out.println("--Start Talker Client and Listener Server--");
            streamLauncher.startListenerServer();
            streamLauncher.registerTalkerStream("talker client message");
        }else {
            System.out.println("--Start Test Pattern--");
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
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
