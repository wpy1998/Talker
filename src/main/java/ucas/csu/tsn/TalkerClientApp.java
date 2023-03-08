package ucas.csu.tsn;

import ucas.csu.tsn.Yang.Stream.Header;
import ucas.csu.tsn.Yang.Stream.TalkerClient;
import ucas.csu.tsn.Yang.Topology.NetworkCard;
import ucas.csu.tsn.Hardware.Computer;

public class TalkerClientApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        NetworkCard networkCard = computer.getCurrentNetworkCard();

        String body = "123456789012345678901234567890123456789012345678901234567890", result = "";
        for (int i = 0; i < 35; i++){
            result = body + result;
        }
        Header header = Header.builder().uniqueId("00-00")
                .rank((short) 0)
                .networkCard(networkCard)
                .build();
        TalkerClient client = TalkerClient.builder()
                .host(computer.cuc_ip)
                .port(17835)
                .header(header)
                .url(computer.urls.get("tsn-talker") + networkCard.getMac()
                        .replace(":", "-") + "/stream-list/")
                .body(result)
                .build();
        client.start();
    }
}
