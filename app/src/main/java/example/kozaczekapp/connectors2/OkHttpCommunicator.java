package example.kozaczekapp.connectors2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import example.kozaczekapp.connectionProvider2.IConnection;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class of implementation OkHttpCommunicator.
 */
public class OkHttpCommunicator implements IConnection {
    OkHttpClient client;
    private static String encoding = "ISO-8859-1";

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
                response.header("encoding",getEncoding());
                responseString = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseString;
    }

    /**
     * @return Encoding for current connection
     */
    @Override
    public String getEncoding() {
        return encoding;
    }
        private boolean isValidAddress(String mBaseUrl) {
        boolean isValidAddress = false;
        try {
            URL url = new URL(mBaseUrl);
            isValidAddress = (url.getHost() != null && url.getPath() != null);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return isValidAddress;
    }
}