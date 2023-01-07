import Hardware.Computer;
import Hardware.LLDP;

import java.io.IOException;

public class TestApp {
    public static void main(String[] args) {
//        Computer computer = new Computer();
        LLDP lldp = new LLDP();
        try {
            lldp.runCommand("tcpdump -i ens33 -nev ether proto 0x88cc -c 1", true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
