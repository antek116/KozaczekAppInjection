package example.kozaczekapp.syncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import example.kozaczekapp.application.MyApp;
import example.kozaczekapp.connectionProvider.IConnection;
import example.kozaczekapp.databaseConnection.DatabaseHandler;
import example.kozaczekapp.kozaczekItems.Article;
import example.kozaczekapp.service.Parser;

public class KozaczekSyncAdapter extends AbstractThreadedSyncAdapter {
    private final AccountManager mAccountManager;
    private Context context;
    private String url = "http://www.kozaczek.pl/rss/plotki.xml";

    @Inject
    IConnection connection;

    public KozaczekSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
        this.context = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("AccountSyncAdapter", "onPerformSync for account[ " + account.name + " ]");
        try {
            //Get the auth token for the current account
            //String authToken = mAccountManager.blockingGetAuthToken(account, "example.kozaczekapp.DatabaseConnection.RssContentProvider", true);

            ((MyApp)getContext()).getComponentInstance().inject(this);
            Log.d("SyncAdapter", "SyncAdapter: Synchronize Started");
            if (connection.getResponse(url) != null) {
                Parser parser1 = new Parser(connection.getResponse(url));
                parser1.setEncoding(connection.getEncoding());
                ArrayList<Article> articles = parser1.parse();
                DatabaseHandler db = new DatabaseHandler(context);
                db.addArticleList(articles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
