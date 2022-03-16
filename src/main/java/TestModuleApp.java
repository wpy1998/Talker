import Hardware.Computer;
import Yang.CUCConnect;
import Yang.NetworkTopology.LLDP;
import org.xml.sax.SAXException;

import java.io.IOException;

public class TestModuleApp {
    public static final String cuc_ip = "10.2.25.85";
    public static final String topology_id = "tsn-network";
    public static void main(String[] args) throws IOException, SAXException {
        Computer computer = new Computer();
        LLDP lldp = new LLDP();
        CUCConnect cucConnect = new CUCConnect();

        //test register and remove Device
        cucConnect.registerDevice(lldp);
//        cucConnect.removeDevice(lldp);

        //test register and remove stream
//        cucConnect.registerAndSendStream("message1");
//        cucConnect.registerAndSendStream("message2");


//        GetInfo getInfo = GetInfo.builder().url("http://" + cuc_ip +
//                ":8181/restconf/operations/tsn-talker-type:test").build();
//        getInfo.getInfo();

    }
}