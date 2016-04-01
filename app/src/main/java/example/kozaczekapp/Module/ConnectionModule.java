package example.kozaczekapp.Module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.kozaczekapp.Connectors.MyHttpConnection;
import example.kozaczekapp.Connectors.MyUrlConnection;
import example.kozaczekapp.Connectors.OkHttpCommunicator;

/**
 * Module class used to make dependency to injection.
 */
@Module
public class ConnectionModule {


    /**
     * @return instance of MyHttpConnection class;
     */
    @Provides
    @Singleton
    MyHttpConnection provideHttpConnection() {
        return new MyHttpConnection();
    }

    /**
     * @return instance of okHttpCommunicator class
     */
    @Provides
    @Singleton
    OkHttpCommunicator provideOkHttpConnection() {
        return new OkHttpCommunicator();
    }

    /**
     * @return instance of MyUrlConnection class
     */
    @Provides
    @Singleton
    MyUrlConnection provideMyUrlConnection() {
        return new MyUrlConnection();
    }
}
