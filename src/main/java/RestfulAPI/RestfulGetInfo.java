package RestfulAPI;

import lombok.Builder;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class RestfulGetInfo {
    private String _url, identity, password;

    @Builder
    public RestfulGetInfo(@NonNull String url,
                          String identity,
                          String password){
        this._url = url;
        this.identity = (identity != null) ? identity: "admin";
        this.password = (password != null) ? password: "admin";
    }

    public String getInfo(){
        String output = null;
        System.out.println("request: " + _url);
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
                int resultCode = connection.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection
                        .getInputStream(), "UTF-8"));
                output = reader.readLine();
                System.out.println("responseCode: " + resultCode);
                System.out.println("responseBody: " + output);
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
