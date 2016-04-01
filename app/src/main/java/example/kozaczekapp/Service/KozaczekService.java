package example.kozaczekapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import example.kozaczekapp.Component.DaggerIConnectionComponent;
import example.kozaczekapp.Component.IConnectionComponent;
import example.kozaczekapp.ConnectionProvider.IConnection;
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

    private static final String TAG = "KozaczekService";
    private static IConnectionComponent component = DaggerIConnectionComponent
            .builder()
            .connectionModule(new ConnectionModule())
            .build();
    private static IConnection connection = component.provideConnection();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public KozaczekService() {
        super(KozaczekService.class.getName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: Service Started");
        String url = intent.getStringExtra(URL);
        loadPreferences();
        if (connection.getResponse(url) != null) {
            Parser parser1 = new Parser(connection.getResponse(url));
            ArrayList<Article> articles = parser1.parse();
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction(INTENT_FILTER);
            DatabaseHandler db = new DatabaseHandler(this);
            db.addArticleList(articles);
//            sendBroadcast(broadcastIntent);
        }
        Log.d(TAG, "onHandleIntent: Broadcast send...");
    }

    private void loadPreferences() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String downloadType = SP.getString(getString(R.string.downloadType), getString(R.string.downloadValue));
        switch (downloadType) {
            case HTTP_CONNECTION:
                connection = component.provideConnection();
                break;
            case URL_CONNECTION:
                connection = component.provideMyUrlConnection();
                break;
            case OK_HTTP_CONNECTION:
                connection = component.provideOKHttpConnection();
                break;
            default:
                connection = component.provideConnection();
        }
    }
}

