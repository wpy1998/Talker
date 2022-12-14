import Hardware.Computer;
import Yang.Stream.Header;
import Yang.Stream.TalkerClient;
import Yang.StreamLauncher;

import java.io.UnsupportedEncodingException;

import static Hardware.Computer.host_merge;

public class TalkerClientApp {
    public static void main(String[] args) throws Exception {
        Computer computer = new Computer();

        String body = "123456789012345678901234567890123456789012345678901234567890";
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
