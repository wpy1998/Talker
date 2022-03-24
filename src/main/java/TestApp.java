import Hardware.Computer;
import Yang.CUCConnect;
import Yang.NetworkTopology.LLDP;

public class TestApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        LLDP lldp = new LLDP();
        CUCConnect cucConnect = new CUCConnect();

        //test register and remove Device
//        cucConnect.registerDevice(lldp);
//        cucConnect.removeDevice(lldp);

        //test register and remove stream
//        cucConnect.registerTalkerStream("message1");
//        cucConnect.registerTalkerStream("message2");

        cucConnect.startListenerServer();
    }
}