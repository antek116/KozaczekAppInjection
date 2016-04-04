package example.kozaczekapp.ConnectionProvider;

public interface IConnection {
    /**
     * Method used to get Response from specific URL.
     *
     * @param mBaseUrl Url to service as String
     * @return Response as a String
     */
    String getResponse(String mBaseUrl);

    /**
     * Sets encoding for current connection
     * @param encoding to be set
     */
    void setEncoding(String encoding);

    /**
     *
     * @return Encoding for current connection
     */
    String getEncoding();
}
