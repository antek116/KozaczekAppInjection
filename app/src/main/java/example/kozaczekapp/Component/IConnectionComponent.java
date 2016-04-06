package example.kozaczekapp.Component;


import javax.inject.Singleton;

import dagger.Component;
import example.kozaczekapp.Connectors.MyHttpConnection;
import example.kozaczekapp.Connectors.MyUrlConnection;
import example.kozaczekapp.Connectors.OkHttpCommunicator;
import example.kozaczekapp.Connectors.VolleyConnection;
import example.kozaczekapp.Module.ConnectionModule;
import example.kozaczekapp.Service.KozaczekService;


/**
 * interface of IconnectionComponent class used to Dagger Injection.
 */
@Singleton
@Component(modules = ConnectionModule.class)
public interface IConnectionComponent {
    void inject(KozaczekService kozaczekService);
}
