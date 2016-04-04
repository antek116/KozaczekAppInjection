package example.kozaczekapp.Module;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.kozaczekapp.Connectors.MyHttpConnection;
import example.kozaczekapp.Connectors.MyUrlConnection;
import example.kozaczekapp.Connectors.OkHttpCommunicator;
import example.kozaczekapp.Connectors.VolleyConnection;

/**
 * Module class used to make dependency to injection.
 */
@Module
public class ConnectionModule {

    Context context;

    public ConnectionModule(Context context){
        this.context = context;
    }


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

    /**
     * @return instance of VolleyConnection class
     */
    @Provides
    @Singleton
    VolleyConnection provideVolleyConnection() {
        return new VolleyConnection(context);
    }
}
