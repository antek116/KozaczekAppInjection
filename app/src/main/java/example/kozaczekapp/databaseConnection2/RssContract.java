package example.kozaczekapp.databaseConnection2;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Class contains properties used to ContentProvider
 */
public class RssContract {
    /**
     * The authority for the articles provider
     */
    public static final String AUTHORITY =
            "example.kozaczekapp.DatabaseConnection.RssContentProvider";


    /**
     * A content:// style uri to the authority for this table.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + Columns.TABLE_NAME);


    // Private constructor to ensure nobody instantiates this
    private RssContract() {
    }

    /**
     * Contains the column names for a Database.
     */
    public abstract class Columns implements BaseColumns {
        public static final String TABLE_NAME = "articles";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ARTICLE_LINK = "articleLink";
        public static final String COLUMN_PUB_DATE = "pubDate";
        public static final String COLUMN_IMAGE_LINK = "imageLink";
        public static final String IMAGE_SIZE = "200";
    }
}
