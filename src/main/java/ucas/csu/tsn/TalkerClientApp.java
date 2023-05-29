package ucas.csu.tsn;

import ucas.csu.tsn.Yang.Stream.Header;
import ucas.csu.tsn.Yang.Stream.TalkerClient;
import ucas.csu.tsn.Yang.Topology.NetworkCard;
import ucas.csu.tsn.Hardware.Computer;

import java.util.ArrayList;
import java.util.List;

public class TalkerClientApp {
    public static void main(String[] args) throws Exception {
//        String k_body = "", m_body = "", g_body = "";
//        for (int i = 0; i < 1000; i++){
//            k_body = k_body + "a";
//        }
//        for (int i = 0; i < 1000; i++){
//            m_body = m_body + k_body;
//        }
//        String m10_body = m_body + m_body + m_body + m_body + m_body + m_body +
//                m_body + m_body + m_body + m_body;
//        for (int i = 0; i < 30; i++){
//            g_body = g_body + m10_body;
//            System.out.println(i * 10 + " MB");
//        }
//        System.out.println("generate Data over!");
        String g_body = "132456789ffasagasdsda";
        TalkerClient client = TalkerClient.builder()
                .host("localhost")
                .port(17835)
                .body(g_body)
                .build();
        client.start();
    }
}
