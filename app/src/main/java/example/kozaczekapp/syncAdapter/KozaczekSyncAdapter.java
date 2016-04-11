package example.kozaczekapp.syncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    @Inject
    IConnection connection;
    private Context context;
    private String url = "http://www.kozaczek.pl/rss/plotki.xml";

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

            ((MyApp) getContext()).getComponentInstance().inject(this);
            Log.d("SyncAdapter", "SyncAdapter: Synchronize Started");
            if (connection.getResponse(url) != null) {
                Parser parser1 = new Parser(connection.getResponse(url));
                parser1.setEncoding(connection.getEncoding());
                ArrayList<Article> articles = parser1.parse();
                ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
                createDeleteOperations(operationList);
                createInsertOperations(operationList, articles);
                getContext().getContentResolver().applyBatch(RssContract.AUTHORITY, operationList);
                getContext().getContentResolver().notifyChange(RssContract.CONTENT_URI, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInsertOperations(ArrayList<ContentProviderOperation> operationList,
                                        ArrayList<Article> articles) {
        for (Article article : articles) {
            ContentValues values = new ContentValues();
            putArticlesFieldsToValuesMap(article, values);
            operationList.add(ContentProviderOperation.newInsert(RssContract.CONTENT_URI)
                    .withValues(values)
                    .withYieldAllowed(true)
                    .build());
        }
    }

    private void putArticlesFieldsToValuesMap(Article article, @NonNull ContentValues values) {
        values.put(RssContract.Columns.COLUMN_TITLE, article.getTitle());
        values.put(RssContract.Columns.COLUMN_DESCRIPTION, article.getDescription());
        values.put(RssContract.Columns.COLUMN_PUB_DATE, article.getPubDate());
        values.put(RssContract.Columns.COLUMN_ARTICLE_LINK, article.getLinkToArticle());
        values.put(RssContract.Columns.COLUMN_IMAGE_LINK, article.getImage().getImageUrl());
    }

    private void createDeleteOperations(ArrayList<ContentProviderOperation> operationList) {
        operationList.add(ContentProviderOperation.newDelete(RssContract.CONTENT_URI).build());
    }
}
