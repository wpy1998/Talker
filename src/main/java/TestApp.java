import Hardware.Computer;
import Yang.ControllerConnect;
import Yang.NetworkTopology.LLDP;

import java.io.IOException;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/24/22 4:32 AM
 */
public class TestApp {
    public static void main(String[] args) throws IOException {
        Computer computer = new Computer();
        ControllerConnect controllerConnect = ControllerConnect.builder().lldp(new LLDP()).build();
        ControllerConnect controllerConnect1 = ControllerConnect.builder().lldp(new LLDP()).build();
    }
}
