package Yang.Network;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LLDPs {
    private String windowsCommand = "ipconfig", linuxCommand = "ifconfig";
    @Getter
    private String systemType;

    public LLDPs(){
        String os = System.getProperty("os.name");
        System.out.println(os.charAt(0));
    }

    //chcp 437 change to english
    //chcp 936 change to chinese
    public void getLocalInterface() throws IOException {
        Process process = new ProcessBuilder(windowsCommand).start();
        InputStream in=process.getInputStream();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "gbk"));
        String line, result = "";
        while ((line = br.readLine ()) != null){
            System.out.println(line);
            result += line;
        }
//        System.out.println(result);
    }
}
