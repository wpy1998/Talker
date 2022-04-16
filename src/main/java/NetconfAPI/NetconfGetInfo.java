package NetconfAPI;

import lombok.Builder;
import lombok.NonNull;
import net.juniper.netconf.Device;
import net.juniper.netconf.XML;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static Hardware.Computer.getNetconfMessageId;

/**
 * @author : wpy
 * @description: TODO
 * @date : 4/15/22 6:54 AM
 */
public class NetconfGetInfo {
    private String hostName, userName, password, url;
    private int port;

    @Builder
    public NetconfGetInfo(@NonNull String hostName,
                          @NonNull String userName,
                          @NonNull String password,
                          @NonNull String url,
                          Integer port){
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
        this.url = url;
        this.port = port == null ? 830 : port;
    }

    public String getInfo() throws IOException, SAXException {
        Device device = Device.builder()
                .hostName(this.hostName)
                .userName(this.userName)
                .password(this.password)
                .port(this.port)
                .timeout(5000)
                .hostKeysFileName("~/.ssh/known_hosts")
                .strictHostKeyChecking(false)
                .build();
        device.connect();

        String messageId = getNetconfMessageId();
        String rpcContent = "<get-config message-id=\"" + messageId + "\" " +
                "xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" +
                "<source><running/></source><filter>" +
                "</filter></get-config>";
        System.out.println("Netconf Request: " + rpcContent);
        XML rpc_reply = device.executeRPC(rpcContent);
        device.close();
        writeToXML(rpc_reply.toString());
        return  rpc_reply.toString();
    }

    private void writeToXML(String content){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("get.xml"));
            out.write(content);
            out.close();
            System.out.println("file create successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
