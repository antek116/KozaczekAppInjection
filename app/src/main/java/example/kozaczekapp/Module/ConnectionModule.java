package example.kozaczekapp.Module;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.kozaczekapp.ConnectionProvider.IConnection;
import example.kozaczekapp.Connectors.MyHttpConnection;
import example.kozaczekapp.Connectors.MyUrlConnection;
import example.kozaczekapp.Connectors.OkHttpCommunicator;
import example.kozaczekapp.Connectors.VolleyConnection;
import example.kozaczekapp.R;

/**
 * Module class used to make dependency to injection.
 */
@Module
public class ConnectionModule {

    private static final String HTTP_CONNECTION = "HttpConnection";
    private static final String URL_CONNECTION = "UrlConnection";
    private static final String OK_HTTP_CONNECTION = "OkHttpConnection";
    private static final String VOLLEY_CONNECTION = "VolleyConnection";

    private Context context;

    public ConnectionModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    IConnection provideIConnection() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String downloadType = prefs.getString(context.getString(R.string.downloadType), context.getString(R.string.downloadValue));
        switch (downloadType) {
            default:
            case HTTP_CONNECTION:
                return new MyHttpConnection();
            case URL_CONNECTION:
                return new MyUrlConnection();
            case OK_HTTP_CONNECTION:
                return new OkHttpCommunicator();
            case VOLLEY_CONNECTION:
                return new VolleyConnection(context);
        }
    }
}
