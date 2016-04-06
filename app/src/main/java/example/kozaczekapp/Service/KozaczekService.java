package example.kozaczekapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import example.kozaczekapp.Component.DaggerIConnectionComponent;
import example.kozaczekapp.Component.IConnectionComponent;
import example.kozaczekapp.ConnectionProvider.IConnection;
import example.kozaczekapp.DatabaseConnection.DatabaseHandler;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Module.ConnectionModule;


/**
 * Implementation of Kozaczek.pl service.
 */
public class KozaczekService extends IntentService {

    public static final String URL = "url";
    private static final String TAG = "KozaczekService";
    private IConnectionComponent component;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    @Inject
    IConnection connection;

    public KozaczekService() {
        super(KozaczekService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        component.inject(this);
        Log.d(TAG, "onHandleIntent: Service Started");
        String url = intent.getStringExtra(URL);
        if (connection.getResponse(url) != null) {
            Parser parser1 = new Parser(connection.getResponse(url));
            parser1.setEncoding(connection.getEncoding());
            ArrayList<Article> articles = parser1.parse();
            DatabaseHandler db = new DatabaseHandler(this);
            db.addArticleList(articles);
        }
    }
    private IConnectionComponent getComponentInstance() {
        if (component == null) {
            return component = DaggerIConnectionComponent
                    .builder()
                    .connectionModule(new ConnectionModule(getBaseContext()))
                    .build();
        } else {
            return component;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        this.component = getComponentInstance();
        super.onStart(intent, startId);

    }
}

