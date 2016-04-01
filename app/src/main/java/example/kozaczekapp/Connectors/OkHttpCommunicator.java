package example.kozaczekapp.Connectors;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import example.kozaczekapp.ConnectionProvider.IConnection;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class of implementation OkHttpCommunicator.
 */
public class OkHttpCommunicator implements IConnection {
    OkHttpClient client;

    /**
     * Constructor where we create clinet as a new isnstance of OkHttpClient;
     */
    public OkHttpCommunicator() {
        client = new OkHttpClient();
    }

    /**
     * Method used to get Response from Server.
     *
     * @param mBaseUrl Url to service as String
     * @return response from service as String.
     */
    @Override
    public String getResponse(String mBaseUrl) {
        String responseString = null;
        Response response;
        if (isValidAddress(mBaseUrl)) {
            Request request = new Request.Builder()
                    .url(mBaseUrl)
                    .build();

            try {
                response = client.newCall(request).execute();
                responseString = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseString;
    }

    private boolean isValidAddress(String mBaseUrl){
        try {
            URL url = new URL(mBaseUrl);
            boolean isValidAddress = (url.getHost() != null && url.getPath() != null);
            return isValidAddress;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }
}