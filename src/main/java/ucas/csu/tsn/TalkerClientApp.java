package ucas.csu.tsn;

import ucas.csu.tsn.Yang.Stream.Header;
import ucas.csu.tsn.Yang.Stream.TalkerClient;
import ucas.csu.tsn.Yang.Topology.NetworkCard;
import ucas.csu.tsn.Hardware.Computer;

import java.util.ArrayList;
import java.util.List;

public class TalkerClientApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        NetworkCard networkCard = computer.getCurrentNetworkCard();

        String body = "";
        for (int i = 0; i < 1000; i++){
            body = body + "a";
        }
        Header header = Header.builder().uniqueId("00-00")
                .rank((short) 0)
                .networkCard(networkCard)
                .build();
        TalkerClient client = TalkerClient.builder()
                .host("192.168.1.15")
                .port(17835)
                .header(header)
                .url(computer.urls.get("tsn-talker") + networkCard.getMac()
                        .replace(":", "-") + "/stream-list/")
                .body(body)
                .isRegister(false)
                .build();
        client.start();
    }
}
