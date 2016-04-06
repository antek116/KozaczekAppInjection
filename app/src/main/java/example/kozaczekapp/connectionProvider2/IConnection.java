package example.kozaczekapp.connectionProvider2;


public interface IConnection {
    /**
     * Method used to get Response from specific URL.
     *
     * @param mBaseUrl Url to service as String
     * @return Response as a String
     */
    String getResponse(String mBaseUrl);

    /**
     *
     * @return Encoding for current connection
     */
    String getEncoding();
}
