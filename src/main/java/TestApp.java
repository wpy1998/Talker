import Yang.Network.LLDP2;

import java.io.IOException;

public class TestApp {
    public static void main(String[] args) throws IOException {
        LLDP2 lldp2 = new LLDP2();
        lldp2.getLocalInterface();
    }
}
