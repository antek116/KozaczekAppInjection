package example.kozaczekapp.databaseConnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class that manages database.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ArticlesDB";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RssContract.Columns.TABLE_NAME + " (" +
                    RssContract.Columns._ID + " INTEGER PRIMARY KEY," +
                    RssContract.Columns.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    RssContract.Columns.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    RssContract.Columns.COLUMN_PUB_DATE + TEXT_TYPE + COMMA_SEP +
                    RssContract.Columns.COLUMN_ARTICLE_LINK + TEXT_TYPE + COMMA_SEP +
                    RssContract.Columns.COLUMN_IMAGE_LINK + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RssContract.Columns.TABLE_NAME;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}



