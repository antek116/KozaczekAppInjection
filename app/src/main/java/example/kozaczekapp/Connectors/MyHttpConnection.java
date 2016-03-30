package example.kozaczekapp.Connectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import example.kozaczekapp.ConnectionProvider.IConnection;

public class MyHttpConnection implements IConnection{

    private static final String ENCODING_STANDARD = "ISO-8859-2";

    @Override
    public String getResponse(String mBaseUrl) {
        String xmlString = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mBaseUrl);
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity r_entity = response.getEntity();
            xmlString = EntityUtils.toString(r_entity, ENCODING_STANDARD);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
