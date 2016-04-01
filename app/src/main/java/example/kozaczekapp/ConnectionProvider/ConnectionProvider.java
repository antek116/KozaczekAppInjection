package example.kozaczekapp.ConnectionProvider;

import javax.inject.Inject;


/**
 * Class used to Depending Injection.
 */
public class ConnectionProvider {


    private IConnection provider;

    /**
     * Depending Injection in constructor.
     *
     * @param provider instance of provider witch extend IConnection interface.
     */
    @Inject
    public ConnectionProvider(IConnection provider) {
        this.provider = provider;
    }

    /**
     * Method used to get Response from specific URL.
     *
     * @param mBaseUrl Url to service as String
     * @return Response as a String
     */
    public String getResponseAsStringFromUrl(String mBaseUrl) {

        return provider.getResponse(mBaseUrl);
    }
}
