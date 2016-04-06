package example.kozaczekapp.Connectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import example.kozaczekapp.ConnectionProvider.IConnection;


/**
 * Class of HttpConnection implementation.
 */
public class MyHttpConnection implements IConnection {

    private static String encoding = "ISO-8859-2";

    /**
     * Method used to get Response from Server.
     * @param mBaseUrl Url to service as String
     * @return response from service as String.
     */
    @Override
    public String getResponse(String mBaseUrl) {
        if(mBaseUrl == null){
            return null;
        }
        String xmlString = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mBaseUrl);
        boolean isValidAddress = httpGet.getURI().getPath()  != null && httpGet.getURI().getHost() != null;
        if(isValidAddress) {
            HttpResponse response;
            try {
                response = httpClient.execute(httpGet);
                HttpEntity r_entity = response.getEntity();
                xmlString = EntityUtils.toString(r_entity, encoding);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return xmlString;
    }
    /**
     * @return Encoding for current connection
     */
    @Override
    public String getEncoding() {
        return encoding;
    }


}
