package HttpOperation;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient {//http处理专用，所有的网络请求均通过该类进行处理

    public String sendPost(String url, String data) {
        String response = null;
        System.out.println("url: " + url);
        System.out.println("request: " + data);
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(url);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(50000)
                        .setConnectionRequestTimeout(50000)
                        .setSocketTimeout(50000).build();
                httppost.setHeader("Connection", "Keep-Alive");
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Authorization", "Basic YWRtaW46YWRtaW4=");
                httppost.setConfig(requestConfig);
                if (data != null){
                    StringEntity stringentity = new StringEntity(data,
                            ContentType.create("application/json", "UTF-8"));
                    httppost.setEntity(stringentity);
                }
                httpresponse = httpclient.execute(httppost);
                response = EntityUtils.toString(httpresponse.getEntity());
                System.out.println("response: " + response);
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}

