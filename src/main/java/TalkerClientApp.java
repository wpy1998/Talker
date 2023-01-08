import Hardware.Computer;
import Yang.Topology.NetworkCard;
import Yang.Stream.Header;
import Yang.Stream.TalkerClient;

public class TalkerClientApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();
        NetworkCard networkCard = computer.getCurrentNetworkCard();

        String body = "123456789012345678901234567890123456789012345678901234567890";
        Header header = Header.builder().uniqueId("00-00")
                .rank((short) 0)
                .networkCard(networkCard)
                .build();
        TalkerClient client = TalkerClient.builder()
                .host("localhost")
                .port(17835)
                .header(header)
                .url(computer.urls.get("tsn-talker") + computer.host_name +
                        networkCard.getMac() + "/stream-list/")
                .body(body)
                .build();
        client.start();
    }
}
