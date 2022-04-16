import NetconfAPI.NetconfGetInfo;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author : wpy
 * @description: TODO
 * @date : 4/16/22 3:57 AM
 */
public class NetconfApp {
    public static void main(String[] args) throws IOException, SAXException {
        NetconfGetInfo getInfo = NetconfGetInfo.builder().hostName("localhost")
                .userName("wpy").password("22003x").port(830)
                .url("").build();
        String message = getInfo.getInfo();
        System.out.println(message);
    }
}
