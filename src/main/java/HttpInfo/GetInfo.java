package HttpInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class GetInfo {
    private String _url, identity, password;

    public GetInfo(String _url, String identity, String password){
        this._url = _url;
        this.identity = identity;
        this.password = password;
    }
    public GetInfo(String _url){
        this._url = _url;
        this.identity = "admin";
        this.password = "admin";
    }

    public String getInfo(){
        String output = null;
        try {
            URL url = new URL(this._url);
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(identity, password.toCharArray());
                    }
                });
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setConnectTimeout(10000);
                connection.connect();
                if(connection.getResponseCode() != 200){
                    System.out.println(connection.getResponseCode());
                    System.out.println("error!");
                }else {
                    BufferedReader result = new BufferedReader(new
                            InputStreamReader((connection.getInputStream())));
                    output = result.readLine();
                    System.out.println("response: " + output);
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return output;
    }
}
