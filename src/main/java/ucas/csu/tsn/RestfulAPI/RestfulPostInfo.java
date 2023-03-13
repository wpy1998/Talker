package ucas.csu.tsn.RestfulAPI;

import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class RestfulPostInfo {
    private String _url, identity, password;
    @Setter
    private boolean isDebug;

    @Builder
    public RestfulPostInfo(@NonNull String url,
                           String identity,
                           String password,
                           Boolean isDebug){
        this._url = url;
        this.identity = (identity != null) ? identity : "admin";
        this.password = (password != null) ? password : "admin";
        this.isDebug = (isDebug != null) ? isDebug : false;
    }

    public int postInfo(String objS){
        int resultCode = 400;
        if (isDebug){
            System.out.println("requestType : postInfo");
            System.out.println("requestBody : " + objS);
        }
        try {
            URL url = new URL(this._url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setInstanceFollowRedirects(true);
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
            connection.setConnectTimeout(5000);
            connection.connect();
            StringBuffer params = new StringBuffer();
            params.append(objS);
            byte[] bytes = params.toString().getBytes();
            connection.getOutputStream().write(bytes);
            resultCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection
                    .getInputStream(), "UTF-8"));
            String output = reader.readLine();
            if (isDebug){
                System.out.println("responseCode: " + resultCode);
                System.out.println("responseBody: " + output);
            }
            connection.disconnect();
        } catch (IOException e) {
            System.out.println("responseCode: " + resultCode);
        }
        return resultCode;
    }
}
