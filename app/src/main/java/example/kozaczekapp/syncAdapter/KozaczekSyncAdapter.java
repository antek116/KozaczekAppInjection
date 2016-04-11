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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
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
import example.kozaczekapp.service.Parser;

public class KozaczekSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "KozaczekSyncAdapter";
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
            if (checkTokenValidity(mAccountManager, account)) {
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
            } else { //if App is visible display activity othervise post notification
                sendNotification(context, context.getString(R.string.notification_token_expired));
                Log.d(TAG, "onPerformSync: token is invalid");
                Intent i = new Intent(context, AuthenticatorActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(AccountKeyConstants.ARG_CLICKED_FROM_SETTINGS, false);
                context.startActivity(i);
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
                .setContentTitle(this.context.getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setLights(0xff00ff00, 300, 100)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
