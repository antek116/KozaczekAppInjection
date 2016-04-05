package example.kozaczekapp.Component;


import javax.inject.Singleton;

import dagger.Component;
import example.kozaczekapp.Connectors.MyHttpConnection;
import example.kozaczekapp.Connectors.MyUrlConnection;
import example.kozaczekapp.Connectors.OkHttpCommunicator;
import example.kozaczekapp.Connectors.VolleyConnection;
import example.kozaczekapp.Module.ConnectionModule;


/**
 * interface of IconnectionComponent class used to Dagger Injection.
 */
@Singleton
@Component(modules = ConnectionModule.class)
public interface IConnectionComponent {
    /**
     * Method use to connect to service via HttpConnection.
     *
     * @return instance of HttpConnection
     */
    MyHttpConnection provideMyHttpConnection();

    /**
     * Method use to connect to service via HttpConnection.
     *
     * @return instance of OkHttpConnection.
     */
    OkHttpCommunicator provideOKHttpConnection();

    /**
     * Method use to connect to service via UrlConnection.
     *
     * @return response from service as a String
     */
    MyUrlConnection provideMyUrlConnection();

    /**
     * Method use to connect to service via VolleyConnection.
     *
     * @return response from service as a String
     */
    VolleyConnection provideVolleyConnection();
}
