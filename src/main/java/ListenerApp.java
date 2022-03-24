import Hardware.Computer;
import Yang.ControllerConnect;
import Yang.NetworkTopology.LLDP;

public class ListenerApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        ControllerConnect controllerConnect = ControllerConnect.builder().lldp(new LLDP()).build();

        controllerConnect.startListenerServer();
    }
}