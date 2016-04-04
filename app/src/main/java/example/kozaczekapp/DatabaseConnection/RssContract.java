package example.kozaczekapp.DatabaseConnection;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 */
public class RssContract {
    /** The authority for the contacts provider */
    public static final String AUTHORITY = "example.kozaczekapp.DatabaseConnection.RssContentProvider";


    /** A content:// style uri to the authority for this table. */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + Columns.TABLE_NAME);



    /** Contains the column names for a Database. */
    public abstract class Columns implements BaseColumns {
        static final String TABLE_NAME = "articles";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_ARTICLE_LINK = "articleLink";
        static final String COLUMN_PUB_DATE = "pubDate";
        static final String COLUMN_IMAGE_LINK = "imageLink";
    }

    // Private constructor to ensure nobody instantiates this
    private RssContract() {
    }
}
