import Hardware.Computer;
import Yang.ControllerConnect;
import Yang.NetworkTopology.LLDP;

import java.io.IOException;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/24/22 1:58 AM
 */
public class TalkerApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Computer computer = new Computer();
        ControllerConnect controllerConnect = ControllerConnect.builder().lldp(new LLDP()).build();

        //test register and remove Device
//        cucConnect.registerDevice(lldp);
//        cucConnect.removeDevice(lldp);

        //test register and remove stream
//        cucConnect.registerTalkerStream("message1");
//        cucConnect.registerTalkerStream("message2");

        controllerConnect.startListenerServer();
    }
}
