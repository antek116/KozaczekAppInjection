package example.kozaczekapp.syncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import example.kozaczekapp.MainActivity;
import example.kozaczekapp.R;
import example.kozaczekapp.application.MyApp;
import example.kozaczekapp.authenticator.AccountKeyConstants;
import example.kozaczekapp.authenticator.AuthenticatorActivity;
import example.kozaczekapp.authenticator.Token;
import example.kozaczekapp.connectionProvider.IConnection;
import example.kozaczekapp.databaseConnection.RssContract;
import example.kozaczekapp.kozaczekItems.Article;
import example.kozaczekapp.parser.Parser;

public class KozaczekSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "KozaczekSyncAdapter";
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
        try {
            if (checkTokenValidity(mAccountManager, account)) {
                ((MyApp) getContext()).getComponentInstance().inject(this);
                Log.d("SyncAdapter", "SyncAdapter: Synchronize Started");
                String responste = connection.getResponse(url);
                if (responste != null) {
                    Parser parser = new Parser(responste);
                    parser.setEncoding(connection.getEncoding());
                    ArrayList<Article> articles = parser.parse();
                    ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
                    createUpdateOperation(operationList, articles);
                    createInsertOperation(operationList, articles);
                    createDeleteOperation(operationList, articles);
                    getContext().getContentResolver().applyBatch(RssContract.AUTHORITY, operationList);
                    getContext().getContentResolver().notifyChange(RssContract.CONTENT_URI, null);
                }
            } else { //send broadcast, if App is visible dont display notification.
                Log.d(TAG, "onPerformSync: token is invalid");
                sendNotification(getContext(), getContext().getString(R.string.notification_token_expired));
                Intent intent = new Intent();
                intent.setAction(AccountKeyConstants.ACTION_DISPLAY_LOGIN);
                getContext().sendBroadcast(intent);
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
            String selection = RssContract.Columns._ID + ">= ?" ;
            String[] params = new String[]{String.valueOf(articlesToBeDeleted)};
            operationList.add(ContentProviderOperation.newDelete(RssContract.CONTENT_URI)
                    .withSelection(selection, params)
                    .build());
        }
        cursor.close();
    }

    /**
     * Check token validity
     * @param accountManager account manager instance
     * @param account for which token is being validated
     * @return true if token is valid
     * @throws Exception
     */
    private boolean checkTokenValidity(AccountManager accountManager, Account account) throws Exception {
        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, AccountKeyConstants.AUTHTOKEN_TYPE_FULL_ACCESS, null, false, null, null);
        boolean result;
        Bundle bnd = future.getResult();
        final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
        Token token = new Token(authtoken);
        result = token.checkIfValid();
        return result;
    }

    /**
     * Sends System notification with given message
     * @param context app context
     * @param message to be set in notification
     */
    private void sendNotification(Context context, String message) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getContext().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setLights(0xff00ff00, 300, 100)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
