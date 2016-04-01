package example.kozaczekapp.DatabaseConnection;

import android.provider.BaseColumns;

/**
 * Contains
 */
//public final class FeedEntry {
//
//    private FeedEntry(){}

public interface FeedEntry extends BaseColumns{
    static final String TABLE_NAME = "articles";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_ARTICLE_LINK = "articleLink";
    static final String COLUMN_PUB_DATE = "pubDate";
    static final String COLUMN_IMAGE_LINK = "imageLink";
}
