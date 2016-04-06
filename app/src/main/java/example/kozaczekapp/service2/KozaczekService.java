package example.kozaczekapp.service2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;


import javax.inject.Inject;

import example.kozaczekapp.application.MyApp;
import example.kozaczekapp.connectionProvider2.IConnection;
import example.kozaczekapp.databaseConnection2.DatabaseHandler;
import example.kozaczekapp.kozaczekItems2.Article;


/**
 * Implementation of Kozaczek.pl service.
 */
public class KozaczekService extends IntentService {

    public static final String URL = "url";
    private static final String TAG = "KozaczekService";

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
        ((MyApp)getApplication()).getComponentInstance().inject(this);
        Log.d(TAG, "onHandleIntent: Service Started");
        String url = intent.getStringExtra(URL);

        if (connection.getResponse(url) != null){
            Parser parser1 = new Parser(connection.getResponse(url));
            parser1.setEncoding(connection.getEncoding());
            ArrayList<Article> articles = parser1.parse();
            DatabaseHandler db = new DatabaseHandler(this);
            db.addArticleList(articles);
        }
    }
}

