import Yang.Network.LLDP2;

import java.io.IOException;

public class TestApp {
    public static void main(String[] args) throws IOException {
        LLDP2 LLDP2 = new LLDP2();
        LLDP2.getLocalInterface();
    }
}
