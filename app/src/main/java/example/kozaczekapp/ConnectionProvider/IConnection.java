package example.kozaczekapp.ConnectionProvider;

public interface IConnection {
    /**
     * Method used to get Response from specific URL.
     *
     * @param mBaseUrl Url to service as String
     * @return Response as a String
     */
    String getResponse(String mBaseUrl);


}
