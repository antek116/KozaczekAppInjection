package example.kozaczekapp.Connectors;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import example.kozaczekapp.ConnectionProvider.IConnection;

public class MyUrlConnection implements IConnection {
    private static final String ENCODING_STANDARD = "ISO-8859-2";
    private String encoding;


    /**
     * Method used to get Response from Server.
     *
     * @param mBaseUrl Url to service as String
     * @return response from service as String.
     */
    @Override
    public String getResponse(String mBaseUrl) {
        InputStream inputStream;
        HttpURLConnection urlConnection;
        URL url;
        try {
            url = new URL(mBaseUrl);
            boolean isValidAddress = (url.getHost() != null && url.getPath() != null);
            if (isValidAddress) {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    return IOUtils.toString(inputStream, ENCODING_STANDARD);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets encoding for current connection
     *
     * @param encoding to be set
     */
    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return Encoding for current connection
     */
    @Override
    public String getEncoding() {
        return encoding;
    }
}
