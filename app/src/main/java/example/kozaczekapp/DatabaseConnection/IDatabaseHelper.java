package example.kozaczekapp.DatabaseConnection;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import example.kozaczekapp.KozaczekItems.Article;

/**
 * interface that contains set of priceless methods using to manage database
 */
public interface IDatabaseHelper {

    /**
     * adds new article list
     *
     * @param articleList contains list of all articles
     */
    void addArticleList(List<Article> articleList);

    /**
     * adds new article
     *
     * @param database
     * @param article to be added
     */
    void addArticle(SQLiteDatabase database, Article article);

    /**
     * gets single article from database
     *
     * @param id of article to be gotten
     * @return single article
     */
    Article getArticle(int id);


    /**
     * gets all articles from database
     *
     * @return list of all articles
     */

    List<Article> getAllArticles();


    /**
     * gets number of articles in database
     *
     * @return articles' count
     */

    int getArticlesCount();

    /**
     * updates article with specify article
     *
     * @param article to be updated
     */
    void updateArticle(Article article);


    /**
     * deletes single article
     *
     * @param article to be deleted
     */

    void deleteArticle(Article article);

    /**
     * deletes all records from database
     */
    void deleteAll();
}
