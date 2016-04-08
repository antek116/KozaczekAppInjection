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
import example.kozaczekapp.databaseConnection.RssContract;
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
//            String authToken = mAccountManager.blockingGetAuthToken(account, "", true);

            ((MyApp)getContext()).getComponentInstance().inject(this);
            Log.d("SyncAdapter", "SyncAdapter: Synchronize Started");
            String response = connection.getResponse(url);
            if (response != null) {
                Parser parser = new Parser(response);
                parser.setEncoding(connection.getEncoding());
                ArrayList<Article> articles = parser.parse();

                //gdy uzywamy bulk operaions
                //1. usuwamy wszystko co mamy (tworzymy lister operacji typu delete
                //2. dodajemy nowe itemki -> tworzyy liste operacji insert()

                /*
                list<Operation> = new list()
                createDeleteOperations(list, articles)
                createAddOperations(list, articles)
                getContext().getContentResolver().applyBatch(RssContract.AUTHORITY, list)
                */

                //TODO: mamy content providera, to go uzywamy
                DatabaseHandler db = new DatabaseHandler(context);
                db.addArticleList(articles);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
