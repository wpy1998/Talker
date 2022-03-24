import Hardware.Computer;
import Yang.ControllerConnect;
import Yang.NetworkTopology.LLDP;

public class ListenerApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        LLDP lldp = new LLDP();
        ControllerConnect controllerConnect = new ControllerConnect();

        controllerConnect.startListenerServer();
    }
}