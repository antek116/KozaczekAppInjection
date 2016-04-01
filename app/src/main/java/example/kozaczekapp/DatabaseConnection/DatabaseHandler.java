package example.kozaczekapp.DatabaseConnection;

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
public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHelper, FeedEntry {

    public static final String IMAGE_SIZE = "200";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ArticlesDB";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_PUB_DATE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_ARTICLE_LINK + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_IMAGE_LINK + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public DatabaseHandler(Context context) {
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

    /**
     * adds a new article list
     *
     * @param articleList contains list of all articles
     */
    @Override
    public void addArticleList(List<Article> articleList) {
        SQLiteDatabase database = getWritableDatabase();

        try {
            //transaction
            database.beginTransaction();
            //delete records in DB
            deleteAll();

            for (Article article : articleList) {
                addArticle(database, article);
            }

            database.setTransactionSuccessful();
        }
        catch (Exception e) {
//            database.
//            mark transaction failed
            database.endTransaction();
        }
        finally {
            database.endTransaction();
            database.close();
        }

    }

    /**
     * adds a new article
     *
     * @param db db to be written
     * @param article to be added
     */
    @Override
    public void addArticle(SQLiteDatabase db, Article article) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_TITLE, article.getTitle());
        values.put(FeedEntry.COLUMN_DESCRIPTION, article.getDescription());
        values.put(FeedEntry.COLUMN_PUB_DATE, article.getPubDate());
        values.put(FeedEntry.COLUMN_ARTICLE_LINK, article.getLinkToArticle());
        values.put(FeedEntry.COLUMN_IMAGE_LINK, article.getImage().getImageUrl());

        // Inserting Row
        db.insert(FeedEntry.TABLE_NAME, null, values);
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

        Cursor cursor = db.query(FeedEntry.TABLE_NAME, new String[]{
                        FeedEntry._ID,
                        FeedEntry.COLUMN_TITLE,
                        FeedEntry.COLUMN_DESCRIPTION,
                        FeedEntry.COLUMN_PUB_DATE,
                        FeedEntry.COLUMN_ARTICLE_LINK,
                        FeedEntry.COLUMN_IMAGE_LINK},
                FeedEntry._ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Article article = null;
        if (cursor != null) {
            cursor.moveToFirst();

            article = new Article(
                    cursor.getString(1),
                    new Image(cursor.getString(2), IMAGE_SIZE),
                    cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_DESCRIPTION)),
                    cursor.getString(4),
                    cursor.getString(5));
            article.setId(cursor.getInt(0));
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
        String selectQuery = "SELECT  * FROM " + FeedEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //FIXME: observe db content changes
        //cursor.registerDataSetObserver();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setId(cursor.getInt(0));
                article.setTitle(cursor.getString(1));
                article.setDescription(cursor.getString(2));
                article.setPubDate(cursor.getString(3));
                article.setLinkToArticle(cursor.getString(4));
                article.setImage(new Image(cursor.getString(5), IMAGE_SIZE));

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
    public int getArticlesCount() {
        //TODO: fix querying
        return getReadableDatabase().execSQL("Select count(" + FeedEntry._ID +") from " + TABLE_NAME + ";");
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
        db.execSQL("delete  from " + FeedEntry.TABLE_NAME);
        db.close();
    }

    /**
     * deletes all records from database
     */
    @Override
    public void deleteAll() {
//        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete  from " + FeedEntry.TABLE_NAME);
//        db.close();
    }

}
