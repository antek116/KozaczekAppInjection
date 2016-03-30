package example.kozaczekapp.Module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.kozaczekapp.Connectors.MyHttpConnection;
import example.kozaczekapp.Connectors.MyUrlConnection;
import example.kozaczekapp.Connectors.OkHttpCommunicator;

@Module
public class ConnectionModule {

    @Provides
    @Singleton
    MyHttpConnection provideHttpConnection(){
        return new MyHttpConnection();
    }

    @Provides
    @Singleton
    OkHttpCommunicator provideOkHttpConnection(){
        return new OkHttpCommunicator();
    }

    @Provides
    @Singleton
    MyUrlConnection provideMyUrlConnection(){
        return new MyUrlConnection();
    }
}
