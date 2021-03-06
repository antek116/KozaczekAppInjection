package example.kozaczekapp.databaseConnection;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Class provides data do database
 */
public class RssContentProvider extends ContentProvider {
    /**
     * when the URI references the entire articles table
     */
    public static final int ARTICLES = 1;
    /**
     * when the URI references the ID of a specific row in the articles table
     */
    public static final int ARTICLES_ID = 2;
    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    /**
     *
     */
    static {
        sURIMatcher.addURI(RssContract.AUTHORITY, RssContract.Columns.TABLE_NAME, ARTICLES);
        sURIMatcher.addURI(RssContract.AUTHORITY, RssContract.Columns.TABLE_NAME + "/#",
                ARTICLES_ID);
    }

    private DataBaseHelper db;

    @Override
    public boolean onCreate() {
        db = new DataBaseHelper(getContext());
        return false;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(RssContract.Columns.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case ARTICLES_ID:
                queryBuilder.appendWhere(RssContract.Columns._ID + "="
                        + uri.getLastPathSegment());
                break;
            case ARTICLES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(db.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = db.getWritableDatabase();

        long id;
        switch (uriType) {
            case ARTICLES:
                id = sqlDB.insert(RssContract.Columns.TABLE_NAME,
                        null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return Uri.parse(RssContract.Columns.TABLE_NAME + "/" + id);
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch
            (@NonNull ArrayList<ContentProviderOperation> operations) throws
            OperationApplicationException {
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        sqlDB.beginTransaction();//Begin transaction
        try {
            ContentProviderResult[] results = super.applyBatch(operations);
            sqlDB.setTransactionSuccessful();//Set the transaction marked successful
            return results;
        } finally {
            sqlDB.endTransaction();//The end of the transaction
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        int rowsDeleted;

        switch (uriType) {
            case ARTICLES:
                rowsDeleted = sqlDB.delete(RssContract.Columns.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            case ARTICLES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(RssContract.Columns.TABLE_NAME,
                            RssContract.Columns._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(RssContract.Columns.TABLE_NAME,
                            RssContract.Columns._ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        int rowsUpdated;

        switch (uriType) {
            case ARTICLES:
                rowsUpdated = sqlDB.update(RssContract.Columns.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ARTICLES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(RssContract.Columns.TABLE_NAME,
                            values,
                            RssContract.Columns._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(RssContract.Columns.TABLE_NAME,
                            values,
                            RssContract.Columns._ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
