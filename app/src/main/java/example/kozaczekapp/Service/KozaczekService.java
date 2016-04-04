package example.kozaczekapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.commons.io.Charsets;

import java.util.ArrayList;

import example.kozaczekapp.Component.DaggerIConnectionComponent;
import example.kozaczekapp.Component.IConnectionComponent;
import example.kozaczekapp.ConnectionProvider.IConnection;
import example.kozaczekapp.Connectors.Encoding;
import example.kozaczekapp.DatabaseConnection.DatabaseHandler;
import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Module.ConnectionModule;
import example.kozaczekapp.R;

/**
 * Implementation of Kozaczek.pl service.
 */
public class KozaczekService extends IntentService {

    public static final String URL = "url";
    public static final String INTENT_FILTER = "example.kozaczekapp.broadcast.intent.filter";

    private static final String HTTP_CONNECTION = "HttpConnection";
    private static final String URL_CONNECTION = "UrlConnection";
    private static final String OK_HTTP_CONNECTION = "OkHttpConnection";
    private static final String VOLLEY_CONNECTION = "VolleyConnection";

    private static final String TAG = "KozaczekService";
    private IConnectionComponent component;
    private IConnection connection;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public KozaczekService() {
        super(KozaczekService.class.getName());
         component = DaggerIConnectionComponent
                .builder()
                .connectionModule(new ConnectionModule(this))
                .build();
        connection = component.provideConnection();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: Service Started");
        String url = intent.getStringExtra(URL);
        loadPreferences();
        if (connection.getResponse(url) != null) {
            Parser parser1 = new Parser(connection.getResponse(url));
            parser1.setEncoding(connection.getEncoding());
            ArrayList<Article> articles = parser1.parse();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(INTENT_FILTER);
            DatabaseHandler db = new DatabaseHandler(this);
            db.addArticleList(articles);
            sendBroadcast(broadcastIntent);
        }
        Log.d(TAG, "onHandleIntent: Broadcast send...");
    }

    private void loadPreferences() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String downloadType = SP.getString(getString(R.string.downloadType), getString(R.string.downloadValue));
        switch (downloadType) {
            case HTTP_CONNECTION:
                connection = component.provideConnection();
                connection.setEncoding(Encoding.ISO_8859_2);
                break;
            case URL_CONNECTION:
                connection = component.provideMyUrlConnection();
                connection.setEncoding(Encoding.ISO_8859_2);
                break;
            case OK_HTTP_CONNECTION:
                connection = component.provideOKHttpConnection();
                connection.setEncoding(Encoding.ISO_8859_1);
                break;
            case VOLLEY_CONNECTION :
                connection = component.provideVolleyConnection();
                connection.setEncoding(Encoding.ISO_8859_1);
                break;
            default:
                connection = component.provideConnection();
                connection.setEncoding("UTF-8");
        }
    }
}

