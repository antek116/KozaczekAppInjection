package example.kozaczekapp.Component;


import javax.inject.Singleton;

import dagger.Component;
import example.kozaczekapp.Connectors.MyHttpConnection;
import example.kozaczekapp.Connectors.MyUrlConnection;
import example.kozaczekapp.Connectors.OkHttpCommunicator;
import example.kozaczekapp.Module.ConnectionModule;

@Singleton
@Component(modules = ConnectionModule.class)
public interface IConnectionComponent {

    MyHttpConnection provideConnection();
    OkHttpCommunicator provideOKHttpConnection();
    MyUrlConnection provideMyUrlConnection();
}
