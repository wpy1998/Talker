package RestfulAPI;

import lombok.Builder;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class RestfulPutInfo {
    String _url, identity, password;

    @Builder
    public RestfulPutInfo(@NonNull String url,
                          String identity,
                          String password){
        this._url = url;
        this.identity = (identity != null) ? identity: "admin";
        this.password = (password != null) ? password: "admin";
    }

    public int putInfo(String objS){
        System.out.println("requestType : putInfo");
        System.out.println("requestBody : " + objS);
        int resultCode = 400;
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
                resultCode = connection.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection
                        .getInputStream(), "UTF-8"));
                String output = reader.readLine();
                System.out.println("responseCode: " + resultCode);
                System.out.println("responseBody: " + output);
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return resultCode;
    }
}
