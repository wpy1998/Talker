package ucas.csu.tsn;

import ucas.csu.tsn.Yang.Stream.Header;
import ucas.csu.tsn.Yang.Stream.TalkerClient;
import ucas.csu.tsn.Yang.Topology.NetworkCard;
import ucas.csu.tsn.Hardware.Computer;

import java.util.ArrayList;
import java.util.List;

public class TalkerClientApp {
    public static void main(String[] args) throws Exception {
        String body = "";
        for (int i = 0; i < 1000; i++){
            body = body + "a";
        }
        TalkerClient client = TalkerClient.builder()
                .host("192.168.1.14")
                .port(17835)
                .body(body)
                .build();
        client.start();
    }
}
