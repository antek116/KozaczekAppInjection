package example.kozaczekapp.syncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import example.kozaczekapp.application.MyApp;
import example.kozaczekapp.connectionProvider.IConnection;
import example.kozaczekapp.databaseConnection.RssContract;
import example.kozaczekapp.kozaczekItems.Article;
import example.kozaczekapp.parser.Parser;

public class KozaczekSyncAdapter extends AbstractThreadedSyncAdapter {
    private final AccountManager mAccountManager;
    @Inject
    IConnection connection;
    @SuppressWarnings("FieldCanBeLocal")
    private String url = "http://www.kozaczek.pl/rss/plotki.xml";

    public KozaczekSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d("AccountSyncAdapter", "onPerformSync for account[ " + account.name + " ]");
        try {
            //Get the auth token for the current account
//            String authToken = mAccountManager.blockingGetAuthToken(account, "", true);

            ((MyApp) getContext()).getComponentInstance().inject(this);
            Log.d("SyncAdapter", "SyncAdapter: Synchronize Started");
            if (connection.getResponse(url) != null) {
                Parser parser = new Parser(connection.getResponse(url));
                parser.setEncoding(connection.getEncoding());
                ArrayList<Article> articles = parser.parse();
                ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
                createUpdateOperation(operationList, articles);
                createInsertOperation(operationList, articles);
                createDeleteOperation(operationList, articles);
                getContext().getContentResolver().applyBatch(RssContract.AUTHORITY, operationList);
                getContext().getContentResolver().notifyChange(RssContract.CONTENT_URI, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create update operation only if specified article link is the same and at least one of
     * the other fields are different
     *
     * @param operationList list of ContentProvider operations
     * @param articles      all parsed articles
     */
    private void createUpdateOperation(ArrayList<ContentProviderOperation> operationList,
                                       ArrayList<Article> articles) {
        for (Article article : articles) {
            ContentValues values = new ContentValues();
            putArticlesFieldsToValuesMap(article, values);
            String where = RssContract.Columns.COLUMN_ARTICLE_LINK + " = ? "
                    + "AND ( "
                    + RssContract.Columns.COLUMN_DESCRIPTION + " != ?" + " OR "
                    + RssContract.Columns.COLUMN_IMAGE_LINK + " != ?" + " OR "
                    + RssContract.Columns.COLUMN_TITLE + " != ?" + " OR "
                    + RssContract.Columns.COLUMN_PUB_DATE + " != ?"
                    + " )";

            String[] params = new String[]{article.getLinkToArticle(), article.getDescription(),
                    article.getImage().getImageUrl(), article.getTitle(), article.getPubDate()};

            operationList.add(ContentProviderOperation.newUpdate(RssContract.CONTENT_URI)
                    .withSelection(where, params)
                    .withValues(values)
                    .build());
        }

    }

    /**
     * Create insert operation only if article does not exist
     *
     * @param operationList list of ContentProvider operations
     * @param articles      all parsed articles
     */
    private void createInsertOperation(ArrayList<ContentProviderOperation> operationList,
                                       ArrayList<Article> articles) {
        for (Article article : articles) {
            ContentValues values = new ContentValues();
            putArticlesFieldsToValuesMap(article, values);

            String selection = RssContract.Columns.COLUMN_ARTICLE_LINK + " = ?";
            String[] params = new String[]{article.getLinkToArticle()};
            Cursor cursor = getContext().getContentResolver().query(RssContract.CONTENT_URI, null,
                    selection, params, null);
            assert cursor != null;
            if (cursor.getCount() == 0) {
                operationList.add(ContentProviderOperation.newInsert(RssContract.CONTENT_URI)
                        .withValues(values)
                        .withYieldAllowed(true)
                        .build());
            }
            cursor.close();

        }
    }


    private void putArticlesFieldsToValuesMap(Article article, @NonNull ContentValues values) {
        values.put(RssContract.Columns.COLUMN_TITLE, article.getTitle());
        values.put(RssContract.Columns.COLUMN_DESCRIPTION, article.getDescription());
        values.put(RssContract.Columns.COLUMN_PUB_DATE, article.getPubDate());
        values.put(RssContract.Columns.COLUMN_ARTICLE_LINK, article.getLinkToArticle());
        values.put(RssContract.Columns.COLUMN_IMAGE_LINK, article.getImage().getImageUrl());
    }

    private void createDeleteOperation(ArrayList<ContentProviderOperation> operationList, ArrayList<Article> articles) {

        Cursor cursor = getContext().getContentResolver().query(RssContract.CONTENT_URI, null,
                null, null, null);

        assert cursor != null;
        int articlesToBeDeleted;
        if(cursor.getCount() < articles.size()){
            articlesToBeDeleted = 0;
        }else{
            articlesToBeDeleted = cursor.getCount() - articles.size();
        }

        if (articlesToBeDeleted != 0) {
            String selection = RssContract.Columns._ID + ">= ?" ;// I'll find mistake and fix it!
            String[] params = new String[]{String.valueOf(articlesToBeDeleted)};
            operationList.add(ContentProviderOperation.newDelete(RssContract.CONTENT_URI)
                    .withSelection(selection, params)
                    .build());
        }
        cursor.close();
    }
}
