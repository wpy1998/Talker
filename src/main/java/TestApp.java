import Hardware.Computer;
import HttpInfo.GetInfo;
import Yang.CUCConnect;
import Yang.NetworkTopology.LLDP;
import org.xml.sax.SAXException;

import java.io.IOException;

public class TestApp {
//    public static final String cuc_ip = "10.2.25.85";
//    public static final String topology_id = "tsn-network";
    public static void main(String[] args) throws IOException, SAXException {
        Computer computer = new Computer();
        LLDP lldp = new LLDP();
        CUCConnect cucConnect = new CUCConnect();

        //test register and remove Device
//        cucConnect.registerDevice(lldp);
//        cucConnect.removeDevice(lldp);

        //test register and remove stream
//        cucConnect.registerTalkerStream("message1");
//        cucConnect.registerTalkerStream("message2");

        cucConnect.registerListener("register Listener");
    }
}