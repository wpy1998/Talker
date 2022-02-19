import Hardware.HardwareMessage;
import HttpOperation.StreamFunction;
import net.i2cat.netconf.NetconfSession;
import net.i2cat.netconf.SessionContext;
import net.i2cat.netconf.errors.NetconfProtocolException;
import net.i2cat.netconf.errors.TransportException;
import net.i2cat.netconf.errors.TransportNotRegisteredException;

import java.io.IOException;
import java.net.URI;

public class App {
    public static void main(String[] args) throws IOException, TransportNotRegisteredException, NetconfProtocolException, TransportException {
        HardwareMessage hdMessage = new HardwareMessage();
        hdMessage.refresh();
        System.out.println(hdMessage.device_ip + ", " + hdMessage.device_mac);
        String url = "http://192.168.1.3:8181/restconf/operations/talker:";
        StreamFunction streamFunction = new StreamFunction();
        streamFunction.join(url, "hello join");
        streamFunction.test(url);
    }
}
