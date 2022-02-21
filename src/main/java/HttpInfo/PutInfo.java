package HttpInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class PutInfo {
    String _url, identity, password;

    public PutInfo(String url){
        this._url = url;
        this.identity = "admin";
        this.password = "admin";
    }

    public PutInfo(String url, String identity, String password){
        this._url = url;
        this.identity = identity;
        this.password = password;
    }

    public void putInfo(String objS){
        try {
            URL url = new URL(this._url);
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "");
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
                StringBuffer params = new StringBuffer();
                params.append(objS);
                byte[] bytes = params.toString().getBytes();
                connection.getOutputStream().write(bytes);
                if(connection.getResponseCode() != 200){
                    System.out.println(connection.getResponseCode());
                    System.out.println("error!");
                }else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection
                            .getInputStream(), "UTF-8"));
                    String output = reader.readLine();
                    System.out.println("response: " + output);
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }
}
