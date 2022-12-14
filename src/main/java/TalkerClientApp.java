import Hardware.Computer;
import Yang.Stream.Header;
import Yang.Stream.TalkerClient;
import Yang.StreamLauncher;

import java.io.UnsupportedEncodingException;

import static Hardware.Computer.host_merge;

public class TalkerClientApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();

        StreamLauncher streamLauncher = StreamLauncher.builder()
                .talkerFront(computer.urls.get("tsn-talker"))
                .listenerFront(computer.urls.get("tsn-listener"))
                .hostName(host_merge)
                .build();

        String body = "11111111111111111111111111111111111";
        Header header = Header.builder().uniqueId("00-00")
                .rank((short) 0)
                .build();
        TalkerClient client = TalkerClient.builder()
                .host("localhost")
                .port(17835)
                .header(header)
                .url(computer.urls.get("tsn-talker") + host_merge + "/stream-list/")
                .body(body)
                .build();
        client.start();
    }
}
