package example.kozaczekapp.databaseConnection2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.kozaczekapp.kozaczekItems2.Article;

/**
 * A helper class that manages database.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHelper {


    public static final int MAX_NUMBER_OF_ARTICLES = 10;
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
    private static final int FIRST_ID = 1;
    private ContentResolver resolver;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        resolver = context.getContentResolver();
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

    /**
     * adds a new article list
     *
     * @param articleList contains list of all articles
     */
    @Override
    public void addArticleList(List<Article> articleList) {
        for (Article article : articleList) {
            addArticle(article);
        }
        resolver.notifyChange(RssContract.CONTENT_URI, null);
    }

    /**
     * adds a new article
     *
     * @param article to be added
     */
    @Override
    public void addArticle(Article article) {
        ContentValues values = new ContentValues();
        putArticlesFieldsToValuesMap(article, values);

        Cursor c = resolver.query(RssContract.CONTENT_URI, null, RssContract.Columns
                .COLUMN_PUB_DATE + " = " + DatabaseUtils.sqlEscapeString(article.getPubDate())
                , null, null);

        if ((c != null ? c.getCount() : 0) != 0) {
            Log.d("ADD ARTICLE: ", "Article exists!");
        } else {
            deleteExceededArticles();
            resolver.insert(RssContract.CONTENT_URI, values);
            Log.d("ADD ARTICLE: ", "Article added!");
        }
        if (c != null) {
            c.close();
        }
    }

    private void deleteExceededArticles() {
        if (getArticlesCount() >= MAX_NUMBER_OF_ARTICLES) {
            Log.d("ADD ARTICLE: ", "Rows >= " + MAX_NUMBER_OF_ARTICLES +
                    " . Removing first element!");
            deleteArticle(FIRST_ID);
        }
    }

    private void putArticlesFieldsToValuesMap(Article article, ContentValues values) {
        values.put(RssContract.Columns.COLUMN_TITLE, article.getTitle());
        values.put(RssContract.Columns.COLUMN_DESCRIPTION, article.getDescription());
        values.put(RssContract.Columns.COLUMN_PUB_DATE, article.getPubDate());
        values.put(RssContract.Columns.COLUMN_ARTICLE_LINK, article.getLinkToArticle());
        values.put(RssContract.Columns.COLUMN_IMAGE_LINK, article.getImage().getImageUrl());
    }

    /**
     * gets all articles from database
     *
     * @return list of all articles
     */
    @Override
    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        Cursor cursor = resolver.query(RssContract.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.fromCursor(cursor);
                // Adding article to list
                articleList.add(article);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return articleList;
    }

    /**
     * gets number of articles in database
     *
     * @return articles' count
     */
    @Override
    public int getArticlesCount() {
        int count = 0;
        Cursor cursor = resolver.query(RssContract.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /**
     * deletes single article
     *
     * @param id specify index of article to be deleted
     */
    @Override
    public void deleteArticle(int id) {
        resolver.delete(RssContract.CONTENT_URI, RssContract.Columns
                ._ID + " = " + id, null);
    }


}
