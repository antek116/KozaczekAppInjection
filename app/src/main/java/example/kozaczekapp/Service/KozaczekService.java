package example.kozaczekapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import example.kozaczekapp.Component.DaggerIConnectionComponent;
import example.kozaczekapp.Component.IConnectionComponent;
import example.kozaczekapp.ConnectionProvider.ConnectionProvider;
import example.kozaczekapp.ConnectionProvider.IConnection;
import example.kozaczekapp.Connectors.Encoding;
import example.kozaczekapp.DatabaseConnection.DatabaseHandler;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Module.ConnectionModule;
import example.kozaczekapp.R;

/**
 * Implementation of Kozaczek.pl service.
 */
public class KozaczekService extends IntentService {

    public static final String URL = "url";
    private static final String HTTP_CONNECTION = "HttpConnection";
    private static final String URL_CONNECTION = "UrlConnection";
    private static final String OK_HTTP_CONNECTION = "OkHttpConnection";
    private static final String VOLLEY_CONNECTION = "VolleyConnection";

    private static final String TAG = "KozaczekService";
    private static final String ENCODING_ISO_8859_2_KEY = "ISO_8859_2";
    private static final String ENCODING_ISO_8859_1_KEY = Encoding.ISO_8859_1;
    private IConnectionComponent component;
    private IConnection connection;
    ConnectionProvider provider;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public KozaczekService() {
        super(KozaczekService.class.getName());
         component = DaggerIConnectionComponent
                .builder()
                .connectionModule(new ConnectionModule(this))
                .build();
        connection = component.provideMyHttpConnection();
        provider = ConnectionProvider.getInstance(connection);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: Service Started");
        String url = intent.getStringExtra(URL);
        loadPreferences();
        if (connection.getResponse(url) != null) {
            Parser parser = new Parser(provider.getResponseAsStringFromUrl(url));
//            Parser parser = new Parser(connection.getResponse(url));
            parser.setEncoding(connection.getEncoding());
            ArrayList<Article> articles = parser.parse();
            DatabaseHandler db = new DatabaseHandler(this);
            db.addArticleList(articles);
        }
    }

    private void loadPreferences() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String downloadType = SP.getString(getString(R.string.downloadType), getString(R.string.downloadValue));
        switch (downloadType) {
            case HTTP_CONNECTION:
                connection = component.provideMyHttpConnection();
                connection.setEncoding(ENCODING_ISO_8859_2_KEY);
                break;
            case URL_CONNECTION:
                connection = component.provideMyUrlConnection();
                connection.setEncoding(ENCODING_ISO_8859_2_KEY);
                break;
            case OK_HTTP_CONNECTION:
                connection = component.provideOKHttpConnection();
                connection.setEncoding(ENCODING_ISO_8859_1_KEY);
                break;
            case VOLLEY_CONNECTION :
                connection = component.provideVolleyConnection();
                connection.setEncoding(ENCODING_ISO_8859_1_KEY);
                break;
            default:
                connection = component.provideMyHttpConnection();
                connection.setEncoding(ENCODING_ISO_8859_1_KEY);
            break;
        }
        provider = ConnectionProvider.getInstance(connection);
    }
}

