package example.kozaczekapp.connectionProvider;

import javax.inject.Inject;


/**
 * Class used to Depending Injection.
 */
public class ConnectionProvider {


    private IConnection provider;
    private static ConnectionProvider instance = null;

    /**
     * Depending Injection in constructor.
     *
     * @param provider instance of provider witch extend IConnection interface.
     */

    private ConnectionProvider(IConnection provider) {
        this.provider = provider;
    }

    /**
     *
     * @return instnce of ConnectionProvider
     */
    @Inject
    public static ConnectionProvider getInstance(IConnection provider){
        if(instance == null){
            instance = new ConnectionProvider(provider);
        }else{
            instance.provider = provider;
        }
        return instance;
    }

    public String getResponseAsStringFromUrl(String mBaseUrl) {

        return provider.getResponse(mBaseUrl);
    }
}
