package example.kozaczekapp.DatabaseConnection;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.KozaczekItems.Image;

/**
 * A helper class that manages database.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHelper {

    public static final String IMAGE_SIZE = "200";
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
//        SQLiteDatabase database = getWritableDatabase();

        try {
          /*  //transaction
            database.beginTransaction();
            //delete records in DB
            deleteAll(database);*/

            for (Article article : articleList) {
                addArticle(article);
            }

//            database.setTransactionSuccessful();
        } catch (Exception e) {
//            database.endTransaction();
        } finally {
//            database.endTransaction();
//            database.close();
        }
    }

    /**
     * adds a new article
     *
     * @param article to be added
     */
    @Override
    public void addArticle(Article article) {
        ContentValues values = new ContentValues();
        values.put(RssContract.Columns.COLUMN_TITLE, article.getTitle());
        values.put(RssContract.Columns.COLUMN_DESCRIPTION, article.getDescription());
        values.put(RssContract.Columns.COLUMN_PUB_DATE, article.getPubDate());
        values.put(RssContract.Columns.COLUMN_ARTICLE_LINK, article.getLinkToArticle());
        values.put(RssContract.Columns.COLUMN_IMAGE_LINK, article.getImage().getImageUrl());

        // Inserting Row
        //db.insert(RssContract.Columns.TABLE_NAME, null, values);


        resolver.insert(RssContract.CONTENT_URI, values);
    }

    /**
     * gets single article from database
     *
     * @param id of article to be gotten
     * @return single article
     */
    @Override
    public Article getArticle(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RssContract.Columns.TABLE_NAME, new String[]{
                        RssContract.Columns._ID,
                        RssContract.Columns.COLUMN_TITLE,
                        RssContract.Columns.COLUMN_DESCRIPTION,
                        RssContract.Columns.COLUMN_PUB_DATE,
                        RssContract.Columns.COLUMN_ARTICLE_LINK,
                        RssContract.Columns.COLUMN_IMAGE_LINK},
                RssContract.Columns._ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Article article = null;
        if (cursor != null) {
            cursor.moveToFirst();

            article = new Article(
                    cursor.getString(cursor.getColumnIndex(RssContract.Columns.COLUMN_PUB_DATE)),
                    new Image(cursor.getString(cursor.getColumnIndex(RssContract.Columns.COLUMN_IMAGE_LINK)),
                            IMAGE_SIZE),
                    cursor.getString(cursor.getColumnIndex(RssContract.Columns.COLUMN_ARTICLE_LINK)),
                    cursor.getString(cursor.getColumnIndex(RssContract.Columns.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(RssContract.Columns.COLUMN_DESCRIPTION)));
            article.setId(cursor.getColumnIndex(RssContract.Columns._ID));
            cursor.close();
        }
        return article;
    }

    /**
     * gets all articles from database
     *
     * @return list of all articles
     */
    @Override
    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + RssContract.Columns.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setId(cursor.getColumnIndex(RssContract.Columns._ID));
                article.setTitle(cursor.getString(cursor
                        .getColumnIndex(RssContract.Columns.COLUMN_TITLE)));
                article.setDescription(cursor.getString(cursor
                        .getColumnIndex(RssContract.Columns.COLUMN_DESCRIPTION)));
                article.setPubDate(cursor.getString(cursor
                        .getColumnIndex(RssContract.Columns.COLUMN_PUB_DATE)));
                article.setLinkToArticle(cursor.getString(cursor
                        .getColumnIndex(RssContract.Columns.COLUMN_ARTICLE_LINK)));
                article.setImage(new Image(cursor.getString(cursor
                        .getColumnIndex(RssContract.Columns.COLUMN_IMAGE_LINK)), IMAGE_SIZE));

                // Adding article to list
                articleList.add(article);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return article list
        return articleList;
    }

    /**
     * gets number of articles in database
     *
     * @return articles' count
     */
    @Override
    public int getArticlesCount(SQLiteDatabase db) {
        //String countQuery = "SELECT  * FROM " + RssContract.Columns.TABLE_NAME;
        //Cursor c = resolver.query();
        return 0;
    }

    /**
     * updates article with specify article
     *
     * @param article to be updated
     */
    @Override
    public void updateArticle(Article article) {
    }

    /**
     * deletes single article
     *
     * @param article to be deleted
     */
    @Override
    public void deleteArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete  from " + RssContract.Columns.TABLE_NAME);
        db.close();
    }

    /**
     * deletes all records from database
     */
    @Override
    public void deleteAll(SQLiteDatabase db) {
        db.execSQL("delete  from " + RssContract.Columns.TABLE_NAME);
    }

}
