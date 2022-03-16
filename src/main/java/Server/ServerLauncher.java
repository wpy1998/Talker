package Server;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:57 PM
 */
public class ServerLauncher {
    public static void main(String[] args) throws InterruptedException {
        ListenerServer server = new ListenerServer(17835);
        server.start();
    }
}
