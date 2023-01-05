import Yang.Network.LLDPs;

import java.io.IOException;

public class TestApp {
    public static void main(String[] args) throws IOException {
        LLDPs lldPs = new LLDPs();
        lldPs.getLocalInterface();
    }
}
